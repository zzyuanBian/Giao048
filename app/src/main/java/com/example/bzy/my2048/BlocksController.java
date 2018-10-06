package com.example.bzy.my2048;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Arrays;

public class BlocksController {
    int[] data = new int[16];

    int empty_count = 16;
    private FrameLayout game_layout;
    private Context context;
    private int WIDTH = (int) Util.WIDTH;
    private int[][][] positions;
    private BlockView[] blockViews = new BlockView[16];
    private int steps_all = 0;
    private int mScore;
    private GameController gc;

    BlocksController(Context context, View v, GameController controller) {
        gc = controller;
        this.context = context;
        game_layout = (FrameLayout) v;
        for (int i = 0; i < 16; i++) {
            data[i] = 0;
        }
        mScore = 0;
        positions = new int[][][]{
                {{0, 0}, {WIDTH / 4, 0}, {WIDTH / 2, 0}, {WIDTH * 3 / 4, 0}},
                {{0, WIDTH / 4}, {WIDTH / 4, WIDTH / 4}, {WIDTH / 2, WIDTH / 4}, {WIDTH * 3 / 4, WIDTH / 4}},
                {{0, WIDTH / 2}, {WIDTH / 4, WIDTH / 2}, {WIDTH / 2, WIDTH / 2}, {WIDTH * 3 / 4, WIDTH / 2}},
                {{0, WIDTH * 3 / 4}, {WIDTH / 4, WIDTH * 3 / 4}, {WIDTH / 2, WIDTH * 3 / 4}, {WIDTH * 3 / 4, WIDTH * 3 / 4}}
        };

    }


    public void handleMove(int dir) {
        switch (dir) {
            case Direction.UP:
            case Direction.DOWN:
                upOrDown(dir);
                break;
            case Direction.LEFT:
            case Direction.RIGHT:
                leftOrRight(dir);
                break;
            default:
                break;
        }
    }


    private void upOrDown(int dir) {
        int line = 0;
        while (line != 4) {
            int mergeCount;
            int[] pre = new int[4];
            for (int i = line; i < 16; i += 4) {
                pre[(i - line) / 4] = data[i];
            }
            if (pre[0] == 0 && pre[0] == pre[1] && pre[0] == pre[2] && pre[0] == pre[3]) {
                line++;
                continue;
            }
            mergeCount = getMergeCount(pre);
            doMove(dir, pre, line, mergeCount);
            line++;
        }
        if (steps_all != 0) {
            create(1);
            steps_all = 0;
        }
    }

    private void oneMergeDown(int[] pre, int line) {
        int[] post = new int[4];
        int first_position = 3;
        int[] temp = copyArr(pre);
        boolean mergeDone = false;
        for (int i = 3; i >= 0; i--) {
            if (temp[i] == 0) {
                continue;
            }
            //找相同
            for (int j = i - 1; j >= 0 && !mergeDone; ) {
                if (temp[i] != temp[j] && temp[j] != 0) {
                    if (!mergeDone && i != first_position) {
                        remove_blocks(line + i * 4);
                        _createBlockView(pre[i], line + 4 * first_position);
                    }
                    first_position--;
                    break;
                }

                if (temp[j] == temp[i]) {
                    temp[j] = 0;
                    post[first_position] = 2 * pre[i];
                    remove_blocks(line + i * 4);
                    remove_blocks(line + j * 4);
                    _createBlockView(2 * pre[i], 4 * first_position + line);
                    mergeDone = true;
                    i--;
                    first_position--;
                    break;
                }
                j--;
                //没找到相同
                if (j < 0 && !mergeDone) {
                    first_position--;
                }
            }
            //已经merge，只移动后排
            if (mergeDone) {
                if (temp[i] != 0) {
                    remove_blocks(line + i * 4);
                    _createBlockView(temp[i], 4 * first_position + line);
                    first_position--;
                }
            }
        }

    }

