package com.example.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;

public class Camera extends AppCompatActivity {

    private Bitmap bitmap;
    private Uri filepath;
    private ImageView ImageShow;
//    private ImageView Buttonupd;
    private Button Buttonupd;
    private static final int IMG_REQUEST = 2;
    private static final int CAM_REQUEST = 1;
    private ProgressBar spinner;
    int ImageChoice;
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Buttonupd = findViewById(R.id.uploadBtn);

        ImageShow = (ImageView) findViewById(R.id.ImageShow);

        spinner = findViewById(R.id.indeterminateBar);
        spinner.setVisibility(View.GONE);

        Buttonupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        Bitmap selectedphoto = (Bitmap) this.getIntent().getParcelableExtra("data");
        ImageShow.setImageBitmap(selectedphoto);

        /*****Retrieving information from previous Activity********/
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ImageChoice = 0;
            } else {
                ImageChoice= extras.getInt("ImageChoice");
            }
        } else {
            ImageChoice = (int) savedInstanceState.getSerializable("ImageChoice");
        }
        /****************/

        if (ImageChoice == 1) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMG_REQUEST);
        }

        else if (ImageChoice == 2) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(cameraIntent.resolveActivity(getPackageManager())!=null)
            {
              File imageFile = null;

                try{
                    imageFile = getImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(imageFile != null)
                {
                    Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(cameraIntent,CAM_REQUEST);
                    Log.d("ifTag","Reached" + MediaStore.EXTRA_OUTPUT);

                }
            }
        }

        else{ Log.d("imageChoiceTag","Something's Wrong"); }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap selectedphoto = null;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && data != null) {
            // get image from gallery and dispaly image on image view
            filepath = data.getData();
            currentImagePath = getPath(filepath);
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                ImageShow.setImageBitmap(bitmap);
                ImageShow.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // for camera request
        }
        else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
                try {
                    filepath = Uri.fromFile(new File(currentImagePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                }catch (Exception e) { Log.d("TAG1","" + e.toString()); }
                ImageShow.setImageBitmap(bitmap);
                ImageShow.setVisibility(View.VISIBLE);
                Log.d("TAGATA","YEP");

        }
    }

    public void post(){
        /****Alert Messages****/

        final AlertDialog.Builder message = new AlertDialog.Builder(Camera.this);
        message.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        message.setCancelable(true);
        /*******************/


        spinner.setVisibility(View.VISIBLE); // Making Progress Bar visible
        //  String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20190315_151357.jpg";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String path = currentImagePath;
        Log.d("postTag","PAth:" + path);
        try {
            params.put("image", new File(path));
        }catch (Exception e){
            Log.d("loopjTag", "Exception!!\n"+e.getMessage() );
        }
        params.add("key","hush!It$ a seCreT");
        params.add("subject_name", "OS");
        client.post("http://192.168.1.119:8383/attendance/android", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("loopjTagPost", "Failed!!!!!!\n" + responseString);
                spinner.setVisibility(View.GONE);
                if (statusCode == 500)
                    message.setMessage("No faces found in the image.Please try again.");
                else {
                    message.setMessage("Image could not be posted");
                    message.setNegativeButton(
                            "Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    post();
                                }
                            });
                }
                AlertDialog alert = message.create();
                alert.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("loopjTagPost", "Success!!!!!\n" + responseString);
                spinner.setVisibility(View.GONE);
                message.setMessage("Image was posted successfully");
                AlertDialog alert = message.create();
                alert.show();
            }
        });
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        //   cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ?", new String[]{document_id}, null);

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;


    }

    private File getImageFile() throws IOException
    {
        //  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        Log.d("getImageFileMethodTag", "Path" + currentImagePath);
        return imageFile;
    }
}
