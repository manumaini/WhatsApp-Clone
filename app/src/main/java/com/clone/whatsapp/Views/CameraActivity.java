package com.clone.whatsapp.Views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clone.whatsapp.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CameraActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private Camera camera;
    private Preview preview;
    private ImageCapture imageCapture;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"};
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private CameraControl cameraControl;
    private ImageButton captureButton;

    private final String DIRETORY_NOT_CREATED = "NO";


    //controllers
    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        screenInit();

        //controllers
        previewView = findViewById(R.id.CameraPreview);
        captureButton = findViewById(R.id.CameraButton);

        previewView.setOnTouchListener(this);
        PushDownAnim.setPushDownAnimTo(captureButton).setOnClickListener(this);


        if (allPermissionGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(CameraActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    private void startCamera() {
        ListenableFuture cameraProviderFuture = ProcessCameraProvider.getInstance(CameraActivity.this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    imageCapture = new ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .build();

                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();

                    ImageCapture.Builder builder = new ImageCapture.Builder();

                    //hdr effect
                    HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
                    if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
                        hdrImageCaptureExtender.enableExtension(cameraSelector);
                    }

                    cameraProvider.unbindAll();
                    camera = cameraProvider.bindToLifecycle(CameraActivity.this, cameraSelector, preview, imageCapture);

                    cameraControl = camera.getCameraControl();

                } catch (InterruptedException | ExecutionException e) {

                }

            }
        }, ContextCompat.getMainExecutor(CameraActivity.this));
    }

    private boolean allPermissionGranted() {
        for (String requestPermission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, requestPermission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera();
            } else {
                Toast.makeText(CameraActivity.this, "Requested Permissions are Required for proper functioning", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void screenInit() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        MeteringPointFactory factory = previewView.getMeteringPointFactory();
        MeteringPoint point = factory.createPoint(motionEvent.getX(), motionEvent.getY());
        FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                .setAutoCancelDuration(5, TimeUnit.SECONDS)
                .build();
        cameraControl.startFocusAndMetering(action);


        return true;
    }

    private String getDirectoryName(){
        String path;
        if(Build.VERSION.SDK_INT >= 29){
            path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }else{
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/camera";
        }

        File dir = new File(path);
        if(!dir.exists() && !dir.mkdirs()){
            return DIRETORY_NOT_CREATED;
        }

        return path;
    }

    private void showToast(String message){
        Toast.makeText(CameraActivity.this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.CameraButton:
                File file = new File(getDirectoryName(),"WhatsAppClone-"+System.currentTimeMillis()+".jpg");
                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        showToast("imageCaptured");
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent(CameraActivity.this,ImagePreviewActivity.class);
                        intent.putExtra("ImageUri",uri.toString());
                        startActivity(intent);
                        //finish();

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        showToast(exception.getLocalizedMessage());

                    }
                });


                break;
        }
    }
}