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

public class MainActivity extends AppCompatActivity {

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

    private static final String UPLOAD_URL = "http://192.168.56.1:8383/attendance/android";

    String currentImagePath = null;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


        //image as button
//         Buttonimg = (ImageView) findViewById(R.id.image);
//         Buttoncam = (ImageView) findViewById(R.id.cam);
//        Buttonupd = (ImageView) findViewById(R.id.uploadd);
        Button Buttonimg = findViewById(R.id.image);
        Button Buttoncam = findViewById(R.id.cam);
        intent = new Intent(this, Camera.class);
        //Images from Gallery
        Buttonimg.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  intent.putExtra("ImageChoice",1);
                  startActivity(intent);

              }
          });

        //Image from Camera
        Buttoncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("ImageChoice", 2);
                startActivity(intent);

            }
        });


    }
}