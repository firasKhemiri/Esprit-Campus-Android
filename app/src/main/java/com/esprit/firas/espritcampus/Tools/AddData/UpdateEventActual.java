package com.esprit.firas.espritcampus.Tools.AddData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Accueil.AccueilActivity;
import com.esprit.firas.espritcampus.Categories.CategoryDialog;
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateEventActual extends Activity implements CategoryDialog.OnCompleteListener{


    public static final String TAG = "Upload Image";

    private int PICK_IMAGE_REQUEST = 100;

    EditText descri;
    EditText location;
    TextView date_lay_beg;
    TextView date_lay_end;
    TextView category;

    RelativeLayout cat_lay;
    TextView cat_name;
    CircleImageView cat_img;

    String nom;
    int cat_id;

    String host;

    ArrayList<String> items;
    ArrayList<Integer> items_id;
    Bitmap decoded;
    SimpleDateFormat myFormat;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    String date_beg;
    String date_end;

    TextView name_event;

    String date_time_beg;
    String date_time_end;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        host = getString(R.string.aphost);

        myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ImageView buttonLoadImage = findViewById(R.id.pic_change);
        Button valider = findViewById(R.id.valider);
        descri = findViewById(R.id.descri);
        name_event = findViewById(R.id.name_event);
        location = findViewById(R.id.location);
        date_lay_beg = findViewById(R.id.date_deb);
        date_lay_end = findViewById(R.id.date_end);
        category = findViewById(R.id.category_text);

        cat_lay = findViewById(R.id.cat_lay);
        cat_img = findViewById(R.id.cat_img);
        cat_name = findViewById(R.id.cat_name);

        items = new ArrayList<>();
        items_id = new ArrayList<>();

        items.add("all categories");
        items_id.add(0);

        date_lay_beg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(true);
            }
        });

        date_lay_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(false);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_event/");
                CategoryDialog catfragment = new CategoryDialog();
                catfragment.setArguments(catargs);
                catfragment.show(fm,"abc");
            }
        });


        cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_event/");
                CategoryDialog catfragment = new CategoryDialog();
                catfragment.setArguments(catargs);
                catfragment.show(fm,"abc");
            }
        });

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFileChooser();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myDateBeg = date_time_beg;
                String myDateEnd = date_time_end;
                try {

                    Date date_b = myFormat.parse(myDateBeg);
                    Date date_e = myFormat.parse(myDateEnd);

                    String db = myFormat.format(date_b);
                    String de = myFormat.format(date_e);

                    nom = String.valueOf(name_event.getText());
                    String desc = String.valueOf(descri.getText());
                    String loc = String.valueOf(location.getText());

             /*       new Services().UpdateEventActual(decoded, handler, nom, desc, loc, db,
                            de, cat_id, getApplicationContext());
*/
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }
    public void onComplete(Category cat) {

        category.setVisibility(View.GONE);

        cat_lay.setVisibility(View.VISIBLE);
        cat_name.setText(cat.getName());

        Glide.with(getApplicationContext())
                .load(host +"/"+ cat.getPic_url()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.london_flat)
                .placeholder(R.drawable.london_flat)
                .into(cat_img);

        cat_id = cat.getId();
    }

    @SuppressLint("HandlerLeak")
    Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
                // txtStatus.setText("Upload Success");
                Toast.makeText(getApplicationContext(),"Upload Success",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), AccueilActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(getApplicationContext(),"Upload Error",Toast.LENGTH_LONG).show();
            }
        }

    };

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ImageView imageView = findViewById(R.id.photup);

            Uri fileUri = data.getData();
            String selectedFilePath = getPath(fileUri);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {

                imageView.setVisibility(View.VISIBLE);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);


                ByteArrayOutputStream out = new ByteArrayOutputStream();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(fileUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(filePath);
                long taille = file.length()/1024;


                //  int taille = bitmap.getByteCount()/1024;
                // int taille = (bitmap.getRowBytes() * bitmap.getHeight())/1024;
                //      Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();


                if(taille>=7000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //       Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=5800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //         Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=4800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //             Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=3800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //        Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();
                }


                else if(taille>=3000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
//                       Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=2000) {

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 5 "+taille,Toast.LENGTH_LONG).show();

                }


                else if(taille>=1000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 6 "+taille,Toast.LENGTH_LONG).show();

                }


                else if(taille>=500) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 7 "+taille,Toast.LENGTH_LONG).show();

                }


                else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 5 "+taille,Toast.LENGTH_LONG).show();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

/*
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
*/

    private void datePicker(final boolean beg){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        if (beg) {
                            date_beg = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            tiemPicker(true);
                        }
                        else
                        {
                            date_end = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            tiemPicker(false);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void tiemPicker(final boolean beg){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        if (beg) {
                            date_time_beg = date_beg + " " + hourOfDay + ":" + minute + ":00";
                            date_lay_beg.setText(date_time_beg);
                        }
                        else
                        {
                            date_time_end = date_end + " " + hourOfDay + ":" + minute + ":00";
                            date_lay_end.setText(date_time_end);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

}
