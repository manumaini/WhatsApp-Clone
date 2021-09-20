package com.clone.whatsapp.Views.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.clone.whatsapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class BottomFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Context context;
    private EditText field;
    private BottomSheetCallbacks callbacks;
    private TextView Bottom_title;
    private String title;

    public static final String TAG = "BottomDialog";

    private BottomFragment(Context context,String Title) {
        this.context = context;
        this.title = Title;
        callbacks = (BottomSheetCallbacks) context;
    }

    public static BottomFragment getInstance(Context context,String Title) {
        return new BottomFragment(context,Title);
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        field = view.findViewById(R.id.Bottom_Name);
        Bottom_title = view.findViewById(R.id.Bottom_Title);
        Bottom_title.setText(title);
        PushDownAnim.setPushDownAnimTo(view.findViewById(R.id.Bottom_Save), view.findViewById(R.id.Bottom_Cancel)).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Bottom_Save:
                if(!field.getText().toString().isEmpty()){
                    callbacks.onChanged(field.getText().toString());
                }else{
                    field.setError("Required");
                }
                dismiss();
                break;
            case R.id.Bottom_Cancel:
                dismiss();
                break;
        }
    }

    public interface BottomSheetCallbacks{
        void onChanged(String change);
    }
}
