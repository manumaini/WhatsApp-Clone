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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfilePresenter implements MainContract.ProfilePresenter {

    private Context context;
    private MainContract.ProfileActivity view;
    private String downloadURl;
    private final String TAG = "ProfilePresenter";

    public ProfilePresenter(Context context, MainContract.ProfileActivity view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void UpdateName(final String name) {
        view.showProgress("Updating...");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(SharedPrefsHelper.getInstance().getUserUid()).update("userName", name).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.hideProgress();
                SharedPrefsHelper.getInstance().setUserName(name);
                view.onNameChanged(name);
             }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgress();
                view.onFailed(e.getLocalizedMessage());
            }
        });

    }

    @Override
    public void UpdateDesc(final String desc) {
        view.showProgress("Updating...");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(SharedPrefsHelper.getInstance().getUserUid()).update("bio", desc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.hideProgress();
                view.onDescChanged(desc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgress();
                view.onFailed(e.getLocalizedMessage());
            }
        });

    }

    @Override
    public void RemoveImage(final String url) {
        view.showProgress("Updating...");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(SharedPrefsHelper.getInstance().getUserUid()).update("imageUrl", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        removeFromStorage(url);
                        view.hideProgress();
                        view.onImageRemoved();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgress();
                view.onFailed(e.getLocalizedMessage());
            }
        });

    }

    private void removeFromStorage(String url) {
        if(!url.isEmpty()){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReferenceFromUrl(url);
            ref.delete();
        }
    }

    private String getFileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    @Override
    public void UpdateImage(Uri imageUri, final String oldUrl) {

        try{

            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            view.showProgress("Updating...");
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

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("Users").document(SharedPrefsHelper.getInstance()
                                    .getUserUid()).update("imageUrl", downloadURl)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            removeFromStorage(oldUrl);
                                            view.hideProgress();
                                            view.onImageUpdated(downloadURl);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    removeFromStorage(downloadURl);
                                    view.hideProgress();
                                    view.onFailed("Update failed!! :"+e.getLocalizedMessage());
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.hideProgress();
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

        }catch(Exception e){

        }





    }


}
