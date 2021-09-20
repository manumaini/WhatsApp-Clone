package com.clone.whatsapp.Presenters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingPresenter implements MainContract.SettingPresenter {

    private Context context;
    private MainContract.SettingActivity view;
    private final String TAG = "Setting presenter";

    public SettingPresenter(Context context, MainContract.SettingActivity view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void loadUserData() {
        view.showProgress("loading");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(SharedPrefsHelper.getInstance().getUserUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        view.setData(user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgress();
                view.onFailed(e.getMessage());
            }
        });


    }
}
