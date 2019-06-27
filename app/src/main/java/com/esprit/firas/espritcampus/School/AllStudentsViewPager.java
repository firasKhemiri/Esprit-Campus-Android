package com.esprit.firas.espritcampus.School;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Users.Users;


public class AllStudentsViewPager extends AppCompatActivity {


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
                    Bundle postargs = new Bundle();
                    postargs.putString("url", "/api/school/"+id+"/students/");
                    Users postfragment = new Users();
                    postfragment.setArguments(postargs);

                    return postfragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Etudiants";
            }
            return "";
        }
    }


}