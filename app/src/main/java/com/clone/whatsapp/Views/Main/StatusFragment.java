package com.clone.whatsapp.Views.Main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clone.whatsapp.Adapters.StatusAdapter;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Status;
import com.clone.whatsapp.Models.UserStatus;
import com.clone.whatsapp.Presenters.StatusPresenter;
import com.clone.whatsapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusFragment extends Fragment implements MainContract.StatusView {


    private static final String TAG = "StatusFragment";
    private Context context;
    private RelativeLayout relativeLayout;
    private CircleImageView userImage;
    private StatusPresenter presenter;
    private RecyclerView recyclerView;
    private StatusAdapter adapter;
    private ArrayList<UserStatus> statusList;


    public StatusFragment(Context context) {
        this.context = context;
    }

    public  StatusFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userImage = view.findViewById(R.id.Status_UserImage);
        presenter = new StatusPresenter(getContext(),this);
        recyclerView = view.findViewById(R.id.Status_recyclerView);
        adapter = new StatusAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        statusList = new ArrayList<>();
        presenter.loadStatus();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSuccess() {

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
    public void updateAdapter(ArrayList<ArrayList<Status>> list) {
        Log.d(TAG, "updateAdapter: "+list.size());
        statusList.clear();
        for(int i = 0 ; i < list.size() ; i++){
            int count = 0;
            for(int j =0 ; j < list.get(i).size() ; j++){
                count++;
            }
            UserStatus status = new UserStatus(list.get(i).get(0).getName(),list.get(i).get(0).getDateTime(),count,list.get(i).get(0).getThumbnailUrl());
            statusList.add(status);
        }
        adapter.updateList(statusList,list);
        adapter.notifyDataSetChanged();


    }
}
