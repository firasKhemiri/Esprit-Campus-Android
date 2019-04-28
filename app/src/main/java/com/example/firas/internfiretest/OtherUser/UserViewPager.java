package com.example.firas.internfiretest.OtherUser;

import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.firas.internfiretest.Accueil.AccueilActivity;
import com.example.firas.internfiretest.Messages.MainMessagesActivity;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Courses.CoursMain;
import com.example.firas.internfiretest.Tools.Feed.PostsMainNoSqLite;
import com.example.firas.internfiretest.Tools.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserViewPager extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {




    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    int user_id;

    TextView name;
    TextView bio;
    TextView followers;
    TextView following;
    CircleImageView photoprof;
    ImageView photocouv;
    ImageButton details;
    ImageButton chat;
    ImageButton more;

    Switch followUser;

    ImageButton edit;

    CoordinatorLayout layyo;

    String pic_url="";
    String fl_name="";
    String email="";
    String location="";
    String followingUser="";
    String userFollowing="";
    String birthday="";
    String classe_name ="";
    String branch_name ="";
    int classe_year =0;
    int classe_id =0;
    Boolean isProfessor= false;


    int convo_id;

    String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_pager_other);

        host = getString(R.string.aphost);

        name = findViewById(R.id.username);
        bio = findViewById(R.id.userbio);


        layyo = findViewById(R.id.layyo);

        layyo.setVisibility(View.INVISIBLE);


        final Intent intent = getIntent();
        user_id = intent.getIntExtra("userid", 0);


   /*     followers = (TextView) findViewById(R.id.followers);
        following = (TextView) findViewById(R.id.user_following);
*/
        photoprof = findViewById(R.id.photoprof);
        photocouv = findViewById(R.id.photocouv);

        followUser = findViewById(R.id.add_friend);

        details = findViewById(R.id.settings);
        chat = findViewById(R.id.chat);
        more = findViewById(R.id.more);

        GetUserData(user_id);

        TabLayout tabLayout = findViewById(R.id.matabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        AppBarLayout appbarLayout = findViewById(R.id.materialup_appbar);
        //    mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

        Toolbar toolbar = findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();



        TabsAdapter pagerAdapter =
                new TabsAdapter(getSupportFragmentManager());
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


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{"Signaler"};

                AlertDialog.Builder builder = new AlertDialog.Builder(UserViewPager.this);
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if (which == 2) {
                            // Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();


                            AlertDialog.Builder builder2 = new AlertDialog.Builder(UserViewPager.this);
                            builder2.setTitle("Signaler");

                            final EditText input = new EditText(UserViewPager.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            builder2.setView(input);

                            input.setHint("Cause de signale");

                            builder2.setPositiveButton("Signaler",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Toast.makeText(getApplicationContext(), "it's " + String.valueOf(input.getText()), Toast.LENGTH_LONG).show();
                                            String s = String.valueOf(input.getText());
                                            new Services().AddMessage(s, user_id, getApplicationContext());
                                        }
                                    });
                            //   Dialog firstDialog = builder.create();
                            builder2.show();

                        }


                    }
                });
                builder.show();
            }
        });




        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainMessagesActivity.class)
                        .putExtra("convo_id",convo_id)
                        .putExtra("cont_id", user_id)
                        .putExtra("my_id", AccueilActivity.my_id)
                        .putExtra("cont_img", pic_url)
                        .putExtra("cont_name", fl_name)
                        .putExtra("msg_id", 0)
                        .putExtra("seen", true)
                        .putExtra("receiver", user_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = UserViewPager.this.getSupportFragmentManager();
                DetailsUser userDialogFragment = new DetailsUser();

                Bundle args = new Bundle();


                args.putString("location",location);
                args.putString("following",userFollowing);
                args.putString("followers", followingUser);
                args.putString("classe", classe_name);
                args.putString("birthday", birthday);
                args.putBoolean("isProfessor", isProfessor);
                args.putInt("userId", user_id);
                //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                userDialogFragment.setArguments(args);

                userDialogFragment.show(fm, null);


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
        TabsAdapter(FragmentManager fm) {
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
                    postargs1.putString("url", "/api/user/"+user_id+"/posts/");
                    PostsMainNoSqLite postfragment1 = new PostsMainNoSqLite();
                    postfragment1.setArguments(postargs1);

                    return postfragment1;

                case 1:
                    Bundle postargs = new Bundle();
                    postargs.putString("url", "/api/user/"+user_id+"/cours/");
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

        @SuppressLint("InflateParams")
        View getTabView(int position) {
            View tab = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            ImageView tv = tab.findViewById(R.id.screen_shot);
            // TextView tv = (TextView)tab.findViewById(R.id.tabtext);
            tv.setImageBitmap(BitmapFactory.decodeResource(UserViewPager.this.getResources(),
                    tabTitles[position]));
            return tab;
        }
    }


















    public void GetUserData(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/user/"+id+"/";


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {

                            layyo.setVisibility(View.VISIBLE);

                            JSONObject menuObject = new JSONObject(response);

                            final int id = menuObject.getInt("id");
                            String username = menuObject.getString("username");
                            fl_name = menuObject.getString("first_name") +" "+ menuObject.getString("last_name");
                            email = menuObject.getString("email");
                            String prof_bio = menuObject.getString("bio");
                            location = menuObject.getString("location");
                            birthday = menuObject.getString("birthdate");
                            String gender = menuObject.getString("gender");
                            pic_url = menuObject.getString("picture");
                            String cover_pic = menuObject.getString("cover_picture");
                            isProfessor = menuObject.getBoolean("is_professor");

                            followingUser = menuObject.getString("followers_count");
                            userFollowing = menuObject.getString("following_count");

                            boolean ifollowuser = menuObject.getBoolean("ifollow");

                            String classe = menuObject.getString("classe");

                            if (!classe.equals("") && !classe.equals("null")) {
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
                            String convo = menuObject.getString("convo_id");

                            convo_id = menuObject.getInt("convo_id");

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


                         /*   followers.setText(followingUser + " abonnés");
                            following.setText(userFollowing + " Abonnements");


                                if(Integer.valueOf(followingUser)>0)
                                    followers.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(getApplicationContext(), SearchFollowers.class)
                                                    .putExtra("url", "api/user/"+user_id+"/followers");
                                            startActivity(intent);
                                        }
                                    });


                                if(Integer.valueOf(userFollowing)>0)
                                    following.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), SearchFollowers.class)
                                                    .putExtra("url", "api/user/"+user_id+"/following");
                                            startActivity(intent);
                                        }
                                    });
*/

                            if (ifollowuser) {
                                followUser.setChecked(true);
                            } else
                                followUser.setChecked(false);

                            followUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked)
                                        new Services().FollowUser(id, getApplicationContext());
                                    else
                                        new Services().UnfollowUser(id, getApplicationContext());

                                }
                            });
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