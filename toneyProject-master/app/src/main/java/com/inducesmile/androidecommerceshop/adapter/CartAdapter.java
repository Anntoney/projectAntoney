package com.inducesmile.androidecommerceshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.CartObject;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.DrawCart;
import com.inducesmile.androidecommerceshop.utils.EventMessage;
import com.inducesmile.androidecommerceshop.utils.Helper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private static final String TAG = CartAdapter.class.getSimpleName();

    private Context context;
    private List<CartObject> cartList;
    private DrawCart drawCart;
    private Gson mGson;

    public CartAdapter(Context context, List<CartObject> cartList) {
        this.context = context;
        this.cartList = cartList;
        drawCart = new DrawCart(context);
        mGson = ((CustomApplication)((Activity)context).getApplication()).getGsonObject();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_order_layout, parent, false);
        return new CartViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final CartObject cartObject = cartList.get(position);

        String serverImagePath = Helper.PUBLIC_FOLDER + cartObject.getImage();
        Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(150, 100).into(holder.orderImage);
        holder.orderName.setText(cartObject.getName());
        holder.orderQuantity.setText(String.valueOf(cartObject.getQuantity()));
        holder.orderPrice.setText("Ksh" + String.valueOf(cartObject.getPrice()) + "0");

        holder.removeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartList.remove(position);
                notifyDataSetChanged();
                //update order in shared preference
                String updatedOrder = mGson.toJson(cartList);
                ((CustomApplication)((Activity)context).getApplication()).getShared().updateCartItems(updatedOrder);
                //update the order subtotal when an item quantity is increased or decreased
                if(cartList.size() <= 0){
                    ((Activity) context).findViewById(R.id.cart_order_wrapper).setVisibility(View.GONE);
                    ((Activity) context).findViewById(R.id.no_order).setVisibility(View.VISIBLE);
                }else{
                    double currentSubtotal = ((CustomApplication)((Activity)context).getApplication()).getSubtotalAmount(cartList);
                    EventBus.getDefault().post(new EventMessage(String.valueOf(currentSubtotal), String.valueOf(cartList.size())));
                }
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartObject.getQuantity() <= 0){
                    return;
                }
                int quantity = cartObject.getQuantity() - 1;
                holder.orderQuantity.setText(String.valueOf(quantity));
                cartObject.setQuantity(quantity);

                changeOrderedProduct(position, cartObject);
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartObject.getQuantity() + 1;
                holder.orderQuantity.setText(String.valueOf(quantity));
                cartObject.setQuantity(quantity);

                changeOrderedProduct(position, cartObject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void changeOrderedProduct(int position, CartObject cartObject){
        cartList.remove(position);
        cartList.add(position, cartObject);

        String updatedOrder = mGson.toJson(cartList);
        ((CustomApplication)((Activity)context).getApplication()).getShared().updateCartItems(updatedOrder);

        //update the order subtotal when an item quantity is increased or decreased
        double currentSubtotal = ((CustomApplication)((Activity)context).getApplication()).getSubtotalAmount(cartList);
        EventBus.getDefault().post(new EventMessage(String.valueOf(currentSubtotal), String.valueOf(cartList.size())));
    }


}
