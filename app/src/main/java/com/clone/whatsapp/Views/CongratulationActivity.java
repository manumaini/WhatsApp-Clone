package com.clone.whatsapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.clone.whatsapp.R;

public class CongratulationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);

        //setting controllers
        button = findViewById(R.id.congratulation_button);

        //initialization
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.congratulation_button:
                startActivity(new Intent(CongratulationActivity.this,UserdetailsActivity.class));
                break;
        }
    }
}