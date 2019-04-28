package com.example.firas.internfiretest.Tools.Feed;

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
import com.example.firas.internfiretest.DbHelpers.LocalFeed.FeedHandler;
import com.example.firas.internfiretest.Entities.Category;
import com.example.firas.internfiretest.Entities.FeedItem;
import com.example.firas.internfiretest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class PostsMainNoSqLite extends Fragment {
    String host;
    ArrayList<FeedItem> allposts;
    private RecyclerView mRecyclerView;
  //  private MyRecyclerViewAdapter adapter;
    private MultiViewTypeAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/feed/";
    int offset = 0;
    LinearLayoutManager mLayoutManager;
    FeedHandler db;
    SwipeRefreshLayout swipy;
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager_activity, container, false);
        db = new FeedHandler(getContext());
        host = getString(R.string.aphost);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);


        host = getString(R.string.aphost);


        mLayoutManager = new LinearLayoutManager(view.getContext());

        assert getArguments() != null;
        url = getArguments().getString("url");



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

                adapter = new MultiViewTypeAdapter(allposts, view.getContext());
                mRecyclerView.setAdapter(adapter);

                AddPost(host + url);


            }
        });


        mRecyclerView.setLayoutManager(mLayoutManager);

        allposts = new ArrayList<>();

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        Objects.requireNonNull(mRecyclerView.getItemAnimator()).endAnimations();

    //    GetLocalPosts();
        AddPost(host + url);


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


                    //    Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }

                }
            }
        });

        return view;
    }

 /*   public void GetLocalPosts() {


        ArrayList<FeedItem> postList;
        postList = db.SelectPosts();


        adapter = new MultiViewTypeAdapter(postList,view.getContext());
        mRecyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

    }*/


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

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            swipy = view.findViewById(R.id.swipe_container);
                            swipy.setRefreshing(false);

                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                FeedItem item = new FeedItem();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");
                                String desc= menuObject.getString("description");
                                String date_created= menuObject.getString("date_created");
                                boolean is_picture= menuObject.getBoolean("is_picture");
//                                boolean is_question= menuObject.getBoolean("is_question");
                                String pic_url= menuObject.getString("picture_url");
                                boolean is_liked =  menuObject.getBoolean("is_liked");

                                int num_likes = menuObject.getInt("num_likes");
                                int num_comments = menuObject.getInt("num_comments");

                                int my_id = menuObject.getInt("my_id");

                                String category_name= menuObject.getString("category_name");

                                String own= menuObject.getString("owner");
                                JSONObject owner = new JSONObject(own);

                             //   String fac_name= owner.getString("fac_name");
                                int user_id= owner.getInt("id");
                                String f_name= owner.getString("first_name");
                                String l_name= owner.getString("last_name");
                                String prof_pic_url= owner.getString("picture");
                                boolean is_social = owner.getBoolean("is_social");

                                String cat = menuObject.getString("category");
                                JSONObject category = new JSONObject(cat);

                                int cat_id = category.getInt("id");
                                String cat_name = category.getString("name");
                                String cat_pic = category.getString("pic_url");

                                Category categorie = new Category();
                                categorie.setId(cat_id);
                                categorie.setName(cat_name);
                                categorie.setPic_url(cat_pic);


                                item.setId(id);
                                item.setPost_name(name);
                                item.setPost_desc(desc);
                           //     item.setIs_question(is_question);
                                item.setIs_picture(is_picture);
                                item.setPic_url(pic_url);
                                item.setIs_social(is_social);
                                item.setIdme(my_id);

                                item.setUser_id(user_id);
                           //     item.setUsername(fac_name);
                                item.setUser_flname(f_name+' '+l_name);
                                item.setPhotoprof(prof_pic_url);

                                item.setLiked(is_liked);

                                item.setDate(date_created);

                                item.setNum_likes(num_likes);
                                item.setNum_comments(num_comments);

                                item.setCategory(categorie);
                                item.setCat_name(category_name);

                                allposts.add(item);

                            }



                            progressBar.setVisibility(View.GONE);

                            if(first) {
                               // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new MultiViewTypeAdapter(allposts,view.getContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;

                          //      db.deletePosts();
                          //      db.deletePosts();

                   /*             for (int i = 0 ; i <allposts.size();i++)
                                    db.AddPOST(allposts.get(i));*/
                            }

                            else

                                adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "error", Toast.LENGTH_LONG).show();

                         /*   Intent intent = new Intent(view.getContext(), LoginActivity.class);
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
                        Toast.makeText(view.getContext(), "network error", Toast.LENGTH_LONG).show();
               /*         Intent intent = new Intent(view.getContext(), LoginActivity.class);
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
