package com.clone.whatsapp.Presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Contact;
import com.clone.whatsapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactPresenter implements MainContract.ContactPresenter {

    private Context context ;
    private MainContract.ContactView view;
    private ArrayList<Contact> list;
    private final String TAG = "Contact Presenter";

    public ContactPresenter(Context context, MainContract.ContactView view) {
        this.context = context;
        this.view = view;
        list = new ArrayList<>();
    }

    @Override
    public Void loadContacts() {
        view.showProgress("Loading...");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: "+ task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);

                        Log.d(TAG, "onComplete: "+user.getUserID().length()+"   "+SharedPrefsHelper.getInstance().getUserUid().length());

                        if(!user.getUserID().equals(SharedPrefsHelper.getInstance().getUserUid())){
                            list.add(new Contact(user.getUserName(),user.getBio(),user.getImageUrl(),user.getUserID()));
                        }

                    }
                    view.hideProgress();
                    view.onSuccess(list);


                }else{
                    view.hideProgress();
                    view.onFailed(task.getException().getLocalizedMessage());
                }

            }
        });
        return null;
    }
}
