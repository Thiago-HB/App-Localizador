package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GNSSView extends View implements View.OnClickListener {
    private final Paint paint = new Paint();
private int r;
private int height, width;
private GnssStatus gnssStatus = null;
    public GNSSView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
int radius = r;
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        radius = (int)(radius*Math.cos(Math.toRadians(45)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        radius = (int)(radius*Math.cos(Math.toRadians(60)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);

        canvas.drawLine(computeXc(0), computeYc(-r), computeXc(0), computeYc(r), paint);
        canvas.drawLine(computeXc(-r), computeYc(0), computeXc(r), computeYc(0), paint);

        paint.setStyle(Paint.Style.FILL);

        if (gnssStatus!=null) {
            for(int i=0; i<gnssStatus.getSatelliteCount(); i++) {
                int provider = gnssStatus.getConstellationType(i);
                float az=gnssStatus.getAzimuthDegrees(i);
                float el=gnssStatus.getElevationDegrees(i);

                switch (provider){
                    case GnssStatus.CONSTELLATION_BEIDOU:
                        paint.setColor(Color.BLUE);
                        break;
                    case GnssStatus.CONSTELLATION_GALILEO:
                        paint.setColor(Color.GREEN);
                        break;
                    case GnssStatus.CONSTELLATION_GPS:
                        paint.setColor(Color.CYAN);
                        break;
                    case GnssStatus.CONSTELLATION_GLONASS:
                        paint.setColor(Color.WHITE);
                        break;
                    default:
                        break;

                }



                // Conversões de coordenadas (parece ser projeção polar)
                float x=(float)(r*Math.cos(Math.toRadians(el))*Math.sin(Math.toRadians(az)));
                float y=(float)(r*Math.cos(Math.toRadians(el))*Math.cos(Math.toRadians(az)));

                // Desenha o satélite (círculo)
                canvas.drawCircle(computeXc(x), computeYc(y), 10, paint);

                // Configura o pincel para desenhar o texto (ID do satélite)
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(30);

                String satID = gnssStatus.getSvid(i)+"";

                // Desenha o ID do satélite
                canvas.drawText(satID,  computeXc(x)+10,  computeYc(y)+10, paint);


            }
        }

        paint.setColor(Color.WHITE);
        canvas.drawText(getResources().getString(R.string.legenda), computeXc(0)-radius,
                computeYc(0)+radius+10, paint);
        canvas.drawText(getResources().getString(R.string.legenda2), computeXc(0)-radius,
                computeYc(0)+radius+20, paint);
        canvas.drawText(getResources().getString(R.string.legenda3), computeXc(0)-radius,
                computeYc(0)+radius+30, paint);

        canvas.drawText(getResources().getString(R.string.legenda), computeXc(0)-radius,
                computeYc(0)-radius-10, paint);
    }

    private int computeXc(double x){
        return (int)(x+width/2);
    };

    private int computeYc(double y){
        return (int)(-y+height/2);
    };

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
            r=(int)(width/2*0.9);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
