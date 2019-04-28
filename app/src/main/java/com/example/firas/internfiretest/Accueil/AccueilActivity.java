package com.example.firas.internfiretest.Accueil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.firas.internfiretest.Categories.CategoriesViewPager;
import com.example.firas.internfiretest.DbHelpers.LocalFeed.FeedHandler;
import com.example.firas.internfiretest.Entities.User;
import com.example.firas.internfiretest.LoginActivity;
import com.example.firas.internfiretest.Messages.MainConversationsActivity;
import com.example.firas.internfiretest.Profile.ProfileViewPager;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Search.SearchViewPager;
import com.example.firas.internfiretest.Tools.AddData.AddCours;
import com.example.firas.internfiretest.Tools.AddData.UpdateEventActual;
import com.example.firas.internfiretest.Tools.AddData.AddStuffMain;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.firas.internfiretest.Tools.Notifications.Config.list_notif_mess;
import static com.example.firas.internfiretest.Tools.Notifications.Config.messages_num;

public class AccueilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView img ;
    TextView nav_name;
    TextView nav_school;
    int classe_id;
    String host;

    User user;

    boolean is_prof;
    boolean is_admin;
    NavigationView navigationView;

    ImageView photocouv,mess_notif;
    TextView mess_count;

    public static int my_id;

    FeedHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //   toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getApplicationContext(),R.color.cardview_light_background));


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);

        nav_name = headerview.findViewById(R.id.nav_name);
        nav_school = headerview.findViewById(R.id.nav_school);
        img = headerview.findViewById(R.id.nav_img);
        photocouv = headerview.findViewById(R.id.photocouv);


        user = new User();

        db = new FeedHandler(getApplicationContext());

        host = getString(R.string.aphost);

      //  messages_num = 0;

        list_notif_mess = new ArrayList<>();


        ImageButton add = findViewById(R.id.add_pub);

        ImageButton messages = findViewById(R.id.messages);

        mess_notif = findViewById(R.id.mess_notif);

        mess_count = findViewById(R.id.mess_count);



    //    mess_count.setVisibility(View.INVISIBLE);
    //    mess_notif.setVisibility(View.INVISIBLE);
        if (messages_num > 0)
        {
            mess_count.setText(String.valueOf(messages_num));
            mess_count.setVisibility(View.VISIBLE);
            mess_notif.setVisibility(View.VISIBLE);
        }

        if (messages_num <= 0) {
            mess_count.setText(String.valueOf(messages_num));
            mess_count.setVisibility(View.INVISIBLE);
            mess_notif.setVisibility(View.INVISIBLE);
        }



        ImageButton searchBut = findViewById(R.id.search_icon);

        TextView search = findViewById(R.id.search);
        RelativeLayout search_lay = findViewById(R.id.search_lay);


        GetUserLocal();
        GetUserData();




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence types[] = new CharSequence[] {"publication", "evenement", "document"};

                AlertDialog.Builder builder = new AlertDialog.Builder(AccueilActivity.this);
                builder.setTitle("Choisir type:");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on types[which]

                        if (which ==0 )
                        {
                            Intent i = new Intent(getApplicationContext(), AddStuffMain.class);
                            i.putExtra("pos",0);
                            startActivity(i);
                        }

                        if (which ==1 )
                        {
                            Intent i = new Intent(getApplicationContext(), AddStuffMain.class);
                            i.putExtra("pos",1);
                            startActivity(i);
                        }

                        if (which ==2 )
                        {
                            Intent i = new Intent(getApplicationContext(), AddCours.class);
                            startActivity(i);
                        }

                    }
                });
                builder.show();


            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainConversationsActivity.class);
                startActivity(i);
            }
        });

        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchViewPager.class);
                startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchViewPager.class);
                startActivity(i);
            }
        });

        search_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchViewPager.class);
                startActivity(i);
            }
        });



        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileViewPager.class);
                startActivity(i);
            }
        });



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new AccueilViewPager_fragment()).commit();


        photocouv.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colortint), PorterDuff.Mode.DARKEN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.categories) {

            Intent i = new Intent(getApplicationContext(), CategoriesViewPager.class);
            startActivity(i);

        }/* else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(getApplicationContext(), SchoolMainPager.class);
            i.putExtra("classe_id",classe_id);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

            Intent i = new Intent(getApplicationContext(), NotificationsViewPager.class);
            i.putExtra("classe_id",classe_id);
            i.putExtra("is_prof",is_prof);
            i.putExtra("is_admin",is_admin);
            startActivity(i);

        }*/ else if (id == R.id.feedback) {

        } else if (id == R.id.deconnect) {

            if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
                LoginManager.getInstance().logOut();

                SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
                pref.edit().clear().apply();

                db.deleteUser();
                db.deleteConversations();
                db.deletePosts();
                db.deleteMessages();
                db.deleteEvents();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else
            {
                SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
                pref.edit().clear().apply();
                db.deleteUser();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GetUserLocal() {
        user = db.SelectUser();
        if (user !=null) {
            img.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(host + "/" + user.getProfile_pic()).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .error(R.drawable.user_avatar)
                    .placeholder(R.drawable.user_avatar)
                    .into(img);


            Glide.with(getApplicationContext())
                    .load(host + "/" + user.getCover_pic())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .error(R.drawable.london_flat)
                    .placeholder(R.drawable.london_flat)
                    .into(photocouv);

            nav_name.setText(user.getUser_flname());

            my_id = user.getId();

         //   Toast.makeText(getApplicationContext(),"id: "+my_id,Toast.LENGTH_LONG).show();
            //  nav_school.setText(school_name);
        }
    }


    public void GetUserData()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/profile/";
        user = new User();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String classe_name;
                    int classe_year;

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {
                            JSONArray res = new JSONArray(response);
                            for (int i = 0; i < res.length(); i++) {

                                JSONObject menuObject = res.getJSONObject(i);

                                String username = menuObject.getString("username");
                                my_id = menuObject.getInt("id");
                                String fl_name = menuObject.getString("first_name") + " " + menuObject.getString("last_name");
                                String email = menuObject.getString("email");
                                String prof_bio = menuObject.getString("bio");
                                String pic_url = menuObject.getString("picture");
                                String cover_pic = menuObject.getString("cover_picture");

                                is_admin = menuObject.getBoolean("is_admin");
                                is_prof = menuObject.getBoolean("is_professor");
                                boolean is_social = menuObject.getBoolean("is_social");

                                messages_num = menuObject.getInt("messages_count");

                                if (messages_num > 0)
                                    {
                                        mess_count.setText(String.valueOf(messages_num));
                                        mess_count.setVisibility(View.VISIBLE);
                                        mess_notif.setVisibility(View.VISIBLE);
                                    }

                                if (messages_num <= 0) {
                                    mess_count.setText(String.valueOf(messages_num));
                                    mess_count.setVisibility(View.INVISIBLE);
                                    mess_notif.setVisibility(View.INVISIBLE);
                                }


                                String msg_ids = menuObject.getString("messages_ids");
                                JSONArray mess_ids = new JSONArray(msg_ids);
                                for (int j = 0; j < mess_ids.length(); j++) {

                                    list_notif_mess.add((Integer) mess_ids.get(j));
                             //       Toast.makeText(getApplicationContext(),"f " + (Integer) mess_ids.get(j),Toast.LENGTH_LONG).show();
                                }
                                    String classe = menuObject.getString("classe");

                                if (!classe.equals("") && !classe.equals("null")) {
                                    JSONObject cur = new JSONObject(classe);
                                    classe_name = cur.getString("name");
                                    classe_id = cur.getInt("id");
                                    classe_year = cur.getInt("year");
                                }
                                else
                                {
                                    navigationView = findViewById(R.id.nav_view);
                                    Menu nav_Menu = navigationView.getMenu();
                                  //  nav_Menu.findItem(R.id.fac).setVisible(false);
                               //     nav_school.setVisibility(View.GONE);
                                }

                                img.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(host  +"/"+ pic_url).dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .centerCrop()
                                        .error(R.drawable.user_avatar)
                                        .placeholder(R.drawable.user_avatar)
                                        .into(img);


                                Glide.with(getApplicationContext())
                                        .load(host +"/"+ cover_pic)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .centerCrop()
                                        .error(R.drawable.london_flat)
                                        .placeholder(R.drawable.london_flat)
                                        .into(photocouv);

                                nav_name.setText(fl_name);
                                nav_school.setText(classe_name);

                                user.setId(my_id);
                                user.setUsername(username);
                                user.setIs_admin(is_admin);
                                user.setIs_prof(is_prof);
                                user.setUser_flname(fl_name);
                                user.setProfile_pic(pic_url);
                                user.setCover_pic(cover_pic);
                                user.setIs_social(is_social);
                                user.setEmail(email);
                                user.setBio(prof_bio);

                            }

                            db.deleteUser();
                            db.AddUser(user);


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
                    /*    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLoadTodays, new IntentFilter("update-message"));
        super.onResume();


        if (messages_num > 0)
        {
            mess_count.setText(String.valueOf(messages_num));
            mess_count.setVisibility(View.VISIBLE);
            mess_notif.setVisibility(View.VISIBLE);
        }

        if (messages_num <= 0) {
            mess_count.setText(String.valueOf(messages_num));
            mess_count.setVisibility(View.INVISIBLE);
            mess_notif.setVisibility(View.INVISIBLE);
        }
    //    Toast.makeText(getApplicationContext(),messages_num+" ",Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverLoadTodays);

    }


    private BroadcastReceiver broadcastReceiverLoadTodays = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        //    Toast.makeText(getApplicationContext(), intent.getExtras().getString("message"), Toast.LENGTH_SHORT).show();

            if (messages_num > 0)
            {
                mess_count.setText(String.valueOf(messages_num));
                mess_count.setVisibility(View.VISIBLE);
                mess_notif.setVisibility(View.VISIBLE);
            }

            if (messages_num <= 0) {
                mess_count.setText(String.valueOf(messages_num));
                mess_count.setVisibility(View.INVISIBLE);
                mess_notif.setVisibility(View.INVISIBLE);
            }
            Log.d("Response3", list_notif_mess+" "+messages_num);

        }
    };

}
