package com.clone.whatsapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.clone.whatsapp.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText countryCode;
    private EditText phoneNumber;
    private String[] countryNames = {"India", "France", "Germany", "Japan"};
    private int[] countryCodes = {91, 33, 49, 81};
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //setting controllers
        spinner = findViewById(R.id.login_spinner);
        countryCode = findViewById(R.id.login_countryCode);
        phoneNumber = findViewById(R.id.login_phoneNumber);
        button  = findViewById(R.id.login_sendSMS);


        //spinner adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //initialization
        countryCode.setText(countryCodes[spinner.getSelectedItemPosition()]+"");
        spinner.setOnItemSelectedListener(this);
        PushDownAnim.setPushDownAnimTo(button).setOnClickListener(this);


    }




    @Override
    public void onClick(View view) {
        if(countryCode.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || phoneNumber.getText().toString().length() <10){
            countryCode.setError("required");
            phoneNumber.setError("required");
        }else{
            Intent intent = new Intent(LoginActivity.this,VerificationActivity.class);
            intent.putExtra("phoneNumber",phoneNumber.getText().toString());
            intent.putExtra("countryCode",countryCode.getText().toString());
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        countryCode.setText(countryCodes[i]+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}