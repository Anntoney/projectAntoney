package com.inducesmile.androidecommerceshop;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.inducesmile.androidecommerceshop.fragment.FavoriteFragment;
import com.inducesmile.androidecommerceshop.fragment.OrdersFragment;
import com.inducesmile.androidecommerceshop.fragment.ProfileFragment;
import com.inducesmile.androidecommerceshop.fragment.WalletFragment;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManagers;
    private FragmentTransaction transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);
        fragmentManagers = getSupportFragmentManager();
        transactions = fragmentManagers.beginTransaction();
        fragment = new ProfileFragment();
        setEnteringFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_orders:
                        fragment = new OrdersFragment();
                        break;
                    case R.id.action_wallet:
                        fragment = new WalletFragment();
                        break;
                    case R.id.action_deals:
                        fragment = new FavoriteFragment();
                        break;
                }
                setEnteringFragment();
                return true;
            }
        });
    }

    private void setEnteringFragment(){
        transactions = fragmentManagers.beginTransaction();
        transactions.replace(R.id.main_container, fragment);
        transactions.addToBackStack(null);
        transactions.commit();
    }

    @Override
    public void onBackPressed() {

    }
}
