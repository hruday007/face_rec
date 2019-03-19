package com.example.attendance;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int IMG_REQUEST = 2;
    private Button UploadBn,ChooseBn;
   private Button Capture;
    private EditText NAME;
    private ImageView imgview;
    private Uri filepath;

    //image as button
   private ImageView Buttonimg;
    private ImageView Buttoncam;
    private ImageView Buttonupd;

    private Bitmap bitmap;
    private static final int CAM_REQUEST = 1;

    private static final String UPLOAD_URL = "http://192.168.56.1:8383";

    String currentImagePath = null;



    private static final String CSRF_PREFERENCE_KEY = "csrf_token";

    //    protected static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookieStore;
    private static SharedPreferences preferences;
    private static String csrfToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UploadBn = (Button) findViewById(R.id.uploadBn);
      //  ChooseBn = (Button) findViewById(R.id.chooseBn);
       // NAME = (EditText) findViewById(R.id.name);
    //    imgview = (ImageView) findViewById(R.id.imageview);
    //    Capture = (Button) findViewById(R.id.capture);



//        ChooseBn.setOnClickListener(this);
//        UploadBn.setOnClickListener(this);
//       Capture.setOnClickListener(this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        //image as button
         Buttonimg = (ImageView) findViewById(R.id.image);
         Buttoncam = (ImageView) findViewById(R.id.cam);
       //  Buttonupd = (ImageView) findViewById(R.id.uploadd);

          Buttonimg.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  selectImage();

              }
          });


        Buttoncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();

            }
        });


      /*  Buttonupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                post("test");

            }
        }); */


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


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
        //    case R.id.chooseBn: selectImage();

          //  break;

         //  case R.id.capture: captureImage();
          // break;

         //  case R.id.uploadBn :
           //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
             //  post("test");

           //break;
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
        //String path = getPath(filepath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if(cameraIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(cameraIntent,CAM_REQUEST);
        //    Log.d("postTag","PAth:" + path);
       /*   File imageFile = null;

            try{
                imageFile = getImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            if(imageFile!=null)
            {
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,CAM_REQUEST);
            } */
        }
     }


    private File getImageFile() throws IOException
    {
      //  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        Log.d("getImageFileMethod", "Path" + currentImagePath);
        return imageFile;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        Bitmap selectedphoto   = null;
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap bitmap = (Bitmap) extras.get("data");
//            imgview.setImageBitmap(bitmap);
//
//
//        }

        if(resultCode== RESULT_OK) {

            if (requestCode == IMG_REQUEST && data != null) {
                // get image from gallery and dispaly image on image view
                 filepath = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    imgview.setImageBitmap(bitmap);
                    imgview.setVisibility(View.VISIBLE);
                    NAME.setVisibility(View.VISIBLE);

                    Uri selectedImage = data.getData();
                    String [] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    selectedphoto = BitmapFactory.decodeFile(filePath);
                   // cursor.close();
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("data",selectedphoto);
                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                // for camera request
            } else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                imgview.setImageBitmap(bitmap);
                imgview.setVisibility(View.VISIBLE);

            }
        }
    }


/*
   public void uploadImg()
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(MainActivity.this,Response,Toast.LENGTH_LONG).show();
                            imgview.setImageResource(0);
                            imgview.setVisibility(View.GONE);
                            NAME.setText("");
                            NAME.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name",NAME.getText().toString().trim());
                params.put("image",imageToString(bitmap));
                return params;
            }
        };
        singleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    } */


    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }




    public void post(String csrfToken){
      //  String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20190315_151357.jpg";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String path = getPath(filepath);
        Log.d("postTag","PAth:" + path);
//        client.addHeader("X-CSRFToken", csrfToken);
        try {
     //       params.put("image", new File(imagePath));
            params.put("image",path);
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