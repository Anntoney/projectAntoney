package com.inducesmile.androidecommerceshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inducesmile.androidecommerceshop.ProductDetailsActivity;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.ProductObject;
import com.inducesmile.androidecommerceshop.fragment.DealsFragment;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.Date;
import java.util.List;


public class DealAdapter extends RecyclerView.Adapter<DealViewHolder>{

    private static final String TAG = DealsFragment.class.getSimpleName();

    protected Context context;
    List<ProductObject> dealList;

    public DealAdapter(Context context, List<ProductObject> dealList) {
        this.context = context;
        this.dealList = dealList;
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_list, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        ProductObject productObject = dealList.get(position);

        if(productObject != null){
            final int id = productObject.getId();
            final String name = productObject.getNames();
            // use Glide to download and display the category image.
            String serverImagePath = Helper.PUBLIC_FOLDER + productObject.getImages();
            Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(500, 400).into(holder.dealImage);

            holder.dealName.setText(productObject.getNames());

            holder.dealPrice.setText("Ksh" + productObject.getPrice());
            holder.dealPrice.setPaintFlags(holder.dealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // calculate product discount
            int priceAfterDiscount = 0;
            String couponTypeType = productObject.getCoupon_type();
            int discount = productObject.getDiscount();
            if(TextUtils.equals(couponTypeType, "Percentage")){
                priceAfterDiscount = Helper.getPriceDiscount(productObject.getPrice(), discount);
                holder.discountPrice.setText("Ksh" + String.valueOf(priceAfterDiscount));
                holder.dealDiscount.setText("Save up to " + discount + "%");
            }
            if(TextUtils.equals(couponTypeType, "Discount")){
                priceAfterDiscount = (productObject.getPrice() - discount);
                holder.discountPrice.setText("Ksh" + String.valueOf(priceAfterDiscount));
                holder.dealDiscount.setText("Ksh" + discount + " discount on product");
            }

            String dateInString = productObject.getEnding_date();
            Date endDate = Helper.convertStringToDate(dateInString);
            Date todayDate = new Date();
            long days = Helper.getDifferenceDays(todayDate, endDate);
            holder.dealExpireDate.setText(days + " remaining");

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productIntent = new Intent(context, ProductDetailsActivity.class);
                    productIntent.putExtra("PRODUCT_ID", id);
                    productIntent.putExtra("PRODUCT_NAME", name);
                    context.startActivity(productIntent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }
}
