package com.example.firas.internfiretest.Tools.AddData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Accueil.AccueilActivity;
import com.example.firas.internfiretest.Categories.CategoryDialog;
import com.example.firas.internfiretest.Entities.Category;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdatePhotoActual extends Activity implements CategoryDialog.OnCompleteListener{


    public static final String TAG = "Upload Image";

    private int PICK_IMAGE_REQUEST = 100;

    public static final int POST_CAT_FRAGMENT = 1;

    String nom;
    EditText post_name;

    String host;
    Bitmap decoded;

    boolean photoChanged;


    int cat_id;
    int post_id;
    TextView category;

    RelativeLayout cat_lay;
    TextView cat_name;
    CircleImageView cat_img;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_publication);

        host = getString(R.string.aphost);

        ImageView buttonLoadImage = findViewById(R.id.pic_change);
        Button valider = findViewById(R.id.valider);
        post_name = findViewById(R.id.nam_post);

        category = findViewById(R.id.category_text);
        cat_lay = findViewById(R.id.cat_lay);
        cat_img = findViewById(R.id.cat_img);
        cat_name = findViewById(R.id.cat_name);

        category.setVisibility(View.GONE);
        cat_lay.setVisibility(View.VISIBLE);

        Intent i = getIntent();

        post_name.setText(i.getStringExtra("post_name"));
        nom = (i.getStringExtra("post_name"));
        ImageView imageView = findViewById(R.id.photup);
        photoChanged = false;


        cat_id = i.getIntExtra("cat_id",0);
        boolean is_picture = i.getBooleanExtra("is_picture",false);
        post_id = i.getIntExtra("post_id",0);
        cat_name.setText( i.getStringExtra("cat_name"));

        Glide.with(getApplicationContext())
                .load(host + "/" + i.getStringExtra("cat_pic")).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.london_flat)
                .placeholder(R.drawable.london_flat)
                .into(cat_img);

        if (is_picture)
        {
            imageView.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext())
                    .load(host + "/" + i.getStringExtra("thumbnail")).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .error(R.drawable.london_flat)
                    .placeholder(R.drawable.london_flat)
                    .into(imageView);
        }


        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFileChooser();
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


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = String.valueOf(post_name.getText());
                if (photoChanged)
                    new Services().UpdatePostActual(post_id,decoded, handler, nom, cat_id, getApplicationContext());
                else
                    new Services().UpdatePost(post_id, nom, cat_id, getApplicationContext());

            }
        });

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
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            } else {
              //  txtStatus.setText("Upload Error");
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

    public void onComplete(Category cat) {

      //  Toast.makeText(getApplicationContext()," "+cat.getName(),Toast.LENGTH_LONG).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ImageView imageView = findViewById(R.id.photup);

            Uri fileUri = data.getData();
            String selectedFilePath = getPath(fileUri);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {
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
                Toast.makeText(getApplicationContext()," "+taille,Toast.LENGTH_LONG).show();


                if(taille>=7000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=5800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=4800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=3800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    Toast.makeText(getApplicationContext()," 3 "+taille,Toast.LENGTH_LONG).show();
                }


                else if(taille>=3000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                       Toast.makeText(getApplicationContext()," 4 "+taille,Toast.LENGTH_LONG).show();

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

                photoChanged = true;

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
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

}

