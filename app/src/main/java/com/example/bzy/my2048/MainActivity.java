package com.example.bzy.my2048;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.bzy.my2048.controller.GameController;
import com.example.bzy.my2048.utils.Constants;
import com.example.bzy.my2048.utils.DeviceInfo;
import com.example.bzy.my2048.view.GridTableView;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int game_width = wm.getDefaultDisplay().getWidth();
        DeviceInfo.getsInstance().setWidth(game_width);

        attachGridView();
        GameController.getsInstance(this);
    }

    private void attachGridView() {
        int gridWidth = (int) DeviceInfo.getsInstance().getWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gridWidth, gridWidth);
        GridTableView gridView = new GridTableView(this);
        gridView.setId(Constants.Id.ID_GRID_VIEW);
        gridView.setLayoutParams(params);
        gridView.setBackgroundColor(Color.rgb(255, 248, 220));
        ((RelativeLayout)findViewById(R.id.root_view)).addView(gridView);
    }
}