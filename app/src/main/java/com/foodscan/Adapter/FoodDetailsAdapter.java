
package com.foodscan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOProduct;


public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.SimpleViewHolder> {

    private Context mContext;
    private DTOProduct dtoProduct;

    public FoodDetailsAdapter(Context context, DTOProduct dtoProduct) {
        this.mContext = context;
        this.dtoProduct = dtoProduct;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_data, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        if (dtoProduct != null) {

            if (position == 0) {

                //************** Organic  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_organic);

            } else if (position == 1) {

                //************** Protein  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_protein);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Protein));
                viewHolder.txt_content_amount.setText(dtoProduct.getProtein() + "g");

            } else if (position == 2) {

                //************** Fiber  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_fiber);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Fiber));
                viewHolder.txt_content_amount.setText(dtoProduct.getDietaryFiber() + "g");

            } else if (position == 3) {

                //************** Salt  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_salt);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Salt));
                viewHolder.txt_content_amount.setText(dtoProduct.getSalt() + "g");

            }

        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        ImageView img_content;
        TextView txt_content_name, txt_content_comments, txt_content_amount;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            img_content = itemView.findViewById(R.id.img_content);
            txt_content_name = itemView.findViewById(R.id.txt_content_name);
            txt_content_comments = itemView.findViewById(R.id.txt_content_comments);
            txt_content_amount = itemView.findViewById(R.id.txt_content_amount);

        }
    }


}
