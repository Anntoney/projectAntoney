package com.inducesmile.androidecommerceshop.utils;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.entity.SuccessObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;

import java.util.Map;

public class PostToServer {

    private static final String TAG = PostToServer.class.getSimpleName();

    private Context context;

    public PostToServer(Context context) {
        this.context = context;
    }

    public void remoteServerCall(String path, Map<String, String> params, Response.Listener<SuccessObject> listener) {
        GsonRequest<SuccessObject> serverRequest = new GsonRequest<SuccessObject>(
                Request.Method.POST,
                path,
                SuccessObject.class,
                params,
                listener,
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(serverRequest);
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
