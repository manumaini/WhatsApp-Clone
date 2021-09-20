package com.clone.whatsapp.Presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusPresenter implements MainContract.StatusPresenter {

    private static final String TAG = "StatusPresenter";
    private Context context;
    private MainContract.StatusView view;
    private DatabaseReference reference;
    private ArrayList<ArrayList<Status>> statusArray;

    public StatusPresenter(Context context, MainContract.StatusView view) {
        this.context = context;
        this.view = view;
        statusArray = new ArrayList<ArrayList<Status>>();
    }


    @Override
    public void loadStatus() {
        try {

            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    statusArray.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Status status = snapshot.getValue(Status.class);
                        Log.d(TAG, "onDataChange: " + status.getUserId() + "   " + status.getCaption());

                        int i;
                        for (i = 0; i < statusArray.size(); i++) {
                            if (statusArray.get(i).get(0).getUserId() == status.getUserId()) {
                                statusArray.get(i).add(status);
                                break;
                            }
                            if (i == statusArray.size() - 1) {
                                statusArray.get(i).add(status);
                            }
                        }
                        if (i == 0) {
                            ArrayList<Status> arr = new ArrayList<>();
                            arr.add(status);
                            statusArray.add(arr);
                        }

                    }
                    Log.d(TAG, "onDataChange: " + statusArray.size());
                    view.updateAdapter(statusArray);
                    //update adapter
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


}
