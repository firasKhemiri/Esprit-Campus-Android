package com.esprit.firas.espritcampus.Categories;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Feed.PostsMainNoSqLite;


public class CategoryPostsViewPager extends AppCompatActivity {

    int cat_id;
    String cat_name;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.accueil_pager3);

         SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
         final String token = pref.getString("token", null);



         Intent intent = getIntent();
         cat_id = intent.getIntExtra("cat_id",0);
         cat_name = intent.getStringExtra("cat_name");



         ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
             CategoryPostsViewPager.PagerAdapter pagerAdapter =
                     new CategoryPostsViewPager.PagerAdapter(getSupportFragmentManager(), CategoryPostsViewPager.this);
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



    class PagerAdapter extends FragmentPagerAdapter {

       // int tabTitles[] = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        String tabTitles[] = new String[]{cat_name};


        Context context;

        PagerAdapter(FragmentManager fm, CategoryPostsViewPager context) {
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
                    postargs.putString("url", "/api/category/"+cat_id+"/posts/");
                    PostsMainNoSqLite postfragment = new PostsMainNoSqLite();
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

}