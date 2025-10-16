package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GNSSView extends View {
    private Paint paint = new Paint();

    public GNSSView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
