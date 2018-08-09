package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.entity.UserObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    EditText fullname, username, email, address, phone;
    private UserObject user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        fullname = (EditText)findViewById(R.id.fullname);
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        address = (EditText)findViewById(R.id.address);
        phone = (EditText)findViewById(R.id.phone_number);

        user = ((CustomApplication)getApplication()).getLoginUser();

        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());

        Button editProfileButton = (Button)findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Helper.isNetworkAvailable(EditProfileActivity.this)){
                    Helper.displayErrorMessage(EditProfileActivity.this, getString(R.string.no_internet));
                }else{
                    String mFullname = fullname.getText().toString();
                    String mUsername = username.getText().toString();
                    String mEmail = email.getText().toString();
                    String mAddress = address.getText().toString();
                    String mPhone = phone.getText().toString();

                    if(TextUtils.isEmpty(mFullname) || TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mEmail)
                            || TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mPhone)){
                        Helper.displayErrorMessage(EditProfileActivity.this, getString(R.string.invalid_input_field));
                    }else{
                        String id = String.valueOf(user.getId());
                        registerToRemoteServer(id, mFullname, mUsername, mEmail, mAddress, mPhone);
                    }
                }
                Intent profileIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(profileIntent);

            }
        });
    }

    private void registerToRemoteServer(String id, String fullname, String username, String email, String address, String phoneNumber){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.ID, id);
        params.put(Helper.FULLNAME, fullname);
        params.put(Helper.USERNAME, username);
        params.put(Helper.EMAIL, email);
        params.put(Helper.ADDRESS, address);
        params.put(Helper.PHONE, phoneNumber);

        GsonRequest<UserObject> serverRequest = new GsonRequest<UserObject>(
                Request.Method.POST,
                Helper.PATH_TO_EDIT_PROFILE,
                UserObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<UserObject> createRequestSuccessListener() {
        return new Response.Listener<UserObject>() {
            @Override
            public void onResponse(UserObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.getStatus());
                    if(TextUtils.isEmpty(response.getStatus())){
                        //save user login data to a shared preference
                        String userData = ((CustomApplication)getApplication()).getGsonObject().toJson(response);
                        ((CustomApplication)getApplication()).getShared().setUserData(userData);

                        // enter main shop page
                        Intent loginUserIntent = new Intent(EditProfileActivity.this, ShopActivity.class);
                        startActivity(loginUserIntent);
                    }else{
                        Toast.makeText(EditProfileActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();
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
