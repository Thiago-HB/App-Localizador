package com.example.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OptionListener implements View.OnClickListener {

    private final Context viewContext;

    public OptionListener(Context context) {
        this.viewContext = context;
    }

    private AppCompatActivity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextWrapper) {
            // Tenta obter o Base Context e chama recursivamente
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        AppCompatActivity activity = getActivity(viewContext);

        if (activity != null) {
            OptionsDialog options = new OptionsDialog();
            options.show(activity.getSupportFragmentManager(), "Options Dialog");
        }
    }
}