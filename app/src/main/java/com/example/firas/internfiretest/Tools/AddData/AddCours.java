package com.example.firas.internfiretest.Tools.AddData;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firas.internfiretest.Accueil.AccueilActivity;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;


public class AddCours extends Activity {


    public static final String TAG = "Upload Image";


    EditText descri;


    String nom;

    String host;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ImageView file_pic;
    TextView file_path;

    String filePath;

    private static final int FILE_SELECT_CODE = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cours);


        host = getString(R.string.aphost);

        Button buttonLoadImage = (Button) findViewById(R.id.load_pic);
        Button valider = (Button) findViewById(R.id.valider);
        descri = (EditText) findViewById(R.id.descri);


        file_pic = (ImageView) findViewById(R.id.file_pic);
        file_path = (TextView) findViewById(R.id.path);


        // nom = new Services().GenerateString();

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFileChooser();
            }
        });

        verifyStoragePermissions(AddCours.this);


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = String.valueOf(descri.getText());
                new Services().AddCours(filePath, handler, nom, getApplicationContext());

            }
        });

    }


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
              //  txtStatus.setText("Upload Error");
                Toast.makeText(getApplicationContext(),"Upload Error",Toast.LENGTH_LONG).show();
            }
        }

    };

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path

                    filePath = getPath(getApplicationContext(),uri);

/*                    file_path.setVisibility(View.VISIBLE);
                    file_pic.setVisibility(View.VISIBLE);
*/
                    Log.d(TAG, "File Path: " + filePath);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) {
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);}


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



}

