package com.esprit.firas.espritcampus.Messages;

import android.annotation.SuppressLint;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.DbHelpers.LocalFeed.FeedHandler;
import com.esprit.firas.espritcampus.Entities.Message;
import com.esprit.firas.espritcampus.OtherUser.UserViewPager;
import com.esprit.firas.espritcampus.R;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.esprit.firas.espritcampus.Tools.Notifications.Config.list_notif_mess;
import static com.esprit.firas.espritcampus.Tools.Notifications.Config.messages_num;


public class MainMessagesActivity extends AppCompatActivity {



    private List<Message> msgList;
    private RecyclerView mRecyclerView;
    private MessageRecyclerViewAdapter adapter;


    String host;

    int cont_id;
    String cont_name;
    String cont_img;
    int my_id;
    int convo_id;

    int count;


    private int previousTotal = 0;
    private boolean loading = true;
    int  visibleItemCount, totalItemCount,pastVisiblesItems,rec_id;

    private boolean first = true;

    String url = "/api/conversation/";

    int offset = 0;

    EditText typeArea;

    ProgressBar progressBar;

    LinearLayoutManager mLayoutManager;


    private FeedHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_activity);

        db = new FeedHandler(getApplicationContext());


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        msgList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);

        progressBar = findViewById(R.id.progress_bar);
        ImageView send = findViewById(R.id.send);

        typeArea= findViewById(R.id.type_msg);
        TextView con_name = findViewById(R.id.con_name);
        CircleImageView con_image = findViewById(R.id.con_image);

        host = getString(R.string.aphost);

        Intent i = getIntent();
        cont_id = i.getIntExtra("cont_id",0);
        cont_name = i.getStringExtra("cont_name");
        cont_img = i.getStringExtra("cont_img");
        my_id = i.getIntExtra("my_id",0);
        rec_id = i.getIntExtra("receiver",0);

     //   last_msg_id = i.getIntExtra("msg_id",0);
        boolean seen = i.getBooleanExtra("seen", true);

        convo_id = i.getIntExtra("convo_id",0);


        con_name.setText(cont_name);
        Glide.with(getApplication())
                .load(host + "/" + cont_img).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(con_image);


        con_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserViewPager.class)
                        .putExtra("userid", cont_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        con_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserViewPager.class)
                        .putExtra("userid", cont_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new MessageRecyclerViewAdapter(getApplicationContext(),msgList);
        mRecyclerView.setAdapter(adapter);

        if (convo_id !=0) {
            GetLocalMessages();
            GetMessages(host + url + convo_id + "/");
        }
        else
            progressBar.setVisibility(View.GONE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(typeArea.getText());
                AddMessage(text);
                typeArea.setText("");
            }
        });


       /* if(!seen && (my_id == rec_id))
            SawMessage(last_msg_id);
*/


        if (convo_id !=0)
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) //check for scroll down
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
                    if (!loading && ((pastVisiblesItems) <=  1)) {

                        if (count - offset >= 30)
                        {
                            offset = offset + 30;
                            Log.i("Yaeye!", "end called");
                            GetMessages(host + url + convo_id + "/?limit=30&offset=" + offset);

                            loading = true;

                         //   Toast.makeText(getApplicationContext(), "sma " + count + "off " + offset, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            offset = offset + 30;
                            int limit = count - offset;
                            Log.i("Yaeye!", "end called");
                            GetMessages(host + url +convo_id + "/?limit="+limit+"&offset=" + offset);

                            loading = true;
                        //    Toast.makeText(getApplicationContext(),"big "+ count + "off "+offset,Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });


    }


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLoadTodays, new IntentFilter("update-message"));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiverLoadTodays = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), intent.getExtras().getString("message"), Toast.LENGTH_SHORT).show();


            int sender = intent.getIntExtra("sender",0);

            if (sender == cont_id) {

                Message msgg = new Message();
                // checking for type intent filter

                String body = intent.getStringExtra("body");

                String message = intent.getStringExtra("message");
                String title = intent.getStringExtra("title");
                String image = intent.getStringExtra("image");
                String time = intent.getStringExtra("timestamp");
              //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss",Locale.FRANCE);
              //  String time = sdf.format(new Date());
                //       Toast.makeText(getApplicationContext(),date,Toast.LENGTH_LONG).show();

                int mess_id = intent.getIntExtra("mess_id", 0);

                if (convo_id == 0)
                {
                    convo_id = intent.getIntExtra("convo_id",0);
                }

               // Toast.makeText(getApplicationContext(), "Push notification: " + convo_id, Toast.LENGTH_LONG).show();


                Toast.makeText(getApplicationContext(),"time: "+time,Toast.LENGTH_LONG).show();

                msgg.setId(mess_id);
                msgg.setSender(sender);
                msgg.setReciever(my_id);
                msgg.setMy_id(my_id);
                msgg.setSeen(true);
                msgg.setCreatedAt(time);
                msgg.setMessage(message);

                msgg.setConImg(cont_img);

                msgList.add(msgg);

                count++;

                adapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(msgList.size() - 1);

                SawMessage(mess_id);

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverLoadTodays);

    }


    public void GetLocalMessages() {


        ArrayList<Message> msgList;
        msgList = db.SelectMessages(convo_id);

//        Toast.makeText(getApplicationContext(), "size" + msgList.size(), Toast.LENGTH_LONG).show();

        if (msgList.size()>0) {
            adapter = new MessageRecyclerViewAdapter(MainMessagesActivity.this, msgList);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.scrollToPosition(msgList.size()-1);

            progressBar.setVisibility(View.GONE);
        }
    }


    public void GetMessages(String url)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(MainMessagesActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    String msg_id;
                    String message;
                    String sender;
                    String reciever;
                    String date;
                    boolean seen;

                    String result;

                    ArrayList<Message> seclist = new ArrayList<>();

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response convo Num", response);

                        try {
                            JSONObject cur = new JSONObject(response);


                            count = cur.getInt("count");
                            result = cur.getString("results");
                            progressBar.setVisibility(View.GONE);
                            JSONArray jObject = new JSONArray(result);

                            for (int i = 0; i < jObject.length(); i++) {

                                JSONObject obj2 = jObject.getJSONObject(i);
                                Message msg = new Message();

                                msg_id = obj2.getString("id");
                                message = obj2.getString("content");
                                sender = obj2.getString("sender");
                                reciever = obj2.getString("reciever");
                                date = obj2.getString("date_created");
                                seen = obj2.getBoolean("seen");

                                msg.setId(Integer.valueOf(msg_id));
                                msg.setSender(Integer.valueOf(sender));
                                msg.setReciever(Integer.valueOf(reciever));
                                msg.setMy_id(my_id);
                                msg.setSeen(seen);
                                msg.setCreatedAt(date);
                                msg.setMessage(StringEscapeUtils.unescapeJava(message));
                                msg.setConvo_id(convo_id);

                                msg.setConImg(cont_img);

                                if(!seen && (my_id == rec_id))
                                    SawMessage(msg.getId());

                                seclist.add(msg);


                             //   Toast.makeText(getApplicationContext(),"my id " + my_id+ "rec " +reciever + "sen "+sender,Toast.LENGTH_LONG).show();
                            }

                            int sz = jObject.length();
                            Collections.reverse(seclist);
                            seclist.addAll(msgList);
                            msgList.clear();
                            msgList.addAll(seclist);

                            if (first) {


                                db.deleteMessages();
                                mRecyclerView.scrollToPosition(msgList.size() - 1);

                                adapter = new MessageRecyclerViewAdapter(MainMessagesActivity.this, msgList);
                                mRecyclerView.setAdapter(adapter);

                                first = false;


                                for (int i = 0 ; i <msgList.size();i++)
                                    db.AddMessage(msgList.get(i));
                            }

                            else
                            {
                                adapter.notifyDataSetChanged();
                                mLayoutManager.scrollToPositionWithOffset(sz, 30);
                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMessagesActivity.this,"error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(MainMessagesActivity.this,"network error", Toast.LENGTH_LONG).show();
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




    @SuppressLint("SimpleDateFormat")
    public void AddMessage(final String message)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(MainMessagesActivity.this);
        String url = host+"/api/message/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                                String mess = StringEscapeUtils.escapeJava(message);
                                Message msg = new Message();
                                msg.setSender(my_id);
                                msg.setReciever(cont_id);
                                msg.setMy_id(my_id);
                                msg.setMessage(message);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                String date = sdf.format(new Date());
                                msg.setCreatedAt(date);

                                msgList.add(msg);
                                count++;

                            adapter.notifyItemChanged(msgList.size());
                            mRecyclerView.scrollToPosition(msgList.size() - 1);

                            if (convo_id == 0)
                            {
                                try {
                                    JSONObject cur = new JSONObject(response);
                                    convo_id = cur.getInt("convo_id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(MainMessagesActivity.this,"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<>();
                params.put("reciever",String.valueOf(cont_id));
                params.put("content",StringEscapeUtils.escapeJava(message));
                return params;
            }
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




    public void SawMessage(int id_ms)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token=pref.getString("token", null);


        RequestQueue queue = Volley.newRequestQueue(MainMessagesActivity.this);
        String url = host+"/api/seen/"+id_ms+"/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        list_notif_mess.remove(Integer.valueOf(cont_id));
                        messages_num--;
                        Log.d("Response saw message", list_notif_mess+" "+ messages_num);


                    //    Toast.makeText(getApplicationContext(),ls,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(MainMessagesActivity.this,"network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("seen","True");
                return params;
            }
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


