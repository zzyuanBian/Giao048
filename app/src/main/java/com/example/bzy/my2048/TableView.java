package com.example.bzy.my2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TableView extends View {
    int width;
    Paint paint;

    public TableView(Context context, int width) {
        super(context);
        this.width = width;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] points = {
                0, 0, width, 0,
                0, width / 4, width ,width / 4,
                0, width / 2, width, width / 2,
                0, width * 3 / 4, width, width * 3 / 4,
                0, width, width, width,
                width / 4, 0, width / 4, width,
                width / 2, 0, width / 2, width,
                width * 3 / 4, 0, width * 3 / 4, width,
                width, 0, width, width
        };
        canvas.drawLines(points, paint);
    }
}
