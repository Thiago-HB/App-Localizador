package com.example.myapplication;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class ConsentListener implements View.OnClickListener {

    DialogFragment dialog;
    public ConsentListener(DialogFragment d){
        dialog = d;
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(dialog.getContext(), "Condições aceitas", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
