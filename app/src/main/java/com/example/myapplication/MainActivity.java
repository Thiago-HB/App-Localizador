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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.LocationListener;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_UPDATES = 1;
LocationManager localizador;
LocationListener locationListener;
    GnssStatus.Callback callback;
     Button start;
     Button stop;

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
        setContentView(R.layout.activity_main);
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

    }

    public void startGNSSUpdate(){

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                atualizaLocationView(location);
                }
            };

            localizador.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long)1000, (float)0,
                    (android.location.LocationListener) locationListener);

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
    };

    public void stopGNSSUpdate(){};

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
    };

}