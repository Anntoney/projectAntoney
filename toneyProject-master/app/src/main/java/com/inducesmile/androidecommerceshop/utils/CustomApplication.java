package com.inducesmile.androidecommerceshop.utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inducesmile.androidecommerceshop.entity.CartObject;
import com.inducesmile.androidecommerceshop.entity.UserObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CustomApplication extends Application {

    private Gson gson;
    private GsonBuilder builder;

    private CustomSharedPreference shared;

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CustomSharedPreference(getApplicationContext());
    }

    public CustomSharedPreference getShared(){
        return shared;
    }

    public Gson getGsonObject(){
        return gson;
    }

    /**
     * get current login user
     * @return
     */
    public UserObject getLoginUser(){
        Gson mGson = getGsonObject();
        String storedUser = getShared().getUserData();
        return mGson.fromJson(storedUser, UserObject.class);
    }

    /**
     * get the total cost of products in the cart
     * @param orderedItems
     * @return
     */
    public double getSubtotalAmount(List<CartObject> orderedItems){
        double total = 0.0;
        for (int i = 0; i < orderedItems.size(); i++){
            int quantity = orderedItems.get(i).getQuantity();
            if(quantity == 0){
                quantity = 1;
            }
            float itemSubtotal = quantity * orderedItems.get(i).getPrice();
            total += (double)itemSubtotal;
        }
        return total;
    }

    /**
     * Get the total number of products add in the cart
     * @return
     */
    public int cartItemCount(){
        String orderList = getShared().getCartItems();
        CartObject[] allCart = getGsonObject().fromJson(orderList, CartObject[].class);
        if(allCart == null){
            return 0;
        }
        return allCart.length;
    }

    /**
     * convert cartObject array to list of cart products
     * @param allProducts list of added products in the cart
     * @return
     */
    public List<CartObject> convertObjectArrayToListObject(CartObject[] allProducts){
        List<CartObject> mProduct = new ArrayList<CartObject>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    /**
     * Add product to cart
     * @param orderItem product to be added to cart
     */
    public void addMenuItemToCart(CartObject orderItem){
        if(shared.getCartItems().equals("")){
            List<CartObject> cartListItems = new ArrayList<CartObject>();
            cartListItems.add(orderItem);
            storeCartOrderList(cartListItems);
        }else{
            String storeOrderList = shared.getCartItems();
            CartObject[] cartItemsCollections = gson.fromJson(storeOrderList, CartObject[].class);
            List<CartObject> allOrders = convertObjectArrayToListObject(cartItemsCollections);
            allOrders.add(orderItem);
            storeCartOrderList(allOrders);
        }
    }

    /**
     * method to store order items in shared preference
     * @param orderList list of orders added to the cart
     */
    private void storeCartOrderList(List<CartObject> orderList){
        String mOrders = gson.toJson(orderList);
        shared.updateCartItems(mOrders);
    }
}