package com.example.attendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int IMG_REQUEST = 1;
    private Button UploadBn,ChooseBn;
   private Button Capture;
    private EditText NAME;
    private ImageView imgview;
    private Bitmap bitmap;
    private static final int CAM_REQUEST =2;

    String currentImagePath = null;

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.chooseBn: selectImage();

            break;

           case R.id.capture: captureImage();
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
            }
        }
    }

    private File getImageFile() throws IOException
    {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" +time+ "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            } else if (requestCode == CAM_REQUEST) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                imgview.setImageBitmap(bitmap);

            }
        }
    }


