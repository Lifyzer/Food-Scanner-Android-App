package com.foodscan.Activity;

import android.app.Dialog;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Fragment.HistoryFragment;
import com.foodscan.Fragment.ProfileFragment;
import com.foodscan.Fragment.ScanFragment;
import com.foodscan.R;
import com.foodscan.Utility.Utility;
import com.rey.material.app.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,  ViewPager.OnPageChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private RelativeLayout rl_history, rl_scan_food, rl_profile;
    private FrameLayout frame_history, frame_scan, frame_profile;
    private ImageView img_history, img_scan, img_profile;
    private TextView txt_history, txt_scan_food, txt_profile;

    public boolean needDialog = false;

    private TabLayout tabLayout;
    public ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        initView();
        initGlobals();
    }

    private void initView() {


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        tabLayout.setSmoothScrollingEnabled(true);
        viewPager.addOnPageChangeListener(MainActivity.this);

        int pos =1;
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

    private void createViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HistoryFragment(), getString(R.string.History));
        adapter.addFrag(new ScanFragment(), getString(R.string.Scan_Food));
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
            Log.e(TAG, "" + e.getMessage());
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


        if(pos == 0){

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_selected ,0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_not_selected ,0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_not_selected ,0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

        }
        else if(pos == 1){

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_not_selected ,0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_selected ,0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_not_selected ,0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

        }
        else if(pos == 2){

            TextView txt0 = tabView0.findViewById(R.id.tab);
            txt0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_history_not_selected ,0, 0);
            txt0.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt1 = tabView1.findViewById(R.id.tab);
            txt1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_scan_not_selected ,0, 0);
            txt1.setTextColor(Utility.getColorWrapper(mContext, R.color.unselected_banner_text));

            TextView txt2 = tabView2.findViewById(R.id.tab);
            txt2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_profile_selected ,0, 0);
            txt2.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
}
