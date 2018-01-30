package com.foodscan.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daimajia.swipe.util.Attributes;
import com.foodscan.Adapter.HistoryAdapter;
import com.foodscan.R;

/**
 * Created by c157 on 22/01/18.
 */

public class HistoryTabFragment extends Fragment {

    private static final String TAG = HistoryTabFragment.class.getSimpleName();

    private Context mContext;

    private View viewFragment;

    private RelativeLayout rl_parent;
    private RecyclerView rv_history;

    private HistoryAdapter historyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.history_tab_fragment, container, false);
            mContext = getActivity();
        }

        return viewFragment;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewFragment != null) {
            initView();
            initGlobals();
        }
    }

    private void initView() {

        rl_parent = viewFragment.findViewById(R.id.rl_parent);
        rv_history = viewFragment.findViewById(R.id.rv_history);

        historyAdapter = new HistoryAdapter(mContext);
        historyAdapter.setMode(Attributes.Mode.Single);
        rv_history.setAdapter(historyAdapter);

    }

    private void initGlobals() {

    }

}
