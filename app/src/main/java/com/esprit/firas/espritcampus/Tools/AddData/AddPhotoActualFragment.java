package com.esprit.firas.espritcampus.Tools.AddData;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class AddPhotoActualFragment extends Fragment  {


    public static final String TAG = "Upload Image";

    private int PICK_IMAGE_REQUEST = 100;

    EditText name_post;

    String nom;

    String host;
    Bitmap decoded;

    View view;

    int cat_id;
    TextView category;

    RelativeLayout cat_lay;
    TextView cat_name;
    CircleImageView cat_img;

    boolean isPicture;

    public static final int POST_CAT_FRAGMENT = 1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_publication, container, false);


        host = getString(R.string.aphost);

        ImageView buttonLoadImage = view.findViewById(R.id.pic_change);
        Button valider = view.findViewById(R.id.valider);
        name_post = view.findViewById(R.id.nam_post);

        category = view.findViewById(R.id.category_text);
        cat_lay = view.findViewById(R.id.cat_lay);
        cat_img = view.findViewById(R.id.cat_img);
        cat_name = view.findViewById(R.id.cat_name);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFileChooser();
            }
        });

        final Fragment frag = this;

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_pub/");
                CategoryDialogFragment catfragment = new CategoryDialogFragment();
                catfragment.setArguments(catargs);

                catfragment.setTargetFragment(frag, POST_CAT_FRAGMENT);
                catfragment.show(fm,"abc");
            }
        });


        cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();

                Bundle catargs = new Bundle();
                catargs.putString("url", "/api/categories_pub/");
                CategoryDialogFragment catfragment = new CategoryDialogFragment();
                catfragment.setArguments(catargs);
                catfragment.setTargetFragment(frag, POST_CAT_FRAGMENT);
                catfragment.show(fm,"abc");
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nom = String.valueOf(name_post.getText());
                if (isPicture)
                {
                    new Services().AddPostActual(decoded, handler, nom, cat_id, view.getContext());
                }
                else
                {
                    new Services().AddPostNoPic(handler,nom, cat_id, view.getContext());
                }
            }
        });

        return view;
    }


    @SuppressLint("HandlerLeak")
    Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
               // txtStatus.setText("Upload Success");
         //       Toast.makeText(view.getContext(),"Upload Success",Toast.LENGTH_LONG).show();

                Intent i = new Intent(view.getContext(), AccueilActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            } else {
              //  txtStatus.setText("Upload Error");
                Toast.makeText(view.getContext(),"Upload Error",Toast.LENGTH_LONG).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == POST_CAT_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // here the part where I get my selected date from the saved variable in the intent and the displaying it.
                Bundle bundle = data.getExtras();
                assert bundle != null;
                String name = bundle.getString("cat_name", "error");
                String pic = bundle.getString("cat_img", "error");
                int id = bundle.getInt("cat_id", 0);


             //   Toast.makeText(view.getContext(), " " + post_name, Toast.LENGTH_LONG).show();

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
                long taille = file.length()/1024;


              //  int taille = bitmap.getByteCount()/1024;
               // int taille = (bitmap.getRowBytes() * bitmap.getHeight())/1024;
//                Toast.makeText(view.getContext()," "+taille,Toast.LENGTH_LONG).show();


                if(taille>=7000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
  //                  Toast.makeText(view.getContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=5800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
     //               Toast.makeText(view.getContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=4800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
    //                Toast.makeText(view.getContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=3800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
   //                 Toast.makeText(view.getContext()," 3 "+taille,Toast.LENGTH_LONG).show();
                }


                else if(taille>=3000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
   //                    Toast.makeText(view.getContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=2000) {

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
   //                 Toast.makeText(view.getContext()," 5 "+taille,Toast.LENGTH_LONG).show();

                }


                else if(taille>=1000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
   //                    Toast.makeText(view.getContext()," 6 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=500) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
  //                     Toast.makeText(view.getContext()," 7 "+taille,Toast.LENGTH_LONG).show();
                }

                else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
   //                 Toast.makeText(view.getContext()," 5 "+taille,Toast.LENGTH_LONG).show();
                }

                imageView.setVisibility(View.VISIBLE);
                isPicture = true;

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
}

