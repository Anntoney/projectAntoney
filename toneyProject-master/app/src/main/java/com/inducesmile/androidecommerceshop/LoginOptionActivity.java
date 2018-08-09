package com.inducesmile.androidecommerceshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inducesmile.androidecommerceshop.entity.CategoryObject;
import com.inducesmile.androidecommerceshop.network.GsonRequest;
import com.inducesmile.androidecommerceshop.network.VolleySingleton;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;

public class LoginOptionActivity extends AppCompatActivity {

    private static final String TAG = LoginOptionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        if(!Helper.isNetworkAvailable(this)){
            Helper.displayErrorMessage(this, getString(R.string.no_internet));
        }else{
            storeTitlesFromRemoteServer();
        }


        Button signInButton = (Button)findViewById(R.id.sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginOptionActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginOptionActivity.this, SignUpActivity.class);
                startActivity(signInIntent);
            }
        });
    }


    private void storeTitlesFromRemoteServer(){
        GsonRequest<CategoryObject[]> serverRequest = new GsonRequest<CategoryObject[]>(
                Request.Method.GET,
                Helper.PATH_TO_CATEGORY_TITLE,
                CategoryObject[].class,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<CategoryObject[]> createRequestSuccessListener() {
        return new Response.Listener<CategoryObject[]>() {
            @Override
            public void onResponse(CategoryObject[] response) {
                try {
                    Log.d(TAG, "Json Response " + response.toString());
                    String[] tabTitles = new String[4];
                    tabTitles[0] = "ALL";
                    if(response.length > 0){
                        for(int i = 0; i < response.length; i++){
                            tabTitles[i+1] = response[i].getName();
                        }
                        String saveTitles = convertArrayToString(tabTitles);
                        ((CustomApplication)getApplication()).getShared().saveTabTitle(saveTitles);

                    }else{
                        Helper.displayErrorMessage(LoginOptionActivity.this, "");
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

    private String convertArrayToString(String[] titleList){
        String commaSeperated = "";
        for(int i = 0; i < titleList.length; i++){
            commaSeperated = commaSeperated + titleList[i] + ",";
        }
        return commaSeperated.substring(0, commaSeperated.length() - 1);
    }
}
