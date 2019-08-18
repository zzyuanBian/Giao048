package com.example.bzy.my2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

public class BlockView extends TextView {

    public BlockView(Context context, int num, int x, int y) {
        super(context);
        setX(x);
        setY(y);
        setText(num + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}