package com.example.firas.internfiretest.Search.Cours;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.firas.internfiretest.Entities.FeedItem;
import com.example.firas.internfiretest.LoginActivity;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Courses.CoursViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class SearchCours extends Fragment {
    String host;
    ArrayList<FeedItem> allcours;
    private RecyclerView mRecyclerView;
    //  private MyRecyclerViewAdapter adapter;
    private CoursViewAdapter adapter;

    private String text;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/allcours";

    int offset = 0;

    private int i;



    LinearLayoutManager mLayoutManager;





    /*  @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.accueil_pager_activity);
  */
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_main, container, false);

        host = getString(R.string.aphost);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);



        progressBar.setVisibility(View.GONE);

        host = getString(R.string.aphost);


        mLayoutManager = new LinearLayoutManager(view.getContext());


        mRecyclerView.setLayoutManager(mLayoutManager);

        allcours = new ArrayList<>();


        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
       // GetCategories(host + url+"?search="+);

        final EditText search = (EditText) view.findViewById(R.id.search);

        Button back = view.findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    text = String.valueOf(search.getText());

                    allcours = new ArrayList<>();

                    first = true;
                    offset = 0;

                    loading = true;
                    mLayoutManager = new LinearLayoutManager(view.getContext());

                    mRecyclerView.setLayoutManager(mLayoutManager);

                    previousTotal = 0;

                    adapter = new CoursViewAdapter(allcours,view.getContext());
                    mRecyclerView.setAdapter(adapter);


                    GetCategories(host + url+"?search="+text);
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

                        offset = offset + 5;
                        Log.i("Yaeye!", "end called");
                        GetCategories(host + url+"?search="+text + "&limit=5&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }
                }
            }
        });

        return view;
    }




    public void GetCategories(String url) {

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

                                item.setLiked(is_liked);

                                item.setNum_likes(num_likes);
                                item.setNum_comments(num_comments);

                                item.setDate(date_created);

                                allcours.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new CoursViewAdapter(allcours,view.getContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else
                                adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "error", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(view.getContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(view.getContext(), "network error", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
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

        adapter = new CoursViewAdapter(allcours,view.getContext());
        mRecyclerView.setAdapter(adapter);

        queue.cancelAll("TAG"+i);
        i++;
        progressBar.setVisibility(View.VISIBLE);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)).setTag("TAG"+i);
        queue.add(postRequest);}
}
