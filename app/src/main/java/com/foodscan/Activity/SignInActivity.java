package com.foodscan.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOLoginData;
import com.foodscan.WsHelper.model.DTOUser;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import io.realm.Realm;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;

    private RelativeLayout rl_parent;
    private TextView txt_signin, txt_forgotpass;
    private EditText edt_email, edt_password;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mContext = SignInActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();

        initView();
        initGlobals();
    }

    private void initView() {

        rl_parent = findViewById(R.id.rl_parent);
        txt_signin = findViewById(R.id.txt_signin);
        txt_forgotpass = findViewById(R.id.txt_forgotpass);
        img_back = findViewById(R.id.img_back);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

    }

    private void initGlobals() {

        String text = "<font color=#5B5B5B> Can't Sign In? </font> <font color=#44B05B> Forgot Password</font>";
        txt_forgotpass.setText(Html.fromHtml(text));

        txt_signin.setOnClickListener(this);
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_signin: {

                if (isInputsValid()) {
                    wsCallLogin();
                }
            }
            break;

            case R.id.img_back: {
                onBackPressed();
            }
            break;

        }
    }

    public void wsCallLogin() {
        if (Utility.isNetworkAvailable(mContext)) {

            try {

                Attribute attribute = new Attribute();
                attribute.setEmail_id(edt_email.getText().toString().trim());
                attribute.setPassword(edt_password.getText().toString().trim());
                attribute.setDevice_type(UserDefaults.DEVICE_TYPE);
                attribute.setSecret_key(tinyDB.getString(UserDefaults.TEMP_TOKEN));
                attribute.setAccess_key(UserDefaults.DEFAULT_ACCESS_KEY);

                new WebserviceWrapper(mContext, attribute, SignInActivity.this, true, getString(R.string.Loading_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.LOGIN.getTypeCode());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "" + e.getMessage());
            }

        } else {
            noInternetconnection(getString(R.string.no_internet_connection));
        }
    }

    private void noInternetconnection(String message) {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                if (isInputsValid()) {
                    wsCallLogin();
                }
            }
        };

        ((SimpleDialog.Builder) builder).message(message)
                .title(getString(R.string.app_name))
                .positiveAction("CANCEL")
                .negativeAction("RETRY");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }


    private boolean isInputsValid() {


        if (Utility.validateStringPresence(edt_email)) {
            if (Utility.validateStringPresence(edt_password)) {

                if (Utility.validateEmail(edt_email)) {
                    return true;
                } else {
                    //password blank
                    Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_valid_email), SignInActivity.this);
                    return false;
                }
            } else {
                //password blank
                Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_password), SignInActivity.this);
                return false;
            }
        } else {
            //email blank
            Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_email), SignInActivity.this);
            return false;
        }

    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.LOGIN.getTypeCode()) {
            if (object != null) {
                try {

                    DTOLoginData dtoLoginData = (DTOLoginData) object;
                    if (dtoLoginData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoLoginData.getUser() != null && dtoLoginData.getUser().size() > 0) {

                            DTOUser dtoUser = dtoLoginData.getUser().get(0);
                            if (dtoLoginData.getUserToken() == null) {
                                return;
                            } else {

                                tinyDB.putBoolean(UserDefaults.IS_LOGIN, true);
                                tinyDB.putString(UserDefaults.USER_TOKEN, dtoLoginData.getUserToken());

                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(dtoUser);
                                realm.commitTransaction();

//                              startActivity(new Intent(SignInActivity.this, MainActivity.class));
//                              finish();

                                setResult(dtoUser);

                            }
                        }

                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoLoginData.getMessage(), SignInActivity.this);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }

    private void setResult(DTOUser dtoUser) {

        if (dtoUser != null) {
            Intent intent = new Intent();
            intent.putExtra("user_data", dtoUser);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }


    }

}
