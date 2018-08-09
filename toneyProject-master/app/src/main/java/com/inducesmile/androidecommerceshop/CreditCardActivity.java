package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.entity.SuccessObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;

import static com.inducesmile.androidecommerceshop.utils.Helper.MY_SOCKET_TIMEOUT_MS;

public class CreditCardActivity extends AppCompatActivity {

    private static final String TAG = CreditCardActivity.class.getSimpleName();

    private CardInputWidget cardInputWidget;
    private Card mCard;
    Card card = null;
    private Stripe stripe;

    private String postContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        setTitle("Pay with Credit Card");

        postContent = getIntent().getExtras().getString("STRIPE_PAYMENT");

        cardInputWidget = (CardInputWidget)findViewById(R.id.card_input_widget);

        Button stripeButton = (Button)findViewById(R.id.stripe_button);
        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCard = cardInputWidget.getCard();
                if(null == mCard || !mCard.validateCard()){
                    // display invalid mCard data error
                    Helper.showMessageDialog(CreditCardActivity.this, "Invalid Card Error! Please try again");
                }else{
                    String cvc = cardInputWidget.getCard().getCVC();
                    int expMonth = cardInputWidget.getCard().getExpMonth();
                    int expYear = cardInputWidget.getCard().getExpYear();
                    String cardNumber = cardInputWidget.getCard().getNumber();

                    card = new Card(cardNumber, expMonth, expYear, cvc);

                    card.setName("Customer Name");

                    Log.d(TAG, "Card number " + cardNumber);

                    try {
                        stripe = new Stripe(CreditCardActivity.this, Helper.STRIPE_CLIENT_ID);
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                    stripe.createToken(card, new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            String errorMessage = error.getLocalizedMessage();
                            Helper.showMessageDialog(CreditCardActivity.this, errorMessage);
                        }

                        @Override
                        public void onSuccess(Token token) {
                            // send token to your server
                            String tokenString = ((CustomApplication)getApplication()).getGsonObject().toJson(token);
                            Log.d(TAG, "Display token: " + tokenString);
                            if(TextUtils.isEmpty(postContent)){
                                Helper.displayErrorMessage(CreditCardActivity.this, "Something went wrong! Please try again");
                            }else{
                                String[] serverValues = postContent.split(";");
                                postCheckoutOrderToRemoteServer(tokenString, serverValues[0], serverValues[1], serverValues[2], serverValues[3], serverValues[4],
                                        serverValues[5], serverValues[6], serverValues[7], serverValues[8]);
                            }
                        }
                    });
                }
            }
        });
    }

    private void postCheckoutOrderToRemoteServer(String token, String userId, String quantity, String discount, String tax, String price,
                                                 String address, String shipping, String payment_method, String order_list){
        Map<String, String> params = new HashMap<String,String>();
        params.put("TOKEN", token);
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

        VolleySingleton.getInstance(CreditCardActivity.this).addToRequestQueue(serverRequest);
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
                        Intent orderIntent = new Intent(CreditCardActivity.this, ComfirmationActivity.class);
                        startActivity(orderIntent);
                    }else{
                        Helper.displayErrorMessage(CreditCardActivity.this, "There is an issue why placing your order. Please try again");
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
}
