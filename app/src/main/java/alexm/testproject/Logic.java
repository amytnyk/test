package alexm.testproject;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by alexm on 27.03.2018.
 */

public class Logic {
    public List<Pair<Integer, Integer>> toys;
    public List<Pair<Integer, Integer>> moves;
    public MediaPlayer mp;
    public int size;
    public int[][] sounds = { {
            R.raw.up_left, R.raw.up, R.raw.up_right,
            R.raw.left, R.raw.right,
            R.raw.down_left, R.raw.down, R.raw.down_right}, {
            R.raw.up_left_ua, R.raw.up_ua, R.raw.up_right_ua,
            R.raw.left_ua, R.raw.right_ua,
            R.raw.down_left_ua, R.raw.down_ua, R.raw.down_right_ua}, {
            R.raw.up_left_ru, R.raw.up_ru, R.raw.up_right_ru,
            R.raw.left_ru, R.raw.right_ru,
            R.raw.down_left_ru, R.raw.down_ru, R.raw.down_right_ru},
    };

    public int[][] dsounds = { {
            R.raw.up, R.raw.left, R.raw.right, R.raw.down}, {
            R.raw.up_ua, R.raw.left_ua, R.raw.right_ua, R.raw.down_ua}, {
            R.raw.up_ru, R.raw.left_ru, R.raw.right_ru, R.raw.down_ru}
    };

    public int[][] nsounds = {
            { R.raw.one, R.raw.two, R.raw.three, R.raw.four, R.raw.five, R.raw.six },
            { R.raw.one_ua, R.raw.two_ua, R.raw.three_ua, R.raw.four_ua, R.raw.five_ua, R.raw.six_ua },
            { R.raw.one_ru , R.raw.two_ru, R.raw.three_ru, R.raw.four_ru, R.raw.five_ru, R.raw.six_ru },
    };


    public Logic(List<Pair<Integer, Integer>> toys, List<Pair<Integer, Integer>> moves, int size) {
        this.toys = toys;
        this.moves = moves;
        this.size = size;
    }

    public int makeTurn(int i) {
        int pos_x = toys.get(i).first;
        int pos_y = toys.get(i).second;
        boolean done = true;
        int index;
        do {
            Random r = new Random();
            index = Math.abs(r.nextInt() % moves.size());
            if (pos_x + moves.get(index).first >= 0 && pos_x + moves.get(index).first < size && pos_y + moves.get(index).second >= 0 && pos_y + moves.get(index).second < size) {
                done = false;
            }
        } while (done);
        return index;
    }

    public void all(Context context) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp = MediaPlayer.create(context, R.raw.all);
        try {
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.start();
    }

    public void playSound(final int ti, boolean d, int language, int n, Context context) {
        mp = MediaPlayer.create(context, nsounds[language][n]);
        try {
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (d)
            mp = MediaPlayer.create(context, sounds[language][ti]);
        else
            mp = MediaPlayer.create(context, dsounds[language][ti]);
        try {
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.start();

        if (ti == 0) {
            Log.d("TEST", "UP-LEFT");
        }
        if (ti == 1) {
            Log.d("TEST", "UP");
        }
        if (ti == 2) {
            Log.d("TEST", "UP-RIGHT");
        }
        if (ti == 3) {
            Log.d("TEST", "LEFT");
        }
        if (ti == 4) {
            Log.d("TEST", "RIGHT");
        }
        if (ti == 5) {
            Log.d("TEST", "DOWN_LEFT");
        }
        if (ti == 6) {
            Log.d("TEST", "DOWN");
        }
        if (ti == 7) {
            Log.d("TEST", "DOWN_RIGHT");
        }
    }

}
