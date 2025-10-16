package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConsentDialog extends DialogFragment {

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.consent_dialog, null);
        Button button = view.findViewById(R.id.consentButton);
        button.setOnClickListener(new ConsentListener(this));
        builder.setView(view);
        return builder.create();

    };
}
