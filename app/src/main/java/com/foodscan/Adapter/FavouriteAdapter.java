
package com.foodscan.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.foodscan.Activity.MainActivity;
import com.foodscan.Activity.ProductDetailsActivity;
import com.foodscan.R;
import com.foodscan.Utility.TinyDB;
import com.foodscan.Utility.UserDefaults;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.helper.Attribute;
import com.foodscan.WsHelper.helper.WebserviceWrapper;
import com.foodscan.WsHelper.model.DTOProduct;
import com.foodscan.WsHelper.model.DTOResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.SimpleViewHolder> implements WebserviceWrapper.WebserviceResponse {

    private static final String TAG = FavouriteAdapter.class.getSimpleName();

    private Context mContext;
    private TinyDB tinyDB;
    private Fragment fragment;
//    private DTOUser dtoUser;
//    private Realm realm;

    //private ArrayList<DTOProduct> arrayList = new ArrayList<>();

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    //public FavouriteAdapter(Context context, ArrayList<DTOProduct> arrayList) {
    public FavouriteAdapter(Context context) {
        this.mContext = context;
        //this.arrayList = arrayList;
        tinyDB = new TinyDB(mContext);


//        realm = Realm.getDefaultInstance();
//        //dtoUser = realm.where(DTOUser.class).findFirst();
//        dtoUser = ((MainActivity)mContext).dtoUser;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_item_without_swipe, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        final DTOProduct dtoProduct = ((MainActivity) mContext).favArrayList.get(position);
        if (dtoProduct != null) {

            if (dtoProduct.getProductImage() != null && dtoProduct.getProductImage().length() > 0) {
                Picasso.with(mContext).load(dtoProduct.getProductImage()).placeholder(R.drawable.img_food_placeholder_small).into(viewHolder.img_food);
            } else {
                viewHolder.img_food.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_food_placeholder_small));
            }

            viewHolder.txt_product_name.setText(((MainActivity) mContext).favArrayList.get(position).getProductName());


            // Disable the Healthy/Unhealthy indicator.
            // Can be re-enabled later on when it will be fully implemented in the database.
            /*
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
            //*/

            /* Shows if the product is organic */
            String isOrganic = dtoProduct.getIsOrganic();
            if (isOrganic != null && isOrganic.length() > 0 && isOrganic.equals("1")) {
                //Natural product
                viewHolder.txt_product_type.setText(mContext.getString(R.string.Natural_Product));
            }

            String createdDate = dtoProduct.getFavouriteCreatedDate();
            if (createdDate != null && createdDate.length() > 0) {

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                String history_date = simpleDate.format(Utility.stringToDate(createdDate, "yyyy-MM-dd hh:mm:ss"));
                viewHolder.txt_created_date.setText(history_date);

            }

            viewHolder.img_remove_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                    String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY), tinyDB.getString(UserDefaults.ENCODE_KEY_IV), ((MainActivity) mContext).dtoUser.getGuid());

                    if (Utility.isNetworkAvailable(mContext)) {

                        Attribute attribute = new Attribute();
                        attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                        attribute.setProduct_id(String.valueOf(((MainActivity) mContext).favArrayList.get(position).getId()));
                        attribute.setIs_favourite("0");
                        ((MainActivity) mContext).favArrayList.get(position).setIsFavourite("0");

                        attribute.setAccess_key(encodeString);
                        attribute.setSecret_key(userToken);


                        ((MainActivity) mContext).updateFavFrag(((MainActivity) mContext).favArrayList.get(position));

                        ((MainActivity) mContext).favArrayList.remove(position);
                        notifyDataSetChanged();

                        new WebserviceWrapper(mContext, attribute, FavouriteAdapter.this, false, mContext.getString(R.string.Loading_msg)).new WebserviceCaller()
                                .execute(WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode());

                        /** Update main array of favourite and history */
                        ((MainActivity) mContext).favouriteBlank();

                        Utility.showLongSnackBar(((MainActivity) mContext).frame_main,
                                mContext.getString(R.string.product_removed_from_favorite), mContext);


                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            viewHolder.img_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userToken = tinyDB.getString(UserDefaults.USER_TOKEN);
                    String encodeString = Utility.encode(tinyDB.getString(UserDefaults.ENCODE_KEY), tinyDB.getString(UserDefaults.ENCODE_KEY_IV), ((MainActivity) mContext).dtoUser.getGuid());

                    if (Utility.isNetworkAvailable(mContext)) {

                        Attribute attribute = new Attribute();
                        attribute.setUser_id(String.valueOf(((MainActivity) mContext).dtoUser.getId()));
                        attribute.setProduct_id(String.valueOf(((MainActivity) mContext).favArrayList.get(position).getId()));
                        attribute.setIs_favourite("0");
                        ((MainActivity) mContext).favArrayList.get(position).setIsFavourite("0");

                        attribute.setAccess_key(encodeString);
                        attribute.setSecret_key(userToken);


                        ((MainActivity) mContext).updateFavFrag(((MainActivity) mContext).favArrayList.get(position));

                        ((MainActivity) mContext).favArrayList.remove(position);
                        notifyDataSetChanged();

                        new WebserviceWrapper(mContext, attribute, FavouriteAdapter.this, true, mContext.getString(R.string.Loading_msg)).new WebserviceCaller()
                                .execute(WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode());

                        //************* update main array of favourite and history **************//
                        ((MainActivity) mContext).favouriteBlank();


                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            viewHolder.card_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productDetails", dtoProduct);
                    ((MainActivity) mContext).startActivityForResult(intent, UserDefaults.REQ_DETAILS);
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return ((MainActivity) mContext).favArrayList.size();
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView txt_product_name, txt_is_healthy, txt_product_type, txt_created_date;
        ImageView img_favourite, img_food, img_remove_favourite;
        CardView card_parent;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            card_parent = itemView.findViewById(R.id.card_parent);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            //txt_is_healthy = itemView.findViewById(R.id.txt_is_healthy);
            txt_product_type = itemView.findViewById(R.id.txt_product_type);
            txt_created_date = itemView.findViewById(R.id.txt_created_date);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_food = itemView.findViewById(R.id.img_food);
            img_remove_favourite = itemView.findViewById(R.id.img_remove_favourite);
        }
    }

    public void setArrayList(ArrayList<DTOProduct> arrayList) {
        //this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onResponse(int apiCode, Object object, Exception error) {

        if (apiCode == WebserviceWrapper.WEB_CALLID.FAVOURITE.getTypeCode()) {
            if (object != null) {
                try {
                    DTOResponse dtoResponse = (DTOResponse) object;
                    if (dtoResponse.getStatus().equalsIgnoreCase(UserDefaults.SUCCESS_STATUS)) {
                        //tinyDB.putBoolean(UserDefaults.NEED_REFRESH_FAVOURITE, true);
                        //tinyDB.putBoolean(UserDefaults.NEED_REFRESH_HISTORY, true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

            }
        }

    }
}
