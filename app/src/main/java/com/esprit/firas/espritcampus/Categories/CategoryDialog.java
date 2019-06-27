package com.esprit.firas.espritcampus.Categories;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
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
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryDialog extends DialogFragment implements CategoryDialogAdapter.OnItemClicked {

    String host;
    ArrayList<Category> allcategories;
    private RecyclerView mRecyclerView;
    private CategoryDialogAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/categories/";

    int offset = 0;

    LinearLayoutManager mLayoutManager;

    String iden;

    View view;

    private OnCompleteListener mListener;


    public CategoryDialog() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.items_popup,container);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assert getArguments() != null;
        url = getArguments().getString("url");

        iden = url;

        host = getString(R.string.aphost);

        mRecyclerView = view.findViewById(R.id.recycler_view_pop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBar = view.findViewById(R.id.progress_bar_pop);

        host = getString(R.string.aphost);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        allcategories = new ArrayList<>();


        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        GetCategories(host + url);

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
                        GetCategories(host + url + "?limit=5&offset=" + offset);

                 //       Toast.makeText(getApplicationContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }
                }
            }
        });



        return view;
    }


    @Override
    public void onItemClick(int position) {
        this.mListener.onComplete(allcategories.get(position));
        this.dismiss();
    }


    public void GetCategories(final String url) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

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

                                int id = menuObject.getInt("id");
                                String name = menuObject.getString("name");
                                String desc = menuObject.getString("description");
                                String pic_url = menuObject.getString("pic_url");
                                int num_posts;
                                if (iden.equals("/api/categories_pub/"))
                                    num_posts = menuObject.getInt("num_posts");
                                else
                                    num_posts = menuObject.getInt("num_events");

                                String own = menuObject.getString("creator");
                                JSONObject owner = new JSONObject(own);

                                int user_id = owner.getInt("id");
                                String f_name = owner.getString("first_name");
                                String l_name = owner.getString("last_name");

                                item.setId(id);
                                item.setName(name);
                                item.setDescription(desc);

                                item.setNum_posts(num_posts);

                                item.setCreator_id(user_id);
                                item.setCreator_name(f_name + ' ' + l_name);
                                item.setPic_url(pic_url);

                                allcategories.add(item);
                            }
                            progressBar.setVisibility(View.GONE);

                            if (first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allcategories);
                                adapter = new CategoryDialogAdapter(allcategories, getApplicationContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            } else
                                adapter.notifyDataSetChanged();

                            adapter.setOnClick(CategoryDialog.this);

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
                    }
                })
        {
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


    public interface OnCompleteListener {
        void onComplete(Category cat);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }


}
