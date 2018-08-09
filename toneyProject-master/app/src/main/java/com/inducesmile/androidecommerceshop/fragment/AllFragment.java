package com.inducesmile.androidecommerceshop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.MenuCategoryObject;

import java.util.ArrayList;
import java.util.List;


public class AllFragment extends Fragment {

    private static final String TAG = AllFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    public AllFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.featured_product);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);

       /* CategoryAdapter mAdapter = new CategoryAdapter(getActivity(), menuObject());
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);*/

        return view;
    }

    private List<MenuCategoryObject> menuObject() {
        List<MenuCategoryObject> featuredProducts = new ArrayList<MenuCategoryObject>();
        featuredProducts.add(new MenuCategoryObject("Masked Hat", "hat1"));
        featuredProducts.add(new MenuCategoryObject("Masked Hat", "hat1"));
        featuredProducts.add(new MenuCategoryObject("Masked Hat", "hat1"));
        featuredProducts.add(new MenuCategoryObject("Masked Hat", "hat1"));
        return featuredProducts;
    }

}
