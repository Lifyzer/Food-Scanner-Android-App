package com.foodscan.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Barcode.BarcodeGraphicTracker;
import com.foodscan.Fragment.HistoryFragment;
import com.foodscan.Fragment.LifyzerFragment;
import com.foodscan.Fragment.ProfileFragment;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOUser;
import com.google.android.gms.vision.barcode.Barcode;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, BarcodeGraphicTracker.BarcodeUpdateListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public FrameLayout frame_main;
    public boolean needDialog = false;
    public DTOUser dtoUser;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    public int offset = 0;
    public String noOfRecords = UserDefaults.REQ_NO_OF_RECORD;
    public boolean mIsLoading = false;
    public boolean isMoreData = false;
    public boolean isFavLoaded = false;
    public ArrayList<DTOProduct> favArrayList = new ArrayList<>();
    public ArrayList<DTOProduct> historyArrayList = new ArrayList<>();
    private Context mContext;
    private RelativeLayout rl_history, rl_scan_food, rl_profile;
    private FrameLayout frame_history, frame_scan, frame_profile;
    private ImageView img_history, img_scan, img_profile;
    private TextView txt_history, txt_scan_food, txt_profile;
    private TinyDB tinyDB;
    private Realm realm;
    //public boolean isMoreData = false;

    //public String productName = "";
    private TabLayout tabLayout;
    private int LOGIN_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_right, R.anim.translate);

        mContext = MainActivity.this;
        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        dtoUser = realm.where(DTOUser.class).findFirst();

        initView();
        initGlobals();
    }

    private void initView() {


        frame_main = findViewById(R.id.frame_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        tabLayout.setSmoothScrollingEnabled(true);
        viewPager.addOnPageChangeListener(MainActivity.this);

        int pos = 1;
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();
        this.viewPager.setCurrentItem(pos);

//        frame_history = findViewById(R.id.frame_history);
//        frame_scan = findViewById(R.id.frame_scan);
//        frame_profile = findViewById(R.id.frame_profile);
//
//        img_history = findViewById(R.id.img_history);
//        img_scan = findViewById(R.id.img_scan);
//        img_profile = findViewById(R.id.img_profile);
//
//        txt_history = findViewById(R.id.txt_history);
//        txt_scan_food = findViewById(R.id.txt_scan_food);
//        txt_profile = findViewById(R.id.txt_profile);
//
//        rl_history = findViewById(R.id.rl_history);
//        rl_scan_food = findViewById(R.id.rl_scan_food);
//        rl_profile = findViewById(R.id.rl_profile);

    }

    private void initGlobals() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            needDialog = bundle.getBoolean("needDialog", false);
            if (needDialog) {
                showRegisterSuccessDialog();
            }
        }

//        rl_history.setOnClickListener(this);
//        rl_scan_food.setOnClickListener(this);
//        rl_profile.setOnClickListener(this);
//
//        //Replace Fragment
//        replaceFragment(new ProfileFragment(), R.id.frame_profile);
//        replaceFragment(new ScanFragment(), R.id.frame_scan);
//        replaceFragment(new HistoryFragment(), R.id.frame_history);
//
//        //Highlite scan
//        img_history.setImageResource(R.drawable.img_history_not_selected);
//        img_profile.setImageResource(R.drawable.img_profile_not_selected);
//        img_scan.setImageResource(R.drawable.img_scan_selected);
//
//        txt_history.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//        txt_profile.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//        txt_scan_food.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
//
//        frame_history.setVisibility(View.GONE);
//        frame_scan.setVisibility(View.VISIBLE);
//        frame_profile.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (viewPager != null && adapter != null) {

            if (viewPager.getCurrentItem() == 2 && tinyDB.getBoolean(UserDefaults.NEED_REFRESH_USER)) {

                dtoUser = realm.where(DTOUser.class).findFirst();

                Fragment fragment = adapter.getItem(2);
                if (fragment instanceof ProfileFragment) {
                    ProfileFragment profileFragment = (ProfileFragment) fragment;
                    profileFragment.userDataChanges();
                    tinyDB.putBoolean(UserDefaults.NEED_REFRESH_USER, false);
                }
            }
        }
    }

    private void createViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HistoryFragment(), getString(R.string.History));
        //adapter.addFrag(new ScanFragment(), getString(R.string.Scan_Food));
        adapter.addFrag(new LifyzerFragment(), getString(R.string.Scan_Food));
        adapter.addFrag(new ProfileFragment(), getString(R.string.Profile));

        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.History));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_not_selected, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.Scan_Food));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_not_selected, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.Profile));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_not_selected, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

