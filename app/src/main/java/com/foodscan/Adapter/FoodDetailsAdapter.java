
package com.foodscan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodscan.R;
import com.foodscan.WsHelper.model.DTOProduct;


public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.SimpleViewHolder> {

    private static final int TOTAL_ITEMS = 11;

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
                    if (isOrganic.equals("1")) {
                        viewHolder.txt_content_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.circle_bg_green_small, 0);
                        viewHolder.txt_content_name.setText(mContext.getString(R.string.Organic));
                        viewHolder.txt_content_comments.setText(mContext.getString(R.string.Natural_Product));
                    } else { // not organic :(
                        viewHolder.txt_content_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.circle_bg_red_small, 0);
                        viewHolder.txt_content_name.setText(mContext.getString(R.string.not_Organic));
                        viewHolder.txt_content_comments.setText(mContext.getString(R.string.Not_Natural_Product));
                    }

                    viewHolder.img_content.setImageResource(R.drawable.img_organic);
                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 1) {
                //************** Protein  **************//

                String protein = dtoProduct.getProtein();
                if (protein != null && protein.length() > 0 && !protein.equals("0")) {
                    viewHolder.img_content.setImageResource(R.drawable.img_protein);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Protein));
                    viewHolder.txt_content_amount.setText(dtoProduct.getProtein() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 2) {
                //************** Sugar  **************//

                String sugar = dtoProduct.getSugar();
                if (sugar != null && sugar.length() > 0 && !sugar.equals("0")) {
                    viewHolder.img_content.setImageResource(R.drawable.img_sugar);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Sugar));
                    viewHolder.txt_content_amount.setText(dtoProduct.getSugar() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);

                }
            } else if (position == 3) {
                //************** Salt  **************//

                String salt = dtoProduct.getSalt();
                if (salt != null && salt.length() > 0 && !salt.equals("0")) {
                    viewHolder.img_content.setImageResource(R.drawable.img_salt);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Salt));
                    viewHolder.txt_content_amount.setText(dtoProduct.getSalt() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 4) {
                //************** Ingredients  **************//

                String Ingredients = dtoProduct.getIngredients();
                if (Ingredients != null && Ingredients.length() > 0) {
                    viewHolder.img_content.setImageResource(R.drawable.img_ingredients);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Ingredients));
                    viewHolder.txt_content_comments.setText(dtoProduct.getIngredients());

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 5) {
                //************** Fat  **************//

                String fat = dtoProduct.getFatAmount();
                if (fat != null && fat.length() > 0 && !fat.equals("0")) {

                    viewHolder.img_content.setImageResource(R.drawable.img_fat);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Fat));
                    viewHolder.txt_content_amount.setText(dtoProduct.getFatAmount() + "g");

                    viewHolder.setVisibility(true);

                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 6) {
                //************** Calories  **************//

                String Calories = dtoProduct.getCalories();
                if (Calories != null && Calories.length() > 0 && !Calories.equals("0")) {

                    viewHolder.img_content.setImageResource(R.drawable.img_calories);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Calories));
                    viewHolder.txt_content_amount.setText(dtoProduct.getCalories() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 7) {
                //************** vitamin  **************//

                String vitamin = dtoProduct.getVitamin();
                if (vitamin != null && vitamin.length() > 0) {

                    viewHolder.img_content.setImageResource(R.drawable.img_vitamin);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Vitamin));
                    viewHolder.txt_content_comments.setText(dtoProduct.getVitamin());

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 8) {
                //************** Saturated Fats  **************//

                String Saturated_Fats = dtoProduct.getSaturatedFats();
                if (Saturated_Fats != null && Saturated_Fats.length() > 0 && !Saturated_Fats.equals("0")) {

                    viewHolder.img_content.setImageResource(R.drawable.img_saturated_fats);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Saturated_fat));
                    viewHolder.txt_content_amount.setText(dtoProduct.getSaturatedFats() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 9) {
                //************** Carbohydrate  **************//

                String Carbohydrate = dtoProduct.getCarbohydrate();
                if (Carbohydrate != null && Carbohydrate.length() > 0 && !Carbohydrate.equals("0")) {

                    viewHolder.img_content.setImageResource(R.drawable.img_carbohydrate);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.Carbohydrate));
                    viewHolder.txt_content_amount.setText(dtoProduct.getCarbohydrate() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            } else if (position == 10) {
                //************** Dietary Fiber  **************//
                String Dietary_Fiber = dtoProduct.getDietaryFiber();
                if (Dietary_Fiber != null && Dietary_Fiber.length() > 0 && !Dietary_Fiber.equals("0")) {

                    viewHolder.img_content.setImageResource(R.drawable.img_fiber);
                    viewHolder.txt_content_name.setText(mContext.getString(R.string.dietary_fiber));
                    viewHolder.txt_content_amount.setText(dtoProduct.getDietaryFiber() + "g");

                    viewHolder.setVisibility(true);
                } else {
                    viewHolder.setVisibility(false);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return TOTAL_ITEMS;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        ImageView img_content;
        TextView txt_content_name, txt_content_comments, txt_content_amount;
        LinearLayout ll_parent;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            img_content = itemView.findViewById(R.id.img_content);
            txt_content_name = itemView.findViewById(R.id.txt_content_name);
            txt_content_comments = itemView.findViewById(R.id.txt_content_comments);
            txt_content_amount = itemView.findViewById(R.id.txt_content_amount);
            ll_parent = itemView.findViewById(R.id.ll_parent);

        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

}