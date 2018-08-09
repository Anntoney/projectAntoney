package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class FavouriteViewHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public ImageView path;
    public TextView price;
    public ImageView delete;
    public View view;

    public FavouriteViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.product_name);
        path = (ImageView) itemView.findViewById(R.id.product_image);
        price = (TextView)itemView.findViewById(R.id.product_price);
        delete = (ImageView)itemView.findViewById(R.id.delete_favorite);
        view = itemView;
    }
}
