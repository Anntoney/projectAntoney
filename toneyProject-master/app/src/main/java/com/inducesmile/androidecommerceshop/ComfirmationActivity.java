package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ComfirmationActivity extends AppCompatActivity {

    private static final String TAG = ComfirmationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirmation);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        setTitle("Order Confirmation");

        Button startTrackingButton = (Button)findViewById(R.id.shopping_button);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(ComfirmationActivity.this, ShopActivity.class);
                startActivity(orderIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent confirmIntent = new Intent(ComfirmationActivity.this, ShopActivity.class);
        startActivity(confirmIntent);
    }
}
