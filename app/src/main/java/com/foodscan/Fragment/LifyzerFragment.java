package com.foodscan.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Activity.MainActivity;
import com.foodscan.Activity.ProductDetailsActivity;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOProductDetailsData;
import com.google.android.gms.vision.barcode.Barcode;
import com.rey.material.app.ThemeManager;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class LifyzerFragment extends Fragment implements WebserviceWrapper.WebserviceResponse, View.OnClickListener {

    private static final String TAG = LifyzerFragment.class.getSimpleName();
    public static String productName;

    private Context mContext;
    private View viewFragment;
    private FrameLayout frame_fragment;

//    public ViewPagerAdapter viewPagerAdapter;

    public RelativeLayout rl_parent;
    //public ViewPager viewPager;
    private TextView txt_product, txt_barcode;
    private TinyDB tinyDB;

    public boolean isViewShown = false, isLoadingFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.lifyzer_fragment, container, false);
            mContext = getActivity();
            tinyDB = new TinyDB(mContext);

        }
        return viewFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (viewFragment != null) {
            setUpControls(viewFragment);

            if (!isViewShown) {
                if (isLoadingFirstTime) {
                    initGlobal();
                    isLoadingFirstTime = false;
                }
            }
        }
    }


    private void setUpControls(View rootView) {

        rl_parent = rootView.findViewById(R.id.rl_parent);
        //viewPager = rootView.findViewById(R.id.viewpager);
        txt_product = rootView.findViewById(R.id.txt_product);
        txt_barcode = rootView.findViewById(R.id.txt_barcode);
        frame_fragment = rootView.findViewById(R.id.frame_fragment);


    }

    private void initGlobal() {

        txt_product.setOnClickListener(this);
        txt_barcode.setOnClickListener(this);

        int currentFrag = tinyDB.getInt(UserDefaults.CURRENT_FRAG);
        if (currentFrag == UserDefaults.CAM_STATE.SCANNING) {
            createViewPager();
        } else {
            replaceBarcode();
        }
    }

    private void createViewPager() {

        txt_product.setTextColor(Utility.getColorWrapper(mContext, R.color.colorPrimary));
        txt_barcode.setTextColor(Utility.getColorWrapper(mContext, R.color.white));

        ScanFragment scanFragment = new ScanFragment();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment, scanFragment);
        fragmentTransaction.commit();

    }


    private void replaceBarcode() {

        txt_product.setTextColor(Utility.getColorWrapper(mContext, R.color.white));
        txt_barcode.setTextColor(Utility.getColorWrapper(mContext, R.color.colorPrimary));

        BarcodeScannerFragment detailsFragment = new BarcodeScannerFragment();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment, detailsFragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.PRODUCT_DETAILS.getTypeCode()) {

            detectedBarcode = "";

            productName = "";

            if (object != null) {
                try {
                    DTOProductDetailsData dtoProductDetailsData = (DTOProductDetailsData) object;
                    if (dtoProductDetailsData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoProductDetailsData.getProduct() != null && dtoProductDetailsData.getProduct().size() > 0) {

                            tinyDB.putBoolean(UserDefaults.NEED_REFRESH_HISTORY, true);

                            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                            intent.putExtra("productDetails", dtoProductDetailsData.getProduct().get(0));
                            startActivity(intent);

                            //Utility.showLongSnackBar(ll_parent, dtoProductDetailsData.getMessage(), mContext);
                        } else {
                            displayNoResultDialog();
                            //Utility.showLongSnackBar(ll_parent, dtoProductDetailsData.getMessage(), mContext);
                        }

                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoProductDetailsData.getMessage(), mContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }

        }

    }

    private void displayNoResultDialog() {

        final Dialog d = new Dialog(mContext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.dialog_no_result_found);
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);

        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        d.getWindow().getAttributes().windowAnimations = (isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);//R.style.DialogAnimation

        TextView txt_ok = d.findViewById(R.id.txt_ok);
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    @Override
    public void onClick(View v) {

        Fragment f = getFragmentManager().findFragmentById(R.id.frame_fragment);

        switch (v.getId()) {

            case R.id.txt_product: {


                if (!(f instanceof ScanFragment)) {
                    tinyDB.putInt(UserDefaults.CURRENT_FRAG, UserDefaults.CAM_STATE.SCANNING);
                    createViewPager();
                }

            }
            break;

            case R.id.txt_barcode: {


                if (!(f instanceof BarcodeScannerFragment)) {
                    tinyDB.putInt(UserDefaults.CURRENT_FRAG, UserDefaults.CAM_STATE.BARCODE);
                    replaceBarcode();
                }

            }
            break;
        }
    }


    //****** View pager Adapter *******//
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    public String detectedBarcode = "";

    public void displayDialog(final Barcode barcode) {

        if (barcode != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (!detectedBarcode.equalsIgnoreCase(barcode.displayValue)) {
                        detectedBarcode = barcode.displayValue;
                        showTextDialog(barcode.displayValue);
                    }
                }
            });
        }
    }

    private void showTextDialog(String detectedText) {

        detectedText = detectedText.replaceAll("[\\t\\n\\r]+", " ");

        productName = detectedText;
        productName = Normalizer.normalize(productName, Normalizer.Form.NFD);
        productName = productName.replaceAll("[^\\p{ASCII}]", "");
        if (((MainActivity) mContext).dtoUser != null) {
            wsCallProductDetails();
        } else {
            ((MainActivity) mContext).showLoginDialog();
        }

//        final Dialog d = new Dialog(mContext);
//        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        d.setContentView(R.layout.dialog_detected_text);
//        d.setCancelable(true);
//        d.setCanceledOnTouchOutside(true);
//
//        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
//
//        d.getWindow().getAttributes().windowAnimations = (isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);//R.style.DialogAnimation
//
//        final EditText edt_detected_text = d.findViewById(R.id.edt_detected_text);
//        edt_detected_text.setText(detectedText);
//
//        edt_detected_text.setSelection(edt_detected_text.getText().length());
//
//        edt_detected_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edt_detected_text.setCursorVisible(true);
//            }
//        });
//
//        TextView txt_viewproduct = d.findViewById(R.id.txt_viewproduct);
//        txt_viewproduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Utility.validateStringPresence(edt_detected_text)) {
//
//                    productName = edt_detected_text.getText().toString();
//                    productName = Normalizer.normalize(productName, Normalizer.Form.NFD);
//                    productName = productName.replaceAll("[^\\p{ASCII}]", "");
//
//                    if (((MainActivity) mContext).dtoUser != null) {
//                        wsCallProductDetails();
//                        d.dismiss();
//                    } else {
//                        //Toast.makeText(mContext, "Please login", Toast.LENGTH_SHORT).show();
//                        ((MainActivity) mContext).showLoginDialog();
//                        d.dismiss();
//                    }
//
//                }
//                d.dismiss();
//            }
//        });
//        d.show();
    }

    public void wsCallProductDetails() {

        if (Utility.isNetworkAvailable(mContext)) {

            try {


                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY), tinyDB.getString(UserDefaults.ENCODE_KEY_IV), ((MainActivity) mContext).dtoUser.getGuid());

                Attribute attribute = new Attribute();
                attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                attribute.setProduct_name(productName);
                attribute.setAccess_key(encodeString);
                attribute.setSecret_key(userToken);
                attribute.setFlag(1); //if 1 - barcode scan, 0 - product scan

                new WebserviceWrapper(mContext, attribute, LifyzerFragment.this, true, getString(R.string.Loading_msg))
                        .new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.PRODUCT_DETAILS.getTypeCode());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

        } else {
            // noInternetConnection(getString(R.string.no_internet_connection));
        }
    }

    public void getCurrentFrag() {
        Fragment f = getFragmentManager().findFragmentById(R.id.frame_fragment);
        if (f instanceof ScanFragment) {
            ScanFragment scanFragment = (ScanFragment) f;
            scanFragment.wsCallProductDetails();
        } else if (f instanceof BarcodeScannerFragment) {
            wsCallProductDetails();
        }
    }

    public void saveCurrentTab() {

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
        if (currentFragment != null) {
            if (currentFragment instanceof BarcodeScannerFragment) {
                tinyDB.putInt(UserDefaults.CURRENT_FRAG, UserDefaults.CAM_STATE.BARCODE);
            } else if (currentFragment instanceof ScanFragment) {
                tinyDB.putInt(UserDefaults.CURRENT_FRAG, UserDefaults.CAM_STATE.SCANNING);
            }
        }
    }

}
