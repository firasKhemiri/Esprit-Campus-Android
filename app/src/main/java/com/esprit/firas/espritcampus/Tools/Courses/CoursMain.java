package com.esprit.firas.espritcampus.Tools.Courses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Entities.FeedItem;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CoursMain extends Fragment {
    String host;
    ArrayList<FeedItem> allposts;
    private RecyclerView mRecyclerView;
  //  private MyRecyclerViewAdapter adapter;
    private CoursViewAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/coursfeed/";

    int offset = 0;

    LinearLayoutManager mLayoutManager;

    SwipeRefreshLayout swipy;





  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_pager_activity);
*/
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager_activity, container, false);

        host = getString(R.string.aphost);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progress_bar);


        host = getString(R.string.aphost);


        mLayoutManager = new LinearLayoutManager(view.getContext());

        url = getArguments().getString("url");


        mRecyclerView.setLayoutManager(mLayoutManager);

        allposts = new ArrayList<>();

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRecyclerView.getItemAnimator().endAnimations();

        AddPost(host + url);

        swipy = view.findViewById(R.id.swipe_container);

        swipy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allposts = new ArrayList<>();

                loading = true;
                mLayoutManager = new LinearLayoutManager(view.getContext());

                mRecyclerView.setLayoutManager(mLayoutManager);

                previousTotal = 0;


                first = true;
                offset = 0;

                adapter = new CoursViewAdapter(allposts, view.getContext());
                mRecyclerView.setAdapter(adapter);


                AddPost(host + url);


            }
        });



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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


                      //  Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

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


                            swipy = view.findViewById(R.id.swipe_container);
                            swipy.setRefreshing(false);

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                FeedItem item = new FeedItem();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");
                                String desc= menuObject.getString("description");
                                String date_created= menuObject.getString("date_created");

                                boolean is_liked =  menuObject.getBoolean("is_liked");

                                int num_likes = menuObject.getInt("num_likes");
                                int num_comments = menuObject.getInt("num_comments");
                                String file_path= menuObject.getString("file_path");


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
                                item.setFilePath(file_path);
                                item.setIs_social(is_social);

                                item.setUser_id(user_id);
                           //     item.setUsername(fac_name);
                                item.setUser_flname(f_name+' '+l_name);
                                item.setPhotoprof(prof_pic_url);


                                item.setDate(date_created);

                                item.setLiked(is_liked);

                                item.setNum_likes(num_likes);
                                item.setNum_comments(num_comments);

                                allposts.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if(first) {
                               // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new CoursViewAdapter(allposts,view.getContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else

                                adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "error", Toast.LENGTH_LONG).show();

                          /*  Intent intent = new Intent(view.getContext(), LoginActivity.class);
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
                   /*     Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);*/
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

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
