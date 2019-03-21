package com.example.attendance;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;

public class Camera extends AppCompatActivity {

    private Bitmap bitmap;
    private Uri filepath;
    private ImageView ImageShow;
    private ImageView Buttonupd;
    private static final int IMG_REQUEST = 2;
    private static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Buttonupd = (ImageView) findViewById(R.id.uploadd);

        ImageShow = (ImageView) findViewById(R.id.ImageShow);

        Buttonupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        Bitmap selectedphoto = (Bitmap) this.getIntent().getParcelableExtra("data");
        ImageShow.setImageBitmap(selectedphoto);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap selectedphoto = null;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && data != null) {
            // get image from gallery and dispaly image on image view
            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                ImageShow.setImageBitmap(bitmap);
               // ImageShow.setVisibility(View.VISIBLE);
              //  NAME.setVisibility(View.VISIBLE);
                ImageShow.setImageBitmap(bitmap);
//                Uri selectedImage = data.getData();
//                String [] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String filePath = cursor.getString(columnIndex);
//                selectedphoto = BitmapFactory.decodeFile(filePath);
//                cursor.close();
//               Intent intent = new Intent(Camera.this,Camera.class);
//               intent.putExtra("data",selectedphoto);
//               startActivity(intent);


            } catch (IOException e) {
                e.printStackTrace();
            }

            // for camera request
        }
        else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ImageShow.setImageBitmap(bitmap);

            ImageShow.setImageBitmap(bitmap);



        }

//
//        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
//        ImageShow.setImageBitmap(bitmap);
//        ImageShow.setImageBitmap(bitmap);
//        ImageShow.setVisibility(View.VISIBLE);

    }

    public void post(){
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
}
