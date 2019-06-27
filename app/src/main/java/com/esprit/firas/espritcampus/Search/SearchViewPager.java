package com.esprit.firas.espritcampus.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Search.Categories.SearchCat;
import com.esprit.firas.espritcampus.Search.Cours.SearchCours;
import com.esprit.firas.espritcampus.Search.Users.SearchMain;
import com.github.clans.fab.FloatingActionButton;

import java.util.Objects;


public class SearchViewPager extends AppCompatActivity {


    ImageButton profile, messages, searchBut;
    Button search;

    FloatingActionButton add_photo,add_mission,add_event;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.accueil_pager3);
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager3, container, false);
*/
         SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
         final String token = pref.getString("token", null);

         if (token == null || Objects.equals(token, ""))
         {
             Intent i = new Intent(SearchViewPager.this, LoginActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(i);
         }

         else {

             ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
             SearchViewPager.PagerAdapter pagerAdapter =
                     new SearchViewPager.PagerAdapter(getSupportFragmentManager(), SearchViewPager.this);
             viewPager.setAdapter(pagerAdapter);

             viewPager.setOffscreenPageLimit(3);

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

        int tabTitles[] = new int[]{R.drawable.group,/* R.drawable.school,*/ R.drawable.categories,R.drawable.tab_study_480};


        Context context;

        PagerAdapter(FragmentManager fm, SearchViewPager context) {
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
                    return new SearchMain();
            }

         /*   switch (position) {
                case 1:
                    return new SearchSchools();
            }*/

            switch (position) {
                case 1:
                    return new SearchCat();
            }

            switch (position) {
                case 2:
                    return new SearchCours();
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
            tv.setImageBitmap(BitmapFactory.decodeResource(SearchViewPager.this.getResources(),
                    tabTitles[position]));
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

}