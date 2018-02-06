package com.foodscan.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Activity.MainActivity;
import com.foodscan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c157 on 22/01/18.
 */

public class HistoryFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = HistoryFragment.class.getSimpleName();

    private Context mContext;

    private View viewFragment;
    private RelativeLayout rl_parent;
    private TabLayout tabLayout;
    public ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {

            viewFragment = inflater.inflate(R.layout.history_fragment, container, false);
            mContext = getActivity();

        }

        return viewFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewFragment != null) {

            setUpControls(viewFragment);
            //initGlobal();

            if (!isViewShown) {
                if (((MainActivity) mContext).viewPager.getCurrentItem() == 0) {
                    if (isLoadingFirstTime) {
                        initGlobal();
                        isLoadingFirstTime = false;

                    }
                }
            }


        }
    }

    private void setUpControls(View rootView) {

        rl_parent = rootView.findViewById(R.id.rl_parent);
        tabLayout = rootView.findViewById(R.id.tabs);
        viewPager = rootView.findViewById(R.id.viewpager);

    }

    private boolean isViewShown = false, isLoadingFirstTime = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null) {
                isViewShown = true;
                if (isLoadingFirstTime) {

                    initGlobal();
                    isLoadingFirstTime = false;

                }
                isLoadingFirstTime = false;
            } else {
                isViewShown = false;
            }
        }
    }

    private void initGlobal() {

        createViewPager();


        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        tabLayout.setSmoothScrollingEnabled(true);
        viewPager.addOnPageChangeListener(HistoryFragment.this);

    }

    private void createViewPager() {

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(new HistoryTabFragment(), getString(R.string.History));
        viewPagerAdapter.addFrag(new FavouriteTabFragment(), getString(R.string.Favourite));

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_search_tab_textview, null);
        tabOne.setText(getString(R.string.History));
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_search_tab_textview, null);
        tabTwo.setText(getString(R.string.Favourite));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int pos = this.viewPager.getCurrentItem();
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

}
