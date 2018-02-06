package com.foodscan.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.foodscan.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = HomeActivity.class.getSimpleName();

    private Context mContext;

    private TextView txt_signin, txt_create_new_account, txt_copy_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;

        initView();
        initGlobals();
    }

    private void initView(){

        txt_signin = findViewById(R.id.txt_signin);
        txt_create_new_account = findViewById(R.id.txt_create_new_account);
        txt_copy_right = findViewById(R.id.txt_copy_right);

    }

    private void initGlobals(){

        txt_signin.setOnClickListener(this);
        txt_create_new_account.setOnClickListener(this);

        //txt_copy_right.setText("@2017 ScanFood Inc.");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.txt_signin:
            {
                startActivity(new Intent(mContext,SignInActivity.class));
            }
                break;

            case R.id.txt_create_new_account:
            {
                startActivity(new Intent(mContext,RegistrationActivity.class));
            }
                break;

        }
    }
}
