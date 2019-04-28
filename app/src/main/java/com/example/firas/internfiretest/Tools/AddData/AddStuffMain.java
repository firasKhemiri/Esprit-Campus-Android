package com.example.firas.internfiretest.Tools.AddData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.firas.internfiretest.Categories.CategoryMain;
import com.example.firas.internfiretest.LoginActivity;
import com.example.firas.internfiretest.R;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddStuffMain extends AppCompatActivity {

    Spinner spinner;

    FrameLayout frame;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stuff_main);

        frame = findViewById(R.id.frame);

        pos = getIntent().getIntExtra("pos",0);

        List<String> myArraySpinner = new ArrayList<String>();

        myArraySpinner.add("Publication");
        myArraySpinner.add("Evénement");
        myArraySpinner.add("Cours");


        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text, myArraySpinner );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);

        spinner.setSelection(pos);

        ChangeFrame(pos);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChangeFrame(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    void ChangeFrame(int position)
    {
        FragmentTransaction transaction;
        switch (position)
        {
            case 1:
                transaction = getSupportFragmentManager().beginTransaction();
                AddEventActualFragment fragEvent = new AddEventActualFragment();

                transaction.replace(R.id.frame, fragEvent);
                transaction.commit();
                break;

            case 0:
                transaction = getSupportFragmentManager().beginTransaction();

                AddPhotoActualFragment fragPhoto = new AddPhotoActualFragment();

                transaction.replace(R.id.frame, fragPhoto);
                transaction.commit();
        }
    }

}