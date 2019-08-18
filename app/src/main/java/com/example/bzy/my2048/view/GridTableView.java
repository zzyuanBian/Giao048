package com.example.bzy.my2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bzy.my2048.controller.BlocksController;
import com.example.bzy.my2048.controller.GameController;
import com.example.bzy.my2048.utils.Constants;
import com.example.bzy.my2048.utils.DeviceInfo;
import com.example.bzy.my2048.utils.Direction;

public class GridTableView extends FrameLayout{

    private Paint mPaint;
    private final int SEGMENT = (int) DeviceInfo.getsInstance().getWidth()/4;

    private GameController gc;
    private BlocksController bc;

    public GridTableView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setStrokeWidth(Constants.STROKE_WIDTH_GRID);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    final float[] LINES =
            {
                    0 * SEGMENT, 1 * SEGMENT, 4 * SEGMENT, 1 * SEGMENT,
                    0 * SEGMENT, 2 * SEGMENT, 4 * SEGMENT, 2 * SEGMENT,
                    0 * SEGMENT, 3 * SEGMENT, 4 * SEGMENT, 3 * SEGMENT,

                    1 * SEGMENT, 0 * SEGMENT, 1 * SEGMENT, 4 * SEGMENT,
                    2 * SEGMENT, 0 * SEGMENT, 2 * SEGMENT, 4 * SEGMENT,
                    3 * SEGMENT, 0 * SEGMENT, 3 * SEGMENT, 4 * SEGMENT,
            };

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(LINES, mPaint);
    }



}
