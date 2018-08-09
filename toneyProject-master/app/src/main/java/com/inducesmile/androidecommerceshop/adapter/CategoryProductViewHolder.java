package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class CategoryProductViewHolder extends RecyclerView.ViewHolder{

    public TextView name, description, price, inStock;

    public ImageView image;

    public View mView;

    public CategoryProductViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.cap_name);
        description = (TextView)itemView.findViewById(R.id.cap_description);
        price = (TextView)itemView.findViewById(R.id.cap_price);
        inStock =(TextView)itemView.findViewById(R.id.cap_in_stock);

        image = (ImageView)itemView.findViewById(R.id.cap_image);

        mView = itemView;
    }
}
