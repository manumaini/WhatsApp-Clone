package com.clone.whatsapp.Presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Data.SharedPrefsHelper;
import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserDetailsPresenter implements MainContract.UserDetailsPresenter {

    private Context context;
    private MainContract.UserDetailsActivity view;


    private String userName;
    private String userDesc;
    private Uri imageUri;
    private String downloadURl;
    private final String TAG = "userDetail Presenter";


    public UserDetailsPresenter(Context context, MainContract.UserDetailsActivity view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void UploadProfile() {

        view.showProgress("updating profile...");
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child("ProfileImages/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

            reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadURl = String.valueOf(uri);
                            Log.d(TAG, "onSuccess: image uploaded" + downloadURl);
                            AddUser();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.onFailed("Could not fetch the DownloadUrl");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.hideProgress();
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    view.onFailed("could not update");
                }
            });

        } catch (Exception e) {
            view.hideProgress();
            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            view.onFailed("could not update");
        }

       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("ProfileImages/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadURl = String.valueOf(uri);
                        Log.d(TAG, "onSuccess: image uploaded" + downloadURl);
                        AddUser();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onFailed("Could not fetch the DownloadUrl");
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgress();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                view.onFailed("could not update");
            }
        });*/

    }

    @Override
    public void AddUser() {

        User user = new User(SharedPrefsHelper.getInstance().getUserUid(), userName,
                SharedPrefsHelper.getInstance().getUserPhone(),
                downloadURl, "", "", "", userDesc);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(SharedPrefsHelper.getInstance().getUserUid())
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    view.hideProgress();
                    SharedPrefsHelper.getInstance().setUserName(userName);
                    view.onSuccess();
                } else {
                    view.hideProgress();
                    view.onFailed("could not update");
                }

            }
        });

    }

    @Override
    public void Continue(Uri imageUri, String username, String userDesc) {
        this.imageUri = imageUri;
        this.userName = username;
        this.userDesc = userDesc;

        if (imageUri != null) {
            UploadProfile();
        } else {
            view.showProgress("updating profile...");
            AddUser();
        }

    }


    private String getFileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }
}
