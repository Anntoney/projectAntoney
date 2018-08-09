package com.inducesmile.androidecommerceshop.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.R;

public class ProductCategoryViewHolder extends RecyclerView.ViewHolder{

    public ImageView categoryImage;
    public TextView categoryName;

    public ProductCategoryViewHolder(View itemView) {
        super(itemView);
        categoryImage = (ImageView)itemView.findViewById(R.id.category_image);
        categoryName = (TextView)itemView.findViewById(R.id.category_name);
    }
}
