package com.esprit.firas.espritcampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Signup.SignupActivity;
import com.esprit.firas.espritcampus.Tools.Services;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;


public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail;
    Button btnLogin, btnSing, btnCheck, btnTest;


    String host;

    // String host="http://192.168.8.100:8000";

    SharedPreferences sharedpreferences;

    String token;

    ProgressBar progressBar;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSing = findViewById(R.id.sign);
        btnCheck = findViewById(R.id.check);
        btnTest = findViewById(R.id.test);


        progressBar = findViewById(R.id.progress_bar);


        host = getString(R.string.aphost);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);

        btnSing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etUsername.getText().length() == 0)
                    etUsername.setError("Saisir un login.");
                else if (etPassword.getText().length() == 0)
                    etPassword.setError("Saisir un mot de passe.");

                else {
                    String username = etUsername.getText().toString();
                    String pass = etPassword.getText().toString();

                    Login(username, pass);
                }

            }

        });


        callbackManager = CallbackManager.Factory.create();


        LoginButton loginButton = findViewById(R.id.login_button);

        loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("access_token", String.valueOf(loginResult.getAccessToken().getToken()));
                editor.apply();


                progressBar.setVisibility(View.VISIBLE);

                Login_fb(String.valueOf(loginResult.getAccessToken().getToken()));
            }



            @Override
            public void onCancel() {
                // App code

                Toast.makeText(getApplicationContext(),"cancel ",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();

                Log.d("Response Face", exception.toString());
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    public void Login(final String username, final String pass) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = host + "/auth/token/";
        StringRequest postRequest = new StringRequest(POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String token = obj.getString("access_token");
                         //   Toast.makeText(getApplicationContext(),"jawwek behi "+ token, Toast.LENGTH_LONG).show();


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("token", token);
                            editor.apply();

                            new Services().AddFCM(getApplicationContext());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id", "zq4VSjDQRM6rSu8jTbDtItuUxfbPKOq9OihFdqqG");
//                params.put("client_id", "moqWNsg7kYnOOZuFtZy9JvdOTNXsqMZRoQSb0g5c");
                params.put("client_secret", "XBtUrDliBozzoSMjxJUwxa5yL5cm9TCNSjY6tKVw6m6yoR0cp5zeIHk1g6ukdv2nDt7f7jH70rA42soWtRkSgwRhToKJvw2suvFq0KNtQOkKoXr9wbociX83CJTJRqW7");
//               params.put("client_secret", "n0gKh9Q8pTLVY9gQfmvqnp8Gll2zeDC6gRKPdqWGmgdUWX05h6uMZm6RdOw4G0wjhoViqcqqEtlVPIXnYLgCitbPb0DWIujreYp6q6VP6rmzBhuP2fJ7FujNgNN2lYXp");
                params.put("grant_type", "password");
                params.put("username", username);
                params.put("password", pass);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }






    public void Login_fb(final String token) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = host + "/auth/convert-token/";
        StringRequest postRequest = new StringRequest(POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String tokenn = obj.getString("access_token");
                     //       Toast.makeText(getApplicationContext(),"jawwek behi "+ tokenn, Toast.LENGTH_LONG).show();


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("token", tokenn);
                            editor.apply();

                            progressBar.setVisibility(View.INVISIBLE);

                            new Services().AddFCM(getApplicationContext());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id", "zq4VSjDQRM6rSu8jTbDtItuUxfbPKOq9OihFdqqG");
//                params.put("client_id", "moqWNsg7kYnOOZuFtZy9JvdOTNXsqMZRoQSb0g5c");
                params.put("client_secret", "XBtUrDliBozzoSMjxJUwxa5yL5cm9TCNSjY6tKVw6m6yoR0cp5zeIHk1g6ukdv2nDt7f7jH70rA42soWtRkSgwRhToKJvw2suvFq0KNtQOkKoXr9wbociX83CJTJRqW7");
//               params.put("client_secret", "n0gKh9Q8pTLVY9gQfmvqnp8Gll2zeDC6gRKPdqWGmgdUWX05h6uMZm6RdOw4G0wjhoViqcqqEtlVPIXnYLgCitbPb0DWIujreYp6q6VP6rmzBhuP2fJ7FujNgNN2lYXp");
                params.put("grant_type", "convert_token");
                params.put("backend", "facebook");
                params.put("token", token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }
}
