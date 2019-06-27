package com.esprit.firas.espritcampus.Search.School;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.esprit.firas.espritcampus.Entities.School;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SearchSchools extends Fragment {



    private List<School> searchList;
    private RecyclerView mRecyclerView;
    private SchoolsViewAdapter adapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_main, container, false);


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_main, container, false);
*/

        searchList = new ArrayList<>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        final EditText search = (EditText) view.findViewById(R.id.search);

        host = getString(R.string.aphost);


        progressBar.setVisibility(View.GONE);

        /*String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
        new DownloadTask().execute(url);*/

        mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);


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

                    adapter = new SchoolsViewAdapter(view.getContext(), searchList);
                    mRecyclerView.setAdapter(adapter);


                    GetSearchResults(host+"/api/schools?search="+text);
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
                        GetSearchResults(host+"/api/schools?search="+text + "&limit=5&offset=" + offset);

                        Toast.makeText(view.getContext(), " " + offset, Toast.LENGTH_LONG).show();

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
                    String name_school;
                    int students;
                    String image_school;
                    boolean ibelong;
                    int id_school;


                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {

                            progressBar.setVisibility(View.GONE);


                            JSONObject cur = new JSONObject(response);
                            String result = cur.getString("results");

                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                School school = new School();

                                JSONObject obj2 = jObject.getJSONObject(i);

                                name_school = obj2.getString("name") ;
                                image_school = obj2.getString("pic_url");
                                id_school = obj2.getInt("id");
                                //int myid = obj2.getInt("my_id");
                                ibelong = obj2.getBoolean("ibelong");
                                students = obj2.getInt("students_count");

                                school.setName(name_school);
                                school.setId(id_school);
                                school.setPhotoprof(image_school);
                                school.setIbelong(ibelong);
                                school.setStudents(students);


                                searchList.add(school);

                              //  Toast.makeText(view.getContext()," "+school.getId(),Toast.LENGTH_LONG).show();
                            }


                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allcategories);
                                adapter = new SchoolsViewAdapter(view.getContext(),searchList);

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
