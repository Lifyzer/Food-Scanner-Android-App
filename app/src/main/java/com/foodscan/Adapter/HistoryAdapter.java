
package com.foodscan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.foodscan.R;
import com.foodscan.Utility.Utility;
import com.foodscan.WsHelper.model.DTOProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HistoryAdapter extends RecyclerSwipeAdapter<HistoryAdapter.SimpleViewHolder> {

    private static final String TAG = HistoryAdapter.class.getSimpleName();

    private Context mContext;


    private ArrayList<DTOProduct> arrayList = new ArrayList<>();


    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public HistoryAdapter(Context context, ArrayList<DTOProduct> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;

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

            String createdDate = dtoProduct.getCreatedDate();
            if (createdDate != null && createdDate.length() > 0) {

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                String history_date = simpleDate.format(Utility.stringToDate(createdDate, "yyyy-MM-dd hh:mm:ss"));
                viewHolder.txt_created_date.setText(history_date);

            }

            String modifiedDate = dtoProduct.getModifiedDate();
            if (modifiedDate != null && modifiedDate.length() > 0) {

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                String history_date = simpleDate.format(Utility.stringToDate(modifiedDate, "yyyy-MM-dd hh:mm:ss"));
                viewHolder.txt_created_date.setText(history_date);

            }

            String isFavourite = dtoProduct.getIsFavourite();
            if (isFavourite != null && isFavourite.length() > 0) {
                if (isFavourite.equals("1")) {
                    viewHolder.img_favourite.setImageResource(R.drawable.img_favourite_solid_green);
                } else {
                    viewHolder.img_favourite.setImageResource(R.drawable.img_favourite_stroke);
                }
            }
        }


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
        ImageView img_favourite;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_is_healthy = itemView.findViewById(R.id.txt_is_healthy);
            txt_product_type = itemView.findViewById(R.id.txt_product_type);
            txt_created_date = itemView.findViewById(R.id.txt_created_date);
            img_favourite = itemView.findViewById(R.id.img_favourite);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public void setArrayList(ArrayList<DTOProduct> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }


}
