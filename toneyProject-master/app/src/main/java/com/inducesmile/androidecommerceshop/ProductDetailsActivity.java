package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inducesmile.androidecommerceshop.entity.CartObject;
import com.inducesmile.androidecommerceshop.entity.ProductObject;
import com.inducesmile.androidecommerceshop.entity.SuccessObject;
import com.inducesmile.androidecommerceshop.entity.UserObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.DrawCart;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.inducesmile.androidecommerceshop.utils.PostToServer;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView favoriteCount;
    private TextView productDescription;
    private TextView productColor;
    private TextView productSize;
    private TextView productQuantity;

    private int productIndex;
    private String productTitle;

    private ProductObject productToCart;

    private ImageView favoriteIcon;

    private PostToServer postToServer;

    private TextView priceOne, nameOne;
    private ImageView imageOne;

    private TextView priceTwo, nameTwo;
    private ImageView imageTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        postToServer = new PostToServer(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        priceOne = (TextView)findViewById(R.id.price_one);
        nameOne = (TextView)findViewById(R.id.name_one);
        imageOne = (ImageView)findViewById(R.id.image_one);

        priceTwo = (TextView)findViewById(R.id.price_two);
        nameTwo = (TextView)findViewById(R.id.name_two);
        imageTwo = (ImageView)findViewById(R.id.image_two);


        productIndex = getIntent().getExtras().getInt(Helper.PRODUCT_ID);
        final String productTitle = getIntent().getExtras().getString(Helper.PRODUCT_NAME);
        setTitle(productTitle);

        productImage = (ImageView)findViewById(R.id.image);

        productName = (TextView)findViewById(R.id.p_name);
        productPrice = (TextView)findViewById(R.id.p_price);
        favoriteCount = (TextView)findViewById(R.id.favorite_count);
        productDescription = (TextView)findViewById(R.id.p_description);
        productColor = (TextView)findViewById(R.id.p_color);
        productSize = (TextView)findViewById(R.id.p_size);
        productQuantity = (TextView)findViewById(R.id.p_quantity);

        favoriteIcon = (ImageView)findViewById(R.id.favorite_icon);
        //if a user has like this product disable icon.
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Helper.isNetworkAvailable(ProductDetailsActivity.this)){
                    Helper.displayErrorMessage(ProductDetailsActivity.this, getString(R.string.no_internet));
                }else{
                    UserObject user = ((CustomApplication)getApplication()).getLoginUser();
                    Log.d(TAG, "Values " + String.valueOf(productIndex) + " " + String.valueOf(user.getId()));
                    sendToServer(String.valueOf(productIndex), String.valueOf(user.getId()));
                }
            }
        });

        if(!Helper.isNetworkAvailable(this)){
            Helper.displayErrorMessage(this, getString(R.string.no_internet));
        }else{
            UserObject user = ((CustomApplication)getApplication()).getLoginUser();
            getProductFromRemoteServer(String.valueOf(productIndex), String.valueOf(user.getId()));
        }

        Button addToBasketButton = (Button)findViewById(R.id.add_to_basket);
        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new Cart object and add to cart list object
                if(productToCart != null){
                    CartObject orderItem = new CartObject(productIndex, productTitle, productToCart.getImages(), productToCart.getPrice(), 1,
                            productToCart.getDiscount(), productToCart.getCoupon_type());
                    ((CustomApplication)getApplication()).addMenuItemToCart(orderItem);
                    invalidateCart();
                }else{
                    Helper.displayErrorMessage(ProductDetailsActivity.this, "Product has not been added to cart");
                }
            }
        });
    }

    private  void sendToServer(String productId, String userId){
        Map<String, String> params = new HashMap<String, String>();
        params.put(Helper.ID, productId);
        params.put(Helper.USER_ID, userId);
        postToServer.remoteServerCall(Helper.PATH_TO_UPDATE_FAVORITE , params, (Listener<SuccessObject>) postAndGetFavorite());
    }

    private Listener<SuccessObject> postAndGetFavorite() {
        return new Listener<SuccessObject>() {
            @Override
            public void onResponse(SuccessObject response) {
                try {
                    favoriteIcon.setImageResource(R.drawable.love);
                    favoriteIcon.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getProductFromRemoteServer(String id, String userId){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.ID, id);
        params.put(Helper.USER_ID, userId);
        GsonRequest<ProductObject[]> serverRequest = new GsonRequest<ProductObject[]>(
                Request.Method.POST,
                Helper.PATH_TO_SINGLE_PRODUCT,
                ProductObject[].class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(serverRequest);
    }

    private Listener<ProductObject[]> createRequestSuccessListener() {
        return new Listener<ProductObject[]>() {
            @Override
            public void onResponse(ProductObject[] response) {
                try {

                    if(response.length > 0){
                        productToCart = response[0];

                        String serverImagePath = Helper.PUBLIC_FOLDER + productToCart.getImages();
                        Glide.with(ProductDetailsActivity.this).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(300, 300).into(productImage);

                        productName.setText(productToCart.getNames());
                        productPrice.setText("Ksh" + productToCart.getPrice());
                        favoriteCount.setText("90");
                        productDescription.setText(productToCart.getDescription());
                        productColor.setText(productToCart.getColor());
                        productSize.setText(""+ productToCart.getSize()+"ml");
                        productQuantity.setText(productToCart.getQuantity() + " Items Available");
                        if(productToCart.getFavorite() == 1){
                            favoriteIcon.setImageResource(R.drawable.love);
                            favoriteIcon.setEnabled(false);
                        }else {
                            favoriteIcon.setEnabled(true);
                            favoriteIcon.setImageResource(R.drawable.redlove);
                        }
                        if(response.length > 1){
                            ProductObject productOne = response[1];
                            String oneImagePath = Helper.PUBLIC_FOLDER + productOne.getImages();
                            Glide.with(ProductDetailsActivity.this).load(oneImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(300, 300).into(imageOne);
                            priceOne.setText("Ksh" + productOne.getPrice());
                            nameOne.setText(productOne.getNames());
                        }

                        if(response.length > 2){
                            ProductObject productTwo = response[2];
                            String twoImagePath = Helper.PUBLIC_FOLDER + productTwo.getImages();
                            Glide.with(ProductDetailsActivity.this).load(twoImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(300, 300).into(imageTwo);
                            priceTwo.setText("Ksh" + productTwo.getPrice());
                            nameTwo.setText(productTwo.getNames());
                        }

                    }else{
                        Helper.displayErrorMessage(ProductDetailsActivity.this, "No favorite product found");
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

    private void invalidateCart() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_shop);
        int mCount = ((CustomApplication)getApplication()).cartItemCount();
        DrawCart dCart = new DrawCart(this);
        menuItem.setIcon(dCart.buildCounterDrawable(mCount, R.drawable.cart));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_shop) {
            Intent checkoutIntent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            startActivity(checkoutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
