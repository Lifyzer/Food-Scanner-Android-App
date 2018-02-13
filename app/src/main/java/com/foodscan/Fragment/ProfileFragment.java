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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.foodscan.Adapter.FavouriteAdapter;
import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOUser;

import io.realm.Realm;

/**
 * Created by c157 on 22/01/18.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private Context mContext;
    private Realm realm;
    private DTOUser dtoUser;

    private View viewFragment;

    private RelativeLayout rl_parent;
    private RecyclerView rv_favourite;
    private TextView txt_username, txt_email;

    private FavouriteAdapter favouriteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (viewFragment == null) {
            viewFragment = inflater.inflate(R.layout.profile_fragment, container, false);

            mContext = getActivity();
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
        }
    }

    private void initView() {

        rl_parent = viewFragment.findViewById(R.id.rl_parent);
        rv_favourite = viewFragment.findViewById(R.id.rv_favourite);

//        favouriteAdapter = new FavouriteAdapter(mContext);
//        favouriteAdapter.setMode(Attributes.Mode.Single);
//        rv_favourite.setAdapter(favouriteAdapter);

        txt_username = viewFragment.findViewById(R.id.txt_username);
        txt_email = viewFragment.findViewById(R.id.txt_email);

    }

    private void initGlobals() {

        if (dtoUser != null) {
            txt_email.setText(dtoUser.getEmail());
            txt_username.setText(dtoUser.getFirstName() + " " + dtoUser.getLastName());
        } else {
            //Toast.makeText(mContext, "Please login", Toast.LENGTH_SHORT).show();
        }

    }


}
