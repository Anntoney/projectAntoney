package com.inducesmile.androidecommerceshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.adapter.PromotionAdapter;
import com.inducesmile.androidecommerceshop.entity.PromotionObject;

import java.util.ArrayList;
import java.util.List;


public class PromotionFragment extends Fragment {

    private static final String TAG = PromotionFragment.class.getSimpleName();

    private RecyclerView promotionRecyclerView;

    public PromotionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        getActivity().setTitle("Promotion");

        promotionRecyclerView = (RecyclerView)view.findViewById(R.id.promotion);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        promotionRecyclerView.setLayoutManager(linearLayoutManager);
        promotionRecyclerView.setHasFixedSize(true);

        PromotionAdapter mAdapter = new PromotionAdapter(getActivity(), testData());
        promotionRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private List<PromotionObject> testData() {
        List<PromotionObject> test = new ArrayList<>();
        test.add(new PromotionObject("Baseball cap", "$30.00 $20.00", "hat"));
        test.add(new PromotionObject("Dunce cap", "$21.00 $10.00", "hat"));
        test.add(new PromotionObject("Gatsby cap", "$22.00 $12.00", "hat"));
        return test;
    }


}
