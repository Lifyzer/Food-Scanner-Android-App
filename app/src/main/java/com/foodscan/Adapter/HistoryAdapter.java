
package com.foodscan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.foodscan.Activity.MainActivity;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOLoginData;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOResponse;
import com.foodscan.WsHelper.model.DTOUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;


public class HistoryAdapter extends RecyclerSwipeAdapter<HistoryAdapter.SimpleViewHolder> implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = HistoryAdapter.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    //private DTOUser dtoUser;
    private Realm realm;


    private ArrayList<DTOProduct> arrayList = new ArrayList<>();


    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public HistoryAdapter(Context context, ArrayList<DTOProduct> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;

        tinyDB = new TinyDB(mContext);
        realm = Realm.getDefaultInstance();
        //dtoUser = realm.where(DTOUser.class).findFirst();
        //dtoUser = ((MainActivity)mContext).dtoUser;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        DTOProduct dtoProduct = arrayList.get(position);
        if (dtoProduct != null) {

            viewHolder.txt_product_name.setText(arrayList.get(position).getProductName());

            String isHealthy = dtoProduct.getIsHealthy();

            if (isHealthy != null && isHealthy.length() > 0) {
                if (isHealthy.equals("0")) {

                    //Not healthy
                    viewHolder.txt_is_healthy.setText(mContext.getString(R.string.Poor));
                    viewHolder.txt_is_healthy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_bg_red_small, 0, 0, 0);
                    viewHolder.txt_is_healthy.setTextColor(Utility.getColorWrapper(mContext, R.color.red));

                } else if (isHealthy.equals("1")) {

                    //Healthy
                    viewHolder.txt_is_healthy.setText(mContext.getString(R.string.Excellent));
                    viewHolder.txt_is_healthy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_bg_green_small, 0, 0, 0);
                    viewHolder.txt_is_healthy.setTextColor(Utility.getColorWrapper(mContext, R.color.colorAccent));
                }
            }

            String isOrganic = dtoProduct.getIsOrganic();
            if (isOrganic != null && isOrganic.length() > 0) {
                if (isOrganic.equals("0")) {
                    //not Natural product
                    viewHolder.txt_product_type.setText(mContext.getString(R.string.Not_Natural_Product));

                } else if (isOrganic.equals("1")) {
                    //Natural product
                    viewHolder.txt_product_type.setText(mContext.getString(R.string.Natural_Product));
                }

            }

            String createdDate = dtoProduct.getHistoryCreatedDate();
            if (createdDate != null && createdDate.length() > 0) {

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                String history_date = simpleDate.format(Utility.stringToDate(createdDate, "yyyy-MM-dd hh:mm:ss"));
                viewHolder.txt_created_date.setText(history_date);

            }

//            String modifiedDate = dtoProduct.getModifiedDate();
//            if (modifiedDate != null && modifiedDate.length() > 0) {
//
//                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
//                String history_date = simpleDate.format(Utility.stringToDate(modifiedDate, "yyyy-MM-dd hh:mm:ss"));
//                viewHolder.txt_created_date.setText(history_date);
//
//            }

            String isFavourite = dtoProduct.getIsFavourite();
            if (isFavourite != null && isFavourite.length() > 0) {
                if (isFavourite.equals("1")) {
                    viewHolder.img_favourite.setImageResource(R.drawable.img_favourite_solid_green);
                } else {
                    viewHolder.img_favourite.setImageResource(R.drawable.img_favourite_stroke);
                }
            }
        }

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(mContext)) {

                    String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                    String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, ((MainActivity)mContext).dtoUser.getGuid());

                    Attribute attribute = new Attribute();
                    attribute.setHistory_id(arrayList.get(position).getHistoryId());
                    attribute.setAccess_key(encodeString);
                    attribute.setSecret_key(userToken);

                    arrayList.remove(position);
                    notifyDataSetChanged();

                    new WebserviceWrapper(mContext, attribute, HistoryAdapter.this, true, mContext.getString(R.string.Loading_msg)).new WebserviceCaller()
                            .execute(WebserviceWrapper.WEB_CALLID.REMOVE_FROM_HISTORY.getTypeCode());

                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                String encodeString = Utility.encode(UserDefaults.ENCODE_KEY, ((MainActivity)mContext).dtoUser.getGuid());

                if (Utility.isNetworkAvailable(mContext)) {

                    Attribute attribute = new Attribute();
                    attribute.setUser_id(String.valueOf(((MainActivity)mContext).dtoUser.getId()));
                    attribute.setProduct_id(String.valueOf(arrayList.get(position).getId()));
                    if (arrayList.get(position).getIsFavourite().equals("1")) {
                        attribute.setIs_favourite("0");
                        arrayList.get(position).setIsFavourite("0");
                    } else if (arrayList.get(position).getIsFavourite().equals("0")) {
                        attribute.setIs_favourite("1");
                        arrayList.get(position).setIsFavourite("1");
                    }

                    attribute.setAccess_key(encodeString);
                    attribute.setSecret_key(userToken);

                    notifyDataSetChanged();

                    new WebserviceWrapper(mContext, attribute, HistoryAdapter.this, true, mContext.getString(R.string.Loading_msg)).new WebserviceCaller()
                            .execute(WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode());

                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });


//        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//            }
//        });


        // mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView txt_product_name, txt_is_healthy, txt_product_type, txt_created_date;
        ImageView img_favourite, img_delete;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_is_healthy = itemView.findViewById(R.id.txt_is_healthy);
            txt_product_type = itemView.findViewById(R.id.txt_product_type);
            txt_created_date = itemView.findViewById(R.id.txt_created_date);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_delete = itemView.findViewById(R.id.img_delete);

            //final int pos = getAdapterPosition();



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    int pos = getAdapterPosition();
//
//
//                }
//            });


        }
    }

    public void setArrayList(ArrayList<DTOProduct> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode()){
            if (object != null){
                try {
                    DTOResponse dtoResponse = (DTOResponse) object;
                    if (dtoResponse.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)){
                        tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, true);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "" + e.getMessage());
                }

            }
        }

    }


}
