package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

public class NewAddressActivity extends AppCompatActivity {

    private static final String TAG = NewAddressActivity.class.getSimpleName();

    private EditText address, zipCose, country;

    private TextView primaryAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        setTitle(getString(R.string.new_addres));

        String primaryAdd = getIntent().getExtras().getString("PRIMARY_ADDRESS");
        primaryAddress = (TextView)findViewById(R.id.primary_address);
        primaryAddress.setText(primaryAdd);

        address = (EditText)findViewById(R.id.street_name);
        zipCose = (EditText)findViewById(R.id.zip_code);
        country = (EditText)findViewById(R.id.country);

        Button saveAddressButton = (Button)findViewById(R.id.save_new_address);
        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressInput = address.getText().toString();
                String zipCodeInput = zipCose.getText().toString();
                String countryInput = country.getText().toString();

                if(TextUtils.isEmpty(addressInput) || TextUtils.isEmpty(zipCodeInput) || TextUtils.isEmpty(countryInput)){
                    Helper.displayErrorMessage(NewAddressActivity.this, "All input fields must be filled");
                }
                String fullAddress = addressInput + ", " + zipCodeInput + ", " + countryInput;
                ((CustomApplication)getApplication()).getShared().saveDeliveryAddress(fullAddress);
                address.setText("");
                zipCose.setText("");
                country.setText("");
                Intent checkIntent = new Intent(NewAddressActivity.this, CartActivity.class);
                startActivity(checkIntent);
            }
        });
    }
}
