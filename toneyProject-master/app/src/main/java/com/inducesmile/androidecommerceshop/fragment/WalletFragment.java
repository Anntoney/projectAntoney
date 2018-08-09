package com.inducesmile.androidecommerceshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.pavlospt.CircleView;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.WalletObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletFragment extends Fragment {

    private static final String TAG = WalletFragment.class.getSimpleName();

    private PieChart pieChart;
    private CircleView circleView;

    public WalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        getActivity().setTitle("Wallet");

        circleView = (CircleView)view.findViewById(R.id.amount_spend);

        pieChart = (PieChart)view.findViewById(R.id.pie_chart);
        pieChart.getDescription().setText("");

        if(!Helper.isNetworkAvailable(getActivity())){
            Helper.displayErrorMessage(getActivity(), getString(R.string.no_internet));
        }else{
            int id = ((CustomApplication)getActivity().getApplication()).getLoginUser().getId();
            walletDetailsFromRemoteServer(String.valueOf(id));
        }

        return view;
    }

    private void walletDetailsFromRemoteServer(String id){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.ID, id);
        GsonRequest<WalletObject> serverRequest = new GsonRequest<WalletObject>(
                Request.Method.POST,
                Helper.PATH_TO_WALLET,
                WalletObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<WalletObject> createRequestSuccessListener() {
        return new Response.Listener<WalletObject>() {
            @Override
            public void onResponse(WalletObject response) {
                try {
                    //Helper.displayErrorMessage(getActivity(), " " + response.getAmount());
                    circleView.setTitleText("$" + response.getAmount());

                    List<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(Float.parseFloat(response.getPod()), "POD"));
                    pieEntries.add(new PieEntry(Float.parseFloat(response.getPaypal()), "PAYPAL"));
                    pieEntries.add(new PieEntry(Float.parseFloat(response.getCreditcard()), "CREDIT CARD"));

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                    pieDataSet.setColors(new int[]{R.color.colorPrimaryDark, R.color.promotion_blue, R.color.colorOpposite}, getContext());

                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.invalidate();

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
