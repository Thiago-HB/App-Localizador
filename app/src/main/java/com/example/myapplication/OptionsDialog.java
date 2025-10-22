package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OptionsDialog extends DialogFragment {

    CheckBox checkGPS, checkGalileo, checkGlonass, checkBeidou;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.options_dialog, null);

        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();

        checkGPS=view.findViewById(R.id.checkGPS);
        checkGalileo=view.findViewById(R.id.checkGalileo);
        checkGlonass=view.findViewById(R.id.checkGlonass);
        checkBeidou=view.findViewById(R.id.checkBeidou);

        checkGPS.setChecked(prefs.getBoolean("blockGPS", false));
        checkGalileo.setChecked(prefs.getBoolean("blockGalileo", false));
        checkGlonass.setChecked(prefs.getBoolean("blockGlonass", false));
        checkBeidou.setChecked(prefs.getBoolean("blockBeidou", false));

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String prefKey = "";
                int vId = buttonView.getId();

                if (vId == R.id.checkGPS) {
                    prefKey = "blockGPS";
                } else if (vId == R.id.checkGalileo) {
                    prefKey = "blockGalileo";
                } else if (vId == R.id.checkGlonass) {
                    prefKey = "blockGlonass";
                } else if (vId == R.id.checkBeidou) {
                    prefKey = "blockBeidou";
                }

                if (!prefKey.isEmpty()) {
                    editor.putBoolean(prefKey, isChecked);
                    editor.apply();
                }
            }
        };

        checkGPS.setOnCheckedChangeListener(listener);
        checkGalileo.setOnCheckedChangeListener(listener);
        checkGlonass.setOnCheckedChangeListener(listener);
        checkBeidou.setOnCheckedChangeListener(listener);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, id) -> {
            dismiss();
        });

        return builder.create();
    }
}