package com.example.coupleapp.Activity.Calender;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class CustmMultipleDotSpan implements LineBackgroundSpan {


    private final float radius;
    private int color;


    public CustmMultipleDotSpan() {
        this.radius = 5;
        this.color = 0;
    }


    public CustmMultipleDotSpan(int color) {
        this.radius = 5;
        this.color = 0;
    }


    public CustmMultipleDotSpan(float radius) {
        this.radius = radius;
        this.color = 0;
    }


    public CustmMultipleDotSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {

        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }

        canvas.drawCircle((left + right) / 2 - 20, bottom + radius, radius, paint);
        paint.setColor(oldColor);
        }
}
