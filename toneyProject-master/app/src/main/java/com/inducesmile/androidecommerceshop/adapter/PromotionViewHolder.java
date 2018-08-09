package com.inducesmile.androidecommerceshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;


public class PromotionViewHolder extends RecyclerView.ViewHolder {

    public ImageView promotionImage;
    public TextView promotionProductName;
    public TextView promotionProductPrice;
    public View bView;

    public PromotionViewHolder(View itemView) {
        super(itemView);
        bView = itemView;
        promotionImage = (ImageView)itemView.findViewById(R.id.promotion_product);
        promotionProductName = (TextView)itemView.findViewById(R.id.promotion_product_name);
        promotionProductPrice = (TextView)itemView.findViewById(R.id.promotion_product_price);
    }
}
