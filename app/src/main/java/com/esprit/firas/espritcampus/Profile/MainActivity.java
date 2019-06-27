package com.esprit.firas.espritcampus.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class MainActivity extends Fragment {


    String url = "/api/profile_posts/";

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    int offset = 0;

    ArrayList<FeedItem> allposts;
    private RecyclerView mRecyclerView;
    private MultiViewTypeAdapter adapter;
    private ProgressBar progressBar;
    View view;


    String host;
    CircleImageView imgprof;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity_main2, container, false);


        host = getString(R.string.aphost);

        allposts = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progress_bar);
        imgprof = view.findViewById(R.id.profimg);

       // final GridLayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        AddPost(host+url);


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
                    if (!loading &&((visibleItemCount + pastVisiblesItems) >= totalItemCount - 5)) {

                        offset = offset + 5;
                        Log.i("Yaeye!", "end called");
                        AddPost(host+ url + "?limit=5&offset="+offset);


                   //     Toast.makeText(view.getContext()," "+ offset, Toast.LENGTH_LONG).show();

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

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                FeedItem item = new FeedItem();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id = menuObject.getInt("id");
                                String name = menuObject.getString("name");
                                String desc = menuObject.getString("description");
                                String date_created = menuObject.getString("date_created");
                                boolean is_picture = menuObject.getBoolean("is_picture");
                                boolean is_question = menuObject.getBoolean("is_question");
                                String pic_url = menuObject.getString("picture_url");


                                String own = menuObject.getString("owner");
                                JSONObject owner = new JSONObject(own);

                              //  String fac_name = owner.getString("fac_name");
                                int user_id = owner.getInt("id");
                                String f_name = owner.getString("first_name");
                                String l_name = owner.getString("last_name");
                                String prof_pic_url = owner.getString("picture");


                                item.setId(id);
                                item.setPost_name(name);
                                item.setPost_desc(desc);
                                item.setIs_question(is_question);
                                item.setIs_picture(is_picture);
                                item.setPic_url(pic_url);
                                item.setDate(date_created);


                                item.setUser_id(user_id);
                            //    item.setUsername(fac_name);
                                item.setUser_flname(f_name + ' ' + l_name);
                                item.setPhotoprof(prof_pic_url);

                                allposts.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if (first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new MultiViewTypeAdapter(allposts, view.getContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            } else

                                adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "error", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    /*    Toast.makeText(view.getContext(), "network error", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
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

