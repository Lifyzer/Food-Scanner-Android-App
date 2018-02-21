package com.foodscan.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Fragment.ProfileFragment;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.WsHelper.model.DTOUser;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import java.io.File;

import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;
    private DTOUser dtoUser;

    private RelativeLayout rl_edit_profile, rl_change_pass, rl_privacy, rl_terms;
    private TextView txt_logout;
    private ImageView img_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mContext = SettingsActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        dtoUser = realm.where(DTOUser.class).findFirst();

        initView();
        initGlobals();

    }

    private void initView() {

        rl_edit_profile = findViewById(R.id.rl_edit_profile);
        rl_change_pass = findViewById(R.id.rl_change_pass);
        rl_privacy = findViewById(R.id.rl_privacy);
        rl_terms = findViewById(R.id.rl_terms);
        txt_logout = findViewById(R.id.txt_logout);
        img_back = findViewById(R.id.img_back);

    }

    private void initGlobals() {

        rl_edit_profile.setOnClickListener(this);
        rl_change_pass.setOnClickListener(this);
        rl_privacy.setOnClickListener(this);
        rl_terms.setOnClickListener(this);
        img_back.setOnClickListener(this);
        txt_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_edit_profile: {

                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);

            }
            break;

            case R.id.rl_change_pass: {

                Intent intent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(intent);

            }
            break;

            case R.id.rl_privacy: {

            }
            break;

            case R.id.rl_terms: {

            }
            break;

            case R.id.img_back: {
                onBackPressed();
            }
            break;

            case R.id.txt_logout: {

                showLoginDialog();

            }
            break;
        }
    }

    private void logOut() {

        //String refreshedToken = t
        // inyDB.getString(UserDefaults.DEVICE_TOKEN);
        String tempToken = tinyDB.getString(UserDefaults.TEMP_TOKEN);

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        tinyDB.clear();

        trimCache(mContext);

        //tinyDB.putString(UserDefaults.DEVICE_TOKEN, refreshedToken);
        tinyDB.putString(UserDefaults.TEMP_TOKEN, tempToken);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SettingsActivity.this.finish();

    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    private void showLoginDialog() {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
                logOut();
            }
        };

        ((SimpleDialog.Builder) builder).message(getString(R.string.logout_conformation))
                .title(getString(R.string.app_name))
                .positiveAction("CANCEL")
                .negativeAction("LOGOUT");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);


    }

}
