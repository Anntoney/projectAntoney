package com.inducesmile.androidecommerceshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.StoreObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.Helper;

public class AboutFragment extends Fragment {

    private static final String TAG = AboutFragment.class.getSimpleName();

    private TextView name, description, address, phone, email, opening_time;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        getActivity().setTitle("Our Store");

        name = (TextView)view.findViewById(R.id.store_name);
        address = (TextView)view.findViewById(R.id.store_address);
        description = (TextView)view.findViewById(R.id.store_info);
        email = (TextView)view.findViewById(R.id.store_email);
        phone = (TextView)view.findViewById(R.id.store_phone_number);
        opening_time = (TextView)view.findViewById(R.id.opening_time);

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            storeInformationFromRemoteServer();
        }

        return view;
    }

    private void storeInformationFromRemoteServer(){
        GsonRequest<StoreObject> serverRequest = new GsonRequest<StoreObject>(
                Request.Method.GET,
                Helper.PATH_TO_STORE,
                StoreObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<StoreObject> createRequestSuccessListener() {
        return new Response.Listener<StoreObject>() {
            @Override
            public void onResponse(StoreObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    if(!TextUtils.isEmpty(response.getName())){
                        name.setText(response.getName());
                        address.setText(response.getAddress());
                        description.setText(response.getDescription());
                        email.setText(response.getEmail());
                        phone.setText(response.getPhone());
                        opening_time.setText(response.getOpening_time());

                    }else{
                        Helper.displayErrorMessage(getActivity(), "Store information missing. Go to admin panel and fix it");
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
