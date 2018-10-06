package com.example.bzy.my2048;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/*

2048小游戏

功能：标准2048游戏规则

按钮：1）开始游戏

  2）保存游戏
  3）载入游戏
  4）结束游戏
*/
public class MainActivity extends Activity {

    int game_width;
    RelativeLayout maint_layout;
    FrameLayout game_layout;
    TableView tableView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        maint_layout = findViewById(R.id.layout_main);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        game_width = wm.getDefaultDisplay().getWidth();

        Util.WIDTH = game_width;

        game_layout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(game_width, game_width);
        game_layout.setLayoutParams(params);
        game_layout.setBackgroundColor(Color.rgb(255, 248, 220));
        maint_layout.addView(game_layout);
        tableView = new TableView(this, game_width);
        game_layout.addView(tableView);

        GameController gc = new GameController(this, maint_layout, game_layout);
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
    }

}