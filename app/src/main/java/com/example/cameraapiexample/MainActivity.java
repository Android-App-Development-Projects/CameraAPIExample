package com.example.cameraapiexample;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mLinearLayout;
    ImageView imageView;
    Button openCamBtn;


    public static final int MY_CAMERA_PERMISSION_REQUEST = 1001;


    private static final int CAMERA_API_REQUEST = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = findViewById(R.id.linearLayout);
        imageView = findViewById(R.id.image_view);
        openCamBtn = findViewById(R.id.open_camera);



        openCamBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED ) {
            // You can use the API that requires the permission.
            openCamera();
        }

        // Should we show an explanation?
        else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showAlert();
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, MY_CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MY_CAMERA_PERMISSION_REQUEST:
            {
                if(grantResults.length > 0){
                    boolean cameraPermissionDenied = grantResults[0] == PackageManager.PERMISSION_DENIED;

                    if(cameraPermissionDenied){
                        Snackbar.make(mLinearLayout, "Camera Permission Denied. Can't Open Camera", Snackbar.LENGTH_LONG).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        openCamera();
                    }
                }
            }
        }
    }


    private void openCamera() {
        //This method will open camera. Write code for the same
        Intent cameraApiIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(cameraApiIntent, CAMERA_API_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_API_REQUEST)
        {
            Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photoBitmap);
        }
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs permission to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Snackbar.make(mLinearLayout, "You won't be able to use Camera as you have denied permission", Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_REQUEST);
                    }
                });
        alertDialog.show();
    }
}