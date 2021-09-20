package com.clone.whatsapp.Views.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clone.whatsapp.Adapters.ChatContactAdapter;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.ChatContact;
import com.clone.whatsapp.Models.User;
import com.clone.whatsapp.Presenters.ChatFragmentPresenter;
import com.clone.whatsapp.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ChatFragment extends Fragment implements MainContract.ChatFragmentView {

    private Context context;
    private RecyclerView recyclerView;
    private LinearLayout banner;
    private ChatFragmentPresenter presenter;
    private ChatContactAdapter adapter;
    private ArrayList<ChatContact> list;
    private AlertDialog dialog;


    private final static String TAG = "ChatFragment";

    public ChatFragment(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public ChatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //controllers
        recyclerView = view.findViewById(R.id.MainChat_RecyclerView);
        banner = view.findViewById(R.id.MainChat_Banner);

        //initialization
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(getContext()).setCancelable(false).setMessage("loading").build();
        adapter = new ChatContactAdapter(getContext());
        presenter = new ChatFragmentPresenter(getContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter.readContacts();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed(String message) {
        Log.d(TAG, "onFailed: " + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.hide();
    }

    @Override
    public void updateAdapter(ArrayList<User> contacts) {

        for (int i = 0; i < contacts.size(); i++) {
            list.add(new ChatContact(contacts.get(i).getUserName(), contacts.get(i).getBio(), contacts.get(i).getImageUrl(), contacts.get(i).getUserID()));
            Log.d(TAG, "updateAdapter: " + list.get(i).getName());
        }

        Log.d(TAG, "updateAdapter: " + list.size());

        adapter.populateList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showBanner() {
        banner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBanner() {
        banner.setVisibility(View.GONE);
    }
}
