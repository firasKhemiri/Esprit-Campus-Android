package com.esprit.firas.espritcampus.Search.Users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;

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

public class SearchFollowers extends AppCompatActivity {

    private List<User> searchList;
    private RecyclerView mRecyclerView;
    private SearchRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private int i;

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    int offset = 0;

    String text;

    String host;

    LinearLayoutManager mLayoutManager;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        searchList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        final EditText search = (EditText) findViewById(R.id.search);

        host = getString(R.string.aphost);

        progressBar.setVisibility(View.GONE);

        Intent i = getIntent() ;
        url = i.getStringExtra("url");

        /*String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
        new DownloadTask().execute(url);*/

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        GetSearchResults(host+"/"+url);


        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    text = String.valueOf(search.getText());


                    searchList = new ArrayList<>();

                    first = true;
                    offset = 0;

                    loading = true;
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());

                    mRecyclerView.setLayoutManager(mLayoutManager);

                    previousTotal = 0;

                    adapter = new SearchRecyclerViewAdapter(getApplicationContext(), searchList);
                    mRecyclerView.setAdapter(adapter);

                    GetSearchResults(host+"/"+url+"?search="+text);
                    return true;
                }
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 5)) {

                        offset = offset + 30;
                        Log.i("Yaeye!", "end called");
                        GetSearchResults(host+"/api/users?search="+text + "&limit=30&offset=" + offset);

                  //      Toast.makeText(getApplicationContext(), " " + offset, Toast.LENGTH_SHORT).show();

                        loading = true;
                    }
                }
            }
        });

    }



    public void GetSearchResults(final String url)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    String name_user;
                    String username;
                    String image_user;
                    String size;
                    int id_user;


                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {

                            progressBar.setVisibility(View.GONE);


                            JSONObject cur = new JSONObject(response);
                            String result = cur.getString("results");

                            String classe_name = "";
                            int classe_id;

                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                User user = new User();

                                JSONObject obj2 = jObject.getJSONObject(i);

                                name_user = obj2.getString("first_name") + " " + obj2.getString("last_name");
                                username = obj2.getString("username");
                                image_user = obj2.getString("picture");
                                id_user = obj2.getInt("id");
                                int myid = obj2.getInt("my_id");
                                boolean ifollow = obj2.getBoolean("ifollow");

                                String classe = obj2.getString("classe");

                                if (!classe.equals("") && !classe.equals("null")) {
                                    JSONObject sch = new JSONObject(classe);
                                    classe_name = sch.getString("name");
                                    classe_id = sch.getInt("id");
                                }

                                //  date_msg = obj2.getString("")

                                user.setUser_flname(name_user);
                                user.setClasse(classe_name);
                                user.setProfile_pic(image_user);
                                user.setId(id_user);
                                user.setIfollowuser(ifollow);
                                user.setMy_id(myid);


                                searchList.add(user);

                              //  Toast.makeText(view.getContext()," "+user.getId(),Toast.LENGTH_LONG).show();
                            }


                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allcategories);
                                adapter = new SearchRecyclerViewAdapter(getApplicationContext(),searchList);

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else
                                adapter.notifyDataSetChanged();


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
                        Toast.makeText(getApplicationContext(),"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };


        queue.cancelAll("TAG"+i);
        i++;
        progressBar.setVisibility(View.VISIBLE);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)).setTag("TAG"+i);
        queue.add(postRequest);
    }


}
