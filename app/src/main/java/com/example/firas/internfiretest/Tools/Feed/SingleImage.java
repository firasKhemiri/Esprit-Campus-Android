package com.example.firas.internfiretest.Tools.Feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.R;

/**
 * Activity that gets transitioned to
 */
public class SingleImage extends AppCompatActivity {
    Intent i;
    String host;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_single_view);

        host = getString(R.string.aphost);

        i = getIntent();
        String img = i.getStringExtra("image");

        ImageView imageView = (ImageView)findViewById(R.id.iv_photo);

        Glide.with(getApplicationContext())
                .load(host+"/"+img).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.placeholder)
                .into(imageView);
    }
}
