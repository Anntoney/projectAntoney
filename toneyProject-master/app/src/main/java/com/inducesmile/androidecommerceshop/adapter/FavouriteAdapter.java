package com.inducesmile.androidecommerceshop.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inducesmile.androidecommerceshop.ProductDetailsActivity;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.ProductObject;
import com.inducesmile.androidecommerceshop.entity.ServerObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder>{

    private static final String TAG = FavouriteAdapter.class.getSimpleName();

    private Context context;
    private List<ProductObject> favorites;

    public FavouriteAdapter(Context context, List<ProductObject> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_layout_list, parent, false);
        return new FavouriteViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, final int position) {
        final ProductObject fObject = favorites.get(position);
        if(fObject.getStatus().equals("Available")){

            final int id = fObject.getId();
            final String name = fObject.getNames();

            String serverImagePath = Helper.PUBLIC_FOLDER + fObject.getImages();
            Glide.with(context).load(serverImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().override(250, 250).into(holder.path);
            holder.name.setText(fObject.getNames());
            holder.price.setText("Ksh" + String.valueOf(fObject.getPrice()));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to product page
                    Intent productIntent = new Intent(context, ProductDetailsActivity.class);
                    productIntent.putExtra("PRODUCT_ID", id);
                    productIntent.putExtra("PRODUCT_NAME", name);
                    context.startActivity(productIntent);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favorites.remove(position);
                    notifyDataSetChanged();
                    if(!Helper.isNetworkAvailable(context)){
                        Helper.displayErrorMessage(context, context.getString(R.string.no_internet));
                    }else{
                        int id = ((CustomApplication)context.getApplicationContext()).getLoginUser().getId();
                        deleteFavoriteFromRemoteServer(String.valueOf(id), String.valueOf(fObject.getId()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    private void deleteFavoriteFromRemoteServer(String id, String productId){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.ID, id);
        params.put("product_id", productId);
        GsonRequest<ServerObject> serverRequest = new GsonRequest<ServerObject>(
                Request.Method.POST,
                Helper.PATH_TO_DELETE_FAVORITE,
                ServerObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(serverRequest);
    }

    private Response.Listener<ServerObject> createRequestSuccessListener() {
        return new Response.Listener<ServerObject>() {
            @Override
            public void onResponse(ServerObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    if(response.getSuccess().equals("OK")){
                        Helper.displayErrorMessage(context, "Favorite deleted successfully");
                    }else{
                        Helper.displayErrorMessage(context, "Failed to delete favorite deleted. Try again");
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
}
