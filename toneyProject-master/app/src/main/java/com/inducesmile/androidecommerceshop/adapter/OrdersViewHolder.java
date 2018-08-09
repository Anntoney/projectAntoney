package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder{

    public TextView productName;
    public TextView productOrderDate;
    public TextView productStatus;
    public ImageView productImageName;

    public OrdersViewHolder(View itemView) {
        super(itemView);

        productName = (TextView)itemView.findViewById(R.id.product_name);
        productOrderDate = (TextView)itemView.findViewById(R.id.product_order_date);
        productStatus = (TextView)itemView.findViewById(R.id.product_status);
        productImageName = (ImageView)itemView.findViewById(R.id.product_image_name);
    }
}
