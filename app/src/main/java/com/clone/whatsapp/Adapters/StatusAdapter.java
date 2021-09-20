package com.clone.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clone.whatsapp.Models.Status;
import com.clone.whatsapp.Models.UserStatus;
import com.clone.whatsapp.R;
import com.clone.whatsapp.Views.StatusViewActivity;
import com.devlomi.circularstatusview.CircularStatusView;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Holder> {

    private static final String TAG = "StatusAdapter";
    private Context context;
    private ArrayList<ArrayList<Status>> list;
    private ArrayList<UserStatus> statusList;

    public StatusAdapter(Context context) {
        this.context = context;
        list = new ArrayList<ArrayList<Status>>();
        statusList = new ArrayList<>();
    }

    public void updateList(ArrayList<UserStatus> statusList , ArrayList<ArrayList<Status>> list) {
        this.statusList = statusList;
        this.list = list;
        //Log.d(TAG, "updateList: "+statusList.get(0).getUserName()+" "+statusList.get(0).getDateTime());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: in status createView holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_status, parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        holder.userName.setText(statusList.get(position).getUserName());
        holder.timeStamp.setText(statusList.get(position).getDateTime());
        holder.statusView.setPortionsCount(statusList.get(position).getStatusCount());
        Glide.with(context).load(statusList.get(position).getThumbnailUrl()).into(holder.imageView);

        PushDownAnim.setPushDownAnimTo(holder.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatusViewActivity.class);
                ArrayList<Status> arr = list.get(position);
                intent.putExtra("StatusList",arr);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+statusList.size());
        return statusList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private CircularStatusView statusView;
        private TextView userName;
        private TextView timeStamp;
        private RelativeLayout parent;

        public Holder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.Status_parent);
            imageView = itemView.findViewById(R.id.Status_ImageView);
            statusView = itemView.findViewById(R.id.circular_status_view);
            userName = itemView.findViewById(R.id.Status_username);
            timeStamp = itemView.findViewById(R.id.Status_timeStamp);
        }
    }
}
