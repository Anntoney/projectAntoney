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
import com.inducesmile.androidecommerceshop.adapter.OrdersAdapter;
import com.inducesmile.androidecommerceshop.entity.OrdersObject;
import com.inducesmile.androidecommerceshop.entity.UserObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersFragment extends Fragment {

    private static final String TAG = OrdersFragment.class.getSimpleName();

    private RecyclerView orderRecyclerView;

    public OrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        getActivity().setTitle("Order history");

        orderRecyclerView = (RecyclerView)view.findViewById(R.id.order_history);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        orderRecyclerView.setLayoutManager(gridLayoutManager);

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            UserObject mUser = ((CustomApplication)getActivity().getApplication()).getLoginUser();
            Log.d(TAG, "user id " + mUser.getId());
            userOrderFromRemoteServer(String.valueOf(mUser.getId()));
        }

        return view;
    }

    private void userOrderFromRemoteServer(String id){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.ID, id);

        GsonRequest<OrdersObject[]> serverRequest = new GsonRequest<OrdersObject[]>(
                Request.Method.POST,
                Helper.PATH_TO_ORDER,
                OrdersObject[].class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<OrdersObject[]> createRequestSuccessListener() {
        return new Response.Listener<OrdersObject[]>() {
            @Override
            public void onResponse(OrdersObject[] response) {
                try {
                    if(response.length > 0){
                        //display restaurant menu information
                        List<OrdersObject> productList = new ArrayList<>();
                        for(int i = 0; i < response.length; i++){
                            Log.d(TAG, "Product count " + response.length);
                            productList.add(response[i]);
                        }
                        OrdersAdapter mAdapter = new OrdersAdapter(getActivity(), productList);
                        orderRecyclerView.setAdapter(mAdapter);
                    }else{
                        Helper.displayErrorMessage(getActivity(), "You have no order yet. Make your first order now");
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
