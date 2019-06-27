package com.esprit.firas.espritcampus.Tools.Users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class Users extends Fragment {


    String url;

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    int offset = 0;

    ArrayList<User> allusers;
    private RecyclerView mRecyclerView;
    private UsersRecyclerAdapter adapter;
    private ProgressBar progressBar;
    View view;


    String host;
    CircleImageView imgprof;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity_main2, container, false);


        host = getString(R.string.aphost);


        url = getArguments().getString("url");

        allusers = new ArrayList<>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        imgprof = (CircleImageView) view.findViewById(R.id.profimg);

       // final GridLayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        GetStudents(host+url);


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
                    if (!loading &&((visibleItemCount + pastVisiblesItems) >= totalItemCount - 5)) {

                        offset = offset + 5;
                        Log.i("Yaeye!", "end called");
                        GetStudents(host+ url + "?limit=5&offset="+offset);


                        Toast.makeText(view.getContext()," "+ offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }
                }
            }
        });

        return view;
    }



    public void GetStudents(String url) {

        SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);


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

                                User item = new User();

                                JSONObject userObject = jObject.getJSONObject(i);

                                int id = userObject.getInt("id");
                                String username = userObject.getString("first_name") +" "+ userObject.getString("last_name");;
                                int user_id = userObject.getInt("id");
                                String f_name = userObject.getString("first_name");
                                String l_name = userObject.getString("last_name");
                                String prof_pic_url = userObject.getString("picture");
                                boolean is_social = userObject.getBoolean("is_social");
                                boolean ifollow = userObject.getBoolean("ifollow");
                                int id_me = userObject.getInt("my_id");


                                String classe_name="";
                                String branch_name="";
                                int classe_id;


                                String classe = userObject.getString("classe");

                                if (!classe.equals("") && !classe.equals("null")) {
                                    JSONObject sch = new JSONObject(classe);
                                    classe_name = sch.getString("name");
                                    classe_id = sch.getInt("id");


                                    String branch = sch.getString("branch");

                                    if (!branch.equals("") && !branch.equals("null")) {
                                        JSONObject bch = new JSONObject(branch);
                                         branch_name = bch.getString("name");
                                    }
                                }

                                item.setId(user_id);
                                item.setUsername(username);
                                item.setUser_flname(f_name + ' ' + l_name);
                                item.setProfile_pic(prof_pic_url);
                                item.setIs_social(is_social);
                                item.setDepartment(branch_name);
                                item.setClasse(classe_name);
                                item.setIfollowuser(ifollow);
                                item.setMy_id(id_me);

                                allusers.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if (first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allusers);
                                adapter = new UsersRecyclerAdapter( view.getContext(),allusers);

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
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }



}


