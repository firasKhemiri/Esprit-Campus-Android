package com.esprit.firas.espritcampus.OtherUser;

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


public class DetailsUser extends DialogFragment {

    View view;

  //  String host;


    String userId;
    String location;
    String birthday;
    String followers;
    String following;
    Boolean isProfessor;
    String classeName;

    CardView schoolCard,followersCard,followingCard,adrCard,birthdayCard;

    TextView schoolText,followersText,followingText,adrText,birthdayText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_details_pop, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Bundle mArgs = getArguments();


        userId = String.valueOf(mArgs.getInt("user_id"));
        following = mArgs.getString("following");
        followers = mArgs.getString("followers");
        classeName = mArgs.getString("classe");
        birthday = mArgs.getString("birthday");
        location = mArgs.getString("location");
        isProfessor = mArgs.getBoolean("isProfessor");


        followingCard = view.findViewById(R.id.following_card);
        followersCard = view.findViewById(R.id.followers_card);
        schoolCard = view.findViewById(R.id.school_card);

        followingText = view.findViewById(R.id.following_text);
        followersText = view.findViewById(R.id.followers_text);
        schoolText = view.findViewById(R.id.school_text);
        adrText = view.findViewById(R.id.adr_text);
        birthdayText = view.findViewById(R.id.birthday_text);


        followingText.setText(following);
        followersText.setText(followers);
        adrText.setText(location);

        birthdayText.setText(birthday);

        if (classeName.equals("") || classeName == null)
        {
            schoolText.setText("Non specifié");
        }
        else
        {
            String prof= "";
            if (isProfessor)
                prof="Professeur";
            else
                prof="Etudiant";

            String type = prof+" en "+ classeName;
            schoolText.setText(type);
        }

     //   host = getString(R.string.aphost);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.getDialog().setTitle("Deatils");



        return view;
    }

}
