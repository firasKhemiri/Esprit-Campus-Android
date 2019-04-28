package com.example.firas.internfiretest.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Courses.CoursMain;
import com.example.firas.internfiretest.Tools.Feed.PostsMainNoSqLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewPager extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    TextView name;
    TextView bio;
    TextView followers;
    TextView following;
    CircleImageView photoprof;
    ImageView photocouv;
    ImageButton settings;

    ImageButton edit;

    String host;

    CoordinatorLayout layyo;

    String classe_name ="";
    String branch_name ="";
    int classe_year =0;
    int classe_id =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        host = getString(R.string.aphost);

        name = findViewById(R.id.username);
        bio = findViewById(R.id.bio);


      /*  followers = findViewById(R.id.followers);
        following = findViewById(R.id.user_following);*/

        photoprof = findViewById(R.id.photoprof);
        photocouv = findViewById(R.id.photocouv);

        layyo = findViewById(R.id.layyo);

        layyo.setVisibility(View.INVISIBLE);

        //  settings = (ImageButton) findViewById(R.id.settings);

        GetUserData();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager  = findViewById(R.id.viewpager);
        AppBarLayout appbarLayout = findViewById(R.id.appbar);
        //    mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

        Toolbar toolbar = findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        ProfileViewPager.TabsAdapter pagerAdapter =
                new ProfileViewPager.TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(2);

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        photocouv.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colortint), PorterDuff.Mode.DARKEN);


        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            photoprof.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            photoprof.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }
        int tabTitles[] = new int[]{R.drawable.tab_news_feed_480, R.drawable.tab_study_480, R.drawable.tab_calendar_480};

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:

                    Bundle postargs1 = new Bundle();
                    postargs1.putString("url", "/api/profile_posts/");
                    PostsMainNoSqLite postfragment1 = new PostsMainNoSqLite();
                    postfragment1.setArguments(postargs1);

                    return postfragment1;

                case 1:
                    Bundle postargs = new Bundle();
                    postargs.putString("url", "/api/profile/cours/");
                    CoursMain postfragment = new CoursMain();
                    postfragment.setArguments(postargs);

                    return postfragment;

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return String.valueOf(tabTitles[position]);
        }

        View getTabView(int position) {
            View tab = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            ImageView tv = (ImageView) tab.findViewById(R.id.screen_shot);
            // TextView tv = (TextView)tab.findViewById(R.id.tabtext);
            tv.setImageBitmap(BitmapFactory.decodeResource(ProfileViewPager.this.getResources(),
                    tabTitles[position]));
            return tab;
        }
    }



















    public void GetUserData()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/profile/";

        String user_id;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            layyo.setVisibility(View.VISIBLE);

                            JSONArray res = new JSONArray(response);
                            for (int i = 0; i < res.length(); i++) {

                                JSONObject menuObject = res.getJSONObject(i);

                                final int id = menuObject.getInt("id");
                                final String username = menuObject.getString("username");
                                final String first_name = menuObject.getString("first_name");
                                final String last_name = menuObject.getString("last_name");
                                final String fl_name = first_name +" "+ last_name;
                                final String email = menuObject.getString("email");
                                final String prof_bio = menuObject.getString("bio");
                                final String location = menuObject.getString("location");
                                final String birthday = menuObject.getString("birthdate");
                                String gender = menuObject.getString("gender");
                                final String pic_url = menuObject.getString("picture");
                                final String cover_pic = menuObject.getString("cover_picture");
                                final String phone = menuObject.getString("phone");


                                String followingUser = menuObject.getString("followers_count");
                                String userFollowing = menuObject.getString("following_count");

                                String classe = menuObject.getString("classe");

                                if (classe!="" && classe!="null" && classe!=null ) {
                                    JSONObject cur = new JSONObject(classe);
                                    classe_name = cur.getString("name");
                                    classe_id = cur.getInt("id");
                                    classe_year = cur.getInt("year");

                                    String branch = cur.getString("branch");

                                    if (!branch.equals("") && !branch.equals("null")) {
                                        JSONObject bch = new JSONObject(branch);
                                        branch_name = bch.getString("name");
                                    }
                                }


                                photoprof.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(host + "/" + pic_url).dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .centerCrop()
                                        .error(R.drawable.user_avatar)
                                        .placeholder(R.drawable.user_avatar)
                                        .into(photoprof);


                                photocouv.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(host + "/" + cover_pic)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .centerCrop()
                                        .error(R.drawable.london_flat)
                                        .placeholder(R.drawable.london_flat)
                                        .into(photocouv);

                                name.setText(fl_name);
                                bio.setText(prof_bio);





                                //TODO
                     /*           followers.setText(followingUser+" abonnés");
                                following.setText(userFollowing+" Abonnements");

                                if(Integer.valueOf(followingUser)>0)
                                    followers.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(getApplicationContext(), SearchFollowers.class)
                                                    .putExtra("url", "api/user/"+id+"/followers");
                                            startActivity(intent);
                                        }
                                    });


                                if(Integer.valueOf(userFollowing)>0)
                                    following.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), SearchFollowers.class)
                                                    .putExtra("url", "api/user/"+id+"/following");
                                            startActivity(intent);
                                        }
                                    });

                                    */



                                ImageButton spin = findViewById(R.id.settings);

                                spin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CharSequence options[] = new CharSequence[]{"Details","Modifier"};

                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewPager.this);
                                        builder.setTitle("Options");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // the user clicked on colors[which]

                                                if (which == 0) {

                                                    Intent i = new Intent(getApplicationContext(),UpdateProfile.class);
                                                    i.putExtra("username",username);
                                                    i.putExtra("first_name",first_name);
                                                    i.putExtra("last_name",last_name);
                                                    i.putExtra("email",email);
                                                    i.putExtra("bio",prof_bio);
                                                    i.putExtra("photoprof",pic_url);
                                                    i.putExtra("photocouv",cover_pic);
                                                    i.putExtra("birthday",birthday);
                                                    i.putExtra("phone",phone);

                                                    startActivity(i);

                                                } else if (which == 1) {

                                                    Intent i = new Intent(getApplicationContext(),UpdateProfile.class);
                                                    i.putExtra("username",username);
                                                    i.putExtra("first_name",first_name);
                                                    i.putExtra("last_name",last_name);
                                                    i.putExtra("email",email);
                                                    i.putExtra("bio",prof_bio);
                                                    i.putExtra("photoprof",pic_url);
                                                    i.putExtra("photocouv",cover_pic);
                                                    i.putExtra("birthday",birthday);
                                                    i.putExtra("phone",phone);
                                                    i.putExtra("adr",location);

                                                    startActivity(i);

                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }

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
                     /*   Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
