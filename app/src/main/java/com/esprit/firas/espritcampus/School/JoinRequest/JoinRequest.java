package com.esprit.firas.espritcampus.School.JoinRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FIRAS on 27/09/2017.
 */



public class JoinRequest extends AppCompatActivity {

    Button send ;
    EditText message ;
    RadioButton etud ;
    RadioButton prof ;

    Spinner choix_dep;
    Spinner choix_classe;


    ArrayList<String> items_dep;
    ArrayList<Integer> items_dep_id;

    ArrayList<String> items_class;
    ArrayList<Integer> items_class_id;

    String host;

    int dep_id;
    int classe_id;

    boolean is_prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_request);

        Intent i = getIntent();
        final int school_id = i.getIntExtra("school_id",0);

        SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);

        items_class= new ArrayList<>();
        items_class_id= new ArrayList<>();

        items_dep= new ArrayList<>();
        items_dep_id= new ArrayList<>();


        host = getString(R.string.aphost);



        send = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        etud = (RadioButton) findViewById(R.id.etud_rad);
        prof = (RadioButton) findViewById(R.id.prof_rad);

        choix_dep= (Spinner) findViewById(R.id.choix_dep);

        choix_classe= (Spinner) findViewById(R.id.choix_classe);
        choix_classe.setVisibility(View.VISIBLE);

        send.setVisibility(View.VISIBLE);



        prof.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    choix_classe.setVisibility(View.GONE);
                    is_prof = true;
                }
            }
        });



        etud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    choix_classe.setVisibility(View.VISIBLE);
                    is_prof = false;
                }

            }
        });



        GetDeps(school_id);



        choix_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = choix_dep.getSelectedItem().toString();
                Toast.makeText(JoinRequest.this,text+" "+items_dep_id.get(choix_dep.getSelectedItemPosition()),Toast.LENGTH_LONG ).show();

                dep_id= items_dep_id.get(choix_dep.getSelectedItemPosition());

                GetClasses(dep_id);

                if (is_prof)
                    choix_classe.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        choix_classe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = choix_classe.getSelectedItem().toString();
                Toast.makeText(JoinRequest.this,text+" "+items_class_id.get(choix_classe.getSelectedItemPosition()),Toast.LENGTH_LONG ).show();

                classe_id= items_class_id.get(choix_classe.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg="";
                msg = message.getText().toString();
                Toast.makeText(getApplicationContext(), school_id+" "+dep_id+" "+classe_id+" "+is_prof+" msg: "+msg+" enm", Toast.LENGTH_LONG).show();

                Services s = new Services();

                String iss_prof;
                if (is_prof)
                    iss_prof = "True";
                else
                    iss_prof = "False";

                s.Addpending(school_id,dep_id,classe_id,iss_prof,msg,getApplicationContext());

            }
        });

    }











    public void GetDeps(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/school/"+id+"/departments/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                Category item = new Category();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");


                                items_dep.add(name);
                                items_dep_id.add(id);

                                choix_dep.setVisibility(View.VISIBLE);


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(JoinRequest.this,
                                    android.R.layout.simple_spinner_dropdown_item, items_dep);
                            choix_dep.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        ) {
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










    public void GetClasses(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/department/"+id+"/classes/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            items_class = new ArrayList<>();
                            items_class_id = new ArrayList<>();

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                Category item = new Category();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");


                                items_class.add(name);
                                items_class_id.add(id);

                                choix_dep.setVisibility(View.VISIBLE);


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(JoinRequest.this,
                                    android.R.layout.simple_spinner_dropdown_item, items_class);
                            choix_classe.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        ) {
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
