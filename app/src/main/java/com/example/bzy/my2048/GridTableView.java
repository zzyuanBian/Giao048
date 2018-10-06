package com.example.bzy.my2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GridTableView extends View {

    Paint paint;
    int width;

    public GridTableView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(4.0f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        width = (int)Util.WIDTH;

    }

    float[] points_draw_gride =
            {0, 0, width, 0,
                    0, width / 4, width, width / 4,
                    0, width / 2, width, width / 2,
                    0, 3 * width / 4, width, 3 * width / 4,
                    0, width, width, width};

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(points_draw_gride, paint);
    }
}
