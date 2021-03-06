package com.esprit.firas.espritcampus.School.Department;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.esprit.firas.espritcampus.LoginActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.School.AllStudentsViewPager;
import com.esprit.firas.espritcampus.Tools.Courses.CoursMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DepartmentViewPager extends AppCompatActivity {


    Button dep_name;
    ImageButton settings;

    TextView students;
    ImageButton classes;
    ImageButton stud_but;

    String host;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dep_classe_pager);

        host = getString(R.string.aphost);

        dep_name = (Button) findViewById(R.id.dep_name);
        students = (TextView) findViewById(R.id.num_students_dep);
        stud_but = (ImageButton) findViewById(R.id.stud_but);
        classes = (ImageButton) findViewById(R.id.dep_classes);


        classes.setVisibility(View.INVISIBLE);
        dep_name.setVisibility(View.INVISIBLE);
        students.setVisibility(View.INVISIBLE);
        stud_but.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        id = intent.getIntExtra("dep_id",0);

        GetDepData(id);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), DepartmentViewPager.this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(1);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }



        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AllStudentsViewPager.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });


        stud_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AllStudentsViewPager.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
    }


class PagerAdapter extends FragmentPagerAdapter {

    // int tabTitles[] = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String tabTitles[] = new String[]{"Cours"};
    Context context;

    PagerAdapter(FragmentManager fm, DepartmentViewPager context) {
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
                postargs.putString("url", "/api/departement/"+id+"/cours/");
                CoursMain postfragment = new CoursMain();
                postfragment.setArguments(postargs);

                return postfragment;

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








    public void GetDepData(int id)
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        String url = host + "/api/department/"+id+"/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    String result;

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);

                        try {
                            JSONObject menuObject = new JSONObject(response);

                                int id = menuObject.getInt("id");
                                String name = menuObject.getString("name");
                                int stud_count = menuObject.getInt("students_count");

                                students.setText(String.valueOf(stud_count));
                                dep_name.setText(name);

                                students.setVisibility(View.VISIBLE);
                                classes.setVisibility(View.VISIBLE);
                                stud_but.setVisibility(View.VISIBLE);
                                dep_name.setVisibility(View.VISIBLE);


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