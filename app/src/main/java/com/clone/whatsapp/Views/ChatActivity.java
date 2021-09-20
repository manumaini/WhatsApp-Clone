package com.clone.whatsapp.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Adapters.ChatAdapter;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Chat;
import com.clone.whatsapp.Presenters.ChatPresenter;
import com.clone.whatsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements MainContract.ChatView, View.OnClickListener {

    private static final String TAG = "ChatActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageButton emojiButton;
    private ImageButton galleryButton;
    private FloatingActionButton sendButton;
    private CircleImageView receiverImage;
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private TextView receName;
    private EditText textMessage;
    private ImageButton backButton;


    private String receiverId;
    private String receiverName;
    private String receiverImageUrl;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //controllers
        recyclerView = findViewById(R.id.Chat_RecyclerView);
        sendButton = findViewById(R.id.Chat_SendButton);
        toolbar = findViewById(R.id.Chat_toolbar);
        textMessage = findViewById(R.id.Chat_textMessage);

        //toolbar Controllers
        receiverImage = toolbar.findViewById(R.id.Chat_Toolbar_UserImage);
        receName = toolbar.findViewById(R.id.Chat_Toolbar_UserName);
        backButton = toolbar.findViewById(R.id.Chat_Toolbar_backButton);


        //initialization
        setSupportActionBar(toolbar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        receiverId = getIntent().getStringExtra("receiverID");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverImageUrl = getIntent().getStringExtra("receiverImageUrl");
        presenter = new ChatPresenter(this, this);
        adapter = new ChatAdapter(ChatActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        receiverInit();
        PushDownAnim.setPushDownAnimTo(sendButton, backButton).setOnClickListener(this);
        presenter.readMessage(currentUser.getUid(),receiverId);


        //loggs
        Log.d(TAG, "onCreate: "+receiverId+"    "+receiverName);

    }

    private void receiverInit() {
        receName.setText(receiverName);
        Glide.with(ChatActivity.this).load(receiverImageUrl).into(receiverImage);
    }

    @Override
    public void onSuccess() {
        textMessage.setText("");

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void clearAdapterList() {
        adapter.clearList();
    }


    @Override
    public void updateAdapter(Chat chat) {
        adapter.addChat(chat);
        adapter.notifyDataSetChanged();
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Chat_SendButton:
                if(!(textMessage.getText().toString().isEmpty())){
                    String time = getCurrentTime();
                    Log.d(TAG, "onClick: " + String.valueOf(time)+"   "+receiverId +"  "+ currentUser.getUid());
                    Chat chat = new Chat(time, textMessage.getText().toString().trim(), Chat.TEXT_MESSAGE, receiverId, currentUser.getUid());
                    presenter.sendMessage(chat);
                }
                break;

            case R.id.Chat_Toolbar_backButton:
                finish();
                break;
        }
    }
}