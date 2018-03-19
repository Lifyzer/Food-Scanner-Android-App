
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

                String isOrganic = dtoProduct.getIsOrganic();
                if (isOrganic != null && isOrganic.length() > 0) {

                } else {
                    isOrganic = "0";
                }

                if (isOrganic.equals("1")) {
                    viewHolder.txt_content_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.circle_bg_green_small, 0);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Organic));
                    viewHolder.txt_content_comments.setText(mContext.getString(R.string.Natural_Product));

                } else {
                    viewHolder.txt_content_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.circle_bg_red_small, 0);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.not_Organic));
                    viewHolder.txt_content_comments.setText(mContext.getString(R.string.Not_Natural_Product));
                }
                viewHolder.img_content.setImageResource(R.drawable.img_organic);

            } else if (position == 1) {

                //************** Protein  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_protein);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Protein));
                viewHolder.txt_content_amount.setText(dtoProduct.getProtein() + "g");

            } else if (position == 2) {

                //************** Sugar  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_sugar);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Sugar));
                viewHolder.txt_content_amount.setText(dtoProduct.getSugar() + "g");

            } else if (position == 3) {

                //************** Salt  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_salt);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Salt));
                viewHolder.txt_content_amount.setText(dtoProduct.getSalt() + "g");

            } else if (position == 4) {

                //************** Ingrediants  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_ingrediants);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Ingrediants));
                viewHolder.txt_content_comments.setText(dtoProduct.getIngrediants());

            } else if (position == 5) {

                //************** Fat  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_fat);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Fat));
                viewHolder.txt_content_amount.setText(dtoProduct.getFatAmount() + "g");

            } else if (position == 6) {

                //************** Calories  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_calories);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Calories));
                viewHolder.txt_content_amount.setText(dtoProduct.getCalories() + "g");

            } else if (position == 7) {

                //************** vitamin  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_vitamin);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Vitamin));
                viewHolder.txt_content_comments.setText(dtoProduct.getVitamin() );

            }else if (position == 8) {

                //************** Saturated Fats  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_saturated_fats);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Saturated_fat));
                viewHolder.txt_content_amount.setText(dtoProduct.getSaturatedFats() + "g" );

            }else if (position == 9) {

                //************** Carbohydrate  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_carbohydrate);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.Carbohydrate));
                viewHolder.txt_content_amount.setText(dtoProduct.getCarbohydrate()+ "g" );

            }else if (position == 10) {

                //************** Dietry Fiber  **************//
                viewHolder.img_content.setImageResource(R.drawable.img_fiber);
                viewHolder.txt_content_name.setText(mContext.getString(R.string.dietry_fiber));
                viewHolder.txt_content_amount.setText(dtoProduct.getDietaryFiber()+ "g" );

            }

        }

    }

    @Override
    public int getItemCount() {
        return 11;
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
