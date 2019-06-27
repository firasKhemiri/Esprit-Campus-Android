package com.esprit.firas.espritcampus.Accueil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Courses.CoursMain;
import com.esprit.firas.espritcampus.Tools.Events.EventsMain;
import com.esprit.firas.espritcampus.Tools.Feed.PostsMainWithSqLite;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AccueilViewPager_fragment extends Fragment {

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager3, container, false);

         SharedPreferences pref = view.getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
         final String token = pref.getString("token", null);

         if (token == null || Objects.equals(token, ""))
         {
             Intent i = new Intent(view.getContext(), LoginActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(i);
         }

         else {



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
*/


             ViewPager viewPager = view.findViewById(R.id.viewpager);
             AccueilViewPager_fragment.PagerAdapter pagerAdapter =
                     new AccueilViewPager_fragment.PagerAdapter(getChildFragmentManager());
             viewPager.setAdapter(pagerAdapter);

             viewPager.setOffscreenPageLimit(2);

             // Give the TabLayout the ViewPager
             TabLayout tabLayout = view.findViewById(R.id.tab_layout);
             tabLayout.setupWithViewPager(viewPager);

             // Iterate over all tabs and set the custom view
             for (int i = 0; i < tabLayout.getTabCount(); i++) {
                 TabLayout.Tab tab = tabLayout.getTabAt(i);
                 assert tab != null;
                 tab.setCustomView(pagerAdapter.getTabView(i));
             }
         }
        return  view;
    }


    class PagerAdapter extends FragmentPagerAdapter {

        int tabTitles[] = new int[]{R.drawable.tab_news_feed_480, R.drawable.tab_study_480, R.drawable.tab_calendar_480};

       // String tabTitles[] = new String[]{"Posts","Cours","Events"};


        Context context;

        PagerAdapter(FragmentManager fm) {
            super(fm);
            this.context = view.getContext();
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
                    PostsMainWithSqLite postfragment = new PostsMainWithSqLite();
                    postfragment.setArguments(postargs);

                    return postfragment;


                case 1:
                    Bundle postargs2 = new Bundle();
                    postargs2.putString("url", "/api/coursfeed/");
                    CoursMain postfragment2 = new CoursMain();
                    postfragment2.setArguments(postargs2);

                    return postfragment2;


                case 2:
                    Bundle postargs3 = new Bundle();
                    postargs3.putString("url", "/api/feed/events/");
                    EventsMain postfragment3 = new EventsMain();
                    postfragment3.setArguments(postargs3);

                    return postfragment3;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return String.valueOf(tabTitles[position]);
        }

        @SuppressLint("InflateParams")
        View getTabView(int position) {
             View tab = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_tab, null);
            ImageView tv = tab.findViewById(R.id.screen_shot);
           // TextView tv = (TextView)tab.findViewById(R.id.tabtext);
            tv.setImageBitmap(BitmapFactory.decodeResource(AccueilViewPager_fragment.this.getResources(),
                    tabTitles[position]));
            return tab;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}