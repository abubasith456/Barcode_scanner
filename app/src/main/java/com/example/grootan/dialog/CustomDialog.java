package com.example.grootan.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.grootan.R;

public class CustomDialog extends DialogFragment {

    private static final String ARGUMENT_TITLE = "ARGUMENT_TITLE";
    private static final String ARGUMENT_MESSAGE = "ARGUMENT_MESSAGE";
    private static final String ARGUMENT_ACTION = "ARGUMENT_ACTION";
    private static final String ARGUMENT_STATUS = "NO_PAYMENT";
    private static final String ARGUMENT_IS_FINISH = "ARGUMENT_IS_FINISH";

    TextView title;
    TextView message;
    TextView action;
    ImageView dialog_img;
    OnDismissListener listener;

    public static CustomDialog newInstance(String title, String msg, String action, String status) {
        Bundle bundle = new Bundle();
        CustomDialog fragment = new CustomDialog();
        try {
            bundle.putString(ARGUMENT_TITLE, title);
            bundle.putString(ARGUMENT_MESSAGE, msg);
            bundle.putString(ARGUMENT_ACTION, action);
            bundle.putString(ARGUMENT_STATUS, status);
            fragment.setArguments(bundle);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Cancel);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return inflater.inflate(R.layout.dialog_custom_popup, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            title = view.findViewById(R.id.text_dialog_title);
            message = view.findViewById(R.id.text_dialog_msg);
            action = view.findViewById(R.id.text_dialog_action);
            dialog_img = view.findViewById(R.id.dialog_img);

            action.setOnClickListener(v -> {
                try {
                    dismiss();
                    if(this.listener != null)
                        this.listener.onDismiss();
                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            });

            String title = getArguments().getString(ARGUMENT_TITLE);
            String msg = getArguments().getString(ARGUMENT_MESSAGE);
            String action = getArguments().getString(ARGUMENT_ACTION);
            String status = getArguments().getString(ARGUMENT_STATUS);

            if (!TextUtils.isEmpty(title)) {
                this.title.setText(title);
                this.title.setVisibility(View.VISIBLE);
            } else {
                this.title.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(msg)) {
                this.message.setText(msg);
            }
            if (!TextUtils.isEmpty(action)) {
                this.action.setText(action);
            }
            if (!TextUtils.isEmpty(status) && status.equals(getResources().getString(R.string.success))) {
                dialog_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_success));
            } else if (!TextUtils.isEmpty(status) && status.equals(getResources().getString(R.string.confirm))) {
                dialog_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_confirm));
            } else {
                dialog_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_warning));
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

//    @Override
//    public void onDismiss(final DialogInterface dialog) {
//        super.onDismiss(dialog);
//        try {
//            if (listener != null) {
//                listener.onDismiss();
//            }
//        } catch (Exception exception) {
//            Log.e("Error ==> ", "" + exception);
//        }
//    }

    public void setListener(final OnDismissListener listener) {
        this.listener = listener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
