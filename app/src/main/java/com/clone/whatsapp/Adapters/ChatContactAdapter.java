package com.clone.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Models.ChatContact;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.ChatActivity;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatContactAdapter extends RecyclerView.Adapter<ChatContactAdapter.Holder> {

    private ArrayList<ChatContact> list;
    private Context context;
    private final static String TAG = "ChatContactAdapter";

    public ChatContactAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public void populateList(ArrayList<ChatContact> list){
        this.list = list;
    }


    @NonNull
    @Override
    public ChatContactAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact,parent,false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatContactAdapter.Holder holder, final int position) {
        holder.user_name.setText(list.get(position).getName());
        holder.user_desc.setText(list.get(position).getLastMSG());
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.user_image);

        PushDownAnim.setPushDownAnimTo(holder.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                //intent.putExtra("userImage",userImage);
                Log.d(TAG, "onClick: "+list.get(position).getUserID()+"     "+list.get(position).getName());
                intent.putExtra("receiverName",list.get(position).getName());
                intent.putExtra("receiverID",list.get(position).getUserID());
                intent.putExtra("receiverImageUrl",list.get(position).getImageUrl());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public CircleImageView user_image;
        public TextView user_name;
        public TextView user_desc;
        private LinearLayout layout;


        public Holder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.Contact_image);
            user_name = itemView.findViewById(R.id.Contact_name);
            user_desc = itemView.findViewById(R.id.Contact_desc);
            layout = itemView.findViewById(R.id.Contact_layout);
        }
    }
}
