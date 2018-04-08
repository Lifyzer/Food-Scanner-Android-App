package com.foodscan.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTORefreshTokenData;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

public class SplashActivity extends AppCompatActivity implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private final int SPLASH_DURATION_MILI = 1000;
    private Context mContext;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = SplashActivity.this;
        tinyDB = new TinyDB(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
        initGlobals();

    }

    private void initView() {

    }

    private void initGlobals() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tinyDB.getBoolean(UserDefaults.IS_LOGIN)) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();

                } else {
                    wsCallRefreshToken();
                }
            }
        }, SPLASH_DURATION_MILI);
    }

    public void wsCallRefreshToken() {
        if (Utility.isNetworkAvailable(mContext)) {

            try {

                Attribute attribute = new Attribute();
                attribute.setAccess_key(UserDefaults.DEFAULT_ACCESS_KEY);

                new WebserviceWrapper(mContext, attribute, SplashActivity.this,
                        true, getString(R.string.Loading_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.REFRESH_TOKEN.getTypeCode());

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
                wsCallRefreshToken();
            }
        };

        ((SimpleDialog.Builder) builder).message(message)
                .title(getString(R.string.app_name))
                .positiveAction("CANCEL")
                .negativeAction("RETRY");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }

    private void launchApp() {
        try {
            //startActivity(new Intent(this, HomeActivity.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Log.e(TAG, "launchApp Exception : ", e);
        }
    }


    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.REFRESH_TOKEN.getTypeCode()) {

            if (object != null) {
                try {
                    DTORefreshTokenData dtoRefreshToken = (DTORefreshTokenData) object;
                    String userToken = dtoRefreshToken.getData().getTempToken();
                    if (userToken != null && userToken.length() > 0) {
                        tinyDB.putString(UserDefaults.TEMP_TOKEN, userToken);
                        launchApp();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }
}
