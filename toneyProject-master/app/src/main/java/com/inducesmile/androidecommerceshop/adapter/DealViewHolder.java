package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class DealViewHolder extends RecyclerView.ViewHolder {

    public TextView dealName;
    public TextView dealPrice;
    public TextView dealDiscount;
    public TextView dealExpireDate;
    public TextView discountPrice;
    public ImageView dealImage;

    public View view;

    public DealViewHolder(View itemView) {
        super(itemView);

        dealName = (TextView)itemView.findViewById(R.id.product_deal_name);
        dealPrice = (TextView)itemView.findViewById(R.id.product_deal_price);
        dealDiscount = (TextView)itemView.findViewById(R.id.product_deal_discount);
        dealExpireDate = (TextView)itemView.findViewById(R.id.product_deal_expiry_date);
        discountPrice = (TextView)itemView.findViewById(R.id.discount_price);
        dealImage = (ImageView)itemView.findViewById(R.id.product_deal_image);
        view = itemView;
    }
}