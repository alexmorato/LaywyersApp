package com.alexmorato.laywyersapp.Helpers;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    public static void ShowMessage(Context context, String msg)
    {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}
