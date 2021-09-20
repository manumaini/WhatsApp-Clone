package com.clone.whatsapp.Presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.clone.whatsapp.Interfaces.MainContract;
import com.clone.whatsapp.Models.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ImagePreviewPresenter implements MainContract.ImagePreviewPresenter {

    private Context context;
    private MainContract.ImagePreviewView view;
    private final String TAG = "ImagePreviewPresenter";
    private String imageUrl;


    public ImagePreviewPresenter(Context context, MainContract.ImagePreviewView view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void UploadStatus(Status status, Uri ImageUri) {

        view.showProgress("uploading status...");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReference().child("StatusImages/" + System.currentTimeMillis() + ".jpg");
        reference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: image is uploaded");
                final Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = String.valueOf(uri);
                        Log.d(TAG, "onSuccess: image uploaded" + imageUrl);
                        status.setName(imageUrl);
                        uploadStatus(status);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onFailed(e.getLocalizedMessage());
                        return;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.onFailed(e.getLocalizedMessage());
                return;
            }
        });
    }



    public void uploadThumbnail(Status status, Uri imageUri) {

        try {
            view.showProgress("uploading status...");
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference().child("StatusImages/" + System.currentTimeMillis() + ".jpg");
            reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: image is uploaded");
                    final Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = String.valueOf(uri);
                            Log.d(TAG, "onSuccess: image uploaded" + imageUrl);
                            status.setThumbnailUrl(imageUrl);

                            uploadStatus(status);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.onFailed(e.getLocalizedMessage());
                            return;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.onFailed(e.getLocalizedMessage());
                    return;
                }
            });


        } catch (Exception e) {
            view.onFailed(e.getLocalizedMessage());
            return;
        }

    }

    private void uploadStatus(Status status) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Status").push().setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    view.onSuccess();
                } else {
                    view.onFailed(task.getException().getLocalizedMessage());
                    return;
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }
}
