package com.esprit.firas.espritcampus.Profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Signup.SignupActivity2;
import com.esprit.firas.espritcampus.Tools.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    SharedPreferences sharedpreferences;


    private static int RESULT_PROF = 1;

    private static int RESULT_COUV = 2;

    String host;

    public static final String TAG = "Upload Image";
    Bitmap bmpp;
    Bitmap bmpc;

    String token;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    EditText firstname,lastname,email,birthday,bio,phone,adresse;
    Button update,pass,delete_user,change_photoprof,change_photocouv;
    ImageView photocouv;
    CircleImageView photoprof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_update);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);


        host = getString(R.string.aphost);

        update = findViewById(R.id.update);
        pass = findViewById(R.id.pass);
        delete_user = findViewById(R.id.delete_user);
        change_photoprof = findViewById(R.id.change_photoprof);
        change_photocouv = findViewById(R.id.change_photocouv);

        photocouv = findViewById(R.id.photocouv);
        photoprof = findViewById(R.id.photoprof);

        TextInputLayout txt = findViewById(R.id.text_input_layout8);


        birthday = findViewById(R.id.birthday);
        firstname = findViewById(R.id.prenom);
        lastname = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        bio = findViewById(R.id.bio);
        phone = findViewById(R.id.phone);
        adresse = findViewById(R.id.adress);


        Intent i = getIntent();

        birthday.setText(i.getStringExtra("birthday"));

        firstname.setText(i.getStringExtra("first_name"));
        lastname.setText(i.getStringExtra("last_name"));
        email.setText(i.getStringExtra("email"));
        bio.setText(i.getStringExtra("bio"));
        phone.setText(i.getStringExtra("phone"));
        adresse.setText(i.getStringExtra("adr"));

        String pic_url = i.getStringExtra("photoprof");
        String cover_pic = i.getStringExtra("photocouv");

        photoprof.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(host + "/" + pic_url).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(photoprof);


        photocouv.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(host + "/" + cover_pic)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .error(R.drawable.london_flat)
                .placeholder(R.drawable.london_flat)
                .into(photocouv);


        photocouv.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colortint), PorterDuff.Mode.DARKEN);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,nom,prenom,emaill,biog,phon,adr,birth;

                if (firstname.getText().length() == 0)
                    firstname.setError("Insérer votre prenom.");
                else if (lastname.getText().length() == 0)
                    lastname.setError("Insérer votre nom.");
                else if (!isValidEmail(email.getText()))
                    email.setError("Insérer un email valide.");
                else if (birthday.getText().length() == 0)
                    birthday.setError("Insérer votre date de naissance.");

                else {


                    myCalendar = Calendar.getInstance();

                    date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }

                    };

                    birthday.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(getApplicationContext(), date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    birthday.setInputType(InputType.TYPE_NULL);


                    nom = String.valueOf(lastname.getText());
                    prenom = String.valueOf(firstname.getText());
                    emaill = String.valueOf(email.getText());
                    biog = String.valueOf(bio.getText());
                    phon = String.valueOf(phone.getText());
                    adr = String.valueOf(adresse.getText());


                    Map<String,String> params = new HashMap<String, String>();
                    params.put("first_name", prenom);
                    params.put("last_name", nom);
                    params.put("email", emaill);
                    params.put("location", adr);
                    params.put("phone", phon);
                    params.put("birthdate", birthday.getText().toString());
                    params.put("bio", biog);


                    new Services().UpdateUser(params, getApplicationContext());
                }
            }
        });





       /* delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UpdateProfile.this, DeleteUser.class);
                startActivity(i);

            }
        });


        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UpdateProfile.this, ChangePass.class);
                startActivity(i);
            }
        });*/

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


        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        birthday.setInputType(InputType.TYPE_NULL);


    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }




    @SuppressLint("HandlerLeak")
    Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
                // txtStatus.setText("Upload Success");
                Toast.makeText(getApplicationContext(),"Succés",Toast.LENGTH_LONG).show();

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

                int taille = (bmpp.getRowBytes() * bmpp.getHeight())/1024;
//                Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();
                Bitmap decoded ;

                if(taille>=70000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    // Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=60000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //  Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=50000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    // Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=40000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //  Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=20000) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmpp.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    photoprof.setImageBitmap(decoded);
                    //   Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                }

                else {
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
//                Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();
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

    public String parseDate(String time) {
        String inputPattern = "dd/MM/yyyy ";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}


