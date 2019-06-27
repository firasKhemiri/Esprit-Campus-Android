package com.esprit.firas.espritcampus.Tools.Events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.esprit.firas.espritcampus.Entities.Event;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.MapFragment;
import com.esprit.firas.espritcampus.Tools.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainSingleEventActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener,Serializable {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private View mFab;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private ParticipatedfollowersRecyclerViewAdapter adapter;

    ImageView img;

    String host;

    int id_event;

    ArrayList<User> userslist;

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    Switch particip;

    TextView num_fol;
    RelativeLayout par_lay;
    RelativeLayout par_layy;

    private CoordinatorLayout layyo;
    Event eventItem = new Event();
    Intent i;
    Services s;

    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        mFab = null;

        userslist = new ArrayList<>();

        s = new Services();

        mRecyclerView = findViewById(R.id.f_particip);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        i = getIntent();
        layyo = findViewById(R.id.event_coordinator);

        layyo.setVisibility(View.GONE);

        host = getString(R.string.aphost);

        progressBar = findViewById(R.id.progress_bar_event);


        num_fol = findViewById(R.id.num_fol);
        par_lay = findViewById(R.id.particip_lay);
        par_layy = findViewById(R.id.particip_layy);




        Toolbar toolbar = findViewById(R.id.flexible_example_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        AppBarLayout appbar = findViewById(R.id.flexible_example_appbar);
        appbar.addOnOffsetChangedListener(this);

        id_event = i.getIntExtra("id",0);
        GetEventData(id_event);

      //  getComments();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }

    public void GetEventData(final int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/event/"+id+"/";


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            layyo.setVisibility(View.VISIBLE);

                            JSONObject menuObject = new JSONObject(response);



                            eventItem.setId(id);
                            eventItem.setPost_name(menuObject.getString("name"));
                            eventItem.setPost_desc(menuObject.getString("description"));
                            eventItem.setPic_url(menuObject.getString("picture_url"));

                            eventItem.setLocation(menuObject.getString("location"));
                            eventItem.setNum_participants(menuObject.getInt("particip_count"));
                            eventItem.setDateBeg(menuObject.getString("date_beg"));
                            eventItem.setDateEnd(menuObject.getString("date_end"));
                            eventItem.setParticipated(menuObject.getBoolean("is_participated"));


                            String participantsJson = menuObject.getString("get_particip_follow");
                            ArrayList<User> participants = new ArrayList<>();

                            if (!participantsJson.equals("") && !participantsJson.equals("null")) {

                                JSONArray jPar = new JSONArray(participantsJson);
                                if ( jPar.length()>0) {
                                    for (int p = 0; p < jPar.length(); p++) {
                                        JSONObject pare = jPar.getJSONObject(p);

                                        int par_id = pare.getInt("id");
                                        String par_flname = pare.getString("first_name") + " " + pare.getString("last_name");
                                        String par_photo = pare.getString("picture");

                                        User userPar = new User();
                                        userPar.setId(par_id);
                                        userPar.setUser_flname(par_flname);
                                        userPar.setProfile_pic(par_photo);

                                        participants.add(userPar);
                                    }
                                }
                            }


                            int maxLimit=0;
                            boolean isLimited = menuObject.getBoolean("is_limited");
                            if (isLimited)
                                maxLimit = menuObject.getInt("max_limit");
                            boolean reachedLimit = menuObject.getBoolean("get_is_limit");

                            eventItem.setLimited(isLimited);
                            if (isLimited) {
                                eventItem.setMaxLimit(maxLimit);
                                eventItem.setReachedLimit(reachedLimit);
                            }



                            eventItem.setPhotoprof(i.getStringExtra("photoprof"));
                            eventItem.setUser_flname(i.getStringExtra("user_name"));

                            eventItem.setLat(menuObject.getDouble("lat"));
                            eventItem.setLng(menuObject.getDouble("lng"));


                            img = findViewById(R.id.image);

                            particip = findViewById(R.id.participate);
                            CircleImageView photoprof = findViewById(R.id.profimg);

                            TextView location = findViewById(R.id.location);
                            TextView date = findViewById(R.id.date);

                            ImageView imageView = findViewById(R.id.thumbnail);
                            TextView title = findViewById(R.id.event_name);
                            TextView descri = findViewById(R.id.descri);
                            TextView user_name = findViewById(R.id.created_by);

                            TextView participated = findViewById(R.id.participants);
                            CardView participants_card = findViewById(R.id.particip_card);


                            title.setText(eventItem.getPost_name());
                            descri.setText(eventItem.getPost_desc());
                            location.setText(eventItem.getLocation());
                            date.setText(s.dateEvn(eventItem.getDateBeg(),eventItem.getDateEnd()));



                            user_name.setText(eventItem.getUser_flname());

                            if (eventItem.isLimited())
                                participated.setText(eventItem.getNum_participants()+" Etudiant(s) (Limité à "+eventItem.getMaxLimit()+" participants).");
                            else
                                participated.setText(eventItem.getNum_participants()+" Etudiant(s).");

                            participants_card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(),MainParticipants.class)
                                            .putExtra("postid",String.valueOf(id));
                                    startActivity(i);
                                }
                            });

                            eventItem.setParticpants(participants);

                            if (eventItem.getParticpants().size()>0) {

                                ArrayList<User> users = new ArrayList<>();
                                int numberPar = eventItem.getParticpants().size();

                                if (eventItem.getParticpants().size()>3) {
                                    for (int u = 0; u < 3; u++) {
                                        users.add(eventItem.getParticpants().get(u));
                                        numberPar--;
                                    }

                                    num_fol.setVisibility(View.VISIBLE);
                                    par_lay.setVisibility(View.VISIBLE);
                                    num_fol.setText("+"+numberPar);


                                }
                                else {
                                    users = eventItem.getParticpants();
                                    num_fol.setVisibility(View.GONE);
                                }

                                adapter = new ParticipatedfollowersRecyclerViewAdapter(getApplicationContext(), users);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                mRecyclerView.setVisibility(View.GONE);
                                num_fol.setVisibility(View.GONE);
                                par_lay.setVisibility(View.GONE);
                                par_layy.setVisibility(View.GONE);
                            }

                            Glide.with(getApplicationContext())
                                    .load(host +"/"+ eventItem.getPic_url()).dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .error(R.drawable.placeholder)
                                    .placeholder(R.drawable.placeholder)
                                    .into(imageView);

                            imageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

                            Glide.with(getApplicationContext())
                                    .load(host  +"/"+ eventItem.getPhotoprof()).dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .fitCenter()
                                    .error(R.drawable.user_avatar)
                                    .placeholder(R.drawable.user_avatar)
                                    .into(photoprof);

                            if (eventItem.isParticipated())
                            {
                                particip.setChecked(true);
                            }
                            else {
                                if (eventItem.isLimited())
                                {
                                    if (eventItem.isReachedLimit())
                                    {
                                        particip.setEnabled(false);
                                    }
                                }
                                particip.setChecked(false);
                            }

                            particip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        new Services().Participate(String.valueOf(id), getApplicationContext());
                                        eventItem.setParticipated(true);
                                    }
                                    else
                                    {
                                        new Services().Unparticipate(String.valueOf(id), getApplicationContext());
                                        eventItem.setParticipated(false);
                                    }

                                }
                            });

                            if (eventItem.isLimited()) {
                                if (eventItem.isReachedLimit()) {
                                    particip.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            Toast.makeText(getApplicationContext(),"Limite d'utilisateurs atteint",Toast.LENGTH_LONG).show();
                                            return true;
                                        }
                                    });
                                }
                            }

                            progressBar.setVisibility(View.GONE);

                            Bundle postargs2 = new Bundle();
                            postargs2.putDouble("lat", eventItem.getLat());
                            postargs2.putDouble("lng", eventItem.getLng());
                            postargs2.putString("location", eventItem.getLocation());

                            ft = getSupportFragmentManager().beginTransaction();
                            Fragment mapFrag = new MapFragment();
                            mapFrag.setArguments(postargs2);

                            ft.add(R.id.map, mapFrag);
                            ft.commitAllowingStateLoss();
                        }
                        catch (JSONException e) {
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
                  /*      Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);*/
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
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
}

