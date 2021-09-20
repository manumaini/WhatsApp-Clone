package com.clone.whatsapp.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Models.Status;
import com.clone.whatsapp.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class StatusViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="StausViewActivity" ;
    private ImageView imageView;
    private ImageButton backButton;
    private ImageButton prevStatus;
    private ImageButton nextStatus;
    private TextView caption;
    private TextView userName;
    private TextView timeStamp;
    private ArrayList<Status> list;
    private int count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_view);

        //controllers
        imageView = findViewById(R.id.StatusView_ImageView);
        backButton = findViewById(R.id.StatusView_BackButton);
        prevStatus = findViewById(R.id.StatusView_prevButton);
        nextStatus = findViewById(R.id.StatusView_nextButton);
        caption = findViewById(R.id.StatusView_caption);
        userName = findViewById(R.id.StatusView_UserName);
        timeStamp = findViewById(R.id.StatusView_timeStamp);

        getData();
        if(count == 0) prevStatus.setVisibility(View.GONE);
        userName.setText(list.get(count).getName());
        timeStamp.setText(list.get(count).getDateTime());
        caption.setText(list.get(count).getCaption());
        Glide.with(this).load(list.get(count).getThumbnailUrl()).into(imageView);

        PushDownAnim.setPushDownAnimTo(prevStatus,nextStatus,backButton).setOnClickListener(this);

    }

    private void getData() {

        list = new ArrayList<Status>();
        list = (ArrayList<Status>) getIntent().getSerializableExtra("StatusList");
        Log.d(TAG, "getData: "+list.size());
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.StatusView_prevButton:
                count--;
                userName.setText(list.get(count).getName());
                timeStamp.setText(list.get(count).getDateTime());
                caption.setText(list.get(count).getCaption());
                Glide.with(this).load(list.get(count).getThumbnailUrl()).into(imageView);
                break;
            case R.id.StatusView_nextButton:
                count++;
                userName.setText(list.get(count).getName());
                timeStamp.setText(list.get(count).getDateTime());
                caption.setText(list.get(count).getCaption());
                Glide.with(this).load(list.get(count).getThumbnailUrl()).into(imageView);
                break;
            case R.id.StatusView_BackButton:
                finish();
                break;
        }

        if (count == 0) {
            prevStatus.setVisibility(View.GONE);
        }else if(count == list.size()-1){
            nextStatus.setVisibility(View.GONE);
        }else{
            prevStatus.setVisibility(View.VISIBLE);
            nextStatus.setVisibility(View.VISIBLE);
        }
    }
}