package com.clone.whatsapp.Views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.User;
import com.clone.whatsapp.Presenters.SettingPresenter;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.Profile.ProfileActivity;
import com.thekhaeng.pushdownanim.PushDownAnim;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, MainContract.SettingActivity {

    private LinearLayout profileLayout;
    private AlertDialog dialog;
    private CircleImageView user_image;
    private TextView user_name;
    private TextView user_desc;
    private User user;

    //presenter
    private SettingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //setting controllers
        profileLayout = findViewById(R.id.Setting_profileLayout);
        user_image = findViewById(R.id.Settings_userImage);
        user_name = findViewById(R.id.Settings_userName);
        user_desc = findViewById(R.id.Settings_userDesc);


        //initialization
        presenter = new SettingPresenter(this,this);
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();



        //listeners
        PushDownAnim.setPushDownAnimTo(profileLayout).setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadUserData();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.Setting_profileLayout:
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                intent.putExtra("UserName",user.getUserName());
                intent.putExtra("UserDesc",user.getBio());
                intent.putExtra("UserImage",user.getImageUrl());
                startActivity(intent);
                break;


        }

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed(String message) {

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

    @Override
    public void setData(User user) {
        this.user = user;
        user_name.setText(user.getUserName());
        user_desc.setText(user.getBio());
        if(user.getImageUrl() != null) Glide.with(this).load(user.getImageUrl()).into(user_image);
        hideProgress();

    }
}