package com.example.attendance;


import android.content.Intent;


import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;


public class LoginActivity extends AppCompatActivity  {

    private static final int CAM_REQUEST =2;

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

               openActivity();


            }
        });
    }

    private void openActivity() {
       Intent intent = new Intent(this, MainActivity.class);
     //   Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // if(cameraIntent.resolveActivity(getPackageManager())!=null) {
         //   startActivityForResult(cameraIntent, CAM_REQUEST);
             startActivity(intent);
        }

}