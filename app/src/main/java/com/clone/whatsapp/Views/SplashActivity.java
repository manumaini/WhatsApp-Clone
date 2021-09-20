package com.clone.whatsapp.Views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
        setContentView(R.layout.activity_splash);

        button = findViewById(R.id.Splash_continue);
        PushDownAnim.setPushDownAnimTo(button).setOnClickListener(this);

        //initialization
        SharedPrefsHelper.initialize(getApplicationContext());

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Splash_continue:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    break;
                }


        }
    }
}