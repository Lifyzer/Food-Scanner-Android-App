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

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;

    private RelativeLayout rl_parent;
    private EditText edt_full_name, edt_email_id, edt_password, edt_confirm_password;
    private TextView txt_terms_and_condition, txt_proceed;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = RegistrationActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();

        initView();
        initGlobals();


    }

    private void initView() {

        rl_parent = findViewById(R.id.rl_parent);
        edt_full_name = findViewById(R.id.edt_full_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        txt_proceed = findViewById(R.id.txt_proceed);
        txt_terms_and_condition = findViewById(R.id.txt_terms_and_condition);
        img_back = findViewById(R.id.img_back);

    }

    private void initGlobals() {

        String text = "<font color=#5B5B5B> By Creating Account, you are automatically accepting all the </font> <font color=#44B05B> Terms & Conditions</font>";
        txt_terms_and_condition.setText(Html.fromHtml(text));

        img_back.setOnClickListener(this);
        txt_proceed.setOnClickListener(this);
        txt_terms_and_condition.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back: {
                onBackPressed();
            }
            break;

            case R.id.txt_proceed: {

                Utility.hideKeyboard(RegistrationActivity.this);
                if (isInputsValid()) {
                    if (Utility.validateEmail(edt_email_id)) {
                        wsCallRegistration();
                    } else {
                        Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_valid_email), RegistrationActivity.this);
                    }
                }
            }
            break;

            case R.id.txt_terms_and_condition:{

                Intent intent = new Intent(mContext, PrivacyPolicyActivity.class);
                startActivity(intent);

            }
            break;
        }
    }

    public void wsCallRegistration() {
        if (Utility.isNetworkAvailable(mContext)) {

            try {

                Attribute attribute = new Attribute();
                attribute.setEmail_id(edt_email_id.getText().toString());
                attribute.setPassword(edt_password.getText().toString());
                attribute.setFirst_name(edt_full_name.getText().toString());
                attribute.setLast_name(" ");
                attribute.setDevice_type(UserDefaults.DEVICE_TYPE);
                attribute.setSecret_key(tinyDB.getString(UserDefaults.TEMP_TOKEN));
                attribute.setAccess_key(UserDefaults.DEFAULT_ACCESS_KEY);

                new WebserviceWrapper(mContext, attribute, RegistrationActivity.this, true, getString(R.string.Processing_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.REGISTRATION.getTypeCode());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "" + e.getMessage());
            }

        } else {
            noInternetConnection(getString(R.string.no_internet_connection));
        }
    }

    private void noInternetConnection(String message) {
        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                if (isInputsValid()) {
                    wsCallRegistration();
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
        if (Utility.validateStringPresence(edt_full_name)) {
            if (Utility.validateStringPresence(edt_email_id)) {
                if (Utility.validateStringPresence(edt_password)) {
                    if (Utility.validateStringPresence(edt_confirm_password)) {
                        if (edt_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
                            if (Utility.validatePasswordLength(edt_password.getText().toString())) {
                                return true;
                            } else {
                                //Password length is short
                                Utility.showLongSnackBar(rl_parent, getString(R.string.password_length_error), RegistrationActivity.this);
                                return false;
                            }

                        } else {
                            //Password and confirm password is diffrent
                            Utility.showLongSnackBar(rl_parent, getString(R.string.password_and_confirmpass_is_different), RegistrationActivity.this);
                            return false;
                        }
                    } else {
                        //Confirm password blank
                        Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_confirm_password), RegistrationActivity.this);
                        return false;
                    }
                } else {
                    //password blank
                    Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_password), RegistrationActivity.this);
                    return false;
                }
            } else {
                //email blank
                Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_email), RegistrationActivity.this);
                return false;
            }

        } else {
            //first name blank
            Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_full_name), RegistrationActivity.this);
            return false;

        }
    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.REGISTRATION.getTypeCode()) {
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


                                setResult(dtoUser);

//                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
//                                intent.putExtra("needDialog", true);
//                                startActivity(intent);
//                                finish();

                            }
                        }
                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoLoginData.getMessage(), RegistrationActivity.this);
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
            //intent.putExtra("needDialog", true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

}
