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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView error;
    private EditText email;
    private EditText password;

    private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);

        UserObject mUser = ((CustomApplication)getApplication()).getLoginUser();
        if(mUser != null){
            Intent loginUserIntent = new Intent(LoginActivity.this, ShopActivity.class);
            startActivity(loginUserIntent);
        }

        error = (TextView)findViewById(R.id.login_error);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Helper.isNetworkAvailable(LoginActivity.this)){
                    Helper.displayErrorMessage(LoginActivity.this, getString(R.string.no_internet));
                }else{
                    String mEmail = email.getText().toString();
                    String mPassword = password.getText().toString();

                    if(TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)){
                        Helper.displayErrorMessage(LoginActivity.this, getString(R.string.invalid_input_field));
                    }else{


                        loginToRemoteServer(mEmail, mPassword);
                    }
                }
            }
        });

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signInIntent);
            }
        });
    }

    private void loginToRemoteServer(String email, String password){
        dialog.setContentText("Signing you in...");
        dialog.setTitleText("Please Wait :)");
        dialog.show();
        Map<String, String> params = new HashMap<String,String>();
        params.put(Helper.EMAIL, email);
        params.put(Helper.PASSWORD, password);

        GsonRequest<UserObject> serverRequest = new GsonRequest<UserObject>(
                Request.Method.POST,
                Helper.PATH_TO_SERVER_LOGIN,
                UserObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(serverRequest);


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
                        Intent loginUserIntent = new Intent(LoginActivity.this, ShopActivity.class);
                        startActivity(loginUserIntent);
                    }else{
                        Toast.makeText(LoginActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();

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
              Toast.makeText(LoginActivity.this, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
