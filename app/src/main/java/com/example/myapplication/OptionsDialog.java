package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OptionsDialog extends DialogFragment implements View.OnClickListener {

    CheckBox checkGPS, checkGalileo, checkGlonass, checkBeidou;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.options_dialog, null);

       prefs = this.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        checkGPS=view.findViewById(R.id.checkGPS);
        checkGalileo=view.findViewById(R.id.checkGalileo);
        checkGlonass=view.findViewById(R.id.checkGlonass);
        checkBeidou=view.findViewById(R.id.checkBeidou);

        checkGPS.setChecked(prefs.getBoolean("blockGPS", false));
        checkGalileo.setChecked(prefs.getBoolean("blockGalileo", false));
        checkGlonass.setChecked(prefs.getBoolean("blockGlonass", false));
        checkBeidou.setChecked(prefs.getBoolean("blockBeidou", false));

        editor= prefs.edit();

        return builder.create();

    }


    @Override
    public void onClick(View v) {
        int b, gp, ga, gl;
        b=checkBeidou.getId();
        gp = checkGPS.getId();
        ga=checkGalileo.getId();
        gl=checkGlonass.getId();
        int vId = v.getId();
        boolean state;

        if(vId == b){
            state=prefs.getBoolean("blockBeidou", false);
            editor.putBoolean("blockBeidou", !state);
            checkBeidou.setChecked(!state);
        }
        if(vId == gp){
            state=prefs.getBoolean("blockGPS", false);
            editor.putBoolean("blockGPS", !state);
            checkGPS.setChecked(!state);
        }
        if(vId == ga){
            state=prefs.getBoolean("blockGalileo", false);
            editor.putBoolean("blockGalileo", !state);
            checkGalileo.setChecked(!state);
        }
        if(vId == gl){
            state=prefs.getBoolean("blockGlonass", false);
            editor.putBoolean("blockGlonass", !state);
            checkGlonass.setChecked(!state);
        }
    }
}


