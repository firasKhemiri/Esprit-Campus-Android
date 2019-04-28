package com.example.firas.internfiretest.Tools.Comments;

import android.content.Context;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.firas.internfiretest.Entities.Comments;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class CommentsDialog extends DialogFragment {

    View view;

    String host;


    ArrayList<Comments> commentslist ;

    private RecyclerView mRecyclerView;
    private CommentsRecyclerViewAdapter adapter;

    private ProgressBar progressBar;
    private int previousTotal = 0;
    private boolean loading = true;
    int visibleItemCount, totalItemCount,pastVisiblesItems;

    LinearLayoutManager mLayoutManager;
    int offset = 0;

    private boolean first = true;

    String postid;

    String url="/api/post/";
    int type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.comments_activity,container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Bundle mArgs = getArguments();

      //  int type = mArgs.getInt("postid");
        url = mArgs.getString("url");
        postid = String.valueOf(mArgs.getInt("postid"));
        type = mArgs.getInt("type");



        host = getString(R.string.aphost);


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        commentslist = new ArrayList<>();

        //RECYCER
        mRecyclerView= view.findViewById(R.id.recycler_view_comments);
        progressBar = view.findViewById(R.id.progress_bar_comments);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //ADAPTER
        adapter=new CommentsRecyclerViewAdapter(view.getContext(),commentslist);
        mRecyclerView.setAdapter(adapter);

        this.getDialog().setTitle("Comments");

        getComments(host+url);

        ImageView addComment = view.findViewById(R.id.send);
        final EditText type_cmt = view.findViewById(R.id.type_cmt);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = String.valueOf(type_cmt.getText());
             //   Toast.makeText(view.getContext(), " " + postid, Toast.LENGTH_LONG).show();
              //  Toast.makeText(view.getContext(), " " + type, Toast.LENGTH_LONG).show();

                if(type == 1)
                    new Services().AddCommentCours(postid, comment ,view.getContext());
                else
                    new Services().AddComment(postid, comment ,view.getContext());

                type_cmt.setText("");

                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                totalItemCount =0;
                first = true;
                commentslist = new ArrayList<>();

                adapter=new CommentsRecyclerViewAdapter(view.getContext(),commentslist);
                mRecyclerView.setAdapter(adapter);

                getComments(host+url);

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

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 1)) {

                        offset = offset + 30;
                        Log.i("Yaeye!", "end called");
                        getComments(host + url +"?limit=30&offset=" + offset);

                        loading = true;
                    }

                }
            }
        });

        return view;
    }



    public void getComments(String url)
    {
        SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);


        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONObject cur = new JSONObject(response);
                            String result = cur.getString("results");


                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                Comments cm = new Comments();

                                JSONObject commentObject = jObject.getJSONObject(i);

                                int id= commentObject.getInt("id");
                                int my_id= commentObject.getInt("my_id");
                                String comment = commentObject.getString("comment");
                                String date_created = commentObject.getString("date_created");
                                String date_modified = commentObject.getString("date_modified");

                                String own= commentObject.getString("owner");
                                JSONObject owner = new JSONObject(own);

                                int user_id= owner.getInt("id");
                                String f_name= owner.getString("first_name");
                                String l_name= owner.getString("last_name");
                                String prof_pic_url= owner.getString("picture");
                                boolean is_social = owner.getBoolean("is_social");


                                progressBar.setVisibility(View.GONE);


                                cm.setId(id);
                                cm.setComment(comment);
                                cm.setUser_id(user_id);
                                cm.setPost(postid);
                                cm.setPhotoprof(prof_pic_url);
                                cm.setName(f_name +" "+l_name);
                                cm.setCreatedAt(date_created);
                                cm.setDate_modified(date_modified);
                                cm.setMyid(my_id);

                                commentslist.add(cm);
                                }

                            if(first) {
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
                params.put("post_id", postid);

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
