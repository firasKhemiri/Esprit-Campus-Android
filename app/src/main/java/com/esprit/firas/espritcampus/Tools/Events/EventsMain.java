package com.esprit.firas.espritcampus.Tools.Events;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.Entities.Event;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class EventsMain extends Fragment {
    String host;
    ArrayList<Event> allevents;
    private RecyclerView mRecyclerView;
  //  private MyRecyclerViewAdapter adapter;
    private MyEventRecyclerInRecyclerViewAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/feed/events";

    int offset = 0;

    GridLayoutManager mLayoutManager;





  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_pager_activity);
*/
    View view;

    SwipeRefreshLayout swipy;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager_activity, container, false);

        host = getString(R.string.aphost);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progress_bar);

        mLayoutManager = new GridLayoutManager(view.getContext(), 2);

        mLayoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch( adapter.getItemViewType(position) ) {
                    case 1:
                        return 2;
                    case 0:
                        return 1;
                    default:
                        return -1;
                }
            }
        });


        url = getArguments().getString("url");


        mRecyclerView.setLayoutManager(mLayoutManager);

        allevents = new ArrayList<>();

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
                allevents = new ArrayList<>();

                loading = true;

                previousTotal = 0;


                first = true;
                offset = 0;

                adapter = new MyEventRecyclerInRecyclerViewAdapter( view.getContext(), allevents);

                swipy = view.findViewById(R.id.swipe_container);

                swipy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        allevents = new ArrayList<>();

                        loading = true;

                        mRecyclerView.setLayoutManager(mLayoutManager);

                        previousTotal = 0;

                        first = true;
                        offset = 0;

                        adapter = new MyEventRecyclerInRecyclerViewAdapter(view.getContext(),allevents);
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
                    if (!loading && ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 12)) {

                        offset = offset + 12;
                        Log.i("Yaeye!", "end called");
                        AddPost(host + url + "?limit=12&offset=" + offset);

             //           Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

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

                            if (jObject.length()>0) {

                                for (int i = 0; i < jObject.length(); i++) {

                                    Event item = new Event();

                                    JSONObject menuObject = jObject.getJSONObject(i);

                                    int id = menuObject.getInt("id");
                                    String name = menuObject.getString("name");
                                    String desc = menuObject.getString("description");
                                    String date_created = menuObject.getString("date_created");
                                    boolean is_picture = menuObject.getBoolean("is_picture");
                                    String pic_url = menuObject.getString("picture_url");
                                    String location = menuObject.getString("location");

                                    boolean is_participated = menuObject.getBoolean("is_participated");
                                    int num_particip = menuObject.getInt("particip_count");


                                    String date_beg = menuObject.getString("date_beg");
                                    String date_end = menuObject.getString("date_end");

                                    int maxLimit = 0;
                                    boolean isLimited = menuObject.getBoolean("is_limited");
                                    if (isLimited)
                                        maxLimit = menuObject.getInt("max_limit");
                                    boolean reachedLimit = menuObject.getBoolean("get_is_limit");

                                    String cat = menuObject.getString("category");
                                    JSONObject category = new JSONObject(cat);

                                    int cat_id = category.getInt("id");
                                    String cat_name = category.getString("name");
                                    String cat_pic = category.getString("pic_url");

                                    Category categorie = new Category();
                                    categorie.setId(cat_id);
                                    categorie.setName(cat_name);
                                    categorie.setPic_url(cat_pic);

                                    int my_id = menuObject.getInt("my_id");

                                    String own = menuObject.getString("owner");
                                    JSONObject owner = new JSONObject(own);

                                    //   String fac_name= owner.getString("fac_name");
                                    int user_id = owner.getInt("id");
                                    String f_name = owner.getString("first_name");
                                    String l_name = owner.getString("last_name");
                                    String prof_pic_url = owner.getString("picture");
                                    boolean is_social = owner.getBoolean("is_social");

                                    String participantsJson = menuObject.getString("get_particip_follow");
                                    ArrayList<User> participants = new ArrayList<>();

                                    if (!participantsJson.equals("") && !participantsJson.equals("null")) {

                                        JSONArray jPar = new JSONArray(participantsJson);
                                        if (jPar.length() > 0) {
                                            for (int p = 0; p < jPar.length(); p++) {
                                                JSONObject pare = jPar.getJSONObject(p);

                                                int par_id = pare.getInt("id");
                                                String par_flname = pare.getString("first_name") + " " + pare.getString("last_name");
                                                String par_photo = pare.getString("picture");

                                                User userPar = new User();
                                                userPar.setId(par_id);
                                                userPar.setUser_flname(par_flname);
                                                userPar.setProfile_pic(par_photo);

                                                participants.add(userPar);
                                            }
                                        }
                                    }

                                    item.setId(id);
                                    item.setPost_name(name);
                                    item.setPost_desc(desc);
                                    item.setIs_picture(is_picture);
                                    item.setPic_url(pic_url);

                                    item.setIs_social(is_social);
                                    item.setPhotoprof(prof_pic_url);
                                    item.setIdme(my_id);
                                    item.setUser_id(user_id);
                                    item.setUser_flname(f_name + ' ' + l_name);
                                    item.setDate(date_created);
                                    item.setLocation(location);
                                    item.setDateBeg(date_beg);
                                    item.setDateEnd(date_end);

                                    item.setParticipated(is_participated);
                                    item.setNum_participants(num_particip);

                                    item.setType(0);

                                    item.setCategory(categorie);

                                    item.setParticpants(participants);

                                    item.setLimited(isLimited);
                                    if (isLimited) {
                                        item.setMaxLimit(maxLimit);
                                        item.setReachedLimit(reachedLimit);
                                    }
                                    allevents.add(item);
                                }

                                allevents.get(0).setType(1);

                            }

                            progressBar.setVisibility(View.GONE);

                            if(first) {
                               // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allevents);
                                adapter = new MyEventRecyclerInRecyclerViewAdapter(view.getContext(),allevents);

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
