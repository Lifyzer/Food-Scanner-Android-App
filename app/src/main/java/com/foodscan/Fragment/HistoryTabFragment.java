package com.foodscan.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.swipe.util.Attributes;
import com.foodscan.Activity.MainActivity;
import com.foodscan.Activity.ProductDetailsActivity;
import com.foodscan.Adapter.HistoryAdapter;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOHistoryData;
import com.foodscan.WsHelper.model.DTOProduct;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by c157 on 22/01/18.
 */

public class HistoryTabFragment extends Fragment implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = HistoryTabFragment.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Realm realm;
    //public DTOUser dtoUser;

    private View viewFragment;

    private RelativeLayout rl_parent, rl_no_data;
    private RecyclerView rv_history;
    //private TextView txt_no_history;
    private ProgressBar load_more_progressbar;

    private HistoryAdapter historyAdapter;
    //private ArrayList<DTOProduct> historyArrayList = new ArrayList<>();

    public boolean isViewShown = false, isLoadingFirstTime = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisibleItemIndex;
    private LinearLayoutManager mLayoutManager;

    private int offset = 0;
    private String noOfRecords = UserDefaults.REQ_NO_OF_RECORD;
    private boolean mIsLoading = false;
    private boolean isMoreData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.history_tab_fragment, container, false);

            mContext = getActivity();
            tinyDB = new TinyDB(mContext);
            realm = Realm.getDefaultInstance();

            //dtoUser = realm.where(DTOUser.class).findFirst();
        }

        return viewFragment;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewFragment != null) {
            initView();
            initGlobals();

            HistoryFragment parentFrag = ((HistoryFragment) HistoryTabFragment.this.getParentFragment());

            if (!isViewShown) {
                if (((MainActivity) mContext).viewPager.getCurrentItem() == 0) {

                    if (parentFrag.viewPager.getCurrentItem() == 0) {
                        if (isLoadingFirstTime) {

                            wsCallGetUSerHistiory(true, false);
                            isLoadingFirstTime = false;

                        } else {

                            if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_HISTORY)) {
                                if (parentFrag.viewPager != null) {
                                    if (parentFrag.viewPager.getCurrentItem() == 0) {
                                        if (parentFrag.viewPagerAdapter != null) {
                                            Fragment fragment = parentFrag.viewPagerAdapter.getItem(0);
                                            if (fragment instanceof HistoryTabFragment) {

                                                HistoryTabFragment historyTabFragment = (HistoryTabFragment) fragment;
                                                historyTabFragment.refreshData();
                                                tinyDB.putBoolean(UserDefaults.NEED_REFRESH_HISTORY, false);

                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null) {
                isViewShown = true;
                if (isLoadingFirstTime) {

                    wsCallGetUSerHistiory(true, false);
                    isLoadingFirstTime = false;

                } else {

                    if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_HISTORY)) {

                        HistoryFragment parentFrag = ((HistoryFragment) HistoryTabFragment.this.getParentFragment());

                        if (parentFrag.viewPager != null) {
                            if (parentFrag.viewPager.getCurrentItem() == 0) {
                                if (parentFrag.viewPagerAdapter != null) {
                                    Fragment fragment = parentFrag.viewPagerAdapter.getItem(0);
                                    if (fragment instanceof HistoryTabFragment) {

                                        HistoryTabFragment historyTabFragment = (HistoryTabFragment) fragment;
                                        historyTabFragment.refreshData();
                                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_HISTORY, false);

                                    }
                                }
                            }
                        }
                    }

                }

                isLoadingFirstTime = false;
            } else {
                isViewShown = false;

            }
        }
    }

    private void initView() {

        rl_parent = viewFragment.findViewById(R.id.rl_parent);
        rv_history = viewFragment.findViewById(R.id.rv_history);
        //txt_no_history = viewFragment.findViewById(R.id.txt_no_history);
        rl_no_data = viewFragment.findViewById(R.id.rl_no_data);
        load_more_progressbar = viewFragment.findViewById(R.id.load_more_progressbar);

    }

    private void initGlobals() {

        mLayoutManager = new LinearLayoutManager(mContext);
        rv_history.setLayoutManager(mLayoutManager);

        //historyAdapter = new HistoryAdapter(mContext, ((MainActivity)mContext).historyArrayList, new HistoryTabFragment());
        historyAdapter = new HistoryAdapter(mContext, new HistoryTabFragment());
        historyAdapter.setMode(Attributes.Mode.Single);
        rv_history.setAdapter(historyAdapter);

        noDataFound();

        rv_history.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                int lastInScreen = pastVisiblesItems + visibleItemCount;

                if ((lastInScreen == totalItemCount) && totalItemCount != 0) {

                    if (!mIsLoading) {
                        if (isMoreData) {
                            wsCallGetUSerHistiory(false, true);
                        }
                    }
                }
            }
        });


    }

    public void wsCallGetUSerHistiory(boolean isProgress, boolean isLoadMore) {

        if (((MainActivity) mContext).dtoUser != null) {

            if (Utility.isNetworkAvailable(mContext)) {

                try {

                    mIsLoading = true;

                    String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                    String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, ((MainActivity) mContext).dtoUser.getGuid());

                    if (isLoadMore) {
                        load_more_progressbar.setVisibility(View.VISIBLE);
                    }

                    Attribute attribute = new Attribute();
                    attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                    attribute.setTo_index(noOfRecords);
                    attribute.setFrom_index(String.valueOf(offset));
                    attribute.setAccess_key(encodeString);
                    attribute.setSecret_key(userToken);

                    new WebserviceWrapper(mContext, attribute, HistoryTabFragment.this, isProgress, getString(R.string.Loading_msg)).new WebserviceCaller()
                            .execute(WebserviceWrapper.WEB_CALLID.HISTORY.getTypeCode());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }

            } else {
                noInternetconnection(getString(R.string.no_internet_connection));
            }

        } else {
            ((MainActivity) mContext).showLoginDialog();
        }

    }

    private void noInternetconnection(String message) {

        com.rey.material.app.Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);

                wsCallGetUSerHistiory(true, false);

            }
        };

        ((SimpleDialog.Builder) builder).message(message)
                .title(getString(R.string.app_name))
                .positiveAction("CANCEL")
                .negativeAction("RETRY");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getChildFragmentManager(), null);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_DETAILS) {

            if (data != null) {

                if (data.hasExtra("productDetails")) {

                    DTOProduct dtoProduct = data.getParcelableExtra("productDetails");

                }
            }
        }
    }

    private static int REQ_DETAILS = 100;


    public void productDetails(DTOProduct dtoProduct, int pos) {

        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("productDetails", dtoProduct);
        startActivityForResult(intent, REQ_DETAILS);

    }


    @Override
    public void onResponse(int apiCode, Object object, Exception error) {



        if (apiCode == WebserviceWrapper.WEB_CALLID.HISTORY.getTypeCode()) {

            mIsLoading = false;
            load_more_progressbar.setVisibility(View.GONE);

            if (object != null) {
                try {

                    DTOHistoryData dtoHistoryData = (DTOHistoryData) object;
                    if (dtoHistoryData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoHistoryData.getHistory() != null && dtoHistoryData.getHistory().size() > 0) {

                            ArrayList<DTOProduct> tempSelfieArrayList = new ArrayList<>();
                            tempSelfieArrayList = dtoHistoryData.getHistory();

                            if (tempSelfieArrayList != null) {

                                offset = offset + tempSelfieArrayList.size();
                                isMoreData = tempSelfieArrayList.size() == UserDefaults.NO_OF_RECORD;
                                ((MainActivity) mContext).historyArrayList.addAll(tempSelfieArrayList);

                            } else {
                                isMoreData = false;
                            }

                            // historyArrayList = dtoHistoryData.getHistory();

                            if (historyAdapter != null) {

                                historyAdapter.setArrayList(((MainActivity) mContext).historyArrayList);

                            } else {

                                //historyAdapter = new HistoryAdapter(mContext, ((MainActivity)mContext).historyArrayList, new HistoryTabFragment());
                                historyAdapter = new HistoryAdapter(mContext, new HistoryTabFragment());
                                historyAdapter.setMode(Attributes.Mode.Single);
                                rv_history.setAdapter(historyAdapter);

                            }
                        }

                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoHistoryData.getMessage(), mContext);
                    }

                    noDataFound();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }

    private void noDataFound() {
        if (((MainActivity) mContext).historyArrayList != null && ((MainActivity) mContext).historyArrayList.size() > 0) {
            rl_no_data.setVisibility(View.GONE);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
        }
    }

    public void refreshData() {

        offset = 0;
        noOfRecords = UserDefaults.REQ_NO_OF_RECORD;
        mIsLoading = false;
        isMoreData = false;
        ((MainActivity) mContext).historyArrayList = new ArrayList<>();
        wsCallGetUSerHistiory(false, false);
    }

    public void UpdateData(){

        if(viewFragment != null){

            if (historyAdapter != null) {
                historyAdapter.setArrayList(((MainActivity) mContext).historyArrayList);
            } else {
                //historyAdapter = new HistoryAdapter(mContext, ((MainActivity)mContext).historyArrayList, new HistoryTabFragment());
                historyAdapter = new HistoryAdapter(mContext, new HistoryTabFragment());
                historyAdapter.setMode(Attributes.Mode.Single);
                rv_history.setAdapter(historyAdapter);
            }
        }
    }

}
