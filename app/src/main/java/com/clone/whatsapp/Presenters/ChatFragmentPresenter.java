package com.clone.whatsapp.Presenters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ChatFragmentPresenter implements MainContract.ChatFragmentPresenter {

    private Context context;
    private MainContract.ChatFragmentView view;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private final static String TAG = "ChatFragment";

    private ArrayList<User> list;
    private ArrayList<String> allUserId;
    private Handler mHandler;

    public ChatFragmentPresenter(Context context, MainContract.ChatFragmentView view) {
        this.context = context;
        this.view = view;
        list = new ArrayList<>();
        allUserId = new ArrayList<>();

    }

    @Override
    public void readContacts() {
        try {
            view.showProgress("loading");
            mAuth = FirebaseAuth.getInstance();
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("ChatList").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    allUserId.clear();
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: " + snapshot.getKey());
                        allUserId.add(snapshot.getKey());
                    }
                    Log.d(TAG, "onDataChange: " + allUserId.size());

                    if(allUserId.size() == 0) {
                        view.showBanner();
                    }else{
                        view.hideBanner();
                        getContacts();
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    view.onFailed(databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            view.onFailed(e.getLocalizedMessage());
        }
    }


    private void getContacts(){
        mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < allUserId.size() ; i++){
                    firestore = FirebaseFirestore.getInstance();
                    final int finalI = i;
                    firestore.collection("Users").document(allUserId.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                User user = task.getResult().toObject(User.class);
                                Log.d(TAG, "onComplete: "+user.getUserID()+"   "+user.getUserName());
                                list.add(user);
                                if (list.size() == allUserId.size()){
                                    Log.d(TAG, "run: "+list.size());
                                    view.hideProgress();
                                    view.updateAdapter(list);
                                }
                            }else{
                                view.onFailed(task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });
    }


    /*private void getContacts() {


        ExecutorService executor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {

                for (String userID : allUserId) {
                    firestore = FirebaseFirestore.getInstance();
                    firestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                User user = task.getResult().toObject(User.class);
                                Log.d(TAG, "onComplete: "+user.getUserID()+"   "+user.getUserName());
                                list.add(user);
                            }else{
                                view.onFailed(task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }



            }


        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: "+list.size());
                view.updateAdapter(list);
            }
        },3000);

    }*/

}
