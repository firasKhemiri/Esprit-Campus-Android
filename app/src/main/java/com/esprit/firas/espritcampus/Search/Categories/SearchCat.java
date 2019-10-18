package com.esprit.firas.espritcampus.Search.Categories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Categories.CategoryViewAdapter;
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by FIRAS on 15/05/2017.
 */

public class SearchCat extends Fragment {
    String host;
    ArrayList<Category> allcategories;
    private RecyclerView mRecyclerView;
    //  private MyRecyclerViewAdapter adapter;
    private CategoryViewAdapter adapter;

    private String text;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    private boolean first = true;

    String url = "/api/categories";

    int offset = 0;

    private int i;
    private TextView notice;



    LinearLayoutManager mLayoutManager;





    /*  @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.accueil_pager_activity);
  */
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_main, container, false);

        host = getString(R.string.aphost);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progress_bar);

        notice = view.findViewById(R.id.notice);

        progressBar.setVisibility(View.GONE);

        host = getString(R.string.aphost);


        mLayoutManager = new LinearLayoutManager(view.getContext());


        mRecyclerView.setLayoutManager(mLayoutManager);

        allcategories = new ArrayList<>();


        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
       // GetCategories(host + url+"?search="+);

        final EditText search = view.findViewById(R.id.search);


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    text = String.valueOf(search.getText());

                    allcategories = new ArrayList<>();

                    first = true;
                    offset = 0;

                    loading = true;
                    mLayoutManager = new LinearLayoutManager(view.getContext());

                    mRecyclerView.setLayoutManager(mLayoutManager);

                    previousTotal = 0;

                    adapter = new CategoryViewAdapter(allcategories,view.getContext());
                    mRecyclerView.setAdapter(adapter);

                    GetCategories(host + url+"?search="+text);
                    return true;
                }
                return false;
            }
        });

        Button back = view.findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });

    /*    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        GetCategories(host + url+"?search="+text + "&limit=5&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }
                }
            }
        });*/

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

                                Category item = new Category();

                                JSONObject menuObject = jObject.getJSONObject(i);

                                int id= menuObject.getInt("id");
                                String name= menuObject.getString("name");
                                String desc= menuObject.getString("description");
                                String pic_url= menuObject.getString("pic_url");


                                String own= menuObject.getString("creator");
                                JSONObject owner = new JSONObject(own);

                                int user_id= owner.getInt("id");
                                String f_name= owner.getString("first_name");
                                String l_name= owner.getString("last_name");


                                item.setId(id);
                                item.setName(name);
                                item.setDescription(desc);


                                item.setCreator_id(user_id);
                                item.setCreator_name(f_name+' '+l_name);
                                item.setPic_url(pic_url);

                                allcategories.add(item);

                            }


                            progressBar.setVisibility(View.GONE);

                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allcategories);
                                adapter = new CategoryViewAdapter(allcategories,view.getContext());

                                mRecyclerView.setAdapter(adapter);
                                first = false;
                            }

                            else
                                adapter.notifyDataSetChanged();

                            if (allcategories.size()==0)
                            {
                                notice.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }

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
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        allcategories = new ArrayList<>();
        adapter = new CategoryViewAdapter(allcategories,view.getContext());
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
