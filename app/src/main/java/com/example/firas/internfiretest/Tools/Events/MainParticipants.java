package com.example.firas.internfiretest.Tools.Events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.firas.internfiretest.Entities.User;
import com.example.firas.internfiretest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by FIRAS on 15/05/2017.
 */

public class MainParticipants extends AppCompatActivity {



    private List<User> userList;
    private RecyclerView mRecyclerView;
    private SearchRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private int i;
    String postid;


    String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_pager_activity);


        Intent i = getIntent();
        postid = i.getStringExtra("postid");

        userList = new ArrayList<>();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        host = getString(R.string.aphost);


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        getParticipants();

      }



    public void getParticipants()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = host+"/api/event/"+postid+"/";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>()
                {
                    String usr;
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //  Toast.makeText(MainPostActivity.this,"id: "+ response, Toast.LENGTH_LONG).show();

                        try {

                            JSONObject cur = new JSONObject(response);

                            int my_id = cur.getInt("my_id");
                            String result = cur.getString("participants");

                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                JSONObject menuObject = jObject.getJSONObject(i);

                                User item = new User();

                                int user_id= menuObject.getInt("id");
                                String f_name= menuObject.getString("first_name");
                                String l_name= menuObject.getString("last_name");
                                String prof_pic_url= menuObject.getString("picture");
                                boolean is_social = menuObject.getBoolean("is_social");
                                boolean ifollow = menuObject.getBoolean("ifollow");


                                progressBar.setVisibility(View.GONE);



                                item.setIs_social(is_social);
                                item.setProfile_pic(prof_pic_url);
                                item.setMy_id(my_id);
                                item.setId(user_id);
                                item.setUser_flname(f_name+' '+l_name);
                                item.setIfollowuser(ifollow);

                                userList.add(item);

                                //     Toast.makeText(getApplicationContext(),cm.getComment(),Toast.LENGTH_LONG).show();
                            }
                            adapter = new SearchRecyclerViewAdapter(getApplicationContext(),userList );

                            mRecyclerView.setAdapter(adapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(MainParticipants.this,"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);
                params.put("event_id", String.valueOf(postid));
                params.put("type", "event");

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
