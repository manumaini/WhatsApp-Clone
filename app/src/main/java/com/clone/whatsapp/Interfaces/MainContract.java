package com.clone.whatsapp.Interfaces;

import android.net.Uri;

import com.clone.whatsapp.Models.Chat;
import com.clone.whatsapp.Models.Contact;
import com.clone.whatsapp.Models.Status;
import com.clone.whatsapp.Models.User;

import java.util.ArrayList;

public interface MainContract {

    interface MainActivity{

    }
    interface MainPresenter{

    }

    interface ProfileActivity{
        void onSuccess();
        void onImageRemoved();
        void onImageUpdated(String url);
        void onNameChanged(String name);
        void onDescChanged(String desc);
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
        void setData(User user);

    }
    interface ProfilePresenter{
        void UpdateName(String name);
        void UpdateDesc(String desc);
        void RemoveImage(String url);
        void UpdateImage(Uri imageUri,String oldUrl);
    }


    interface SettingActivity{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
        void setData(User user);
    }

    interface SettingPresenter{
        void loadUserData();
    }


    interface UserDetailsActivity{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();

    }

    interface UserDetailsPresenter{
        void UploadProfile();
        void AddUser();
        void Continue(Uri imageUri , String username , String userDesc);
    }

    interface ContactPresenter{
        Void loadContacts();
    }

    interface ContactView{
        void onSuccess(ArrayList<Contact> list);
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
    }


    interface ChatView{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
        void clearAdapterList();
        void updateAdapter(Chat chat);
    }

    interface ChatInterface{
        void sendMessage(Chat chat);
        void readMessage(String userID, String receiverID);
    }


    interface ChatFragmentPresenter{
        void readContacts();

    }

    interface ChatFragmentView{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
        void updateAdapter(ArrayList<User> contacts);
        void showBanner();
        void hideBanner();
    }

    interface ImagePreviewView{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
    }

    interface ImagePreviewPresenter{
        void UploadStatus(Status status, Uri ImageUri);
    }


    interface StatusView{
        void onSuccess();
        void onFailed(String message);
        void showProgress(String message);
        void hideProgress();
        void updateAdapter(ArrayList<ArrayList<Status>> list);
    }

    interface StatusPresenter{
        void loadStatus();

    }


}
