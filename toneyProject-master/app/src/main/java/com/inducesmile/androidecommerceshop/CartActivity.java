package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.inducesmile.androidecommerceshop.adapter.CartAdapter;
import com.inducesmile.androidecommerceshop.entity.CartObject;
import com.inducesmile.androidecommerceshop.entity.SuccessObject;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.CustomSharedPreference;
import com.inducesmile.androidecommerceshop.utils.EventMessage;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.inducesmile.androidecommerceshop.utils.PostToServer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = CartActivity.class.getSimpleName();

    private Gson mGson;
    private CustomSharedPreference pref;
    private List<CartObject> orderedItems;

    private LinearLayout noOrder;
    private RelativeLayout cartWrapper;

    private TextView orderTotalAmount;
    private TextView orderItemCount;

    private TextView couponMessage;
    private EditText couponBox;
    private Button couponButton;

    private PostToServer postToServer;
    private boolean hasRedeemed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Your Cart");

        postToServer = new PostToServer(this);

        mGson = ((CustomApplication)getApplication()).getGsonObject();
        pref = ((CustomApplication)getApplication()).getShared();

        couponMessage = (TextView)findViewById(R.id.coupon_message);
        couponBox = (EditText)findViewById(R.id.coupon);
        couponButton = (Button)findViewById(R.id.coupon_button);
        couponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCoupon = couponBox.getText().toString();
                if(TextUtils.isEmpty(enteredCoupon)){
                    Helper.displayErrorMessage(CartActivity.this, "You must enter a coupon code");
                }else{
                    redeemCouponFromServer(enteredCoupon);
                }
            }
        });
        hideCouponSection();

        noOrder = (LinearLayout)findViewById(R.id.no_order);
        noOrder.setVisibility(View.GONE);

        cartWrapper = (RelativeLayout)findViewById(R.id.cart_order_wrapper);
        cartWrapper.setVisibility(View.GONE);

        orderItemCount = (TextView) findViewById(R.id.order_item_count);
        orderTotalAmount = (TextView) findViewById(R.id.order_total_amount);

        RecyclerView cartRecyclerView = (RecyclerView) findViewById(R.id.cart_item);

        GridLayoutManager grid = new GridLayoutManager(this, 2);
        cartRecyclerView.setLayoutManager(grid);
        cartRecyclerView.setHasFixedSize(true);

        Button backToMenuButton = (Button)findViewById(R.id.back_to_menu_button);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(CartActivity.this, ShopActivity.class);
                startActivity(menuIntent);
            }
        });

        final Button checkoutButton = (Button)findViewById(R.id.check_out_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                if(orderedItems.size() > 0){
                    String finalOrderList = mGson.toJson(orderedItems);
                    checkoutIntent.putExtra("FINAL_ORDER", finalOrderList);
                    checkoutIntent.putExtra("REDEEM", hasRedeemed);
                    startActivity(checkoutIntent);
                }
            }
        });

        String itemInCart = pref.getCartItems();
        if(TextUtils.isEmpty(itemInCart) || itemInCart.equals("[]")){
            noOrder.setVisibility(View.VISIBLE);
        }else{
            cartWrapper.setVisibility(View.VISIBLE);
            CartObject[] ordersInCart = mGson.fromJson(itemInCart, CartObject[].class);
            orderedItems = ((CustomApplication)getApplication()).convertObjectArrayToListObject(ordersInCart);
            CartAdapter mAdapter = new CartAdapter(CartActivity.this, orderedItems);
            cartRecyclerView.setAdapter(mAdapter);

            if(isProductDiscounted(orderedItems)){
                showCouponSection();
                String discountAmount = getCouponMessage(orderedItems);
                couponMessage.setText("Enter a coupon code to get " + discountAmount + " discount");
            }else{
                hideCouponSection();
            }

            orderItemCount.setText(String.valueOf(orderedItems.size()));
            //DrawCart drawCart = new DrawCart(this);
            double subtotal = ((CustomApplication)getApplication()).getSubtotalAmount(orderedItems);
            orderTotalAmount.setText("Ksh" + String.valueOf(subtotal));
        }
    }

    private boolean isProductDiscounted(List<CartObject> productsInCart){
        for(int i = 0; i < productsInCart.size(); i++){
            CartObject productObject = productsInCart.get(i);
            if(productObject.getDiscount() != 0 || !TextUtils.isEmpty(productObject.getCouponType())){
                return true;
            }
        }
        return false;
    }

    private String getCouponMessage(List<CartObject> productsInCart){
        String cType = "";
        for(int i = 0; i < productsInCart.size(); i++){
            CartObject productObject = productsInCart.get(i);
            if(!TextUtils.isEmpty(productObject.getCouponType())){
                String types = productObject.getCouponType();
                if(types.equals("Percentage")){
                   cType = productObject.getDiscount() + "%";
                }else{
                    cType = "Ksh" + productObject.getDiscount();
                }
                break;
            }
        }
        return cType;
    }

    private void hideCouponSection(){
        couponMessage.setVisibility(View.GONE);
        couponBox.setVisibility(View.GONE);
        couponButton.setVisibility(View.GONE);
    }

    private void showCouponSection(){
        couponMessage.setVisibility(View.VISIBLE);
        couponBox.setVisibility(View.VISIBLE);
        couponButton.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromAdapter(EventMessage event){
        orderTotalAmount.setText("Ksh" + event.getSubtotal());
        orderItemCount.setText(event.getItemCount());
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private  void redeemCouponFromServer(String couponCode){
        Map<String, String> params = new HashMap<String, String>();
        params.put("coupon_code", couponCode);
        postToServer.remoteServerCall(Helper.PATH_TO_REDEEM_COUPON, params, (Response.Listener<SuccessObject>) redeemCoupon());
    }

    private Response.Listener<SuccessObject> redeemCoupon() {
        return new Response.Listener<SuccessObject>() {
            @Override
            public void onResponse(SuccessObject response) {
                Log.d(TAG, "Success result " + response.getSuccess());
                try {
                    if(response.getSuccess() == 1){
                        hasRedeemed = true;
                    }else{
                        hasRedeemed = false;
                    }
                    couponBox.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        String countCart = pref.getCartItems();
        if(TextUtils.isEmpty(countCart) || countCart.equals("[]")){
            Intent shopIntent = new Intent(CartActivity.this, ShopActivity.class);
            startActivity(shopIntent);
        }else{
            super.onBackPressed();
        }
    }
}
