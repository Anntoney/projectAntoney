package com.inducesmile.androidecommerceshop.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inducesmile.androidecommerceshop.ProductDetailsActivity;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.ProductObject;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private Context context;
    private List<ProductObject> categoryObject;

    public CategoryAdapter(Context context, List<ProductObject> categoryObject) {
        this.context = context;
        this.categoryObject = categoryObject;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_category_list, parent, false);
        return new CategoryViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final ProductObject productObject = categoryObject.get(position);
        if(productObject != null){
            final int id = productObject.getId();
            holder.categoryName.setText(productObject.getNames());
            holder.categoryPrice.setText("Ksh" + productObject.getPrice());

            // use Glide to download and display the category image.
            String serverImagePath = Helper.PUBLIC_FOLDER + productObject.getImages();
            Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(400, 300).into(holder.categoryImage);

            holder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent categoryIntent = new Intent(context, ProductDetailsActivity.class);
                    categoryIntent.putExtra(Helper.PRODUCT_NAME, productObject.getNames());
                    categoryIntent.putExtra(Helper.PRODUCT_ID, id);
                    context.startActivity(categoryIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return categoryObject.size();
    }

}
