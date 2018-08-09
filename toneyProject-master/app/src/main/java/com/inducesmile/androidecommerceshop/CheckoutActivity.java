package com.inducesmile.androidecommerceshop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;
import com.inducesmile.androidecommerceshop.adapter.CheckoutAdapter;
import com.inducesmile.androidecommerceshop.entity.CartObject;
import com.inducesmile.androidecommerceshop.entity.CheckoutObject;
import com.inducesmile.androidecommerceshop.entity.PaymentResponseObject;
import com.inducesmile.androidecommerceshop.entity.SuccessObject;
import com.inducesmile.androidecommerceshop.entity.UserObject;
import com.inducesmile.androidecommerceshop.mpesa.ApiConstants;
import com.inducesmile.androidecommerceshop.mpesa.MResponse;
import com.inducesmile.androidecommerceshop.mpesa.OAuth;
import com.inducesmile.androidecommerceshop.mpesa.STKPush;
import com.inducesmile.androidecommerceshop.mpesa.Utils;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.CustomSharedPreference;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.inducesmile.androidecommerceshop.R.string.subtotal;
import static com.inducesmile.androidecommerceshop.utils.Helper.MY_SOCKET_TIMEOUT_MS;

public class CheckoutActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    private TextView orderItemCount, orderTotalAmount, orderVat, orderFullAmount, orderDiscount, orderShipping;
    private TextView restaurantName, restaurantAddress;

    private ImageView editAddress;

    private CustomSharedPreference shared;

    private RadioButton mainAdress, secondaryAddress;
    private RadioGroup addressGroup;
    private String alternativeAddress;
    private String selectedAddress = "";
    private static ProgressDialog d;
    private static String Numb;

    private RadioGroup radioGroup;
    private RadioButton payPalPayment;
    private RadioButton creditCardPayment;
    private RadioButton cashOnDelivery, lipaNaMpesa;

    private UserObject user;
    private List<CartObject> checkoutOrder;
    private String finalList;

    private double subTotal;
    private double finalCost;

    private Gson gson;

    private CheckoutObject checkoutObject;
    private float taxPercent;

    private static PayPalConfiguration config;
    private static final int REQUEST_PAYMENT_CODE = 99;

    private boolean isDiscount = false;
    private String[] couponDiscountType;
    private int shippingCost;
    String payment = "";
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(Helper.PAY_PAL_CLIENT_ID);

        if(!Helper.isNetworkAvailable(this)){
            Helper.displayErrorMessage(this, getString(R.string.no_internet));
        }else{
            checkoutInformationFromRemoteServer();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(getString(R.string.checkouts));
/////////////////
         pd= new SweetAlertDialog(CheckoutActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        d=new ProgressDialog(this);
        d.setCancelable(false);
        ///////////
        isDiscount = getIntent().getExtras().getBoolean("REDEEM");

        finalList = getIntent().getExtras().getString("FINAL_ORDER");
        Log.d(TAG, "JSON FORMAT" + finalList);
        gson = ((CustomApplication)getApplication()).getGsonObject();
        CartObject[] mOrders = gson.fromJson(finalList, CartObject[].class);
        checkoutOrder = ((CustomApplication)getApplication()).convertObjectArrayToListObject(mOrders);

        couponDiscountType = getProductDiscount(checkoutOrder);

        shared = ((CustomApplication)getApplication()).getShared();
        user = gson.fromJson(shared.getUserData(), UserObject.class);

        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        payPalPayment = (RadioButton)findViewById(R.id.pay_pal_payment);
        payPalPayment.setOnCheckedChangeListener(this);
        creditCardPayment = (RadioButton)findViewById(R.id.credit_card_payment);
        creditCardPayment.setOnCheckedChangeListener(this);
        cashOnDelivery = (RadioButton)findViewById(R.id.cash_on_delivery);
        cashOnDelivery.setOnCheckedChangeListener(this);
        lipaNaMpesa = (RadioButton)findViewById(R.id.lipaNaMpesa);
        lipaNaMpesa.setOnCheckedChangeListener(this);




        editAddress = (ImageView)findViewById(R.id.add_address);

        addressGroup = (RadioGroup)findViewById(R.id.address_group);
        //selectDeliveryAddress();
        mainAdress = (RadioButton)findViewById(R.id.main_address);
        secondaryAddress = (RadioButton)findViewById(R.id.secondary_address);

        restaurantName = (TextView)findViewById(R.id.restaurant_name);
        restaurantAddress = (TextView)findViewById(R.id.restaurant_address);

        alternativeAddress = ((CustomApplication)getApplication()).getShared().getSavedDeliveryAddress();
        if(TextUtils.isEmpty(alternativeAddress)){
            secondaryAddress.setVisibility(View.GONE);
            mainAdress.setText(user.getAddress());
        }else{
            secondaryAddress.setText(alternativeAddress);
            mainAdress.setText(user.getAddress());
        }

        orderItemCount = (TextView)findViewById(R.id.order_item_count);
        orderTotalAmount =(TextView)findViewById(R.id.order_total_amount);
        orderVat = (TextView)findViewById(R.id.order_vat);
        orderFullAmount = (TextView)findViewById(R.id.order_full_amounts);

        orderDiscount = (TextView)findViewById(R.id.order_discount);
        if(isDiscount){
            if(couponDiscountType[0].equals("Percentage")){
                orderDiscount.setText(couponDiscountType[1] + "%");
            }else{
                orderDiscount.setText("Ksh" + couponDiscountType[1]);
            }
        }
        orderShipping = (TextView)findViewById(R.id.order_shipping);

        orderItemCount.setText(String.valueOf(checkoutOrder.size()));
        subTotal = ((CustomApplication)getApplication()).getSubtotalAmount(checkoutOrder);
        orderTotalAmount.setText("Ksh" + String.valueOf(subTotal) + "0");

        RecyclerView checkoutRecyclerView = (RecyclerView)findViewById(R.id.checkout_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        checkoutRecyclerView.setLayoutManager(linearLayoutManager);

        checkoutRecyclerView.setHasFixedSize(true);

        CheckoutAdapter mAdapter = new CheckoutAdapter(CheckoutActivity.this, checkoutOrder);
        checkoutRecyclerView.setAdapter(mAdapter);

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAddressIntent = new Intent(CheckoutActivity.this, NewAddressActivity.class);
                newAddressIntent.putExtra("PRIMARY_ADDRESS", user.getAddress());
                startActivity(newAddressIntent);
            }
        });

        Button placeOrderButton = (Button)findViewById(R.id.place_an_order);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
               /* if(radioGroup.getCheckedRadioButtonId() < 0){
                    Helper.displayErrorMessage(CheckoutActivity.this, "Payment option must be selected before checkout");
                    return;
                }

                if(addressGroup.getCheckedRadioButtonId() < 0){
                    Helper.displayErrorMessage(CheckoutActivity.this, "You must provide a delivery address before checkout");
                    return;
                }*/
                String sAddress = returnAddress();
                Log.d(TAG, "Address: " + sAddress);

                String paymentSelected = payment;
                Log.d(TAG, "Payment: " + paymentSelected);

                int productDiscount = 0;
                if(isDiscount){
                    productDiscount = Integer.parseInt(couponDiscountType[1]);
                }

                if(paymentSelected.equals("PAY PAL")){
                    initializePayPalPayment();
                    Log.d(TAG, "Still inside");
                }else if(paymentSelected.equals("CREDIT CARD")){
                    String serverContent = user.getId() + ";" + checkoutOrder.size() + ";" + productDiscount + ";" + taxPercent +
                            ";" + finalCost + ";" + sAddress + ";" + shippingCost + ";" + paymentSelected + ";" + finalList;
                    Intent cardIntent = new Intent(CheckoutActivity.this, CreditCardActivity.class);
                    Log.d(TAG, "Server Content " + serverContent);
                    cardIntent.putExtra("STRIPE_PAYMENT", serverContent);
                    startActivity(cardIntent);
                }else if(paymentSelected.equals("CASH ON DELIVERY")){
                    //postCheckoutOrderToRemoteServer(String.valueOf(user.getId()), String.valueOf(checkoutOrder.size()), String.valueOf(productDiscount),
                            //String.valueOf(taxPercent), String.valueOf(finalCost), sAddress, String.valueOf(shippingCost), paymentSelected, finalList);
                }else if(paymentSelected.equalsIgnoreCase("LIPA NA MPESA")){

                    showMpesaPopup();
                }
            }
        });

    }

    private void showMpesaPopup() {
        final Dialog d = new Dialog(CheckoutActivity.this);
        d.setContentView(R.layout.mpesa_popup);
        d.setCancelable(false);
        final TextView subTotal = d.findViewById(R.id.totalCostTV);
        subTotal.setText(String.valueOf(100));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  d.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final TextInputEditText phone = d.findViewById(R.id.mpesa_phone_number);

        ImageView cancel = d.findViewById(R.id.cancelMpesaPopup);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        Button pay = d.findViewById(R.id.mpesaPayBTN);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Numb= phone.getText().toString();
                if (!Numb.isEmpty()
                        && !String.valueOf(subTotal).equals("")
                        && Utils.isNetworkAvailable(CheckoutActivity.this))
                {
                    d.dismiss();

                    OAuth oAuth = new OAuth(
                            ApiConstants.safaricom_Auth_key,
                            ApiConstants.safaricom_Secret);
                    oAuth.setProduction(ApiConstants.PRODUCTION_DEBUG);


                    String url = ApiConstants.BASE_URL + ApiConstants.ACCESS_TOKEN_URL;

                    if (oAuth.getProduction() == ApiConstants.PRODUCTION_RELEASE)
                        url = ApiConstants.PRODUCTION_BASE_URL + ApiConstants.ACCESS_TOKEN_URL;


                    new AuthService(CheckoutActivity.this).execute(url, oAuth.getOauth());


                } else {
                    Toast.makeText(CheckoutActivity.this, "An Error Occurred, Fill required fields and have internet on", Toast.LENGTH_LONG).show();
                }


            }
        });
        d.show();

    }
    class AuthService extends AsyncTask<String, Void, String> {

        final Context context;

        AuthService(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            d.setMessage("Processing Request...");
            d.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + strings[1]);
            return com.inducesmile.androidecommerceshop.mpesa.Request.get(strings[0], headers);
        }

        protected void onPostExecute(String result) {
            d.setMessage("Finishing up...");
            if (result == null) {
                Toast.makeText(context, "Error Getting Oauth Key", Toast.LENGTH_LONG).show();
                d.dismiss();
                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.get("access_token") != null) {

                    String token = jsonObject.get("access_token").toString();


                    STKPush stkPush = new
                            STKPush(
                            ApiConstants.safaricom_bussiness_short_code,
                            ApiConstants.DEFAULT_TRANSACTION_TYPE,
                            //TODO: eka hapa subtotal,
                            String.valueOf(1),
                            Utils.sanitizePhoneNumber(Numb),
                            ApiConstants.safaricom_party_b,
                            Utils.sanitizePhoneNumber(Numb),
                            ApiConstants.callback_url,
                            "Shop",
                            "Pay");


                    String url = ApiConstants.BASE_URL + ApiConstants.PROCESS_REQUEST_URL;

                    if (stkPush.getProduction() == ApiConstants.PRODUCTION_RELEASE) {
                        url = ApiConstants.PRODUCTION_BASE_URL + ApiConstants.PROCESS_REQUEST_URL;
                    }


                    new PayService().execute(url, generateJsonStringParams(stkPush), token);

                }

                return;
            } catch (Exception ignored) {


            }
        }
    }

    class PayService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + strings[2]);
            return com.inducesmile.androidecommerceshop.mpesa.Request.post(strings[0], strings[1], headers);

        }

        protected void onPostExecute(String result) {
            /*

            {
                "MerchantRequestID":"11273-1846699-2",
                "CheckoutRequestID":"ws_CO_DMZ_59302413_05082018193602819",
                "ResponseCode": "0",
                "ResponseDescription":"Success. Request accepted for processing",
                "CustomerMessage":"Success. Request accepted for processing"
                    }
          */

            d.dismiss();
           // Toast.makeText(CheckoutActivity.this, result, Toast.LENGTH_SHORT).show();
          //  Log.d("RES::",result);


            try {
                JSONObject object = new JSONObject(result);

                String responseDescription = object.getString("ResponseDescription");
                String ResponseCode = object.getString("ResponseCode");
                //todo: encrypt before saving to statics
                MResponse.CheckoutRequestID= object.getString("CheckoutRequestID");
                    if(ResponseCode.equals("0") && responseDescription.contains("Success. Request accepted for processing")){
                        //it opened sim toolkit so tutume resp kwa callback url to see if he actually paid

                        pd.setTitleText("Almost Done");
                        pd.setContentText("Contacting servers ...");
                        pd.show();
                        ApiConstants.isFromstk=true;

                    }else{
                        //some other error for now istead of querying callback just an error resp will do
                        //TODO:save history
                        Toasty.error(CheckoutActivity.this,"Something went wrong.. Try again after some seconds",Toast.LENGTH_SHORT).show();

                    }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void ParseResult(String response, SweetAlertDialog pd) throws JSONException {

   JSONObject jsonObject= new JSONObject(response);
   JSONObject body = jsonObject.getJSONObject("Body");
   JSONObject stkCallback= body.getJSONObject("stkCallback");
   String CODE= stkCallback.getString("ResultCode");
if(CODE.equals("0")){
    pd
            .setTitleText("Order Successfull!")
            .setContentText("")
            .setConfirmText("OK")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    // finish();
                }
            })
            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
}else{
    Toasty.error(
            CheckoutActivity.this,
            "Cant process the request at the moment.. Try again after some seconds",Toast.LENGTH_SHORT).show();

}

    }

    private static String generateJsonStringParams(STKPush push) {
        JSONObject postData = new JSONObject();

        try {
            postData.put("BusinessShortCode", push.getBusinessShortCode());
            postData.put("Password", push.getPassword());
            postData.put("Timestamp", Utils.getTimestamp());
            postData.put("TransactionType", push.getTransactionType());
            postData.put("Amount", push.getAmount());
            postData.put("PartyA", push.getPartyA());
            postData.put("PartyB", push.getPartyB());
            postData.put("PhoneNumber", push.getPhoneNumber());
            postData.put("CallBackURL", push.getCallBackURL());
            postData.put("AccountReference", push.getAccountReference());
            postData.put("TransactionDesc", push.getTransactionDesc());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData.toString();

    }
    private void checkoutInformationFromRemoteServer(){
        GsonRequest<CheckoutObject> serverRequest = new GsonRequest<CheckoutObject>(
                Request.Method.GET,
                Helper.PATH_TO_CHECKOUT,
                CheckoutObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<CheckoutObject> createRequestSuccessListener() {
        return new Response.Listener<CheckoutObject>() {
            @Override
            public void onResponse(CheckoutObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    if(!TextUtils.isEmpty(response.getCompany_name()) || !TextUtils.isEmpty(response.getCompany_address())){
                        checkoutObject = response;
                        restaurantName.setText(response.getCompany_name());
                        restaurantAddress.setText(response.getCompany_address());

                        shippingCost = response.getSettings_shipping();
                        taxPercent = response.getSettings_tax();

                        orderShipping.setText("Ksh" + shippingCost);

                        finalCost = getFinalTotalPrice(shippingCost, taxPercent);

                        orderVat.setText(String.valueOf(taxPercent) + "%");
                        orderFullAmount.setText("Ksh" + String.valueOf(finalCost));

                    }else{
                        Helper.displayErrorMessage(CheckoutActivity.this, "Shop information missing in the server! please contact admin");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }


    private double getFinalTotalPrice(int shipping, float tax){
        if(isDiscount){
            int discount = Integer.parseInt(couponDiscountType[1]);
            subTotal = subTotal - discount;
        }

        int shippingAmount = 0;
        double discountTotal = 0;
        if(shipping != 0){
            shippingAmount = shipping;
        }
        discountTotal = subTotal + shippingAmount;
        Log.d(TAG, "subtotal " + subtotal);
        Log.d(TAG, "discount " + discountTotal);
        int taxAmount = 0;
        if(tax != 0){
            taxAmount = (int)((tax * discountTotal)/100);
        }
        double finalTotal = discountTotal + taxAmount;
        return finalTotal;
    }

    private String returnAddress(){
        String chosenAddress = "";
        if(mainAdress.isChecked()){
            chosenAddress = user.getAddress();
        }
        if(secondaryAddress.isChecked()){
            chosenAddress = alternativeAddress;
        }
        return chosenAddress;
    }

    /*private String getSelectedPayment(){
        if(payPalPayment.isChecked()){
            payment = "PAY PAL";
        }
        if(creditCardPayment.isChecked()){
            payment= "CREDIT CARD";
        }
        if(cashOnDelivery.isChecked()){
            payment = "CASH ON DELIVERY";
        } if(cashOnDelivery.isChecked()){
            payment = "LIPA NA MPESA";
        }
        return payment;
    }
*/
    private void initializePayPalPayment(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(subTotal)), "USD", "Product Order", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_PAYMENT_CODE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PAYMENT_CODE) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    String jsonPaymentResponse = confirm.toJSONObject().toString(4);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    PaymentResponseObject responseObject = gson.fromJson(jsonPaymentResponse, PaymentResponseObject.class);
                    if(responseObject != null){
                        String paymentId = responseObject.getResponse().getId();
                        String paymentState = responseObject.getResponse().getState();
                        Log.d(TAG, "Log payment id and state " + paymentId + " " + paymentState);

                        int productDiscount = 0;
                        if(isDiscount){
                            productDiscount = Integer.parseInt(couponDiscountType[1]);
                        }
                        //send order to server
                        String deliveryAddress = returnAddress();
                        postCheckoutOrderToRemoteServer(String.valueOf(user.getId()), String.valueOf(checkoutOrder.size()), String.valueOf(productDiscount),
                                String.valueOf(taxPercent), String.valueOf(finalCost), deliveryAddress, String.valueOf(shippingCost), "PAY PAL", finalList);

                    }
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    private void postCheckoutOrderToRemoteServer(String userId, String quantity, String discount, String tax, String price,
                                                 String address, String shipping, String payment_method, String order_list){
        Map<String, String> params = new HashMap<String,String>();
        params.put("USER_ID", userId);
        params.put("QUANTITY", quantity);
        params.put("DISCOUNT", discount);
        params.put("TAX", tax);
        params.put("TOTAL_PRICE", price);
        params.put("ADDRESS", address);
        params.put("SHIPPING", shipping);
        params.put("PAYMENT", payment_method);
        params.put("ORDER_LIST", order_list);

        GsonRequest<SuccessObject> serverRequest = new GsonRequest<SuccessObject>(
                Request.Method.POST,
                Helper.PATH_TO_PLACE_ORDER,
                SuccessObject.class,
                params,
                createOrderRequestSuccessListener(),
                createOrderRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<SuccessObject> createOrderRequestSuccessListener() {
        return new Response.Listener<SuccessObject>() {
            @Override
            public void onResponse(SuccessObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.getSuccess());
                    if(response.getSuccess() == 1){
                        //delete paid other
                        ((CustomApplication)getApplication()).getShared().updateCartItems("");
                        //confirmation page.
                        Intent orderIntent = new Intent(CheckoutActivity.this, ComfirmationActivity.class);
                        startActivity(orderIntent);
                    }else{
                        Helper.displayErrorMessage(CheckoutActivity.this, "There is an issue why placing your order. Please try again");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createOrderRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private String[] getProductDiscount(List<CartObject> productsInCart){
        String[] cType = new String[2];
        for(int i = 0; i < productsInCart.size(); i++){
            CartObject productObject = productsInCart.get(i);
            if(!TextUtils.isEmpty(productObject.getCouponType())){
                String discountType = productObject.getCouponType();
                int discountValue = productObject.getDiscount();
                cType[0] = discountType;
                cType[1] = String.valueOf(discountValue);
                break;
            }
        }
        return cType;
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ApiConstants.isFromstk == true){
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ApiConstants.CHECH_CALLBACK, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    pd.dismissWithAnimation();
                    ApiConstants.isFromstk=false;

                    Log.d("RESPONS:: ",response);



                    try {
                        ParseResult(response,pd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismissWithAnimation();
                    ApiConstants.isFromstk=false;
                    Log.d("RESPONS:: ",error.toString());

                    //Toasty.error(PostMerchantDetails.this,error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String,String> data = new HashMap<>();
                    data.put("id",MResponse.CheckoutRequestID);
                    return data;

                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 1;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue queue=  Volley.newRequestQueue(CheckoutActivity.this);

            queue.add(stringRequest);



        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


        switch (compoundButton.getId()){
            case R.id.pay_pal_payment:
                if(b){
                    payment = "PAY PAL";
                    creditCardPayment.setChecked(false);
                    cashOnDelivery.setChecked(false);
                    lipaNaMpesa.setChecked(false);


                }

            break;
            case R.id.credit_card_payment:
                if(b){
                    payment= "CREDIT CARD";
                    payPalPayment.setChecked(false);
                    cashOnDelivery.setChecked(false);
                    lipaNaMpesa.setChecked(false);

                }
                break;
            case R.id.cash_on_delivery:
                if(b){
                    payment = "CASH ON DELIVERY";
                    payPalPayment.setChecked(false);
                    creditCardPayment.setChecked(false);
                    lipaNaMpesa.setChecked(false);

                }
                break;
            case R.id.lipaNaMpesa:
                if(b){
                    payment = "LIPA NA MPESA";
                    payPalPayment.setChecked(false);
                    cashOnDelivery.setChecked(false);
                    creditCardPayment.setChecked(false);


                }
                break;




        }
    }
}