    private void oneMergeUp(int[] pre, int line) {
        int first_position = 0;
        int[] temp = copyArr(pre);
        boolean mergeDone = false;
        for (int i = 0; i < 4; i++) {
            if (temp[i] == 0) {
                continue;
            }
            //找相同
            for (int j = i + 1; j < 4 && !mergeDone; ) {
                if (temp[i] != temp[j] && temp[j] != 0) {
                    if (!mergeDone && i != first_position) {
                        remove_blocks(line + 4 * i);
                        _createBlockView(pre[i], line + 4 * first_position);
                    }
                    first_position++;
                    break;
                }

                if (temp[j] == temp[i]) {
                    temp[j] = 0;
                    remove_blocks(line + i * 4);
                    remove_blocks(line + j * 4);
                    _createBlockView(2 * pre[i], 4 * first_position + line);
                    mergeDone = true;
                    i++;
                    first_position++;
                    break;
                }
                j++;
                //没找到相同
                if (j == 4 && !mergeDone) {
                    first_position++;
                }
            }
            //已经merge，只移动后排
            if (mergeDone) {
                if (temp[i] != 0) {
                    remove_blocks(line + i * 4);
                    _createBlockView(temp[i], 4 * first_position + line);
                    first_position++;
                }
            }
        }
    }

    private void onlyMoveDown(int[] pre, int line) {
        int step = 0;
        for (int i = 3; i >= 0; i--) {
            if (pre[i] == 0) {
                step++;
            } else {
                if (step != 0) {
                    remove_blocks(4 * i + line);
                    _createBlockView(pre[i], 4 * (i + step) + line);
                }
            }
        }
    }

    private void onlyMoveUp(int[] pre, int line) {
        int step = 0;
        for (int i = 0; i < 4; i++) {
            if (pre[i] == 0) {
                step++;
            } else {
                if (step != 0) {
                    remove_blocks(4 * i + line);
                    _createBlockView(pre[i], 4 * (i - step) + line);
                }
            }
        }
    }


    public void leftOrRight(int dir) {
        int line = 0;
        while (line != 4) {
            int mergeCount = 0;
            int[] pre = new int[4];

            for (int i = 0; i < 4; i++) {
                pre[i] = data[4 * line + i];
            }
            if (pre[0] == 0 && pre[0] == pre[1] && pre[0] == pre[2] && pre[0] == pre[3]) {
                line++;
                continue;
            }
            mergeCount = getMergeCount(pre);
            doMove(dir, pre, line, mergeCount);
            line++;
        }

        if (steps_all != 0) {
            create(1);
            steps_all = 0;
        }

    }

    private void doMove(int dir, int[] pre, int line, int mergeCount) {

        if (mergeCount == 0) {
            if (dir == Direction.LEFT) {
                onlyMoveLeft(pre, line);
            } else if (dir == Direction.UP) {
                onlyMoveUp(pre, line);
            } else if (dir == Direction.RIGHT) {
                onlyMoveRight(pre, line);
            } else {
                onlyMoveDown(pre, line);
            }
        } else if (mergeCount == 1) {
            if (dir == Direction.LEFT) {
                oneMergeLeft(pre, line);
            } else if (dir == Direction.UP) {
                oneMergeUp(pre, line);
            } else if (dir == Direction.RIGHT) {
                oneMergeRight(pre, line);
            } else {
                oneMergeDown(pre, line);
            }
        } else {
            if (dir == Direction.LEFT) {
                twoMergeLeft(pre, line);
            } else if (dir == Direction.UP) {
                twoMergeUp(pre, line);
            } else if (dir == Direction.RIGHT) {
                twoMergeRight(pre, line);
            } else {
                twoMergeDown(pre, line);
            }
        }

        gc.updateScore(mScore + "");

    }

    public boolean gameIsOver() {
        boolean isOver = true;
        for (int i = 0; i < 16; i++) {
            isOver &= _BlockIsDead(i);
        }
        return isOver;
    }

    private boolean _BlockIsDead(int i) {
        if (i == 0) {
            //左上角
            return data[i] != data[i + 1] && data[i] != data[i + 4];
        } else if (i == 3) {
            //右上角
            return data[i] != data[i - 1] && data[i] != data[i + 4];
        } else if (i == 12) {
            //左下角
            return data[i] != data[i + 1] && data[i] != data[i - 4];
        } else if (i == 15) {
            //右下角
            return data[i] != data[i - 1] && data[i] != data[i - 4];
        } else if (i == 4 || i == 8) {
            //左边
            return data[i] != data[i + 1] && data[i] != data[i - 4] && data[i] != data[i + 4];
        } else if (i == 1 || i == 2) {
            //上边
            return data[i] != data[i + 1] && data[i] != data[i - 1] && data[i] != data[i + 4];
        } else if (i == 7 || i == 11) {
            //右边
            return data[i] != data[i - 1] && data[i] != data[i - 4] && data[i] != data[i + 4];
        } else if (i == 13 || i == 14) {
            //下边
            return data[i] != data[i + 1] && data[i] != data[i - 1] && data[i] != data[i - 4];
        } else
            //中间
            return data[i] != data[i + 1] && data[i] != data[i - 1] && data[i] != data[i + 4] && data[i] != data[i - 4];

    }

