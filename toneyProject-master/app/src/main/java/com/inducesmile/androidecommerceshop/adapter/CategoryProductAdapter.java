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

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductViewHolder>{

    private static final String TAG = CategoryProductAdapter.class.getSimpleName();

    private Context context;
    private List<ProductObject> productList;


    public CategoryProductAdapter(Context context, List<ProductObject> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public CategoryProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_in_category_layout, parent, false);
        return new CategoryProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryProductViewHolder holder, int position) {
        final ProductObject productObject = productList.get(position);
        if(productObject != null){
            final int id = productObject.getId();

            // use Glide to download and display the category image.
            String serverImagePath = Helper.PUBLIC_FOLDER + productObject.getImages();
            Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(500, 500).into(holder.image);


            String productExcerpt = productObject.getDescription();

            if(productObject.getDescription().length()>50){

                productExcerpt  =   productObject.getDescription().substring(0,50);
            }



            holder.name.setText(productObject.getNames());
            holder.description.setText(productExcerpt);
            holder.price.setText("Ksh" + productObject.getPrice());
            holder.inStock.setText(productObject.getStatus());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productIntent = new Intent(context, ProductDetailsActivity.class);
                    productIntent.putExtra("PRODUCT_ID", id);
                    productIntent.putExtra("PRODUCT_NAME", productObject.getNames());
                    context.startActivity(productIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
