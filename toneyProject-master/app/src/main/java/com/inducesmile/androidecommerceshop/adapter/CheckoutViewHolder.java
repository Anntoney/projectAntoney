package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class CheckoutViewHolder extends RecyclerView.ViewHolder{

    public TextView checkoutName, checkoutQuantity, checkoutPrice;

    public CheckoutViewHolder(View itemView) {
        super(itemView);
        checkoutName = (TextView)itemView.findViewById(R.id.checkout_name);
        checkoutQuantity = (TextView)itemView.findViewById(R.id.checkout_quantity);
        checkoutPrice = (TextView)itemView.findViewById(R.id.checkout_price);
    }
}
