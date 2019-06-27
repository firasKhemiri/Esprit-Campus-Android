package com.esprit.firas.espritcampus.School.Categories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class CategoriesFrag extends DialogFragment {

    View view;

    String host;

    ArrayList<Category> cats;

    int schoolId;
    String location;
    int students;
    int profs;
    int deps;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager mLayoutManager;

    private CategoriesRecyclerViewAdapter adapter;
    CheckBox catbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.categories_recycler, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Bundle mArgs = getArguments();

        cats = new ArrayList<>();

        schoolId = mArgs.getInt("school_id");

        recyclerView = view.findViewById(R.id.recycler_view_categories);
        progressBar = view.findViewById(R.id.progress_bar_cats);


        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        //ADAPTER
        adapter=new CategoriesRecyclerViewAdapter(view.getContext(),cats);

        GetCategories(schoolId);

        return view;
    }


    public void GetCategories(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/school/"+id+"/categories/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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

                                Category item = new Category();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");
                                String pic= menuObject.getString("pic_url");

                                item.setId(id);
                                item.setName(name);
                                item.setPic_url(pic);


                                cats.add(item);
                            }
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
