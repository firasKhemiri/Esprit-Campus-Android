package com.esprit.firas.espritcampus.School;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.School.JoinRequest.JoinRequest;
import com.esprit.firas.espritcampus.Tools.Feed.PostsMainNoSqLite;
import com.esprit.firas.espritcampus.Tools.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SchoolMainPager extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    TextView fac_name;
    TextView bio;
    CircleImageView photoprof;
    ImageView photocouv;

    String host;

    int school_id ;

    ImageButton categories;
    ArrayList<String> items;
    ArrayList<Integer> items_id;

    ViewPager viewPager;

    Button join;
    Button delete_req;
    AppBarLayout appbarLayout;

    String url_students;
    String url_school;

    String url;
    ImageButton docs;

    ImageButton more;

    ImageButton details;

    TabLayout tabLayout;

    PagerAdapter pagerAdapter;

    int students_count;
    int profs_count;
    int deps_count;

    String address;

    public static ArrayList<Category> SCHOOl_CATS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_pager);

        Intent intent = getIntent();
        school_id = intent.getIntExtra("school_id",0);

        details = (ImageButton) findViewById(R.id.settings);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.matabs);
        pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), SchoolMainPager.this);
        viewPager.setAdapter(pagerAdapter);

        host = getString(R.string.aphost);

        url_students = "/api/school/" + school_id + "/feed/";
        url_school = "/api/school/" + school_id + "/posts/";

        fac_name = (TextView) findViewById(R.id.username);
        bio = (TextView) findViewById(R.id.userbio);


        items = new ArrayList<>();
        items_id = new ArrayList<>();

        items.add("all categories");
        items_id.add(0);

        photoprof = (CircleImageView) findViewById(R.id.photoprof);
        photocouv = (ImageView) findViewById(R.id.photocouv);

        join = (Button) findViewById(R.id.join);
        delete_req = (Button) findViewById(R.id.delete_request);

        more = findViewById(R.id.more);

        categories = findViewById(R.id.categories);



        join.setVisibility(View.INVISIBLE);
        delete_req.setVisibility(View.INVISIBLE);

        photocouv.setVisibility(View.INVISIBLE);
        photoprof.setVisibility(View.INVISIBLE);
        bio.setVisibility(View.INVISIBLE);
        fac_name.setVisibility(View.INVISIBLE);



        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = SchoolMainPager.this.getSupportFragmentManager();
                DetailsSchool userDialogFragment = new DetailsSchool();

                Bundle args = new Bundle();


                args.putInt("students_count",students_count);
                args.putInt("profs_count",profs_count);
                args.putInt("deps_count", deps_count);
                args.putString("location", address);
                args.putInt("school_id", school_id);

                //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                userDialogFragment.setArguments(args);

                userDialogFragment.show(fm, null);


            }
        });



/*        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DocumentsViewPager.class);
                i.putExtra("school_id",school_id);
                startActivity(i);
            }
        });*/



        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{"Emplois du temps","Relévés des notes","Autres"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SchoolMainPager.this);
                builder.setTitle("Autres documents");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if (which == 0) {



                        } else if (which == 1) {


                        }
                         else if (which == 2) {


                        }
                    }
                });
                builder.show();
            }
        });


        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


/*
        dropdown.setVisibility(View.INVISIBLE);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = dropdown.getSelectedItem().toString();
                Toast.makeText(view.getContext(), text + " " + items_id.get(dropdown.getSelectedItemPosition()), Toast.LENGTH_LONG).show();

                if (items_id.get(dropdown.getSelectedItemPosition()) != 0) {
                    url_school = "/api/school/" + school_id + "/category/" + items_id.get(dropdown.getSelectedItemPosition()) + "/posts/";
                    url_students = "/api/school/" + school_id + "/category/" + items_id.get(dropdown.getSelectedItemPosition()) + "/feed/";
                } else {
                    url_students = "/api/school/" + school_id + "/feed/";
                    url_school = "/api/school/" + school_id + "/posts/";
                }
                viewPager.getAdapter().notifyDataSetChanged();

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    assert tab != null;
                    tab.setCustomView(pagerAdapter.getTabView(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        GetSchoolData(school_id);

        appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        //    mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();



        viewPager.setOffscreenPageLimit(3);


        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
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



    class PagerAdapter extends FragmentPagerAdapter {

        int tabTitles[] = new int[]{R.drawable.tab_news_feed_480, R.drawable.tab_study_480};

        Context context;

        PagerAdapter(FragmentManager fm, SchoolMainPager context) {
            super(fm);
            this.context = getApplicationContext();
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:

                    Bundle postargs = new Bundle();
                    postargs.putString("url", url_school);
                    PostsMainNoSqLite postfragment = new PostsMainNoSqLite();
                    postfragment.setArguments(postargs);

                    return postfragment;

                case 1:

                    Bundle studentpostargs = new Bundle();
                    studentpostargs.putString("url", url_students);
                    PostsMainNoSqLite studentPostsfragment = new PostsMainNoSqLite();
                    studentPostsfragment.setArguments(studentpostargs);

                    return studentPostsfragment;

                case 2:

                    Bundle postargs2 = new Bundle();
                    postargs2.putString("url", url_school);
                    PostsMainNoSqLite postfragment2 = new PostsMainNoSqLite();
                    postfragment2.setArguments(postargs2);

                    return postfragment2;

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
            tv.setImageBitmap(BitmapFactory.decodeResource(SchoolMainPager.this.getResources(),
                    tabTitles[position]));
            return tab;
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

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);
                  /*      num_students.setVisibility(View.VISIBLE);
                        stud_but.setVisibility(View.VISIBLE);*/
                        photocouv.setVisibility(View.VISIBLE);
                        photoprof.setVisibility(View.VISIBLE);
                        bio.setVisibility(View.VISIBLE);
                        fac_name.setVisibility(View.VISIBLE);

                        try {
                            JSONObject res = new JSONObject(response);


                         //   school_id = res.getInt("id");
                            String name = res.getString("name");
                            String description = res.getString("description");
                            int owner_id = res.getInt("owner_id");
                            String owner_name = res.getString("owner");

                            students_count = res.getInt("students_only_count");
                            profs_count = res.getInt("profs_count");
                            deps_count = res.getInt("departments_count");
                            address = res.getString("adr");

                            String pic_url = res.getString("pic_url");
                            String cover_pic = res.getString("cover_pic");

                            boolean ibelong = res.getBoolean("ibelong");
                            boolean user_has_school = res.getBoolean("user_has_school");
                            boolean pending = res.getBoolean("pending");



                            //      num_students.setText(String.valueOf(students_count));

                            photoprof.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext())
                                    .load(host + "/"  + pic_url).dontAnimate()
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

                            if(!user_has_school) {
                                docs.setVisibility(View.INVISIBLE);
                                if(pending){
                                    delete_req.setVisibility(View.VISIBLE);
                                }
                                else
                                    join.setVisibility(View.VISIBLE);



                            }

                            else {
                                join.setVisibility(View.GONE);
                                delete_req.setVisibility(View.GONE);
                            }


                            join.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), JoinRequest.class);
                                    i.putExtra("school_id", SchoolMainPager.this.school_id);
                                    startActivity(i);
                                }
                            });

                            delete_req.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Services s = new Services();
                                    s.Deletepending(SchoolMainPager.this.school_id,getApplicationContext());

                                    delete_req.setVisibility(View.GONE);
                                    join.setVisibility(View.VISIBLE);
                                }
                            });



                            if(ibelong) {
                          //      dep.setVisibility(View.VISIBLE);
//                                docs.setVisibility(View.VISIBLE);
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






}