    private void twoMergeLeft(int[] pre, int line) {
        remove_blocks(4 * line);
        remove_blocks(4 * line + 1);
        _createBlockView(2 * pre[0], 4 * line);
        remove_blocks(4 * line + 2);
        remove_blocks(4 * line + 3);
        _createBlockView(2 * pre[2], 4 * line + 1);

    }

    private void twoMergeUp(int[] pre, int line) {
        remove_blocks(line);
        remove_blocks(line + 4);
        _createBlockView(2 * pre[0], line);

        remove_blocks(line + 8);
        remove_blocks(line + 12);
        _createBlockView(2 * pre[2], line + 4);
    }

    private void twoMergeRight(int[] pre, int line) {
        remove_blocks(4 * line + 2);
        remove_blocks(4 * line + 3);
        _createBlockView(2 * pre[2], 4 * line + 3);

        remove_blocks(4 * line);
        remove_blocks(4 * line + 1);
        _createBlockView(2 * pre[0], 4 * line + 2);

    }

    private void twoMergeDown(int[] pre, int line) {
        remove_blocks(line + 3 * 4);
        remove_blocks(line + 2 * 4);
        _createBlockView(2 * pre[3], line + 3 * 4);

        remove_blocks(line + 4);
        remove_blocks(line);
        _createBlockView(2 * pre[1], line + 2 * 4);
    }

    private int getMergeCount(int[] pre) {
        int mergeCount = 0;
        int[] temp = copyArr(pre);
        for (int i = 0; i < 4; i++) {
            if (temp[i] != 0) {
                for (int j = i + 1; j < 4; j++) {
                    if (temp[j] != 0 && temp[j] != temp[i]) {
                        break;
                    }
                    if (temp[i] == temp[j]) {
                        mergeCount++;
                        temp[j] = 0;
                        break;
                    }
                }
            }
        }
        return mergeCount;
    }


    private int[] copyArr(int[] pre) {
        int[] temp = new int[pre.length];
        for (int i = 0; i < pre.length; i++) {
            temp[i] = pre[i];
        }
        return temp;
    }


    private void onlyMoveLeft(int[] pre, int line) {
        int step = 0;
        for (int i = 0; i < 4; i++) {
            if (pre[i] == 0) {
                step++;
            } else {
                if (step != 0) {
                    remove_blocks(4 * line + i);
                    _createBlockView(pre[i], 4 * line + i - step);
                }
            }
        }
    }

    private void onlyMoveRight(int[] pre, int line) {
        int step = 0;
        for (int i = 3; i >= 0; i--) {
            if (pre[i] == 0) {
                step++;
            } else {
                if (step != 0) {
                    remove_blocks(4 * line + i);
                    _createBlockView(pre[i], 4 * line + i + step);
                }
            }
        }
    }

    private void oneMergeRight(int[] pre, int line) {
        //bug
        //[0 2 0 2]
        //[0 2 0 2]
        int first_position = 3;
        int[] temp = copyArr(pre);
        boolean mergeDone = false;
        for (int i = 3; i >= 0; i--) {

            if (pre[i] == 0) {
                continue;
            }
            for (int j = i - 1; j >= 0 && !mergeDone; ) {
                if (temp[i] != temp[j] && temp[j] != 0) {
                    if (!mergeDone && i != first_position) {
                        remove_blocks(4 * line + i);
                        _createBlockView(pre[i], 4 * line + first_position);
                    }
                    first_position--;
                    break;
                }

                if (temp[j] == temp[i]) {
                    temp[j] = 0;
                    remove_blocks(4 * line + i);
                    remove_blocks(4 * line + j);
                    _createBlockView(2 * pre[i], 4 * line + first_position);
                    mergeDone = true;
                    i--;
                    first_position--;
                    break;
                }
                j--;
                if (j == -1 && !mergeDone) {
                    first_position--;
                }
            }
            if (mergeDone) {
                if (temp[i] != 0) {
                    remove_blocks(4 * line + i);
                    _createBlockView(temp[i], 4 * line + first_position);
                    first_position--;
                }
            }
        }
    }

