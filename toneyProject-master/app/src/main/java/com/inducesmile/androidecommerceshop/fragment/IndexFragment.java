package com.inducesmile.androidecommerceshop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


public class IndexFragment extends Fragment {

    private static final String TAG = IndexFragment.class.getSimpleName();

    private CarouselView carouselView;

    int[] sampleImages = {R.drawable.vegetables, R.drawable.ruits, R.drawable.juice};

    private RecyclerView recyclerView;

    public IndexFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        carouselView = (CarouselView)view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        recyclerView = (RecyclerView)view.findViewById(R.id.horizontal_product);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            listProductFromRemoteServer();
        }

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void listProductFromRemoteServer(){
        GsonRequest<ProductObject[]> serverRequest = new GsonRequest<ProductObject[]>(
                Request.Method.GET,
                Helper.PATH_TO_SHOP_HOME,
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
                        recyclerView.setAdapter(mAdapter);

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
