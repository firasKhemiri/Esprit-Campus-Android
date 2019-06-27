package com.esprit.firas.espritcampus.School;/*package com.esprit.firas.internproject.School;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.internproject.LoginActivity;
import com.esprit.firas.internproject.R;
import com.esprit.firas.internproject.Tools.Feed.PostsMainNoSqLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SchoolViewPager extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    TextView fac_name;
    TextView bio;
    TextView followers;
    TextView following;
    CircleImageView photoprof;
    ImageView photocouv;
    ImageButton settings;

    ImageButton edit;

    String host;
    AppBarLayout appbarLayout;

    Button join;

    ImageButton dep_but;
    int id = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_pager);

        host = getString(R.string.aphost);

        fac_name = (TextView) findViewById(R.id.username);
        bio = (TextView) findViewById(R.id.bio);


      //  followers = (TextView) findViewById(R.id.followers);
      //  following = (TextView) findViewById(R.id.prof_following);


        photoprof = (CircleImageView) findViewById(R.id.photoprof);
        photocouv = (ImageView) findViewById(R.id.photocouv);

        dep_but = (ImageButton) findViewById(R.id.dep_but);


        join = (Button) findViewById(R.id.join);


        GetSchoolData(id);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        appbarLayout = (AppBarLayout) findViewById(R.id.appbar);
    //    mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:

                    Bundle postargs = new Bundle();
                    postargs.putString("url", "/api/school/"+ id +"/feed/");
                    PostsMainNoSqLite postfragment = new PostsMainNoSqLite();
                    postfragment.setArguments(postargs);

                    return postfragment;

                case 1:

                    Bundle studentsargs = new Bundle();
                    studentsargs.putString("url", "/api/school/"+ id +"/users//");
                    PostsMainNoSqLite studentfragment = new PostsMainNoSqLite();
                    studentfragment.setArguments(studentsargs);

                    return studentfragment;

            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Posts";
                case 1: return "students";
            }
            return "";
        }
    }





    public void GetSchoolData(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/school/"+id+"/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {
                            JSONArray res = new JSONArray(response);
                            for (int i = 0; i < res.length(); i++) {

                                JSONObject menuObject = res.getJSONObject(i);

                                int id = menuObject.getInt("id");
                                String name = menuObject.getString("name");
                                String description = menuObject.getString("description");
                                int owner_id = menuObject.getInt("owner_id");
                                String owner_name = menuObject.getString("owner_name");

                                String pic_url = menuObject.getString("pic_url");
                                String cover_pic = menuObject.getString("cover_pic");

                                boolean ibelong = menuObject.getBoolean("ibelong");

                                boolean user_has = menuObject.getBoolean("user_has_school");

                                if(ibelong)
                                    dep_but.setVisibility(View.VISIBLE);

                                if(user_has)
                                    join.setVisibility(View.INVISIBLE);

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

                                fac_name.setText(name);
                                bio.setText(description);

                                appbarLayout.setVisibility(View.VISIBLE);
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
}*/