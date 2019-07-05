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
import com.foodscan.WsHelper.model.DTOLoginData;
import com.foodscan.WsHelper.model.DTOUser;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import io.realm.Realm;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;
    private DTOUser dtoUser;


    private RelativeLayout rl_parent;
    private ImageView img_back;
    private EditText edt_full_name, edt_email_id;
    private TextView txt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = EditProfileActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        dtoUser = realm.where(DTOUser.class).findFirst();

        initView();
        initGlobals();
    }

    private void initView() {

        rl_parent = findViewById(R.id.rl_parent);
        img_back = findViewById(R.id.img_back);
        edt_full_name = findViewById(R.id.edt_full_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        txt_save = findViewById(R.id.txt_save);

    }

    private void initGlobals() {

        if (dtoUser != null) {

            edt_email_id.setText(dtoUser.getEmail());
            edt_full_name.setText(dtoUser.getFirstName());


//            edt_email_id.post(new Runnable() {
//                @Override
//                public void run() {
//                    edt_email_id.setSelection(edt_email_id.getText().length());
//                }
//            });


            edt_full_name.setSelection(edt_full_name.getText().length());

        }

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
                    wsCallEditProfile();
                }

            }
            break;

        }
    }

    public void wsCallEditProfile() {
        if (Utility.isNetworkAvailable(mContext)) {

            try {

                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY), tinyDB.getString(UserDefaults.ENCODE_KEY_IV), dtoUser.getGuid());


                Attribute attribute = new Attribute();
                attribute.setFirst_name(edt_full_name.getText().toString());
                attribute.setEmail_id(edt_email_id.getText().toString());
                attribute.setUser_id(String.valueOf(dtoUser.getId()));
                attribute.setAccess_key(encodeString);
                attribute.setSecret_key(userToken);

                new WebserviceWrapper(mContext, attribute, EditProfileActivity.this, true, getString(R.string.Processing_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.EDIT_PROFILE.getTypeCode());


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

        } else {
            Utility.showLongSnackBar(rl_parent, getString(R.string.no_internet_connection), EditProfileActivity.this);
        }
    }

    private boolean isInputsValid() {

        if (Utility.validateStringPresence(edt_full_name)) {
            if (Utility.validateStringPresence(edt_email_id)) {
                if (Utility.validateEmail(edt_email_id)) {
                    return true;
                } else {
                    //Email not valid
                    Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_valid_email), EditProfileActivity.this);
                    return false;
                }
            } else {
                //email blank
                Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_email), EditProfileActivity.this);
                return false;
            }

        } else {
            //first name blank
            Utility.showLongSnackBar(rl_parent, getString(R.string.please_enter_full_name), EditProfileActivity.this);
            return false;
        }

    }

    private void SuccessEditProfile() {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                onBackPressed();
            }
        };

        ((SimpleDialog.Builder) builder).message(getString(R.string.profile_change_success))
                .title(getString(R.string.app_name))
                .negativeAction("OK");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.EDIT_PROFILE.getTypeCode()) {
            if (object != null) {
                try {

                    DTOLoginData dtoLoginData = (DTOLoginData) object;
                    if (dtoLoginData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoLoginData.getUser() != null && dtoLoginData.getUser().size() > 0) {

                            tinyDB.putBoolean(UserDefaults.NEED_REFRESH_USER, true);
                            DTOUser dtoUser = dtoLoginData.getUser().get(0);

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(dtoUser);
                            realm.commitTransaction();

//                            onBackPressed();
//                            EditProfileActivity.this.finish();

                            SuccessEditProfile();
                        }

                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoLoginData.getMessage(), EditProfileActivity.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

}
