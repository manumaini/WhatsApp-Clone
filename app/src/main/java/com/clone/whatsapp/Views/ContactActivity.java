package com.clone.whatsapp.Views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clone.whatsapp.Adapters.ContactAdapter;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Contact;
import com.clone.whatsapp.Presenters.ContactPresenter;
import com.clone.whatsapp.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ContactActivity extends AppCompatActivity implements MainContract.ContactView, SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ContactAdapter adapter;
    private AlertDialog dialog;
    private ContactPresenter presenter;
    private SearchView searchView;
    private ArrayList<Contact> items;
    private ArrayList<Contact> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //controllers
        recyclerView = findViewById(R.id.Contact_RecyclerView);
        toolbar = findViewById(R.id.Contact_toolbar);

        //initialization
        items = new ArrayList<>();
        result = new ArrayList<>();
        adapter = new ContactAdapter(this);
        presenter = new ContactPresenter(this,this);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        SpotsDialog.Builder builder = new SpotsDialog.Builder();
        dialog = builder.setContext(this).setCancelable(false).setMessage("loading").build();

        //loadContacts
        presenter.loadContacts();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.ContactMenu_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ContactMenu_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ArrayList<Contact> list) {
        items = list;
        adapter.populateList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(ContactActivity.this, message, Toast.LENGTH_LONG).show();
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
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        result.clear();

        for(Contact sample : items){
            if(sample.getName().contains(s)){
                result.add(sample);
            }
        }
        adapter.populateList(result);
        adapter.notifyDataSetChanged();
        return false;
    }
}