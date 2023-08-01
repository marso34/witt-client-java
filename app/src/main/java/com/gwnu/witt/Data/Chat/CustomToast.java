package com.gwnu.witt.Data.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gwnu.witt.R;

public class CustomToast extends Toast {
    private static String lastDisplayedMessage;
    private View toastView; // Store the custom toast view
    public CustomToast(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence s) {
        super.setText(s);
        lastDisplayedMessage = s.toString();
    }

    public static String getLastDisplayedMessage() {
        return lastDisplayedMessage;
    }

    // Override the makeText method to use the custom layout
    public static CustomToast makeText(Context context, CharSequence message, int duration) {
        CustomToast toast = new CustomToast(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView text = layout.findViewById(R.id.customToastText);
        text.setText(message);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.toastView = layout;
        return toast;

    }
    public int getCustomToastWidth() {
        if (toastView != null) {
            return toastView.getWidth();
        }
        return 0;
    }
}

