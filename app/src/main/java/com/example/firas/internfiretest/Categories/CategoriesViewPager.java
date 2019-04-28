package com.example.firas.internfiretest.Categories;

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

import com.example.firas.internfiretest.LoginActivity;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Search.Categories.SearchCat;
import com.example.firas.internfiretest.Search.Cours.SearchCours;
import com.example.firas.internfiretest.Search.Users.SearchMain;
import com.github.clans.fab.FloatingActionButton;

import java.util.Objects;


public class CategoriesViewPager extends AppCompatActivity {


    ImageButton profile, messages, searchBut;
    Button search;

    FloatingActionButton add_photo,add_mission,add_event;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.accueil_pager4);
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.accueil_pager3, container, false);
*/
         SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
         final String token = pref.getString("token", null);

         if (token == null || Objects.equals(token, ""))
         {
             Intent i = new Intent(CategoriesViewPager.this, LoginActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(i);
         }

         else {

             ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
             CategoriesViewPager.PagerAdapter pagerAdapter =
                     new CategoriesViewPager.PagerAdapter(getSupportFragmentManager(), CategoriesViewPager.this);
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

        int tabTitles[] = new int[]{R.drawable.tab_news_feed_480,/* R.drawable.school,*/ R.drawable.tab_calendar_480};


        Context context;

        PagerAdapter(FragmentManager fm, CategoriesViewPager context) {
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

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_pub/");
                CategoryMain catfragment = new CategoryMain();
                catfragment.setArguments(catargs);

                return catfragment;
            }

         /*   switch (position) {
                case 1:
                    return new SearchSchools();
            }*/

            switch (position) {
                case 1:
                    Bundle catargs = new Bundle();
                    catargs.putString("url", "/api/categories_event/");
                    CategoryMain catfragment = new CategoryMain();
                    catfragment.setArguments(catargs);

                    return catfragment;


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
            tv.setImageBitmap(BitmapFactory.decodeResource(CategoriesViewPager.this.getResources(),
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