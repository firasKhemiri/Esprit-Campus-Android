package com.esprit.firas.espritcampus.School;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Lists.Departments;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DetailsSchool extends DialogFragment {

    View view;

  //  String host;


    int schoolId;
    String location;
    int students;
    int profs;
    int deps;

    CardView schoolCard, studentsCard, profsCard,depCard,birthdayCard;

    TextView schoolText, studentsText, profsText,adrText,depsText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.uschool_pop, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Bundle mArgs = getArguments();


        schoolId = mArgs.getInt("school_id");
        students = mArgs.getInt("students_count");
        profs = mArgs.getInt("profs_count");
        deps = mArgs.getInt("deps_count");
        location = mArgs.getString("location");


        profsCard = view.findViewById(R.id.prof_card);
        studentsCard = view.findViewById(R.id.students_card);
        schoolCard = view.findViewById(R.id.school_card);
        depCard = view.findViewById(R.id.deps_card);

        profsText = view.findViewById(R.id.prof_text);
        studentsText = view.findViewById(R.id.students_text);
        schoolText = view.findViewById(R.id.school_text);
        adrText = view.findViewById(R.id.adr_text);
        depsText = view.findViewById(R.id.deps_text);


        profsText.setText(String.valueOf(profs));
        studentsText.setText((String.valueOf(students)));
        depsText.setText((String.valueOf(students)));

        if (location.equals("") || location == null)
        {
            adrText.setText("Non specifié");
        }
        else
            adrText.setText(location);

     //   host = getString(R.string.aphost);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.getDialog().setTitle("Deatils");



        profsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), AllProfsViewPager.class);
                i.putExtra("id", schoolId);
                startActivity(i);
            }
        });


        studentsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), AllStudentsViewPager.class);
                i.putExtra("id", schoolId);
                startActivity(i);
            }
        });


        depCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Departments.class);
                i.putExtra("url", "/api/school/" + schoolId + "/departments/");
                startActivity(i);
            }
        });

        return view;
    }

}
