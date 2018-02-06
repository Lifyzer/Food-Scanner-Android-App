package com.foodscan.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodscan.Adapter.FoodDetailsAdapter;
import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOProduct;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private Context mContext;

    private ImageView img_back;
    private RecyclerView rv_details;

    private DTOProduct dtoProduct;
    private FoodDetailsAdapter foodDetailsAdapter;
    private TextView txt_product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mContext = ProductDetailsActivity.this;
        initView();
        initGlobals();
    }

    private void initView() {

        img_back = (ImageView) findViewById(R.id.img_back);
        rv_details = (RecyclerView) findViewById(R.id.rv_details);
        txt_product_name = (TextView) findViewById(R.id.txt_product_name);

    }

    private void initGlobals() {

        img_back.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            dtoProduct = bundle.getParcelable("productDetails");
            if (dtoProduct != null) {

                txt_product_name.setText(dtoProduct.getProductName());

                foodDetailsAdapter = new FoodDetailsAdapter(mContext, dtoProduct);
                rv_details.setAdapter(foodDetailsAdapter);

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back: {
                onBackPressed();
            }
            break;
        }

    }
}
