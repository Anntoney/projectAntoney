package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public ImageView orderImage;

    public TextView orderName;
    public TextView orderPrice;
    public TextView orderQuantity;
    public TextView removeOrder;

    public Button minusButton;
    public Button plusButton;

    public CartViewHolder(View itemView) {
        super(itemView);

        orderImage = (ImageView)itemView.findViewById(R.id.order_image);

        orderName = (TextView)itemView.findViewById(R.id.order_name);
        orderPrice = (TextView)itemView.findViewById(R.id.order_price);
        orderQuantity = (TextView)itemView.findViewById(R.id.order_quantity);
        removeOrder = (TextView)itemView.findViewById(R.id.remove_order);

        minusButton = (Button)itemView.findViewById(R.id.minus_button);
        plusButton = (Button)itemView.findViewById(R.id.plus_button);
    }
}
