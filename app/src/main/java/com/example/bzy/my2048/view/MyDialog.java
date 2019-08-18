package com.example.bzy.my2048.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.bzy.my2048.R;

public class MyDialog extends Dialog {

    public MyDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }
}
