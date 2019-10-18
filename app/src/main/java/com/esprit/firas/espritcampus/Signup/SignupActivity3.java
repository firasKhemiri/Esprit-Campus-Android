package com.esprit.firas.espritcampus.Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity3 extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private static int RESULT_PROF = 1;

    private static int RESULT_COUV = 2;


    public static final String TAG = "Upload Image";
    Bitmap bmpp;
    Bitmap bmpc;



    EditText descri;


    String token;
    String host;


    EditText bio;
    Button btnLogin,change_photoprof,change_photocouv;
    ImageView photocouv;
    CircleImageView photoprof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_next_next);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);


        btnLogin = (Button) findViewById(R.id.btnLogin);

        change_photoprof = (Button) findViewById(R.id.change_photoprof);
        change_photocouv = (Button) findViewById(R.id.change_photocouv);

        photocouv = (ImageView) findViewById(R.id.photocouv);
        photoprof = (CircleImageView) findViewById(R.id.photoprof);

        bio = (EditText) findViewById(R.id.bio);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bio_text = bio.getText().toString();
                Map<String,String> params = new HashMap<String, String>();
                params.put("bio", bio_text);

                Services s = new Services();
                s.UpdateUser(params,getApplicationContext());

            }
        });



        change_photoprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_PROF);
            }
        });

        change_photocouv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_COUV);
            }
        });

    }



    Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
                // txtStatus.setText("Upload Success");
                Toast.makeText(getApplicationContext(),"Succès",Toast.LENGTH_LONG).show();


            } else {
                //  txtStatus.setText("Upload Error");
                Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_LONG).show();
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PROF && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            String selectedFilePath = getPath(filePath);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {
                bmpp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                int taille = (bmpp.getRowBytes() * bmpp.getHeight()) / 1024;
//                Toast.makeText(getApplicationContext(), " " + taille, Toast.LENGTH_LONG).show();
                Bitmap decoded;

                if (taille >= 70000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    // Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 60000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //  Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 50000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    // Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 40000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //  Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 20000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //   Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    // Toast.makeText(getApplicationContext()," 5 "+taille,Toast.LENGTH_LONG).show();
                }

                new Services().AddPhotoprof(decoded, handler, getApplicationContext());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == RESULT_COUV && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri filePath = data.getData();
                String selectedFilePath = getPath(filePath);
                Log.i(TAG, " File path : " + selectedFilePath);
                try {
                    bmpc = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    int taille = (bmpc.getRowBytes() * bmpc.getHeight())/1024;
//                    Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();
                    Bitmap decoded ;

                    if(taille>=70000) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 20, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        // Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                    }

                    else if(taille>=60000) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 30, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        //  Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                    }

                    else if(taille>=50000) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 40, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        // Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                    }

                    else if(taille>=40000) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 50, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        //  Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();

                    }

                    else if(taille>=20000) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 60, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        //   Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                    }

                    else {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmpc.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        photocouv.setImageBitmap(decoded);
                        // Toast.makeText(getApplicationContext()," 5 "+taille,Toast.LENGTH_LONG).show();

                    }

                    new Services().AddPhotoCouv(decoded, handler, getApplicationContext());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



}


