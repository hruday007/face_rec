package com.example.attendance;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView imageview = (ImageView)findViewById(R.id.ImageShow);
        Bitmap selectedphoto  =(Bitmap)this.getIntent().getParcelableExtra("data");
        imageview.setImageBitmap(selectedphoto);
    }
}
