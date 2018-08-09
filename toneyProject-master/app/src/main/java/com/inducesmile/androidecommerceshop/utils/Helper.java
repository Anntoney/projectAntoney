package com.inducesmile.androidecommerceshop.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Helper {

    private static final String TAG = Helper.class.getSimpleName();

    public static final String ID = "id";
    public static final String FULLNAME = "fullname";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String USER_ID = "user_id";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String FAVORITE_ID = "FAVORITE_ID";


    public static final String PUBLIC_FOLDER = "http://antoney.hibasoftware.co.ke/public/";
    public static final String PUBLIC_API_FOLDER = "https://antoney.hibasoftware.co.ke/public/api/";

    public static final String PATH_TO_SERVER_LOGIN = PUBLIC_API_FOLDER + "login";
    public static final String PATH_TO_SERVER_REGISTRATION = PUBLIC_API_FOLDER + "signin";
    public static final String PATH_TO_SHOP_HOME = PUBLIC_API_FOLDER + "products";
    public static final String PATH_TO_CATEGORIES = PUBLIC_API_FOLDER + "categories";
    public static final String PATH_TO_PRODUCT_IN_CATEGORY = PUBLIC_API_FOLDER + "productsincategory";
    public static final String PATH_TO_CATEGORY = PUBLIC_API_FOLDER + "category";
    public static final String PATH_TO_SALES = PUBLIC_API_FOLDER + "sales";
    public static final String PATH_TO_PROMOTION = PUBLIC_API_FOLDER + "promotion";
    public static final String PATH_TO_STORE = PUBLIC_API_FOLDER + "store";
    public static final String PATH_TO_EDIT_PROFILE = PUBLIC_API_FOLDER + "editprofile";
    public static final String PATH_TO_ORDER = PUBLIC_API_FOLDER + "order";
    public static final String PATH_TO_FAVORITE = PUBLIC_API_FOLDER + "favorite";
    public static final String PATH_TO_DELETE_FAVORITE = PUBLIC_API_FOLDER + "deletefavorite";
    public static final String PATH_TO_WALLET = PUBLIC_API_FOLDER + "wallet";
    public static final String PATH_TO_SINGLE_PRODUCT = PUBLIC_API_FOLDER + "item";
    public static final String PATH_TO_CHECKOUT = PUBLIC_API_FOLDER + "checkout";
    public static final String PATH_TO_PLACE_ORDER = PUBLIC_API_FOLDER + "placeorder";
    public static final String PATH_TO_CATEGORY_TITLE = PUBLIC_API_FOLDER + "threecategories";
    public static final String PATH_TO_UPDATE_FAVORITE = PUBLIC_API_FOLDER + "updatefavorite";
    public static final String PATH_TO_REDEEM_COUPON = PUBLIC_API_FOLDER + "redeemcoupon";

    public static final String PATH_TO_PAY_PAL = PUBLIC_API_FOLDER + "paypal";

    public static final String PAY_PAL_CLIENT_ID = "";
    public static final String STRIPE_CLIENT_ID = "";


    public static final int MY_SOCKET_TIMEOUT_MS = 5000;

    public static final String USER_DATA = "USER_DATA";
    public static final String CART = "CART";
    public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    public static final String CREDIT_CARD = "CREDIT_CARD";
    public static final String SHARED_PREF = "SHARED_PREFERENCE";
    public static final String NOTIFICATION_LIST = "NOTIFY";
    public static final String TAB_TITLE = "TAB_TITLE";

    public static final int MINIMUM_LENGTH = 5;

    public static void displayErrorMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String convertDateToString(Date date) {
        String dateStr = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            dateStr = df.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateStr;
    }

    public static Date convertStringToDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date convertDate = null;
        try {
            convertDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getPriceDiscount(int price, int discount){
        int realDiscount = ((price * discount) / 100);
        return  realDiscount;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showMessageDialog(Context context, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
