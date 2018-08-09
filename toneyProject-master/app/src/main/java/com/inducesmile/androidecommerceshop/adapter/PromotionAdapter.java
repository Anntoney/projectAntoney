package com.inducesmile.androidecommerceshop.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.PromotionObject;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionViewHolder>{

    private static final String TAG = PromotionAdapter.class.getSimpleName();

    int[] colorArrays = new int[]{R.color.promotion_blue, R.color.promotion_red, R.color.promotion_ivory };

    private Context context;
    private List<PromotionObject> promotion;

    public PromotionAdapter(Context context, List<PromotionObject> promotion) {
        this.context = context;
        this.promotion = promotion;
    }

    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_layout, parent, false);
        return new PromotionViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(PromotionViewHolder holder, int position) {
        int backgroundColor = context.getResources().getColor(colorArrays[position]);
        holder.bView.setBackgroundColor(backgroundColor);
        PromotionObject pObject = promotion.get(position);
        holder.promotionProductName.setText(pObject.getName());
        holder.promotionProductPrice.setText(pObject.getPrice());
        int resource = getResourseId(context, pObject.getImage(), "drawable", context.getPackageName());
        holder.promotionImage.setImageResource(resource);
    }

    @Override
    public int getItemCount() {
        return promotion.size();
    }

    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}
