package com.example.bzy.my2048.controller;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bzy.my2048.MainActivity;
import com.example.bzy.my2048.R;
import com.example.bzy.my2048.controller.BlocksController;
import com.example.bzy.my2048.sounds.GlobalSoundManager;
import com.example.bzy.my2048.sounds.SoundType;
import com.example.bzy.my2048.utils.Constants;
import com.example.bzy.my2048.utils.Direction;
import com.example.bzy.my2048.utils.LogUtils;
import com.example.bzy.my2048.view.GridTableView;
import com.example.bzy.my2048.view.MyDialog;

public class GameController implements View.OnClickListener,View.OnTouchListener{

    private static GameController sInstance = null;
    private final int[] src =
            {2, 8, 4, 2,
                    4096, 4, 8, 4,
                    4, 8, 4, 8,
                    8, 4, 2048, 1024};
    private BlocksController bc;
    private GridTableView gridView;
    private Context context;
    private int mMaxScore;
    private String mPlayer;
    private SharedPreferences mPreferences;
    private Button bt_ok;
    private EditText et;
    private MyDialog dialog;
    private LinearLayout mControlPad;

    private Button startButton;

    private GameController(Context context) {
        this.context = context;
        dialog = new MyDialog(context);

        mControlPad = ((MainActivity) context).findViewById(R.id.control_pad);
        gridView = ((MainActivity) context).findViewById(Constants.Id.ID_GRID_VIEW);
        LogUtils.log("GameController gridView = " + gridView + " , context = " + context);

        startButton = mControlPad.findViewById(R.id.bt_start);
        startButton.setOnClickListener(this);

        mPreferences = context.getSharedPreferences("bzy", Context.MODE_PRIVATE);
        mMaxScore = mPreferences.getInt("max_score", 0);
        mPlayer = mPreferences.getString("player_name", "无");
    }

    public static GameController getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GameController(context);
        }
        return sInstance;
    }

    private void startGame(boolean isLoadGame) {
        gridView.setOnTouchListener(this);
        if (isLoadGame) {
            return;
        }

        if (bc == null) {
            bc = BlocksController.getInstance(context);
        } else {
            bc.remove_all_blocks();
            bc.setScore(0);
        }

        if (LogUtils.DBG) {
            bc.createForTest(src);
        } else {
            bc.create(2);
        }
        updateScore(bc.getScore() + "");
        GlobalSoundManager.getInstance().playSound(SoundType.START);
    }

    private void saveGame() {

    }

    private void loadGame() {

    }

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
        gridView.setOnTouchListener(null);
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

    private float x1 = 0;
    private float y1 = 0;
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
//                        stopGame();
//                        Toast.makeText(context, "Game Over!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
        return true;
    }

    public void updateScore(String score) {
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 156277) {
                Bundle data = msg.getData();
                String playerName = data.getString("player_name", "");
                int highScore = data.getInt("max_score", 0);
            }
        }
    };
}
