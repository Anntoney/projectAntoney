package com.inducesmile.androidecommerceshop.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inducesmile.androidecommerceshop.CategoryProductActivity;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.CategoryObject;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryViewHolder>{

    private Context context;
    private List<CategoryObject> categoryList;

    public ProductCategoryAdapter(Context context, List<CategoryObject> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public ProductCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_item, parent, false);
        return new ProductCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductCategoryViewHolder holder, int position) {
        final CategoryObject categoryObject = categoryList.get(position);
        if(categoryObject != null){
            final int categoryId = categoryObject.getId();
            // use Glide to download and display the category image.
            String serverImagePath = Helper.PUBLIC_FOLDER + categoryObject.getImage();
            Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(500, 300).into(holder.categoryImage);
            holder.categoryName.setText("For " + categoryObject.getName());
            holder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent categoryIntent = new Intent(context, CategoryProductActivity.class);
                    categoryIntent.putExtra("CATEGORY_ID", categoryId);
                    categoryIntent.putExtra("CATEGORY_NAME", categoryObject.getName());
                    context.startActivity(categoryIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
