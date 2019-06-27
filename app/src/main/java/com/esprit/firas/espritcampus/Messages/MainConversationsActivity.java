package com.esprit.firas.espritcampus.Messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.DbHelpers.LocalFeed.FeedHandler;
import com.esprit.firas.espritcampus.Entities.Conversation;
import com.esprit.firas.espritcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainConversationsActivity extends AppCompatActivity {




    private List<Conversation> convoList;
    private RecyclerView mRecyclerView;
    private ConversationsRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    private boolean first;

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;

    String url = "/api/conversations/";

    int offset = 0;

    String host;
    LinearLayoutManager mLayoutManager;

    private FeedHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_activity);

        convoList = new ArrayList<>();

        db = new FeedHandler(getApplicationContext());

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBar = findViewById(R.id.progress_bar);

        host = getString(R.string.aphost);

        /*String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
        new DownloadTask().execute(url);*/

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);


        first = true;

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
                        GetConversations(host + url + "?limit=5&offset=" + offset);

                        Toast.makeText(getApplicationContext(), " " + offset, Toast.LENGTH_LONG).show();

                        loading = true;
                    }

                }
            }
        });


        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLoadTodays, new IntentFilter("update-message"));

        convoList = new ArrayList<>();

        loading = true;
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        previousTotal = 0;

        offset = 0;

        adapter = new ConversationsRecyclerViewAdapter(getApplicationContext(),convoList);
        mRecyclerView.setAdapter(adapter);

        GetLocalConversations();

        GetConversations(host + url);

    }



    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverLoadTodays);

    }

    private BroadcastReceiver broadcastReceiverLoadTodays = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            convoList = new ArrayList<>();

            loading = true;
            mLayoutManager = new LinearLayoutManager(getApplicationContext());

            mRecyclerView.setLayoutManager(mLayoutManager);

            previousTotal = 0;


            offset = 0;

            adapter = new ConversationsRecyclerViewAdapter(getApplicationContext(),convoList);
            mRecyclerView.setAdapter(adapter);


            GetConversations(host + url);


        }
    };

    public void GetLocalConversations() {

        ArrayList<Conversation> convoList;
        convoList = db.SelectConversations();



        adapter = new ConversationsRecyclerViewAdapter(getApplicationContext(), convoList);
        mRecyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

    }


    public void GetConversations(String url)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    int convo_id;
                    String message;
                    String cont_name;
                    String cont_img;
                    boolean cont_social;
                    int myid;
                    int cont_id;
                    int msg_id;
                    String msg;
                    int sender;
                    int reciever;
                    boolean seen;

                    String result;
                    String users;

                    String date_msg;

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            if (first)
                            {
                                convoList = new ArrayList<>();
                            }

                            JSONObject cur = new JSONObject(response);
                            result = cur.getString("results");
                            progressBar.setVisibility(View.GONE);
                            JSONArray jObject = new JSONArray(result);
                            for (int i = 0; i < jObject.length(); i++) {

                                JSONObject c = jObject.getJSONObject(i);
                                Conversation convo = new Conversation();

                                convo_id = c.getInt("id");
                                users = c.getString("users");
                                myid = c.getInt("my_id");

                                JSONArray userObject = new JSONArray(users);
                                for (int j = 0; j < userObject.length(); j++) {

                                    JSONObject u = userObject.getJSONObject(j);
                                    if (!Objects.equals(myid, u.getInt("id"))) {
                                        cont_id = u.getInt("id");
                                        cont_name = u.getString("first_name")+" "+u.getString("last_name");
                                        cont_img = u.getString("picture");
                                        cont_social = u.getBoolean("is_social");
                                        break;
                                    }
                                }

                                message = c.getString("message");

                                JSONObject m = new JSONObject(message);

                                msg_id = m.getInt("id");
                                sender = m.getInt("sender");
                                reciever = m.getInt("reciever");
                                msg = m.getString("content");
                                seen = m.getBoolean("seen");
                                date_msg = m.getString("date_created");

                                convo.setCont_name(cont_name);
                                convo.setCont_image(cont_img);
                                convo.setCont_id(cont_id);
                                convo.setCont_social(cont_social);
                                convo.setMy_id(myid);
                                convo.setConversation_id(convo_id);
                                convo.setSender(sender);
                                convo.setReciever(reciever);
                                convo.setMsg_id(msg_id);
                                convo.setSeen(seen);
                                convo.setMessage(msg);
                                convo.setMy_id(myid);
                                convo.setDateStr(date_msg);

                                progressBar.setVisibility(View.GONE);
                                convoList.add(convo);
                            }

                            if(first) {
                                // adapter = new MyRecyclerViewAdapter(getApplicationContext(), allposts);
                                adapter = new ConversationsRecyclerViewAdapter(getApplicationContext(), convoList);
                                mRecyclerView.setAdapter(adapter);
                                first = false;

                                db.deleteConversations();

                              //  Toast.makeText(getApplicationContext(),"delete",Toast.LENGTH_LONG).show();

                                for (int i = 0 ; i <convoList.size();i++)
                                    db.AddConversation(convoList.get(i));
                            }

                            else
                                adapter.notifyDataSetChanged();
                                adapter = new ConversationsRecyclerViewAdapter(getApplicationContext(), convoList);
                                mRecyclerView.setAdapter(adapter);

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(getApplicationContext(),"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

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
