package com.inducesmile.androidecommerceshop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.inducesmile.androidecommerceshop.adapter.ProductCategoryAdapter;
import com.inducesmile.androidecommerceshop.entity.CategoryObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private RecyclerView categoriesRecyclerView;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle("Product Categories");

        categoriesRecyclerView = (RecyclerView)view.findViewById(R.id.product_categories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            categoriesFromRemoteServer();
        }
        return view;
    }

    private void categoriesFromRemoteServer(){

        GsonRequest<CategoryObject[]> serverRequest = new GsonRequest<CategoryObject[]>(
                Request.Method.GET,
                Helper.PATH_TO_CATEGORY,
                CategoryObject[].class,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<CategoryObject[]> createRequestSuccessListener() {
        return new Response.Listener<CategoryObject[]>() {
            @Override
            public void onResponse(CategoryObject[] response) {
                try {
                    Log.d(TAG, "counts " + response.length);
                    if(response.length > 0){
                        //display restaurant menu information
                        List<CategoryObject> categoryList = new ArrayList<>();
                        for(int i = 0; i < response.length; i++){
                            categoryList.add(response[i]);
                        }
                        ProductCategoryAdapter mAdapter = new ProductCategoryAdapter(getActivity(), categoryList);
                        categoriesRecyclerView.setAdapter(mAdapter);

                    }else{
                        Helper.displayErrorMessage(getActivity(), "No category found");
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
