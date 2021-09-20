package com.clone.whatsapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clone.whatsapp.Models.Chat;
import com.clone.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private ArrayList<Chat> list;
    private Context context;
    private final static int MSG_TYPE_LEFT = 0;
    private final static int MSG_TYPE_RIGHT = 1;
    private final static String TAG = "ChatAdapter";


    public ChatAdapter(ArrayList<Chat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public ChatAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void initialChat(ArrayList<Chat> list) {
        this.list = list;
    }

    public void clearList(){
        list.clear();
    }

    public void addChat(Chat chat) {
        list.add(chat);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatitemleft, parent,false);
            return new Holder(view);
        } else if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatitemright, parent,false);
            return new Holder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.text.setText(list.get(getItemCount()-1-position).getTextMessage());
        holder.stamp.setText(list.get(getItemCount()-1-position).getDateTime());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView text;
        private TextView stamp;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.ChatLeftItem_Text);
            stamp = itemView.findViewById(R.id.ChatLeftItem_Stamp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "getItemViewType: "+list.get(getItemCount()-1-position).getTextMessage()+"     "+ list.get(getItemCount()-1-position).getSender());
        if(list.get(getItemCount()-1-position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
