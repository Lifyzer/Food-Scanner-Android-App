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

import com.daimajia.swipe.util.Attributes;
import com.foodscan.Activity.MainActivity;
import com.foodscan.Adapter.HistoryAdapter;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOHistoryData;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOUser;
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
    private DTOUser dtoUser;

    private View viewFragment;

    private RelativeLayout rl_parent;
    private RecyclerView rv_history;
    private TextView txt_no_history;

    private HistoryAdapter historyAdapter;
    private ArrayList<DTOProduct> historyArrayList = new ArrayList<>();

    private boolean isViewShown = false, isLoadingFirstTime = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.history_tab_fragment, container, false);

            mContext = getActivity();
            tinyDB = new TinyDB(mContext);
            realm = Realm.getDefaultInstance();

            dtoUser = realm.where(DTOUser.class).findFirst();
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
                if (((MainActivity) mContext).viewPager.getCurrentItem() == 0){

                    if (parentFrag.viewPager.getCurrentItem() == 0) {
                        if (isLoadingFirstTime) {
                            wsCallGetUSerHistiory(true, false);
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

                    wsCallGetUSerHistiory(true, false);
                    isLoadingFirstTime = false;

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
        txt_no_history = viewFragment.findViewById(R.id.txt_no_history);

        historyAdapter = new HistoryAdapter(mContext, historyArrayList);
        historyAdapter.setMode(Attributes.Mode.Single);
        rv_history.setAdapter(historyAdapter);

    }

    private void initGlobals() {

    }

    private void wsCallGetUSerHistiory(boolean isProgress, boolean isLoadMore) {

        if (Utility.isNetworkAvailable(mContext)) {

            try {

                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, dtoUser.getGuid());

                Attribute attribute = new Attribute();
                attribute.setUser_id(String.valueOf(dtoUser.getId()));
                attribute.setAccess_key(encodeString);
                attribute.setSecret_key(userToken);

                new WebserviceWrapper(mContext, attribute, HistoryTabFragment.this, true, getString(R.string.Loading_msg)).new WebserviceCaller()
                        .execute(WebserviceWrapper.WEB_CALLID.HISTORY.getTypeCode());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "" + e.getMessage());
            }

        } else {
            noInternetconnection(getString(R.string.no_internet_connection));
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
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.HISTORY.getTypeCode()) {
            if (object != null) {
                try {

                    DTOHistoryData dtoHistoryData = (DTOHistoryData) object;
                    if (dtoHistoryData.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {

                        if (dtoHistoryData.getHistory() != null && dtoHistoryData.getHistory().size() > 0) {

                            historyArrayList = dtoHistoryData.getHistory();

                            if (historyAdapter != null) {

                                historyAdapter.setArrayList(historyArrayList);

                            } else {

                                historyAdapter = new HistoryAdapter(mContext, historyArrayList);
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
        if (historyArrayList != null && historyArrayList.size() > 0) {
            txt_no_history.setVisibility(View.GONE);
        } else {
            txt_no_history.setVisibility(View.VISIBLE);
        }
    }

}
