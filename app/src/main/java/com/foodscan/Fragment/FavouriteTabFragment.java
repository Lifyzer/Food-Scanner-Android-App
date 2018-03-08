package com.foodscan.Fragment;


import android.content.Context;
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
import com.foodscan.Adapter.FavouriteAdapter;
import com.foodscan.Adapter.HistoryAdapter;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOUserFavouriteData;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import java.util.ArrayList;

/**
 * Created by c157 on 22/01/18.
 */

public class FavouriteTabFragment extends Fragment implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = FavouriteTabFragment.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;

    private View viewFragment;



    public boolean isViewShown = false, isLoadingFirstTime = true;
//    private int offset = 0;
//    private String noOfRecords = UserDefaults.REQ_NO_OF_RECORD;
//    private boolean mIsLoading = false;
//    private boolean isMoreData = false;

    public RelativeLayout rl_parent, rl_no_data;
    private RecyclerView rv_favourite;
    private ProgressBar load_more_progressbar;

    private LinearLayoutManager mLayoutManager;
    private FavouriteAdapter favouriteAdapter;
    //private ArrayList<DTOProduct> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.favourite_tab_fragment, container, false);
            mContext = getActivity();
            tinyDB = new TinyDB(mContext);


        }

        return viewFragment;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewFragment != null) {
            initView();
            initGlobals();

            HistoryFragment parentFrag = ((HistoryFragment) FavouriteTabFragment.this.getParentFragment());

            if (!isViewShown) {
                if (((MainActivity) mContext).viewPager.getCurrentItem() == 0) {

                    if (parentFrag.viewPager.getCurrentItem() == 1) {
                        if (isLoadingFirstTime) {

                            if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                                if (parentFrag.viewPagerAdapter != null) {
                                    Fragment fragment = parentFrag.viewPagerAdapter.getItem(1);
                                    if (fragment instanceof FavouriteTabFragment) {
                                        refreshData();
                                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, false);

                                    }
                                }

                            } else if (((MainActivity) mContext).isFavLoaded) {
                                if (favouriteAdapter != null) {
                                    favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);
                                } else {
                                    //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                                    favouriteAdapter = new FavouriteAdapter(mContext);
                                    rv_favourite.setAdapter(favouriteAdapter);
                                }
                                noDataFound();
                            } else {
                                wsCallGetUserFavourite(true, false);
                            }

                            //wsCallGetUserFavourite(true, false);
                            isLoadingFirstTime = false;

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

                    if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                        refreshData();
                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, false);

                    }
                    else if (((MainActivity) mContext).isFavLoaded) {
                        if (favouriteAdapter != null) {
                            favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);
                        } else {
                            //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                            favouriteAdapter = new FavouriteAdapter(mContext);
                            rv_favourite.setAdapter(favouriteAdapter);
                        }
                        noDataFound();

                    } else {
                        wsCallGetUserFavourite(true, false);
                    }

                    //wsCallGetUserFavourite(true, false);
                    isLoadingFirstTime = false;

                } else {
                    if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                        refreshData();
                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, false);

                    } else {

                        if (favouriteAdapter != null) {
                            favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);
                        } else {
                            //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                            favouriteAdapter = new FavouriteAdapter(mContext);
                            rv_favourite.setAdapter(favouriteAdapter);
                        }
                        noDataFound();

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
        rv_favourite = viewFragment.findViewById(R.id.rv_favourite);
        load_more_progressbar = viewFragment.findViewById(R.id.load_more_progressbar);
        rl_no_data = viewFragment.findViewById(R.id.rl_no_data);

    }

    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisibleItemIndex;

    private void initGlobals() {

        mLayoutManager = new LinearLayoutManager(mContext);
        rv_favourite.setLayoutManager(mLayoutManager);

        //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
        favouriteAdapter = new FavouriteAdapter(mContext);
        rv_favourite.setAdapter(favouriteAdapter);

        noDataFound();

        rv_favourite.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                int lastInScreen = pastVisiblesItems + visibleItemCount;

                if ((lastInScreen == totalItemCount) && totalItemCount != 0) {

                    if (!((MainActivity) mContext).mIsLoading) {
                        if (((MainActivity) mContext).isMoreData) {
                            wsCallGetUserFavourite(false, true);
                        }
                    }
                }
            }
        });

    }

    public void wsCallGetUserFavourite(boolean isProgress, boolean isLoadMore) {
        if (((MainActivity) mContext).dtoUser != null) {

            if (Utility.isNetworkAvailable(mContext)) {

                ((MainActivity) mContext).mIsLoading = true;
                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, ((MainActivity) mContext).dtoUser.getGuid());

                if (isLoadMore) {
                    load_more_progressbar.setVisibility(View.VISIBLE);
                }

                Attribute attribute = new Attribute();
                attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                attribute.setTo_index(((MainActivity) mContext).noOfRecords);
                attribute.setFrom_index(String.valueOf(((MainActivity) mContext).offset));
                attribute.setAccess_key(encodeString);
                attribute.setSecret_key(userToken);

                new WebserviceWrapper(mContext, attribute, FavouriteTabFragment.this, isProgress, getString(R.string.Loading_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.USER_FAVOURITE.getTypeCode());


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

                wsCallGetUserFavourite(true, false);

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
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.USER_FAVOURITE.getTypeCode()) {

            ((MainActivity) mContext).isFavLoaded = true;

            ((MainActivity) mContext).mIsLoading = false;
            load_more_progressbar.setVisibility(View.GONE);
            if (object != null) {

                try {

                    DTOUserFavouriteData dtoUserFavouriteData = (DTOUserFavouriteData) object;
                    if (dtoUserFavouriteData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoUserFavouriteData.getProduct() != null && dtoUserFavouriteData.getProduct().size() > 0) {

                            ArrayList<DTOProduct> tempSelfieArrayList = new ArrayList<>();
                            tempSelfieArrayList = dtoUserFavouriteData.getProduct();

                            if (tempSelfieArrayList != null) {

                                ((MainActivity) mContext).offset = ((MainActivity) mContext).offset + tempSelfieArrayList.size();
                                ((MainActivity) mContext).isMoreData = tempSelfieArrayList.size() == UserDefaults.NO_OF_RECORD;
                                ((MainActivity) mContext).favArrayList.addAll(tempSelfieArrayList);

                            } else {
                                ((MainActivity) mContext).isMoreData = false;
                            }

                            if (favouriteAdapter != null) {

                                favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);

                            } else {

                                //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                                favouriteAdapter = new FavouriteAdapter(mContext);
                                rv_favourite.setAdapter(favouriteAdapter);

                            }
                        }
                    } else {
                        Utility.showLongSnackBar(rl_parent, dtoUserFavouriteData.getMessage(), mContext);
                    }

                    noDataFound();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
    }

    public void noDataFound() {
        if (((MainActivity) mContext).favArrayList != null && ((MainActivity) mContext).favArrayList.size() > 0) {
            rl_no_data.setVisibility(View.GONE);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
        }
    }

    public void refreshData() {

        ((MainActivity) mContext).offset = 0;
        ((MainActivity) mContext).noOfRecords = UserDefaults.REQ_NO_OF_RECORD;
        ((MainActivity) mContext).mIsLoading = false;
        ((MainActivity) mContext).isMoreData = false;
        ((MainActivity) mContext).favArrayList = new ArrayList<>();
        wsCallGetUserFavourite(false, false);
    }

    public void UpdateData(){

        if(viewFragment != null){

            if (favouriteAdapter != null) {
                favouriteAdapter.setArrayList(((MainActivity) mContext).historyArrayList);
            } else {
                //historyAdapter = new HistoryAdapter(mContext, ((MainActivity)mContext).historyArrayList, new HistoryTabFragment());
                favouriteAdapter = new FavouriteAdapter(mContext);
                rv_favourite.setAdapter(favouriteAdapter);
            }
        }
    }

}
