package com.foodscan.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.foodscan.WsHelper.model.DTOResponse;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;

    private RelativeLayout rl_parent;
    private TextView txt_send;
    private ImageView img_back;
    private EditText edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = ForgotPasswordActivity.this;
        tinyDB = new TinyDB(mContext);

        initView();
        initGlobals();
    }

    private void initView() {
        rl_parent = findViewById(R.id.rl_parent);
        img_back = findViewById(R.id.img_back);
        edt_email = findViewById(R.id.edt_email);
        txt_send = findViewById(R.id.txt_send);
    }

    private void initGlobals() {
        img_back.setOnClickListener(this);
        txt_send.setOnClickListener(this);

        retrieveUsedEmail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back: {
                onBackPressed();
            }
            break;

            case R.id.txt_send: {
                if (isInputsValid()) {
                    wsCallForgetPassword();
                }
            }
            break;

        }
    }

    public void wsCallForgetPassword() {

        if (Utility.isNetworkAvailable(mContext)) {

            try {
                Attribute attribute = new Attribute();
                attribute.setEmail_id(edt_email.getText().toString().trim());
                attribute.setSecret_key(tinyDB.getString(UserDefaults.TEMP_TOKEN));
                attribute.setAccess_key(UserDefaults.DEFAULT_ACCESS_KEY);

                new WebserviceWrapper(mContext, attribute, ForgotPasswordActivity.this, true, getString(R.string.Processing_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.FORGET_PASSWORD.getTypeCode());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
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
                    wsCallForgetPassword();
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

    private void SuccessForgetPass() {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                onBackPressed();
            }
        };

        //Snackbar.make(rl_parent, getString(R.string.forget_password_success), Snackbar.LENGTH_LONG);
        ((SimpleDialog.Builder) builder).message(getString(R.string.forget_password_success))
                .title(getString(R.string.app_name))
                .negativeAction("OK");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }

    private boolean isInputsValid() {

        if (Utility.validateStringPresence(edt_email)) {
            if (Utility.validateEmail(edt_email)) {
                return true;
            } else {
                //email not valid
                Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_valid_email), ForgotPasswordActivity.this);
                return false;
            }
        } else {
            //email blank
            Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_email), ForgotPasswordActivity.this);
            return false;
        }

    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.FORGET_PASSWORD.getTypeCode()) {

            if (object != null) {
                DTOResponse dtoResponse = (DTOResponse) object;
                if (dtoResponse.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {
                    SuccessForgetPass();
                } else {
                    Utility.showLongSnackBar(rl_parent, "Something went wrong please try again later", ForgotPasswordActivity.this);
                }
            }

        }

    }

    private void retrieveUsedEmail() {
        String email = tinyDB.getString(SignInActivity.USER_EMAIL_KEY);
        if (!email.isEmpty()) {
            edt_email.setText(email);
        }
    }
}
