package com.example.firas.internfiretest.School;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Users.Users;


public class AllProfsViewPager extends AppCompatActivity {


    String host;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_view_pager);

        host = getString(R.string.aphost);

        Intent i = getIntent();
        id = i.getIntExtra("id",0);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }




    class TabsAdapter extends FragmentPagerAdapter {
        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:
                    Bundle postargs2 = new Bundle();
                    postargs2.putString("url", "/api/school/"+id+"/professors/");
                    Users postfragment2 = new Users();
                    postfragment2.setArguments(postargs2);

                    return postfragment2;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Professeurs";
            }
            return "";
        }
    }


}