    private void oneMergeLeft(int[] pre, int line) {
        //
        //[0 64 32 32]
        int first_position = 0;
        int[] temp = copyArr(pre);
        boolean mergeDone = false;
        for (int i = 0; i < 4; i++) {
            if (temp[i] == 0) {
                continue;
            }
            //找相同
            for (int j = i + 1; j < 4 && !mergeDone; ) {
                if (temp[i] != temp[j] && temp[j] != 0) {
                    if (!mergeDone && i != first_position) {
                        remove_blocks(4 * line + i);
                        _createBlockView(pre[i], 4 * line + first_position);
                    }
                    first_position++;
                    break;
                }

                if (temp[j] == temp[i]) {
                    temp[j] = 0;
                    remove_blocks(4 * line + i);
                    remove_blocks(4 * line + j);
                    _createBlockView(2 * pre[i], 4 * line + first_position);
                    mergeDone = true;
                    i++;
                    first_position++;
                    break;
                }
                j++;
                //没找到相同
                if (j == 4 && !mergeDone) {
                    first_position++;
                }
            }
            //已经merge，只移动后排
            if (mergeDone) {
                if (temp[i] != 0) {
                    remove_blocks(4 * line + i);
                    _createBlockView(temp[i], 4 * line + first_position);
                    first_position++;
                }
            }
        }
    }


    public void merge() {

    }

    //空白块随机创建数字块
    public boolean create(int createCount) {
        while (createCount > 0 && empty_count > 0) {
            int seed = (int) (Math.random() * 10);
            int num;
            if (seed < 9) {
                num = 2;
            } else {
                num = 4;
            }
            seed = (int) (Math.random() * 16);
            if (data[seed] == 0) {
                data[seed] = num;
                _createBlockView(num, seed);
                createCount--;

            }
        }
        if (empty_count != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void _createBlockView(int num, int position) {

        int x, y;
        if (position < 4) {
            x = positions[0][position % 4][0];
            y = positions[0][position % 4][1];
        } else if (position < 8) {
            x = positions[1][position % 4][0];
            y = positions[1][position % 4][1];
        } else if (position < 12) {
            x = positions[2][position % 4][0];
            y = positions[2][position % 4][1];
        } else {
            x = positions[3][position % 4][0];
            y = positions[3][position % 4][1];
        }

        int red = 255, green = 255, blue = 255;
        switch (num) {
            case 2:
                red = 187;
                break;
            case 4:
// 30 144 255
                red = 30;
                green = 144;
                blue = 255;
                break;
            case 8:
// 78 238 148
                red = 78;
                green = 238;
                blue = 148;
                break;
            case 16:
// 255 215 0
                green = 215;
                blue = 0;
                break;
            case 32:
// 255 165 0
                green = 165;
                blue = 0;
                break;
            case 64:
// 139 69 19
                red = 139;
                green = 69;
                blue = 19;
                break;
            case 128:
// 255 20 147
                red = 255;
                green = 0;
                blue = 0;
                break;
            case 256:
// 255 192 203
                red = 255;
                green = 192;
                blue = 203;
                break;
            case 512:
// 160 32 240
                red = 160;
                green = 32;
                blue = 240;
                break;
        }

        BlockView bv = new BlockView(context, num, x, y);
        bv.setBackgroundColor(Color.rgb(red, green, blue));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WIDTH / 4 - 20, WIDTH / 4 - 20);
        params.setMargins(10, 10, 10, 10);
        bv.setLayoutParams(params);
        bv.setGravity(Gravity.CENTER);
        if (num < 1024) {
            bv.setTextSize(40);
        } else {
            bv.setTextSize(32);
        }
        bv.setPadding(15, 15, 15, 15);

        game_layout.addView(bv);
        blockViews[position] = bv;
        data[position] = num;
        empty_count--;
        steps_all++;
        mScore += num;
    }

    public void remove_blocks(int position) {
        data[position] = 0;
        game_layout.removeView(blockViews[position]);
        blockViews[position] = null;
        empty_count++;
    }

    public void remove_all_blocks() {
        for (int i = 0; i < 16; i++) {
            remove_blocks(i);
        }
    }

    public void createForTest(int[] src) {
        for (int i = 0; i < 16; i++) {
            if (src[i] == 0)
                continue;
            _createBlockView(src[i], i);
        }
    }


    public void setScore(int score) {
        this.mScore = score;
    }

    public int getScore() {
        return mScore;
    }
}
