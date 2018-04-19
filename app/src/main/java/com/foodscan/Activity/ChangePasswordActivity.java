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
import com.foodscan.WsHelper.model.DTOUser;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import io.realm.Realm;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;
    private DTOUser dtoUser;

    private RelativeLayout rl_parent;
    private ImageView img_back;
    private EditText edt_current_pass, edt_new_password, edt_confirm_pass;
    private TextView txt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = ChangePasswordActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        dtoUser = realm.where(DTOUser.class).findFirst();

        initView();
        initGlobals();

    }

    private void initView() {

        rl_parent = findViewById(R.id.rl_parent);
        img_back = findViewById(R.id.img_back);
        edt_current_pass = findViewById(R.id.edt_current_account);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_confirm_pass = findViewById(R.id.edt_confirm_pass);
        txt_save = findViewById(R.id.txt_save);

    }

    private void initGlobals() {

        img_back.setOnClickListener(this);
        txt_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back: {
                onBackPressed();
            }
            break;

            case R.id.txt_save: {
                if (isInputsValid()) {
                    wsCallChangePassword();
                }
            }
            break;
        }
    }


    private void wsCallChangePassword() {
        if (Utility.isNetworkAvailable(mContext)) {

            String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
            String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY),tinyDB.getString(UserDefaults.ENCODE_KEY_IV), dtoUser.getGuid());

            Attribute attribute = new Attribute();
            attribute.setUser_id(String.valueOf(dtoUser.getId()));
            attribute.setPassword(edt_current_pass.getText().toString());
            attribute.setNew_password(edt_new_password.getText().toString());
            attribute.setAccess_key(encodeString);
            attribute.setSecret_key(userToken);

            new WebserviceWrapper(mContext, attribute, ChangePasswordActivity.this, true, getString(R.string.Processing_msg)).new WebserviceCaller()
                    .execute(WebserviceWrapper.WEB_CALLID.CHANGE_PASSWORD.getTypeCode());


        } else {
            Utility.showLongSnackBar(rl_parent, getString(R.string.no_internet_connection), ChangePasswordActivity.this);
        }
    }


    private boolean isInputsValid() {
        if (Utility.validateStringPresence(edt_current_pass)) {
            if (Utility.validateStringPresence(edt_new_password)) {
                if (Utility.validateStringPresence(edt_confirm_pass)) {
                    if (edt_new_password.getText().toString().equals(edt_confirm_pass.getText().toString())) {
                        if (Utility.validatePasswordLength(edt_new_password.getText().toString())) {
                            return true;
                        } else {
                            //Password length is short
                            Utility.showLongSnackBar(rl_parent, getString(R.string.password_lenght_six_char), ChangePasswordActivity.this);
                            return false;
                        }
                    } else {
                        //Password and confirm password is diffrent
                        Utility.showLongSnackBar(rl_parent, getString(R.string.password_and_confrirmpass_is_diffrent), ChangePasswordActivity.this);
                        return false;
                    }
                } else {
                    //confirm password blank
                    Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_confirm_password), ChangePasswordActivity.this);
                    return false;
                }
            } else {
                //New password blank
                Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_new_password), ChangePasswordActivity.this);
                return false;
            }
        } else {
            //current password blank
            Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_current_password), ChangePasswordActivity.this);
            return false;
        }

    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {
        if (apiCode == WebserviceWrapper.WEB_CALLID.CHANGE_PASSWORD.getTypeCode()) {
            if (object != null) {
                try {
                    DTOResponse dtoResponse = (DTOResponse) object;
                    if (dtoResponse.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

//                        Utility.showLongSnackBar(rl_parent, dtoResponse.getMessage(), ChangePasswordActivity.this);
//
//                        onBackPressed();
//                        ChangePasswordActivity.this.finish();

                        SuccessChangePass();

                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoResponse.getMessage(), ChangePasswordActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }

            }
        }
    }


    private void SuccessChangePass() {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                onBackPressed();
            }
        };

        ((SimpleDialog.Builder) builder).message(getString(R.string.password_change_success))
                .title(getString(R.string.app_name))
                .negativeAction("OK");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }


}
