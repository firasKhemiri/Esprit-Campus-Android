package com.example.firas.internfiretest.Tools.UpdateData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Accueil.AccueilActivity;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;


/**
 * Created by FIRAS on 11/06/2017.
 */

public class UpdatePhoto extends Activity {

    String host;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_photo);

        host = getString(R.string.aphost);


        final Intent i = getIntent();
        String img = i.getStringExtra("image");
        String name = i.getStringExtra("nom");
        final int id = i.getIntExtra("id",0);

        ImageView postImage = (ImageView) findViewById(R.id.photup);

        Button valider = (Button) findViewById(R.id.valider);
        final EditText descri = (EditText) findViewById(R.id.descri);

        descri.setText(name);

        Glide.with(getApplicationContext())
                .load(host+"/"+img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(postImage);


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = String.valueOf(descri.getText());
                new Services().UpdatePhoto(String.valueOf(id),nom,getApplicationContext());

                Intent i = new Intent(UpdatePhoto.this, AccueilActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
    }
}

