package com.foodscan.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private Context mContext;

    private TextView txt_signin, txt_create_new_account, txt_copy_right;
    private ImageView img_back;

    private int LOGIN_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = HomeActivity.this;

        initView();
        initGlobals();
        triggerLoginNeededMessage();
    }

    private void initView() {

        txt_signin = findViewById(R.id.txt_signin);
        txt_create_new_account = findViewById(R.id.txt_create_new_account);
        txt_copy_right = findViewById(R.id.txt_copy_right);
        img_back = findViewById(R.id.img_back);

    }

    private void initGlobals() {

        txt_signin.setOnClickListener(this);
        txt_create_new_account.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void triggerLoginNeededMessage() {

        Snackbar.make(findViewById(R.id.rl_parent), getString(R.string.please_login), Snackbar.LENGTH_LONG)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE) {

            if (data != null) {

                if (data.hasExtra("user_data")) {

                    DTOUser dtoUser = data.getParcelableExtra("user_data");
                    Intent intent = new Intent();
                    intent.putExtra("user_data", dtoUser);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_signin: {
                Intent intent = new Intent(mContext, SignInActivity.class);
                startActivityForResult(intent, LOGIN_REQ_CODE);
            }
            break;

            case R.id.txt_create_new_account: {
                Intent intent = new Intent(mContext, RegistrationActivity.class);
                startActivityForResult(intent, LOGIN_REQ_CODE);
            }
            break;

            case R.id.img_back: {
                onBackPressed();
            }
            break;

        }
    }
}
