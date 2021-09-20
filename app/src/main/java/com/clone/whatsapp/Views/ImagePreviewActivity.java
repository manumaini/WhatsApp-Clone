package com.clone.whatsapp.Views;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Status;
import com.clone.whatsapp.Presenters.ImagePreviewPresenter;
import com.clone.whatsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class ImagePreviewActivity extends AppCompatActivity implements View.OnClickListener , MainContract.ImagePreviewView {

    private static final String TAG = "ImagePreview Activity";
    private ImageView imageView;
    private FloatingActionButton uploadStatus;
    private EditText caption;
    private ImageButton backButton;
    private ImagePreviewPresenter presenter ;
    private Uri uri;

    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        //controllers
        imageView = findViewById(R.id.PreView_ImageView);
        uploadStatus = findViewById(R.id.Preview_UploadButton);
        caption = findViewById(R.id.Preview_Caption);
        backButton = findViewById(R.id.Preview_BackButton);

        presenter = new ImagePreviewPresenter(this,this);

        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();

        PushDownAnim.setPushDownAnimTo(uploadStatus, backButton).setOnClickListener(this);
        loadImage();
    }

    private void loadImage() {
        uri = Uri.parse(getIntent().getStringExtra("ImageUri"));
        Log.d(TAG, "loadImage: " + uri);
        imageView.setImageURI(uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Preview_BackButton:
                finish();
                break;

            case R.id.Preview_UploadButton :
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String cap = caption.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String datetime = sdf.format(new Date());
                Log.d(TAG, "onClick: "+uri + " _ "+ datetime +" _ "+cap+" _ "+ mUser.getUid()+"_"+SharedPrefsHelper.getInstance().getUserName() );

                Status status = new Status(SharedPrefsHelper.getInstance().getUserName(),cap,datetime,mUser.getUid(),"");
                presenter.uploadThumbnail(status , uri);
                break;
        }

    }

    @Override
    public void onSuccess() {
        showToast("status uploaded");
        hideProgress();
        finish();
    }

    @Override
    public void onFailed(String message) {
        showToast(message);
        hideProgress();
    }

    @Override
    public void showProgress(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.hide();
    }

    private void showToast(String message){
        Toast.makeText(ImagePreviewActivity.this,message,Toast.LENGTH_LONG).show();
    }
}