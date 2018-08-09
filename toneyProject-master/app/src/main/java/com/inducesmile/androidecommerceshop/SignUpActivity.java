package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextView displayError;

    private EditText fullname;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText address;
    private EditText phoneNumber;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        fullname = (EditText)findViewById(R.id.fullname);
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        address = (EditText)findViewById(R.id.address);
        phoneNumber = (EditText)findViewById(R.id.phone_number);

        Button createAccountButton = (Button)findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Helper.isNetworkAvailable(SignUpActivity.this)){
                    Helper.displayErrorMessage(SignUpActivity.this, getString(R.string.no_internet));
                }else{
                    String mFullname = fullname.getText().toString();
                    String mUsername = username.getText().toString();
                    String mEmail = email.getText().toString();
                    String mPassword = password.getText().toString();
                    String mAddress = address.getText().toString();
                    String mPhone = phoneNumber.getText().toString();

                    if(TextUtils.isEmpty(mFullname) || TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mEmail) ||
                            TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mPhone)){
                        Helper.displayErrorMessage(SignUpActivity.this, getString(R.string.invalid_input_field));
                    }else{
                        registerToRemoteServer(mFullname, mUsername, mEmail, mPassword, mAddress, mPhone);
                    }
                }
            }
        });
    }

    private void registerToRemoteServer(String fullname, String username, String email, String password, String address, String phoneNumber){
        dialog.setContentText("Signing up..");
        dialog.show();

        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.FULLNAME, fullname);
        params.put(Helper.USERNAME, username);
        params.put(Helper.EMAIL, email);
        params.put(Helper.PASSWORD, password);
        params.put(Helper.ADDRESS, address);
        params.put(Helper.PHONE, phoneNumber);

        GsonRequest<UserObject> serverRequest = new GsonRequest<UserObject>(
                Request.Method.POST,
                Helper.PATH_TO_SERVER_REGISTRATION,
                UserObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(SignUpActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<UserObject> createRequestSuccessListener() {
        return new Response.Listener<UserObject>() {
            @Override
            public void onResponse(UserObject response) {
                dialog.dismiss();
                try {
                    Log.d(TAG, "Json Response " + response.getStatus());
                    if(TextUtils.isEmpty(response.getStatus())){
                        //save user login data to a shared preference
                        String userData = ((CustomApplication)getApplication()).getGsonObject().toJson(response);
                        ((CustomApplication)getApplication()).getShared().setUserData(userData);

                        // enter main shop page
                        Intent loginUserIntent = new Intent(SignUpActivity.this, ShopActivity.class);
                        startActivity(loginUserIntent);
                    }else{
                        Toast.makeText(SignUpActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();
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
                dialog.dismiss();
                error.printStackTrace();
            }
        };
    }

}
