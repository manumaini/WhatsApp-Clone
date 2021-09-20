package com.clone.whatsapp.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Presenters.UserDetailsPresenter;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.Main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class UserdetailsActivity extends AppCompatActivity implements View.OnClickListener, MainContract.UserDetailsActivity {

    private CircleImageView profile_image;
    private FloatingActionButton add_profile;
    private EditText user_name;
    private EditText user_desc;
    private Uri image_uri;
    private UserDetailsPresenter presenter;
    private AlertDialog dialog;
    private Button button;


    //static reference
    public static final int PICK_IMAGE = 1;
    private final String TAG = "Userdetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);


        //setting controllers
        profile_image = findViewById(R.id.UserDetails_profile_img);
        add_profile = findViewById(R.id.UserDetails_profile_pick);
        user_name = findViewById(R.id.UserDetails_userName);
        user_desc = findViewById(R.id.UserDetails_profile_desc);
        button = findViewById(R.id.UserDetails_button);

        //initialization
        presenter = new UserDetailsPresenter(this, this);
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();

        PushDownAnim.setPushDownAnimTo(add_profile,button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserDetails_profile_pick:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;

            case R.id.UserDetails_button:
                if (user_name.getText().toString().isEmpty()) {
                    user_name.setError("Required field");
                } else {
                    presenter.Continue(image_uri, user_name.getText().toString(), user_desc.getText().toString());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: in here"+ requestCode + " "+ resultCode);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: in here too");
            if (data != null) {
                image_uri = data.getData();
                Log.d(TAG, "onActivityResult: "+ image_uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);

                    profile_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this,"Updated Successfully",Toast.LENGTH_LONG).show();
        startActivity(new Intent(UserdetailsActivity.this, MainActivity.class));
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
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
}