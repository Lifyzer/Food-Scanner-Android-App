package com.foodscan.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodscan.Activity.MainActivity;
import com.foodscan.Adapter.FavouriteAdapter;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOUser;
import com.foodscan.WsHelper.model.DTOUserFavouriteData;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by c157 on 22/01/18.
 */

public class ProfileFragment extends Fragment implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private Context mContext;
    //private Realm realm;
    //private DTOUser dtoUser;
    private TinyDB tinyDB;

    private View viewFragment;

    private RelativeLayout rl_parent;
    private RecyclerView rv_favourite;
    private TextView txt_username, txt_email;

    private FavouriteAdapter favouriteAdapter;
    public boolean isViewShown = false, isLoadingFirstTime = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.profile_fragment, container, false);

            mContext = getActivity();
            tinyDB = new TinyDB(mContext);
            //realm = Realm.getDefaultInstance();

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
            if (!isViewShown) {
                if (((MainActivity) mContext).viewPager.getCurrentItem() == 2) {
                    {
                        if (isLoadingFirstTime) {

                            if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                                if (((MainActivity) mContext).viewPager.getCurrentItem() == 2) {
                                    {
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

                            } else {
                                wsCallGetUserFavourite(true, false);
                            }

                            isLoadingFirstTime = false;
                        } else {

                            if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                                if (((MainActivity) mContext).viewPager.getCurrentItem() == 2) {
                                    {
                                        refreshData();
                                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, false);
                                    }
                                }

                            } else {

                                if (favouriteAdapter != null) {
                                    favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);
                                } else {
                                    //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                                    favouriteAdapter = new FavouriteAdapter(mContext);
                                    rv_favourite.setAdapter(favouriteAdapter);
                                }

                            }

                        }
                        isLoadingFirstTime = false;
                    }
                }
            }
        }
    }

    private void initView() {

        rl_parent = viewFragment.findViewById(R.id.rl_parent);
        rv_favourite = viewFragment.findViewById(R.id.rv_favourite);
        txt_username = viewFragment.findViewById(R.id.txt_username);
        txt_email = viewFragment.findViewById(R.id.txt_email);

    }

    private void initGlobals() {

        if (((MainActivity)mContext).dtoUser != null) {
            txt_email.setText(((MainActivity)mContext).dtoUser.getEmail());
            txt_username.setText(((MainActivity)mContext).dtoUser.getFirstName() + " " + ((MainActivity)mContext).dtoUser.getLastName());
        } else {
            //Toast.makeText(mContext, "Please login", Toast.LENGTH_SHORT).show();
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

                        if (((MainActivity) mContext).viewPager.getCurrentItem() == 2) {
                            {
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

                    } else {
                        wsCallGetUserFavourite(true, false);
                    }

                    isLoadingFirstTime = false;

                } else {

                    if (tinyDB.getBoolean(UserDefaults.NEED_REFRESH_FAVOURITE)) {

                        if (((MainActivity) mContext).viewPager.getCurrentItem() == 2) {
                            {
                                refreshData();
                                tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, false);
                            }
                        }

                    } else {

                        if (favouriteAdapter != null) {
                            favouriteAdapter.setArrayList(((MainActivity) mContext).favArrayList);
                        } else {
                            //favouriteAdapter = new FavouriteAdapter(mContext, ((MainActivity)mContext).favArrayList);
                            favouriteAdapter = new FavouriteAdapter(mContext);
                            rv_favourite.setAdapter(favouriteAdapter);
                        }

                    }

                }
                isLoadingFirstTime = false;
            } else {
                isViewShown = false;

            }
        }
    }

    public void wsCallGetUserFavourite(boolean isProgress, boolean isLoadMore) {
        if (((MainActivity) mContext).dtoUser != null) {

            if (Utility.isNetworkAvailable(mContext)) {

                ((MainActivity) mContext).mIsLoading = true;
                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, ((MainActivity) mContext).dtoUser.getGuid());

                if (isLoadMore) {
                    // load_more_progressbar.setVisibility(View.VISIBLE);
                }

                Attribute attribute = new Attribute();
                attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                attribute.setTo_index(((MainActivity) mContext).noOfRecords);
                attribute.setFrom_index(String.valueOf(((MainActivity) mContext).offset));
                attribute.setAccess_key(encodeString);
                attribute.setSecret_key(userToken);

                new WebserviceWrapper(mContext, attribute, ProfileFragment.this, isProgress, getString(R.string.Loading_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.USER_FAVOURITE.getTypeCode());

            } else {
                // noInternetconnection(getString(R.string.no_internet_connection));
            }

        } else {
            ((MainActivity) mContext).showLoginDialog();
        }
    }


    @Override
    public void onResponse(int apiCode, Object object, Exception error) {


        if (apiCode == WebserviceWrapper.WEB_CALLID.USER_FAVOURITE.getTypeCode()) {

            ((MainActivity) mContext).mIsLoading = false;
            ((MainActivity) mContext).isFavLoaded = true;

            //load_more_progressbar.setVisibility(View.GONE);
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

                    //noDataFound();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }
            }
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

    public void afterLogin() {
        {
            wsCallGetUserFavourite(true, false);
            txt_email.setText(((MainActivity)mContext).dtoUser.getEmail());
            txt_username.setText(((MainActivity)mContext).dtoUser.getFirstName() + " " + ((MainActivity)mContext).dtoUser.getLastName());
        }
    }


}