//            case R.id.rl_history: {
//
//                img_history.setImageResource(R.drawable.img_history_selected);
//                img_profile.setImageResource(R.drawable.img_profile_not_selected);
//                img_scan.setImageResource(R.drawable.img_scan_not_selected);
//
//                txt_history.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
//                txt_profile.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//                txt_scan_food.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//
//                frame_history.setVisibility(View.VISIBLE);
//                frame_scan.setVisibility(View.GONE);
//                frame_profile.setVisibility(View.GONE);
//
//            }
//            break;
//
//            case R.id.rl_scan_food: {
//
//                img_history.setImageResource(R.drawable.img_history_not_selected);
//                img_profile.setImageResource(R.drawable.img_profile_not_selected);
//                img_scan.setImageResource(R.drawable.img_scan_selected);
//
//                txt_history.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//                txt_profile.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//                txt_scan_food.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
//
//                frame_history.setVisibility(View.GONE);
//                frame_scan.setVisibility(View.VISIBLE);
//                frame_profile.setVisibility(View.GONE);
//
//            }
//            break;
//
//            case R.id.rl_profile: {
//
//                img_history.setImageResource(R.drawable.img_history_not_selected);
//                img_profile.setImageResource(R.drawable.img_profile_selected);
//                img_scan.setImageResource(R.drawable.img_scan_not_selected);
//
//                txt_history.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//                txt_profile.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
//                txt_scan_food.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));
//
//                frame_history.setVisibility(View.GONE);
//                frame_scan.setVisibility(View.GONE);
//                frame_profile.setVisibility(View.VISIBLE);
//
//            }
//            break;
        }
    }


    public void replaceFragment(Fragment fragment, int containerView) {

        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(containerView, fragment);
            transaction.addToBackStack("");
            // Commit the transaction
            transaction.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    private void showRegisterSuccessDialog() {

        final Dialog d = new Dialog(mContext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.dialog_registration_successful);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int pos = this.viewPager.getCurrentItem();
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();

    }

    @Override
    public void onPageSelected(int position) {

        int pos = this.tabLayout.getSelectedTabPosition();
        this.viewPager.setCurrentItem(pos);

        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        View tabView0 = tab0.getCustomView();

        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        View tabView1 = tab1.getCustomView();

        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        View tabView2 = tab2.getCustomView();


        if (pos == 0) {

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_selected, 0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_not_selected, 0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_not_selected, 0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            if (viewPager != null && adapter != null) {

                Fragment fragment = adapter.getItem(position);
                if (fragment instanceof HistoryFragment) {
                    HistoryFragment historyFragment = (HistoryFragment) fragment;
                    if (!tinyDB.getBoolean(UserDefaults.IS_LOGIN)) {

                    }

                }
            }


        } else if (pos == 1) {

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_not_selected, 0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_selected, 0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_not_selected, 0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

        } else if (pos == 2) {

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_not_selected, 0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_not_selected, 0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_selected, 0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                //if (fragment instanceof ScanFragment)
                if (fragment instanceof LifyzerFragment)
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        if (requestCode == LOGIN_REQ_CODE) {

            if (data != null) {

                if (data.hasExtra("user_data")) {

                    dtoUser = data.getParcelableExtra("user_data");
                    if (dtoUser != null) {

                        int currentIndex = viewPager.getCurrentItem();

                        Fragment fragment = adapter.getItem(currentIndex);

                        if (currentIndex == 0) {

                            if (fragment instanceof HistoryFragment) {
                                HistoryFragment historyFragment = (HistoryFragment) fragment;
                                historyFragment.afterLogin();
                            }

                        } else if (currentIndex == 1) {

                            //if (fragment instanceof ScanFragment) {
                            if (fragment instanceof LifyzerFragment) {
                                LifyzerFragment lifyzerFragment = (LifyzerFragment) fragment;
                                lifyzerFragment.getCurrentFrag();
                                //scanFragment.wsCallProductDetails();
                            }

                        } else if (currentIndex == 2) {

                            ProfileFragment profileFragment = (ProfileFragment) fragment;
                            profileFragment.afterLogin();
                            //profileFragment.wsCallGetUserFavourite(true, false);

                        }

                        try {
                            Fragment fragment1 = adapter.getItem(0);
                            if (fragment1 instanceof HistoryFragment && currentIndex != 0) {

                                HistoryFragment historyFragment = (HistoryFragment) fragment1;
                                historyFragment.isLoadingFirstTime = true;

                            }

//                            Fragment fragment2 = adapter.getItem(1);
//                            if (fragment2 instanceof ScanFragment) {
//
//                            }

                            Fragment fragment3 = adapter.getItem(2);
                            if (fragment3 instanceof ProfileFragment && currentIndex != 2) {

                                ProfileFragment profileFragment = (ProfileFragment) fragment3;
                                profileFragment.isLoadingFirstTime = true;

                            }


                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());

                        }
                    }
                }
            }
        } else if (requestCode == UserDefaults.REQ_DETAILS) {

            if (data != null) {

                if (data.hasExtra("productDetails")) {

                    DTOProduct dtoProduct = data.getParcelableExtra("productDetails");
                    //***********  For history ***************//
                    for (int i = 0; i < historyArrayList.size(); i++) {

                        if (historyArrayList.get(i).getId().equals(dtoProduct.getId())) {

                            try {

                                //historyArrayList.set(i, dtoProduct);
                                historyArrayList.get(i).setIsFavourite(dtoProduct.getIsFavourite());

                                Fragment fragment1 = adapter.getItem(0);
                                if (fragment1 instanceof HistoryFragment) {

                                    HistoryFragment historyFragment = (HistoryFragment) fragment1;
                                    historyFragment.updateAdapter(dtoProduct);

                                }

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                            break;
                        }
                    }

                    //***********  For favourites ***************//

                    Fragment fragment1 = adapter.getItem(0);
                    if (fragment1 instanceof HistoryFragment) {

                        HistoryFragment historyFragment = (HistoryFragment) fragment1;
                        historyFragment.updateFavourite(dtoProduct);

                    }

                    Fragment fragment2 = adapter.getItem(2);
                    if (fragment2 instanceof ProfileFragment) {
                        ProfileFragment profileFragment = (ProfileFragment) fragment2;
                        profileFragment.updateFavourite(dtoProduct);
                    }

//                    for (int i = 0; i < favArrayList.size(); i++) {
//
//                        if (favArrayList.get(i).getId().equals(dtoProduct.getId())) {
//                            try {
//                                if (dtoProduct.getIsFavourite().equals("1")) {
//
//
//                                } else {
//                                    favArrayList.remove(i);
//                                    Fragment fragment1 = adapter.getItem(0);
//                                    if (fragment1 instanceof HistoryFragment) {
//                                        HistoryFragment historyFragment = (HistoryFragment) fragment1;
//                                        historyFragment.updateFavourite(dtoProduct);
//                                    }
//                                }
//
//
//                            } catch (Exception e) {
//                                Log.e(TAG, e.getMessage());
//                            }
//
//                        }
//
//                        break;
//
//                    }
                }

            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        if(viewPager.getCurrentItem() == 1){
//
//            if(adapter != null){
//                Fragment fragment = adapter.getItem(1);
//
//                if(fragment instanceof ScanFragment){
//
//                    ScanFragment scanFragment = (ScanFragment)fragment;
//                    return scanFragment.getTouchEvent(event);
//
//                }
//
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    public void updateFavFrag(DTOProduct dtoProduct) {

        for (int i = 0; i < historyArrayList.size(); i++) {

            if (historyArrayList.get(i).getId().equals(dtoProduct.getId())) {

                try {

                    //historyArrayList.set(i, dtoProduct);
                    historyArrayList.get(i).setIsFavourite(dtoProduct.getIsFavourite());

                    Fragment fragment1 = adapter.getItem(0);
                    if (fragment1 instanceof HistoryFragment) {

                        HistoryFragment historyFragment = (HistoryFragment) fragment1;
                        historyFragment.updateAdapter(dtoProduct);

                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                break;
            }
        }
    }

    public void updateinHistoryFrag() {

    }

    public void showLoginDialog() {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);

                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivityForResult(intent, LOGIN_REQ_CODE);

            }
        };

        ((SimpleDialog.Builder) builder).message(getString(R.string.please_login))
                .title(getString(R.string.app_name))
                .positiveAction("CANCEL")
                .negativeAction("LOGIN");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void historyBlank() {
        if (viewPager != null && adapter != null) {

            Fragment fragment = adapter.getItem(0);
            if (fragment != null) {
                HistoryFragment historyFragment = (HistoryFragment) fragment;
                historyFragment.historyBlank();
            }
        }
    }

    public void favouriteBlank() {
        if (viewPager != null && adapter != null) {

            Fragment frag0 = adapter.getItem(0);
            if (frag0 instanceof HistoryFragment) {
                HistoryFragment historyFragment = (HistoryFragment) frag0;
                historyFragment.favouriteBlank();
            }

            Fragment frag2 = adapter.getItem(2);
            if (frag2 instanceof ProfileFragment) {
                ProfileFragment profileFragment = (ProfileFragment) frag2;
                profileFragment.noDataFound();
            }
        }
    }


    @Override
    public void onBarcodeDetected(Barcode barcode) {

        if (barcode != null) {

            Log.e(TAG, "detected");
            if (viewPager.getCurrentItem() == 1) {
                Fragment fragment = adapter.getItem(1);
                if (fragment instanceof LifyzerFragment) {
                    LifyzerFragment lifyzerFragment = (LifyzerFragment) fragment;
                    lifyzerFragment.displayDialog(barcode);
                }
            }

        }

    }

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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        //Toast.makeText(mContext, "On back press", Toast.LENGTH_SHORT).show();
//
//        if(viewPager != null && (viewPager.getCurrentItem() == 1)){
//
//            Fragment fragment = adapter.getItem(1);
//            if(fragment != null && (fragment instanceof  LifyzerFragment)){
//
//                LifyzerFragment lifyzerFragment = (LifyzerFragment)fragment;
//                lifyzerFragment.saveCurrentTab();
//            }
//        }
//    }
}
