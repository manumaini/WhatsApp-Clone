package com.clone.whatsapp.Views.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clone.whatsapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class BottomFragmentImagePicker extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "Image picker";
    private FloatingActionButton removeImage;
    private FloatingActionButton  pickImage;
    private Callbacks callbacks;


    private BottomFragmentImagePicker(Context context) {
        callbacks = (Callbacks) context;

    }

    public static BottomFragmentImagePicker getInstance(Context context){
        return new BottomFragmentImagePicker(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_fragment_pick_image,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        removeImage = view.findViewById(R.id.Bottom_RemoveImage);
        pickImage = view.findViewById(R.id.Bottom_PickImage);

        PushDownAnim.setPushDownAnimTo(removeImage , pickImage).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.Bottom_PickImage:
                callbacks.onClickImagePicker();
                dismiss();
                break;
            case R.id.Bottom_RemoveImage:
                callbacks.onClickRemoveImage();
                dismiss();
                break;

        }
    }

    public interface Callbacks{
        void onClickImagePicker();
        void onClickRemoveImage();
    }
}
