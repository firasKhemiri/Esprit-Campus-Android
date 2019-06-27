package com.esprit.firas.espritcampus.Signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.esprit.firas.espritcampus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SignupActivity2 extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    String token;

    String host;

    EditText lsname, fsname,datenaiss;
  //  DatePicker date;
    Button btnLogin;
    String etPassword,etUsername,etEmail;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_next);
        host = getString(R.string.aphost);


        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);


        Intent i = getIntent();
        etUsername = i.getStringExtra("username");
        etPassword = i.getStringExtra("password");
        etEmail = i.getStringExtra("email");

        lsname = (EditText) findViewById(R.id.lsname);
        fsname = (EditText) findViewById(R.id.fsname);
        //  date = (DatePicker) findViewById(R.id.date);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        datenaiss = (EditText) findViewById(R.id.date_naiss);

      //  Toast.makeText(getApplicationContext(),"user "+ etUsername+"pass "+etPassword+"email "+etEmail, Toast.LENGTH_LONG).show();





        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datenaiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity2.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        datenaiss.setInputType(InputType.TYPE_NULL);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (lsname.getText().length() == 0)
                    lsname.setError("Saisir un nom.");
                else if (fsname.getText().length() == 0)
                    fsname.setError("Saisir un prenom.");
                else if (datenaiss.getText().length() == 0)
                    datenaiss.setError("Saisir votre date de naissance.");



                else {

                    final String first_name = fsname.getText().toString();
                    final String last_name = lsname.getText().toString();



                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = host + "/api/signup/";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);

                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(response);

                                //        Toast.makeText(getApplicationContext(),"jawwek behi "+ token, Toast.LENGTH_LONG).show();

                                        Login(etUsername,etPassword);

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
                                    Toast.makeText(getApplicationContext(), " username"+etUsername + " "+first_name
                                            +" "+last_name+" "+etPassword+" "+etEmail, Toast.LENGTH_LONG).show();

                                    Login(etUsername,etPassword);


                                }
                            }
                    ) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username", etUsername);
                            params.put("first_name", first_name);
                            params.put("last_name", last_name);
                            params.put("password", etPassword);
                            params.put("email", etEmail);
                            params.put("birthdate", datenaiss.getText().toString());

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
        });
    }



    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datenaiss.setText(sdf.format(myCalendar.getTime()));
    }



    public void Login(final String username, final String pass) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = host + "/auth/token/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

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



                            Intent i = new Intent(SignupActivity2.this, SignupActivity3.class);
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


}