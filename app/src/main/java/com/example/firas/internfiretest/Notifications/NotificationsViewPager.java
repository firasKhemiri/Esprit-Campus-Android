package com.example.firas.internfiretest.Notifications;

import android.content.Context;
import android.content.Intent;
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

import com.example.firas.internfiretest.Notifications.Notifs.MainNotifsFragment;
import com.example.firas.internfiretest.Notifications.Pendings.MainPendingsFragment;
import com.example.firas.internfiretest.R;


public class NotificationsViewPager extends AppCompatActivity {


    String host;
    int id;

    public  boolean is_prof;
    boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_pager3);

        host = getString(R.string.aphost);

        Intent intent = getIntent();
        is_prof = intent.getBooleanExtra("is_prof",false);
        is_admin = intent.getBooleanExtra("is_admin",false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), NotificationsViewPager.this);
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


class PagerAdapter extends FragmentPagerAdapter {

    // int tabTitles[] = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    String tabTitles[] = new String[]{"Notifications","Demandes"};


    Context context;

    PagerAdapter(FragmentManager fm, NotificationsViewPager context) {
        super(fm);
        this.context = getApplicationContext();
    }

    @Override
    public int getCount() {
        if(is_prof || is_admin)
            return 2;
        else return 1;
       // return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MainNotifsFragment();

            case 1:
                return new MainPendingsFragment();
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

}