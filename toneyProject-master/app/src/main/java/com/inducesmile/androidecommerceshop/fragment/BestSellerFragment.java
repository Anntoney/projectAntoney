package com.inducesmile.androidecommerceshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.adapter.CategoryAdapter;
import com.inducesmile.androidecommerceshop.entity.ProductObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class BestSellerFragment extends Fragment {

    private static final String TAG = BestSellerFragment.class.getSimpleName();

    private RecyclerView bestRecyclerView;


    public BestSellerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_best_seller, container, false);
        getActivity().setTitle("Best Sellers");

        bestRecyclerView = (RecyclerView)view.findViewById(R.id.best_seller_list);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        bestRecyclerView.setLayoutManager(mGrid);
        bestRecyclerView.setHasFixedSize(true);
        bestRecyclerView.setNestedScrollingEnabled(false);

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            productPromotionFromRemoteServer();
        }

        return view;
    }

    private void productPromotionFromRemoteServer(){
        GsonRequest<ProductObject[]> serverRequest = new GsonRequest<ProductObject[]>(
                Request.Method.GET,
                Helper.PATH_TO_SALES,
                ProductObject[].class,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<ProductObject[]> createRequestSuccessListener() {
        return new Response.Listener<ProductObject[]>() {
            @Override
            public void onResponse(ProductObject[] response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    if(response.length > 0){
                        //display restaurant menu information
                        List<ProductObject> productList = new ArrayList<>();
                        for(int i = 0; i < response.length; i++){
                            Log.d(TAG, "Product count " + response.length);
                            productList.add(response[i]);
                        }
                        CategoryAdapter mAdapter = new CategoryAdapter(getActivity(), productList);
                        bestRecyclerView.setAdapter(mAdapter);

                    }else{
                        Helper.displayErrorMessage(getActivity(), "No product found");
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
