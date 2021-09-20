package com.clone.whatsapp.Views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView phone_number;

    private TextView verification_title;
    private TextView timer;
    private LinearLayout resend;
    private Button button;
    private PinView pinView;

    private String phoneNumber;
    private String countryCode;
    private String userID;
    private String phoneID;
    private String TAG = "Verification Activity";
    private AlertDialog dialog;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private boolean canSend=false;
    private Button Continue;


    //verification
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //setting controllers
        phone_number = findViewById(R.id.verification_phone);
        verification_title = findViewById(R.id.verification_title);
        button = findViewById(R.id.verification_button);
        pinView = findViewById(R.id.PinView);
        resend = findViewById(R.id.verification_resend);
        timer = findViewById(R.id.verification_timer);


        //initialization
        Intent intent = getIntent();
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();
        phoneNumber = intent.getStringExtra("phoneNumber");
        countryCode = intent.getStringExtra("countryCode");
        phone_number.setText("+" + countryCode + " " + phoneNumber);
        phoneID = "+" + countryCode + phoneNumber;
        verification_title.setText("Verify +" + countryCode + " " + phoneNumber);
        PushDownAnim.setPushDownAnimTo(button,resend).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;

                String pinCode = phoneAuthCredential.getSmsCode();
                pinView.setText(pinCode);
                hideProgressBar();
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent: " + verificationID);
                mVerificationId = verificationID;
                mResendToken = forceResendingToken;
                hideProgressBar();
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        //starting verification
        startPhoneNumberVerification(phoneID);

    }

    private void startPhoneNumberVerification(String phoneID) {
        countTimer();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneID, 30, TimeUnit.SECONDS, this, mCallbacks);
        mVerificationInProgress = true;
        showProgressBar("sending SMS...");
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        canSend = false;
        countTimer();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 30, TimeUnit.SECONDS, this, mCallbacks, token);
        showProgressBar("sending SMS...");
    }

    public void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgressBar("Signing In...");
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressBar();
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    userID = user.getUid();
                    SharedPrefsHelper.getInstance().setUserUid(userID);
                    SharedPrefsHelper.getInstance().setUserPhone(user.getPhoneNumber());
                    onSuccess();

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(VerificationActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                        onFailed("Invalid code");
                    }else{
                        onFailed("Signing Error");
                    }
                }
            }
        });
    }

    void countTimer(){
        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long l) {
                timer.setText("00:"+l/1000);
            }

            @Override
            public void onFinish() {
                canSend = true;
                hideProgressBar();
            }
        }.start();
    }


    public void showProgressBar(String message) {
        dialog.setMessage(message);
        dialog.show();
    }


    public void hideProgressBar() {
        dialog.hide();
    }


    public void onSuccess() {
        ShowDialog();


    }

    private void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
        View dialog_view = LayoutInflater.from(VerificationActivity.this).inflate(R.layout.verification_successful_dialog,null);
        builder.setView(dialog_view);
        Continue = dialog_view.findViewById(R.id.Dialog_button_continue);
        Continue.setOnClickListener(this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.verification_button:
                if (pinView.getText().toString().length() < 6) {
                    Log.d(TAG, "onClick: " + pinView.getText().toString());
                    pinView.setError("required");
                } else {
                    Log.d(TAG, "onClick: " + pinView.getText().toString());
                    verifyPhoneNumberWithCode(mVerificationId, pinView.getText().toString());
                }
                break;
            case R.id.verification_resend:
                if(canSend){
                    resendVerificationCode(phoneID,mResendToken);
                }
                break;
            case R.id.Dialog_button_continue:
                Intent intent = new Intent(VerificationActivity.this, UserdetailsActivity.class);
                startActivity(intent);
                break;
        }


    }


}