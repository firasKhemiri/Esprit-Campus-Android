package com.example.firas.internfiretest.Search.Users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.firas.internfiretest.Entities.User;
import com.example.firas.internfiretest.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by FIRAS on 15/05/2017.
 */

public class SearchMain extends Fragment {

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

    View view;

    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_main, container, false);

        searchList = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        final EditText search = view.findViewById(R.id.search);

        host = getString(R.string.aphost);

        progressBar.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        Button back = view.findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        //  adapter = new MyRecyclerViewAdapter(MainActivity.this, feedsList);


      /*  search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = String.valueOf(search.getText());
                GetSearchResults(text);

            }
        });*/

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    text = String.valueOf(search.getText());


                    searchList = new ArrayList<>();

                    first = true;
                    offset = 0;

                    loading = true;
                    mLayoutManager = new LinearLayoutManager(view.getContext());

                    mRecyclerView.setLayoutManager(mLayoutManager);

                    previousTotal = 0;

                    adapter = new SearchRecyclerViewAdapter(view.getContext(), searchList);
                    mRecyclerView.setAdapter(adapter);

                    GetSearchResults(host+"/api/users?search="+text);
                    return true;
                }
                return false;
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

                        offset = offset + 30;
                        Log.i("Yaeye!", "end called");
                        GetSearchResults(host+"/api/users?search="+text + "&limit=30&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_SHORT).show();

                        loading = true;
                    }
                }
            }
        });

        return view;
    }



    public void GetSearchResults(final String url)
    {

        SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);


        RequestQueue queue = Volley.newRequestQueue(view.getContext());

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
                                adapter = new SearchRecyclerViewAdapter(view.getContext(),searchList);

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
            public Map<String, String> getHeaders() {

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
