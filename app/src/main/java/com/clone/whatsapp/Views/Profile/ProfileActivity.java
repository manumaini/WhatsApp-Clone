package com.clone.whatsapp.Views.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.User;
import com.clone.whatsapp.Presenters.ProfilePresenter;
import com.clone.whatsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, MainContract.ProfileActivity , BottomFragment.BottomSheetCallbacks , BottomFragmentImagePicker.Callbacks {

    private TextView userName;
    private TextView userDesc;
    private TextView userPhone;
    private ImageView userNameButton;
    private ImageView userDescButton;
    private CircleImageView userImage;
    private boolean isName;
    private ProfilePresenter presenter;
    private Toolbar toolbar;
    private final String TAG ="Profile Activity";
    private FloatingActionButton pickImage;
    private Uri image_uri;

    private AlertDialog dialog;
    public static final int PICK_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //setting controllers
        userName = findViewById(R.id.Profile_username);
        userDesc = findViewById(R.id.Profile_UserDesc);
        userPhone = findViewById(R.id.Profile_userPhone);
        userNameButton = findViewById(R.id.Profile_ChangeUsername);
        userDescButton = findViewById(R.id.Profile_ChangeUserDesc);
        userImage = findViewById(R.id.Profile_userImage);
        toolbar = findViewById(R.id.Profile_toolbar);
        pickImage = findViewById(R.id.Profile_ImagePickButton);
        setSupportActionBar(toolbar);

        //initialization
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userDesc.setText(getIntent().getStringExtra("UserDesc"));
        userName.setText(getIntent().getStringExtra("UserName"));
        presenter = new ProfilePresenter(this,this);
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();
        Glide.with(this).load(getIntent().getStringExtra("UserImage")).into(userImage);



        //listeners
        PushDownAnim.setPushDownAnimTo(userDescButton, userNameButton, pickImage).setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        BottomFragment bottomFragment;
        switch (view.getId()) {
            case R.id.Profile_ChangeUsername:
                isName = true;
                bottomFragment= BottomFragment.getInstance(this,"Enter the name");
                bottomFragment.show(getSupportFragmentManager(),BottomFragment.TAG);
                break;
            case R.id.Profile_ChangeUserDesc:
                isName = false;
                bottomFragment = BottomFragment.getInstance(this,"Enter the description");
                bottomFragment.show(getSupportFragmentManager(),BottomFragment.TAG);
                break;
            case R.id.Profile_ImagePickButton:
                BottomFragmentImagePicker picker = BottomFragmentImagePicker.getInstance(this);
                picker.show(getSupportFragmentManager(),BottomFragmentImagePicker.TAG);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: in here too");
            if (data != null) {
                image_uri = data.getData();
                presenter.UpdateImage(image_uri,getIntent().getStringExtra("UserImage"));
            }else{
                onFailed("Unexpected error occurred while picking the image");
            }
        }
    }

    //after updates starts
    @Override
    public void onSuccess() {

    }

    @Override
    public void onImageRemoved() {
        userImage.setImageBitmap(null);
    }

    @Override
    public void onImageUpdated(String url) {
        Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show();
        Glide.with(this).load(url).into(userImage);

    }


    @Override
    public void onNameChanged(String name) {
        Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show();
        userName.setText(name);
    }

    @Override
    public void onDescChanged(String desc) {
        Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show();
        userDesc.setText(desc);
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //after updates ends

    //loading starts

    @Override
    public void showProgress(String message) {
        dialog.setMessage(message);
        dialog.show();

    }

    @Override
    public void hideProgress() {
        dialog.hide();
    }

    //loading ends

    //view methods starts

    @Override
    public void setData(User user) {
        userName.setText(user.getUserName());
        userDesc.setText(user.getBio());
        userPhone.setText(user.getUserPhone());
        Glide.with(this).load(user.getImageUrl()).into(userImage);
    }

    @Override
    public void onChanged(String change) {
            if(isName){
                presenter.UpdateName(change);
            }else{
                //desc
                presenter.UpdateDesc(change);
            }
    }

    @Override
    public void onClickImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onClickRemoveImage() {

        presenter.RemoveImage(getIntent().getStringExtra("UserImage"));
    }

    //view methods ends
}