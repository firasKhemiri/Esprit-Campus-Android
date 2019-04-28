package com.example.firas.internfiretest.Accueil;/*
package com.example.firas.internproject.Accueil;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.firas.internproject.Categories.CategoryMain;
import com.example.firas.internproject.LoginActivity;
import com.example.firas.internproject.Messages.MainConversationsActivity;
import com.example.firas.internproject.Profile.ProfileViewPager;
import com.example.firas.internproject.R;
import com.example.firas.internproject.Search.SearchViewPager;
import com.example.firas.internproject.Tools.Feed.PostsMainNoSqLite;
import com.github.clans.fab.FloatingActionButton;

import java.util.Objects;



public class AccueilViewPager extends AppCompatActivity {


    ImageButton profile, messages, searchBut;
    Button search;

    FloatingActionButton add_photo,add_mission,add_event;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.accueil_pager3);

         SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
         final String token = pref.getString("token", null);

         if (token == null || Objects.equals(token, ""))
         {
             Intent i = new Intent(AccueilViewPager.this, LoginActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(i);
         }

         else {


             profile = (ImageButton) findViewById(R.id.profile);

             messages = (ImageButton) findViewById(R.id.messages);

             searchBut = (ImageButton) findViewById(R.id.search_icon);

             search = (Button) findViewById(R.id.search);

        /*     add_photo = (FloatingActionButton) findViewById(R.id.add_photo);
             add_mission = (FloatingActionButton) findViewById(R.id.add_missionn);
             add_event = (FloatingActionButton) findViewById(R.id.add_event);


             add_photo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i = new Intent(getApplicationContext(), UpdatePhotoActual.class);
                     startActivity(i);
                 }
             });

             add_mission.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i = new Intent(getApplicationContext(), AddMission.class);
                     startActivity(i);
                 }
             });

             add_event.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i = new Intent(getApplicationContext(), AddEvent.class);
                     startActivity(i);
                 }
             });


             profile.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i = new Intent(getApplicationContext(), ProfileViewPager.class);
                     startActivity(i);
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


             ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
             AccueilViewPager.PagerAdapter pagerAdapter =
                     new AccueilViewPager.PagerAdapter(getSupportFragmentManager(), AccueilViewPager.this);
             viewPager.setAdapter(pagerAdapter);

             viewPager.setOffscreenPageLimit(4);

             // Give the TabLayout the ViewPager
             TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
             tabLayout.setupWithViewPager(viewPager);

             // Iterate over all tabs and set the custom view
             for (int i = 0; i < tabLayout.getTabCount(); i++) {
                 TabLayout.Tab tab = tabLayout.getTabAt(i);
                 assert tab != null;
                 tab.setCustomView(pagerAdapter.getTabView(i));
             }
         }
     //   return  view;
    }


    class PagerAdapter extends FragmentPagerAdapter {

       // int tabTitles[] = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        String tabTitles[] = new String[]{"Posts","Cours","Events"};


        Context context;

        PagerAdapter(FragmentManager fm, AccueilViewPager context) {
            super(fm);
            this.context = getApplicationContext();
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    Bundle postargs = new Bundle();
                    postargs.putString("url", "/api/feed/");
                    PostsMainNoSqLite postfragment = new PostsMainNoSqLite();
                    postfragment.setArguments(postargs);

                    return postfragment;


                case 1:

                    Bundle postargs2 = new Bundle();
                    postargs2.putString("url", "/api/feed/");
                    PostsMainNoSqLite postfragment2 = new PostsMainNoSqLite();
                    postfragment2.setArguments(postargs2);

                    return postfragment2;


                case 2:

                    Bundle postargs3 = new Bundle();
                    postargs3.putString("url", "/api/feed/");
                    PostsMainNoSqLite postfragment3 = new PostsMainNoSqLite();
                    postfragment3.setArguments(postargs3);

                    return postfragment3;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return String.valueOf(tabTitles[position]);
        }

        View getTabView(int position) {
            View tab = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab2, null);
           // ImageView tv = (ImageView) tab.findViewById(R.id.screen_shot);
            TextView tv = (TextView)tab.findViewById(R.id.tabtext);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}*/