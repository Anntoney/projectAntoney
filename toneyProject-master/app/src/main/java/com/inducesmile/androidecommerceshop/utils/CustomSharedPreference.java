package com.inducesmile.androidecommerceshop.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreference {

    private SharedPreferences sharedPref;

    public CustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences(Helper.SHARED_PREF, Context.MODE_PRIVATE);
    }

    public SharedPreferences getInstanceOfSharedPreference(){
        return sharedPref;
    }

    //Save user information
    public void setUserData(String userData){
        sharedPref.edit().putString(Helper.USER_DATA, userData).apply();
    }

    public String getUserData(){
        return sharedPref.getString(Helper.USER_DATA, "");
    }

    //Save Cart Information
    public void updateCartItems(String cartItem){
        sharedPref.edit().putString(Helper.CART, cartItem).apply();
    }

    public String getCartItems(){
        return sharedPref.getString(Helper.CART, "");
    }

    //Save Delivery Address Information
    public void saveDeliveryAddress(String address){
        sharedPref.edit().putString(Helper.DELIVERY_ADDRESS, address).apply();
    }

    public String getSavedDeliveryAddress(){
        return sharedPref.getString(Helper.DELIVERY_ADDRESS, "");
    }

    //Save Payment Card Information
    public void saveCreditCardDetails(String cardDetails){
        sharedPref.edit().putString(Helper.CREDIT_CARD, cardDetails).apply();
    }

    public String getSavedCreditCardDetails(){
        return sharedPref.getString(Helper.CREDIT_CARD, "");
    }

    public void storeNotification(String notificationList){
        sharedPref.edit().putString(Helper.NOTIFICATION_LIST, notificationList).apply();
    }

    public String getStoredNotifications(){
        return sharedPref.getString(Helper.NOTIFICATION_LIST, "");
    }

    public void saveTabTitle(String titles){
        sharedPref.edit().putString(Helper.TAB_TITLE, titles).apply();
    }

    public String getTabTitles(){
        return sharedPref.getString(Helper.TAB_TITLE, "");
    }


}
