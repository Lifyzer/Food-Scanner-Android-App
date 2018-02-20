package com.foodscan.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.WsHelper.model.DTOUser;

import io.realm.Realm;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

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
            edt_full_name.setText(dtoUser.getFirstName() + " " + dtoUser.getLastName());

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

            }
            break;

        }
    }

//    private boolean isInputsValid() {
//
//    }

}
