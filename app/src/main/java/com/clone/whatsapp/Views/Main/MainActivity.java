package com.clone.whatsapp.Views.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.clone.whatsapp.Adapters.MainSectionAdapter;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.CameraActivity;
import com.clone.whatsapp.Views.ContactActivity;
import com.clone.whatsapp.Views.SettingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private int tabPosition;
    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting controllers
        viewPager = findViewById(R.id.main_viewPager);
        tabLayout = findViewById(R.id.main_tabLayout);
        floatingActionButton = findViewById(R.id.main_floatingButton);
        toolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);


        setUpViewpager();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        PushDownAnim.setPushDownAnimTo(floatingActionButton).setOnClickListener(this);

    }

    private void setUpViewpager() {
        MainSectionAdapter sectionAdapter = new MainSectionAdapter(getSupportFragmentManager());
        sectionAdapter.addFragment(new ChatFragment(this), "Chat");
        sectionAdapter.addFragment(new StatusFragment(this), "Status");
        sectionAdapter.addFragment(new CallFragment(this), "Call");
        viewPager.setAdapter(sectionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainmenu_search:
                break;
            case R.id.mainmenu_newGroup:
                break;
            case R.id.mainmenu_newBroadcast:
                break;
            case R.id.mainmenu_whatsAppWeb:
                break;
            case R.id.mainmenu_starredMessage:
                break;
            case R.id.mainmenu_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabPosition = position;
        Log.d(TAG, "onPageSelected: " + position);
        floatingActionButton.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                switch (tabPosition) {
                    case 0:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_message));
                        floatingActionButton.show();
                        break;
                    case 1:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_camerao));
                        floatingActionButton.show();
                        break;
                    case 2:
                        break;
                }


            }
        }, 100);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

        if (tabPosition == 0) {
            //start contact activity
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (tabPosition == 1) {
            //start pick for gallery or camera
            Intent intent = new Intent(MainActivity.this , CameraActivity.class);
            startActivity(intent);
        } else {

        }

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
}