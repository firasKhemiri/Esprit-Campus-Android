package com.esprit.firas.espritcampus.Tools.AddData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Accueil.AccueilActivity;
import com.esprit.firas.espritcampus.Categories.CategoryDialogFragment;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;



public class AddEventActualFragment extends Fragment implements NumberPicker.OnValueChangeListener{


    public static final String TAG = "Upload Image";

    private int PICK_IMAGE_REQUEST = 100;

    EditText descri;
    EditText location;
    TextView date_lay_beg;
    TextView date_lay_end;
    TextView category;
    TextView limit;
    Services s;
    public static final int CAT_FRAGMENT = 1;

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

    boolean isLimited;
    int maxLimit;

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

    View view;

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_event, container, false);

        host = getString(R.string.aphost);

        myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        s = new Services();

        ImageView buttonLoadImage = view.findViewById(R.id.pic_change);
        Button valider = view.findViewById(R.id.valider);
        descri = view.findViewById(R.id.descri);
        name_event = view.findViewById(R.id.name_event);
        location = view.findViewById(R.id.location);
        date_lay_beg = view.findViewById(R.id.date_deb);
        date_lay_end = view.findViewById(R.id.date_end);
        category = view.findViewById(R.id.category_text);

        cat_lay = view.findViewById(R.id.cat_lay);
        cat_img = view.findViewById(R.id.cat_img);
        cat_name = view.findViewById(R.id.cat_name);
        limit = view.findViewById(R.id.limit);

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

        isLimited = false;
        maxLimit=0;

        final Fragment frag = this;

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_event/");
                CategoryDialogFragment catfragment = new CategoryDialogFragment();
                catfragment.setArguments(catargs);

                catfragment.setTargetFragment(frag, CAT_FRAGMENT);
                catfragment.show(fm,"abc");
            }
        });


        cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_event/");
                CategoryDialogFragment catfragment = new CategoryDialogFragment();
                catfragment.setArguments(catargs);
                catfragment.setTargetFragment(frag, CAT_FRAGMENT);
                catfragment.show(fm,"abc");
            }
        });


        limit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(Objects.requireNonNull(getContext()));
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.number_picker_dialog);
                TextView b1 = d.findViewById(R.id.button1);
                TextView b2 = d.findViewById(R.id.button2);
                final NumberPicker np = d.findViewById(R.id.numberPicker1);
                np.setMaxValue(200);
                np.setMinValue(1);
                np.setWrapSelectorWheel(true);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        limit.setText("Limité à "+String.valueOf(np.getValue())+" participants");
                        isLimited = true;
                        maxLimit = np.getValue();
                        d.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        isLimited = false;
                        limit.setText("Pas de limite de participants");
                        d.dismiss();
                    }
                });
                d.show();


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

                    new Services().AddEventActual(decoded, handler, nom, desc, loc, db,
                            de, cat_id, view.getContext(),isLimited,maxLimit,0.0,0.0);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    @SuppressLint("HandlerLeak")
    Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
                // txtStatus.setText("Upload Success");
         //       Toast.makeText(view.getContext(), "Upload Success", Toast.LENGTH_LONG).show();

                Intent i = new Intent(view.getContext(), AccueilActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            } else view.getContext();
        }
    };

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAT_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // here the part where I get my selected date from the saved variable in the intent and the displaying it.
                Bundle bundle = data.getExtras();
                assert bundle != null;
                String name = bundle.getString("cat_name", "error");
                String pic = bundle.getString("cat_img", "error");
                int id = bundle.getInt("cat_id", 0);


         //       Toast.makeText(view.getContext(), " " + post_name, Toast.LENGTH_LONG).show();

                category.setVisibility(View.GONE);

                cat_lay.setVisibility(View.VISIBLE);
                cat_name.setText(name);

                Glide.with(view.getContext())
                        .load(host + "/" + pic).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .error(R.drawable.london_flat)
                        .placeholder(R.drawable.london_flat)
                        .into(cat_img);

                cat_id = id;
            }
        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ImageView imageView = view.findViewById(R.id.photup);

            Uri fileUri = data.getData();
            String selectedFilePath = getPath(fileUri);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {

                imageView.setVisibility(View.VISIBLE);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), fileUri);


                ByteArrayOutputStream out = new ByteArrayOutputStream();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = view.getContext().getContentResolver().query(fileUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(filePath);
                long taille = file.length() / 1024;


                //  int taille = bitmap.getByteCount()/1024;
                // int taille = (bitmap.getRowBytes() * bitmap.getHeight())/1024;
                //      Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();


                if (taille >= 7000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //       Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 5800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //         Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 4800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //             Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 3800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //        Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();
                } else if (taille >= 3000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
//                       Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                } else if (taille >= 2000) {

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
        //            Toast.makeText(view.getContext(), " 5 " + taille, Toast.LENGTH_LONG).show();

                } else if (taille >= 1000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
          //          Toast.makeText(view.getContext(), " 6 " + taille, Toast.LENGTH_LONG).show();

                } else if (taille >= 500) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
             //       Toast.makeText(view.getContext(), " 7 " + taille, Toast.LENGTH_LONG).show();

                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
         //           Toast.makeText(view.getContext(), " 5 " + taille, Toast.LENGTH_LONG).show();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = view.getContext().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }



    private void datePicker(final boolean beg){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        if (beg) {
                            date_beg = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            timePicker(true);
                        }
                        else
                        {
                            date_end = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            timePicker(false);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }


    private void timePicker(final boolean beg){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;
                        try {
                            if (beg) {
                                date_time_beg = date_beg + " " + hourOfDay + ":" + minute + ":00";
                                //        date_lay_beg.setText(date_time_beg);

                                if (s.getDateDiffInMins(date_time_beg) > 1800) {
                                    date_lay_beg.setText("Commence "+s.singleDateEvnMinis(date_time_beg));

                                //    Toast.makeText(getContext(), "Date" + s.getDateDiffInMins(date_time_beg), Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getContext(), "Date invalide", Toast.LENGTH_LONG).show();

                            } else {

                                date_time_end = date_end + " " + hourOfDay + ":" + minute + ":00";
                             //   date_lay_end.setText(date_time_end);
                            if (s.getDateDiffInMins(date_time_end) > 1800) {
                                if (s.compareDates(date_time_end,date_time_beg)>1800) {
                                    date_lay_end.setText("Fini "+s.singleDateEvnMinis(date_time_end));
                                }
                                else
                                    Toast.makeText(getContext(),"Date invalide",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getContext(),"Date invalide",Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

}
