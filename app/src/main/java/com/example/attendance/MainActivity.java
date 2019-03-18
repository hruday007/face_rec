package com.example.attendance;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.mime.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int IMG_REQUEST = 2;
    private Button UploadBn,ChooseBn;
    private Button Capture;
    private EditText NAME;
    private ImageView imgview;
    private Bitmap bitmap;
    private static final int CAM_REQUEST = 1;

    String currentImagePath = null;

    /***************/
    private static final String CSRF_PREFERENCE_KEY = "csrf_token";

//    protected static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookieStore;
    private static SharedPreferences preferences;
    private static String csrfToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadBn = (Button) findViewById(R.id.uploadBn);
        ChooseBn = (Button) findViewById(R.id.chooseBn);
        NAME = (EditText) findViewById(R.id.name);
        imgview = (ImageView) findViewById(R.id.imageview);
        Capture = (Button) findViewById(R.id.capture);


        ChooseBn.setOnClickListener(this);
        UploadBn.setOnClickListener(this);
        Capture.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.chooseBn: selectImage();

            break;

           case R.id.capture: captureImage();
           break;

            case R.id.uploadBn:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                post("test");
            break;
        }
    }
   //Select image from Gallery
    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

   //Camturing image from CAM
    public void captureImage(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getPackageManager())!=null)
        {
            File imageFile = null;

            try{
                imageFile = getImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            if (imageFile!=null)
            {
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,CAM_REQUEST);
                //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File getImageFile() throws IOException
    {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + time+ "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== RESULT_OK) {
            if (requestCode == IMG_REQUEST && data != null) {
                // get image from gallery and dispaly image on image view
                Uri path = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    imgview.setImageBitmap(bitmap);
                    imgview.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == CAM_REQUEST) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgview.setImageBitmap(bitmap);
                }catch (NullPointerException e){
                    Log.d("!!!!","ee code dengingi");
                }
            }

            else{}
        }
    }

    public void loopjRequest(){
//      String path = Environment.getExternalStorageDirectory().getPath();
//      String imagePath = path + "/Download/dog-best-friend-1.jpg";
        String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20190315_151357.jpg";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        cookieStore = new PersistentCookieStore(MainActivity.this);
//        client.setCookieStore(cookieStore);
//
//        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        csrfToken = preferences.getString(CSRF_PREFERENCE_KEY, null);
//        client.addHeader("X-CSRFToken", csrfToken);


//        params.put("text", "some string");
//        params.put("username", "admin");
//        params.put("pass", "anallstar");
////        try {
////            params.put("image", new File(imagePath));
////        }catch (Exception e){
////            Log.d("loopjTag", "Exception!!\n"+e.getMessage() );
////        }

        client.get("http://192.168.43.53:8383/token", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("loopjTag", "Failed!!!!!!\n" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {

                Log.d("loopjTagGet", "Success!!!!!\n");
                csrfToken = responseString;
                post(csrfToken);

            }
        });
    }

    public void loopjRequest2(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.43.53:8383/", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d("loopjReq2Method","Success!!");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("loopjReq2Method","Failed!!" + statusCode);
                Log.d("loopj2",responseBody.toString());


            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d("loopjReq2Method","Retrying!!");

            }
        });
    }

    public void post(String csrfToken){
        String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20190315_151357.jpg";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        client.addHeader("X-CSRFToken", csrfToken);
        try {
            params.put("image", new File(imagePath));
        }catch (Exception e){
            Log.d("loopjTag", "Exception!!\n"+e.getMessage() );
       }
       params.add("key","hush!It$ a seCreT");
//        params.put("csrfmiddlewaretoken","uUQp3O4sc621d0f5CxsazKUBFxAjCOWvGNRvwlF1HqW7b2IzqugIShxxf57JWgBh");
        client.post("http://192.168.1.119:8383/attendance/android", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("loopjTagPost", "Failed!!!!!!\n" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("loopjTagPost", "Success!!!!!\n" + responseString);
            }
        });
    }
}


