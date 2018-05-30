package com.foodscan.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodscan.Adapter.FoodDetailsAdapter;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOResponse;
import com.foodscan.WsHelper.model.DTOUser;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, WebserviceWrapper.WebserviceResponse {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private Context mContext;
    private Realm realm;
    private DTOUser dtoUser;
    private TinyDB tinyDB;

    private ImageView img_back, img_favourite, img_product;
    private RecyclerView rv_details;
    private TextView txt_product_name, txt_is_healthy, txt_ingradiants;

    private DTOProduct dtoProduct;
    private FoodDetailsAdapter foodDetailsAdapter;
    private boolean isChangeMade = false;
    private RelativeLayout rl_parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = ProductDetailsActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        dtoUser = realm.where(DTOUser.class).findFirst();

        initView();
        initGlobals();
    }

    private void initView() {

        rl_parent = findViewById(R.id.rl_parent);
        img_back = findViewById(R.id.img_back);
        rv_details = findViewById(R.id.rv_details);
        txt_product_name = findViewById(R.id.txt_product_name);
        img_favourite = findViewById(R.id.img_favourite);
        img_product = findViewById(R.id.img_product);
        txt_is_healthy = findViewById(R.id.txt_is_healthy);
        txt_ingradiants = findViewById(R.id.txt_ingradiants);

    }

    private void initGlobals() {

        img_back.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            dtoProduct = bundle.getParcelable("productDetails");
            if (dtoProduct != null) {

                if (dtoProduct.getProductImage() != null) {
                    Picasso.with(mContext).load(dtoProduct.getProductImage()).placeholder(R.drawable.img_placeholder_large).into(img_product);
                }


                txt_product_name.setText(dtoProduct.getProductName());
                foodDetailsAdapter = new FoodDetailsAdapter(mContext, dtoProduct);
                rv_details.setAdapter(foodDetailsAdapter);

                String is_fav = dtoProduct.getIsFavourite();
                if (is_fav == null || is_fav.length() <= 0) {
                    is_fav = "0";
                }

                if (is_fav.equals("0")) {
                    img_favourite.setImageResource(R.drawable.img_fav_white_stroke);
                } else if (is_fav.equals("1")) {
                    img_favourite.setImageResource(R.drawable.img_fav_white_solid);
                }

                String isHealthy = dtoProduct.getIsHealthy();
                if (isHealthy != null && isHealthy.length() > 0) {
                    if (isHealthy.equals("0")) {

                        //Not healthy
                        txt_is_healthy.setText(mContext.getString(R.string.Poor));
                        txt_is_healthy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_bg_red_small, 0, 0, 0);
                        txt_is_healthy.setTextColor(Utility.getColorWrapper(mContext, R.color.red));

                    } else if (isHealthy.equals("1")) {

                        //Healthy
                        txt_is_healthy.setText(mContext.getString(R.string.Excellent));
                        txt_is_healthy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_bg_green_small, 0, 0, 0);
                        txt_is_healthy.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
                    }
                }
            }
        }

        img_favourite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back: {
                onBackPressed();
            }
            break;

            case R.id.img_favourite: {
                callWsFavourite();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {

        if (isChangeMade) {

            Intent intent = new Intent();
            intent.putExtra("productDetails", dtoProduct);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        super.onBackPressed();

    }

    private void callWsFavourite() {

        String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
        String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY),tinyDB.getString(UserDefaults.ENCODE_KEY_IV), dtoUser.getGuid());

        if (Utility.isNetworkAvailable(mContext)) {

            Attribute attribute = new Attribute();
            attribute.setUser_id(String.valueOf(dtoUser.getId()));
            attribute.setProduct_id(String.valueOf(dtoProduct.getId()));
            if (dtoProduct.getIsFavourite().equals("1")) {
                attribute.setIs_favourite("0");
                dtoProduct.setIsFavourite("0");
                img_favourite.setImageResource(R.drawable.img_fav_white_stroke);
            } else if (dtoProduct.getIsFavourite().equals("0")) {
                attribute.setIs_favourite("1");
                dtoProduct.setIsFavourite("1");
                img_favourite.setImageResource(R.drawable.img_fav_white_solid);
            }

            attribute.setAccess_key(encodeString);
            attribute.setSecret_key(userToken);


            new WebserviceWrapper(mContext, attribute, ProductDetailsActivity.this, true, mContext.getString(R.string.Loading_msg)).new WebserviceCaller()
                    .execute(WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode());

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode()) {

            isChangeMade = true;

            if (object != null) {
                try {

                    DTOResponse dtoResponse = (DTOResponse) object;
                    if (dtoResponse.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {
                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, true);
                    }
                    try {
                        Utility.showLongSnackBar(rl_parent, dtoResponse.getMessage(), ProductDetailsActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }
}
