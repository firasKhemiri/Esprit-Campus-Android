package com.example.firas.internfiretest.Tools.Events;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.firas.internfiretest.Entities.Event;
import com.example.firas.internfiretest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class EventsMainHorizontal extends Fragment {
    String host;
    ArrayList<Event> allevents;
    private RecyclerView mRecyclerView;
  //  private MyRecyclerViewAdapter adapter;
    private MyEventRecyclerViewAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/feed/events";

    int offset = 0;

    LinearLayoutManager mLayoutManager;





  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_pager_activity);
*/
    View view;

    SwipeRefreshLayout swipy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager_activity, container, false);

        host = getString(R.string.aphost);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


        host = getString(R.string.aphost);


        mLayoutManager = new LinearLayoutManager(view.getContext());

        url = getArguments().getString("url");


        mRecyclerView.setLayoutManager(mLayoutManager);

        allevents = new ArrayList<>();

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRecyclerView.getItemAnimator().endAnimations();

        AddPost(host + url);


        swipy = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allevents = new ArrayList<>();

                loading = true;
                mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);

                mRecyclerView.setLayoutManager(mLayoutManager);

                previousTotal = 0;


                first = true;
                offset = 0;

                adapter = new MyEventRecyclerViewAdapter( view.getContext(), allevents);

                swipy = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

                swipy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        allevents = new ArrayList<>();

                        loading = true;
                        mLayoutManager = new LinearLayoutManager(view.getContext());

                        mRecyclerView.setLayoutManager(mLayoutManager);

                        previousTotal = 0;

                        first = true;
                        offset = 0;

                        adapter = new MyEventRecyclerViewAdapter(view.getContext(),allevents);
                        mRecyclerView.setAdapter(adapter);

                        AddPost(host + url);
                    }
                });

                mRecyclerView.setAdapter(adapter);
                AddPost(host + url);
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

                        offset = offset + 5;
                        Log.i("Yaeye!", "end called");
                        AddPost(host + url + "?limit=5&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }

                }
            }
        });

        return view;
    }




    public void AddPost(String url) {

        SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);


        //  Toast.makeText(MainActivity.this,"id: "+ token, Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            swipy = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
                            swipy.setRefreshing(false);

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                Event item = new Event();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");
                                String desc= menuObject.getString("description");
                                String date_created= menuObject.getString("date_created");
                                boolean is_picture= menuObject.getBoolean("is_picture");
                                String pic_url= menuObject.getString("picture_url");
                                String location= menuObject.getString("location");

                                boolean is_participated= menuObject.getBoolean("is_participated");
                                int num_particip = menuObject.getInt("particip_count");


                                String date_beg= menuObject.getString("date_beg");
                                String date_end= menuObject.getString("date_end");

                                int my_id = menuObject.getInt("my_id");


                                    String own= menuObject.getString("owner");
                                    JSONObject owner = new JSONObject(own);

                             //   String fac_name= owner.getString("fac_name");
                                    int user_id= owner.getInt("id");
                                    String f_name= owner.getString("first_name");
                                    String l_name= owner.getString("last_name");
                                    String prof_pic_url= owner.getString("picture");
                                    boolean is_social = owner.getBoolean("is_social");


                                item.setId(id);
                                item.setPost_name(name);
                                item.setPost_desc(desc);
                                item.setIs_picture(is_picture);
                                item.setPic_url(pic_url);

                                item.setIs_social(is_social);
                                item.setPhotoprof(prof_pic_url);
                                item.setIdme(my_id);
                                item.setUser_id(user_id);
                                item.setUser_flname(f_name+' '+l_name);
                                item.setDate(date_created);
                                item.setLocation(location);
                                item.setDateBeg(date_beg);
                                item.setDateEnd(date_end);

                                item.setParticipated(is_participated);
                                item.setNum_participants(num_particip);

                                allevents.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if(first) {
                               // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allevents);
                                adapter = new MyEventRecyclerViewAdapter(view.getContext(),allevents);

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else

                                adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "error", Toast.LENGTH_LONG).show();

                           /* Intent intent = new Intent(view.getContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);  */

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                 /*       Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
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
