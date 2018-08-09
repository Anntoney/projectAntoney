package com.inducesmile.androidecommerceshop.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.adapter.CustomFragmentPageAdapter;

public class ShopHomeFragment extends Fragment {

    private static final String TAG = ShopHomeFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ShopHomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_home, container, false);
        getActivity().setTitle(getString(R.string.exotics));

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager(), getActivity()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
