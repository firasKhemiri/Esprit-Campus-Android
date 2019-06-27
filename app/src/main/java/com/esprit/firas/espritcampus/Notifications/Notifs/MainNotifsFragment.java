package com.esprit.firas.espritcampus.Notifications.Notifs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.esprit.firas.espritcampus.Entities.Notif;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class MainNotifsFragment extends Fragment {

    private List<Notif> demandList;
    private RecyclerView mRecyclerView;
    private NotifsRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    private boolean first = true;


    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    String url = "/api/notifications/";

    int offset = 0;

    String host;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager_activity, container, false);


        demandList = new ArrayList<>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        host = getString(R.string.aphost);

        /*String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
        new DownloadTask().execute(url);*/

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_notif);
        add.setVisibility(View.VISIBLE);


        GetNotifs(host + url);


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
                        GetNotifs(host + url + "?limit=5&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }

                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(),CreateNotif.class);
                startActivity(i);
            }
        });


        return view;
    }

    public void GetNotifs(String url)
    {

        SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    int id;
                    String message;
                    String cont_name;
                    String cont_img;
                    boolean cont_social;
                    int myid;
                    int cont_id;

                    String result;

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                Notif demand = new Notif();

                                JSONObject c = jObject.getJSONObject(i);

                                id = c.getInt("id");
                                String name = c.getString("name");
                                String description = c.getString("description");
                                String date = c.getString("date_created");

                                JSONObject user = c.getJSONObject("owner");
                                cont_name = user.getString("first_name") + " " + user.getString("last_name");
                                cont_id = user.getInt("id");
                                cont_social = user.getBoolean("is_social");
                                cont_img = user.getString("picture");
                                boolean is_admin = user.getBoolean("is_admin");

                                if (is_admin) {
                                    JSONObject fac = user.getJSONObject("belongsTo");
                                    cont_name = fac.getString("name");
                                    cont_img = fac.getString("pic_url");
                                    cont_id = fac.getInt("id");
                                }


                                User userr = new User();


                                demand.setId(id);
                                userr.setUser_flname(cont_name);
                                userr.setProfile_pic(cont_img);
                                userr.setId(cont_id);
                                userr.setIs_social(cont_social);
                                demand.setUser(userr);
                                //  demand.setMyid(myid);
                                //  demand.setDep_id(dep_id);
                                demand.setName(name);
                                demand.setDescription(description);
                                demand.setCreatedAt(date);


                                demandList.add(demand);
                            }

                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new NotifsRecyclerViewAdapter(view.getContext(), demandList);

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else

                                adapter.notifyDataSetChanged();


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(),"error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(view.getContext(),"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);

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
