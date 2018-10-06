package com.example.bzy.my2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameController implements View.OnTouchListener, View.OnClickListener {
    private boolean DBG = false;
    private int[] src =
            {2, 8, 4, 2,
                    4096, 4, 8, 4,
                    4, 8, 4, 8,
                    8, 4, 2048, 1024};
    private BlocksController bc;
    private FrameLayout game_layout;
    private Context context;
    private int mMaxScore;
    private String mPlayer;
    SharedPreferences mPreferences;
    TextView tv_score;
    TextView tv_maxScore;
    TextView tv_playerName;
    Button bt_ok;
    EditText et;
    MyDialog dialog;

    GameController(Context context, View v1, View v2) {
        this.context = context;
        Button bt_start = v1.findViewById(R.id.bt_start);
        Button bt_save = v1.findViewById(R.id.bt_save);
        Button bt_load = v1.findViewById(R.id.bt_load);

        tv_score = v1.findViewById(R.id.id_score);
        tv_maxScore = v1.findViewById(R.id.id_highScore);
        tv_playerName = v1.findViewById(R.id.player_name);

        dialog = new MyDialog(context);


        game_layout = (FrameLayout) v2;
        bt_start.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_load.setOnClickListener(this);

        mPreferences = context.getSharedPreferences("bzy", Context.MODE_PRIVATE);
        mMaxScore = mPreferences.getInt("max_score", 0);
        mPlayer = mPreferences.getString("player_name", "无");

        tv_maxScore.setText("最高分：" + mMaxScore);
        tv_playerName.setText("保持者：" + mPlayer);
    }

    //开始游戏，随机生成2个数字块
    @SuppressLint("ClickableViewAccessibility")
    private void startGame(boolean isLoadGame) {
        game_layout.setOnTouchListener(this);

        if (isLoadGame) {
            return;
        }

        if (bc == null) {
            bc = new BlocksController(context, game_layout, this);
        } else {
            bc.remove_all_blocks();
            bc.setScore(0);
        }

        if (DBG) {
            bc.createForTest(src);
        } else {
            bc.create(2);
        }
        updateScore(bc.getScore() + "");
    }

    //保存游戏
    private void saveGame() {

    }

    //载入游戏
    private void loadGame() {
        if (false)
            startGame(true);
        else
            Toast.makeText(context, "没有保存的进度", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void stopGame() {

        if (bc.getScore() >= mMaxScore) {
            dialog.show();
            et = dialog.findViewById(R.id.id_et);
            bt_ok = dialog.findViewById(R.id.input_done);
            bt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = et.getText().toString();
                    int score = bc.getScore();

                    Message message = new Message();
                    message.what = 156277;
                    Bundle data = new Bundle();
                    data.putString("player_name", name);
                    data.putInt("max_score", score);
                    message.setData(data);
                    handler.sendMessage(message);

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("player_name", name);
                    editor.putInt("max_score", score);
                    editor.apply();
                    dialog.cancel();
                }
            });
        }
        game_layout.setOnTouchListener(null);
    }


    private float x1;
    private float y1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float y2 = event.getY();

                if (Math.abs(x1 - x2) < 50 && Math.abs(y1 - y2) < 50) {
                    break;
                }
                int direction;
                if (Math.abs(x1 - x2) > Math.abs(y1 - y2)) {
                    if (x1 - x2 > 0) {
                        direction = Direction.LEFT;
                    } else {
                        direction = Direction.RIGHT;
                    }
                } else {
                    if (y1 - y2 > 0) {
                        direction = Direction.UP;
                    } else {
                        direction = Direction.DOWN;
                    }
                }
                bc.handleMove(direction);
                if (bc.empty_count == 0) {
                    if (!bc.gameIsOver()) {
                        stopGame();
                        Toast.makeText(context, "Game Over!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                startGame(false);
                break;
            case R.id.bt_save:
                saveGame();
                break;
            case R.id.bt_load:
                loadGame();
                break;
            default:
                break;
        }
    }

    public void updateScore(String score) {
        tv_score.setText("当前：" + score);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 156277) {
                Bundle data = msg.getData();
                String playerName = data.getString("player_name", "");
                int highScore = data.getInt("max_score", 0);
                tv_maxScore.setText("最高分：" + highScore);
                tv_playerName.setText("保持者：" + playerName);
            }
        }
    };
}
