package com.clone.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.clone.whatsapp.Models.Contact;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.ChatActivity;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    private ArrayList<Contact> list;
    private Context context;
    private Bitmap userImage;
    private static final String TAG = "ContactAdapter";

    public ContactAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public void populateList(ArrayList<Contact> list){
        this.list = list;
    }



    @NonNull
    @Override
    public ContactAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactAdapter.Holder holder, final int position) {
        holder.user_name.setText(list.get(position).getName());
        holder.user_desc.setText(list.get(position).getDesc());
        Glide.with(context).asBitmap().load(list.get(position).getImageUrl()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                userImage = resource;
                holder.user_image.setImageBitmap(userImage);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });




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
        public LinearLayout layout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.Contact_image);
            user_name = itemView.findViewById(R.id.Contact_name);
            user_desc = itemView.findViewById(R.id.Contact_desc);
            layout = itemView.findViewById(R.id.Contact_layout);

        }
    }
}
