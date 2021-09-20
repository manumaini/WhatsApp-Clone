package com.clone.whatsapp.Presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatPresenter implements MainContract.ChatInterface {

    private Context context;
    private MainContract.ChatView view;
    private DatabaseReference reference;
    private final String TAG = "ChatPresenter";

    public ChatPresenter(Context context, MainContract.ChatView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void sendMessage(Chat chat) {

        Log.d(TAG, "sendMessage: "+chat.getReceiver()+"    "+chat.getSender());
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Chat").push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    view.onSuccess();
                    Log.d(TAG, "onComplete: chat uploaded");
                } else {
                    Log.d(TAG, "onComplete: chat upload failed");
                }
            }
        });

        //chatlist
        DatabaseReference reference1 = reference.child("ChatList").child(chat.getSender()).child(chat.getReceiver());
        reference1.child("ChatId").setValue(chat.getReceiver());

        DatabaseReference reference2 = reference.child("ChatList").child(chat.getReceiver()).child(chat.getSender());
        reference2.child("ChatId").setValue(chat.getSender());
    }

    @Override
    public void readMessage(final String userID, final String receiverID) {

        try {
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Chat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    view.clearAdapterList();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat != null){
                            if (chat.getSender().equals(userID) && chat.getReceiver().equals(receiverID)
                                    || chat.getSender().equals(receiverID) && chat.getReceiver().equals(userID)) {

                                view.updateAdapter(chat);
                            }
                        }


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
}
