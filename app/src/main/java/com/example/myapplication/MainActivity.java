package com.example.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.ContextWrapper; // Importação adicionada

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.location.LocationListener;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_UPDATES = 1;
    LocationManager localizador;
    LocationListener locationListener;
    GnssStatus.Callback callback;
    Button start;
    Button stop;

    GNSSView gnssView;

    // **MÉTODO getActivity FOI REMOVIDO DAQUI**

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        ConsentDialog dialog = new ConsentDialog();
        dialog.show(getSupportFragmentManager(), "Consent Dialog");
        localizador = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        start = findViewById(R.id.startGNSS);
        stop = findViewById(R.id.stopGNSS);
        stop.setVisibility(View.INVISIBLE);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startGNSSUpdate();
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopGNSSUpdate();
                stop.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
            }
        });

        gnssView = findViewById(R.id.GnssView);
        gnssView.setVisibility(View.INVISIBLE);

        // **CLASSE ANÔNIMA CORRIGIDA**
        gnssView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica de desembrulhamento embutida
                Context context = v.getContext();
                AppCompatActivity activity = null;

                while (context != null) {
                    if (context instanceof AppCompatActivity) {
                        activity = (AppCompatActivity) context;
                        break;
                    }
                    if (context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    } else {
                        break;
                    }
                }

                // Exibição do Diálogo
                OptionsDialog options = new OptionsDialog();
                options.show(MainActivity.this.getSupportFragmentManager(), "Options Dialog");
            }
        });
    }

    public void startGNSSUpdate(){

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){

            gnssView.setVisibility(View.VISIBLE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                }
            };

            localizador.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0,
                    locationListener,
                    Looper.getMainLooper());

            callback = new GnssStatus.Callback() {
                @Override
                public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                    super.onSatelliteStatusChanged(status);
                    atualizaGNSSView(status);
                }
            };

            localizador.registerGnssStatusCallback(callback, new Handler(Looper.getMainLooper()));

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES);
        }
    }

    public void stopGNSSUpdate(){

        if (locationListener != null) {
            localizador.removeUpdates(locationListener);
            locationListener = null;
        }

        if (callback != null) {
            localizador.unregisterGnssStatusCallback(callback);
            callback = null;
        }

        if (gnssView != null) {
            gnssView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_UPDATES){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startGNSSUpdate();
            } else {
                Toast.makeText(this, "Impossível prosseguir, usuário negou permissões",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    public void atualizaGNSSView(GnssStatus status){
        gnssView.newStatus(status);
    }
}