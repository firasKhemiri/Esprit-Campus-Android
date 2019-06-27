package com.esprit.firas.espritcampus.Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Accueil.AccueilActivity;
import com.esprit.firas.espritcampus.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    String host;
    String token;



    EditText etUsername, etPassword,etEmail;
    Button btnLogin;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        host = getString(R.string.aphost);


        sharedpreferences = getSharedPreferences("MyPREFERENCES",MODE_PRIVATE);


        etUsername = (EditText)findViewById(R.id.etUsername);
        etEmail = (EditText)findViewById(R.id.etEamil);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        callbackManager = CallbackManager.Factory.create();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                if (etUsername.getText().length() == 0)
                    etUsername.setError("Saisir un login.");
                else if (etPassword.getText().length() == 0)
                    etUsername.setError("Saisir un mot de passe.");
                else if (etEmail.getText().length() == 0)
                    etUsername.setError("Saisir un mot de passe.");

                else{
                    Intent intent = new Intent(getApplicationContext(), SignupActivity2.class)
                            .putExtra("username", username)
                            .putExtra("email", email)
                            .putExtra("password", password);
                    startActivity(intent);
                }
            }
        });




    /*    com.kosalgeek.asynctask.AsyncResponse as = new com.kosalgeek.asynctask.AsyncResponse() {
            @Override
            public void processFinish(String s)
            {
                Toast.makeText(SignupActivity.this, s, Toast.LENGTH_LONG).show();
                Log.v("TAG", s);
            }
        };

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(SignupActivity.this,as);
        loginTask.execute("http://192.168.8.101:8000/api/checklog");
*/



        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("access_token", String.valueOf(loginResult.getAccessToken().getToken()));
                editor.apply();

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

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }




    public void Login_fb(final String token) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = host + "/auth/convert-token/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String tokenn = obj.getString("access_token");
                    //        Toast.makeText(getApplicationContext(),"jawwek behi "+ tokenn, Toast.LENGTH_LONG).show();


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("token", tokenn);
                            editor.apply();



                            Intent i = new Intent(SignupActivity.this, AccueilActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

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
                params.put("client_secret", "XBtUrDliBozzoSMjxJUwxa5yL5cm9TCNSjY6tKVw6m6yoR0cp5zeIHk1g6ukdv2nDt7f7jH70rA42soWtRkSgwRhToKJvw2suvFq0KNtQOkKoXr9wbociX83CJTJRqW7");
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