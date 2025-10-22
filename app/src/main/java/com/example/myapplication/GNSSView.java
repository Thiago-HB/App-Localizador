package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.util.AttributeSet;
import android.view.View;
import android.content.ContextWrapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.OptionsDialog;

public class GNSSView extends View implements View.OnClickListener {
    private final Paint paint = new Paint();
    private int r;
    private int height, width;

    boolean b, gp, ga, gl;

    private GnssStatus gnssStatus = null;

    SharedPreferences prefs;

    public GNSSView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        setOnClickListener(this);
        prefs = this.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        b=prefs.getBoolean("blockBeidou", false);
        gp = prefs.getBoolean("blockGPS", false);
        ga=prefs.getBoolean("blockGalileo", false);
        gl=prefs.getBoolean("blockGlonass", false);
    }

    private AppCompatActivity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(computeXc(0), computeYc(0), r, paint);

        int r45 = (int)(r * (90.0 - 45.0) / 90.0);
        int r60 = (int)(r * (90.0 - 60.0) / 90.0);

        canvas.drawCircle(computeXc(0), computeYc(0), r45, paint);
        canvas.drawCircle(computeXc(0), computeYc(0), r60, paint);

        canvas.drawLine(computeXc(0), computeYc(-r), computeXc(0), computeYc(r), paint);
        canvas.drawLine(computeXc(-r), computeYc(0), computeXc(r), computeYc(0), paint);

        paint.setStyle(Paint.Style.FILL);

        int usedInFixCount = 0;
        int totalSatelliteCount = 0;

        if (gnssStatus!=null) {

            b=prefs.getBoolean("blockBeidou", false);
            gp = prefs.getBoolean("blockGPS", false);
            ga=prefs.getBoolean("blockGalileo", false);
            gl=prefs.getBoolean("blockGlonass", false);

            totalSatelliteCount = gnssStatus.getSatelliteCount();

            for(int i=0; i<totalSatelliteCount; i++) {
                boolean state = false;
                int provider = gnssStatus.getConstellationType(i);
                float az=gnssStatus.getAzimuthDegrees(i);
                float el=gnssStatus.getElevationDegrees(i);

                // Conta os satélites usados no fix ANTES da verificação de bloqueio
                if (gnssStatus.usedInFix(i)) {
                    usedInFixCount++;
                }

                switch (provider){
                    case GnssStatus.CONSTELLATION_BEIDOU:
                        paint.setColor(Color.BLUE);
                        state = b;
                        break;
                    case GnssStatus.CONSTELLATION_GALILEO:
                        paint.setColor(Color.GREEN);
                        state = ga;
                        break;
                    case GnssStatus.CONSTELLATION_GPS:
                        paint.setColor(Color.CYAN);
                        state = gp;
                        break;
                    case GnssStatus.CONSTELLATION_GLONASS:
                        paint.setColor(Color.WHITE);
                        state = gl;
                        break;
                    default:
                        paint.setColor(Color.GRAY);
                        state = false;
                        break;
                }

                float r_el = (float)(r * (90.0 - el) / 90.0);
                float x=(float)(r_el*Math.sin(Math.toRadians(az)));
                float y=(float)(r_el*Math.cos(Math.toRadians(az)));

                if(!state) {
                    // Desenha o satélite
                    canvas.drawCircle(computeXc(x), computeYc(y), 10, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(30);
                    paint.setColor(Color.BLACK);

                    String satID = gnssStatus.getSvid(i)+"";

                    canvas.drawText(satID,  computeXc(x)+10,  computeYc(y)+10, paint);
                }
            }
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.LEFT);

        int legendBaseY = computeYc(0) + r + 30;
        int verticalOffset = -150;
        int textY = legendBaseY + verticalOffset;


        String statsText = "Sats: " + usedInFixCount + " / " + totalSatelliteCount;


        canvas.drawText(statsText, computeXc(-r), textY - 30, paint);



        canvas.drawText(getResources().getString(R.string.legenda), computeXc(-r),
                textY+60, paint);

        canvas.drawText(getResources().getString(R.string.legenda2), computeXc(-r),
                textY + 90, paint);

        canvas.drawText(getResources().getString(R.string.legenda3), computeXc(-r),
                textY + 120, paint);
    }

    private int computeXc(double x){
        return (int)(x+width/2);
    }

    private int computeYc(double y){
        return (int)(-y+height/2);
    }

    public void newStatus(GnssStatus status){
        this.gnssStatus = status;
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        if(width<height){
            r=(int)(width/2*0.9);
        } else{
            r=(int)(height/2*0.9);
        }
    }

    @Override
    public void onClick(View v) {
        AppCompatActivity activity = getActivity(getContext());

        if (activity != null) {
            OptionsDialog options = new OptionsDialog();
            options.show(activity.getSupportFragmentManager(), "Options Dialog");
        }
    }
}