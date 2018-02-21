package com.foodscan.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.foodscan.R;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private Context mContext;

    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mContext = ChangePasswordActivity.this;

        initView();
        initGlobals();

    }

    private void initView(){

        img_back = findViewById(R.id.img_back);

    }

    private void initGlobals(){

        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_back:
            {
                onBackPressed();
            }
                break;


        }

    }
}
