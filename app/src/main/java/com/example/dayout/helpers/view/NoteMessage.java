package com.example.dayout.helpers.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import com.example.dayout.R;
import com.google.android.material.snackbar.Snackbar;

public class NoteMessage {

    public static void message(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        //toast.getView().setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.orange_500));
        TextView tv = ((TextView) ((LinearLayout) toast.getView()).getChildAt(0));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.acme);
        tv.setTypeface(typeface);
        toast.show();
    }

    public static void errorMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
        TextView tv = ((TextView) ((LinearLayout) toast.getView()).getChildAt(0));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.acme);
        tv.setTypeface(typeface);
        toast.show();
    }

    @SuppressLint("ResourceType")
    public static void showSnackBar(Activity activity, String message){

        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content) ,message, Snackbar.LENGTH_INDEFINITE);

        View view = snackbar.getView();

        view.setBackgroundResource(R.color.orange_500);

        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(activity.getResources().getDimension(R.dimen._5ssp));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close_24, 0);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Typeface typeface = ResourcesCompat.getFont(activity,R.font.acme);
        textView.setTypeface(typeface);

        textView.setOnClickListener(v -> snackbar.dismiss());

        snackbar.show();
    }

}
