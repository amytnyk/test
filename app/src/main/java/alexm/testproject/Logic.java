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
    public int[] sounds = {
            R.raw.up_left, R.raw.up, R.raw.up_right,
            R.raw.left, R.raw.right,
            R.raw.down_left, R.raw.down, R.raw.down_right
    };

    public int[] nsounds = {
        R.raw.one, R.raw.two, R.raw.three, R.raw.four, R.raw.five, R.raw.six
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
            index = Math.abs(r.nextInt() % 8);
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

    public void playSound(final int ti, int n, Context context) {
        mp = MediaPlayer.create(context, nsounds[n]);
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
        mp = MediaPlayer.create(context, sounds[ti]);
        try {
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.start();

        if (ti == 0) {
            Log.d("TEST", "UP-LEFT");
            //Toast.makeText(, "UP-LEFT", Toast.LENGTH_SHORT).show();
        }
        if (ti == 1) {
            Log.d("TEST", "UP");
            //Toast.makeText(MainActivity.this, "UP", Toast.LENGTH_SHORT).show();
        }
        if (ti == 2) {
            Log.d("TEST", "UP-RIGHT");
            //Toast.makeText(MainActivity.this, "UP-RIGHT", Toast.LENGTH_SHORT).show();
        }
        if (ti == 3) {
            Log.d("TEST", "LEFT");
            // Toast.makeText(MainActivity.this, "LEFT", Toast.LENGTH_SHORT).show();
        }
        if (ti == 4) {
            Log.d("TEST", "RIGHT");
            //Toast.makeText(MainActivity.this, "RIGHT", Toast.LENGTH_SHORT).show();
        }
        if (ti == 5) {
            Log.d("TEST", "DOWN_LEFT");
            //Toast.makeText(MainActivity.this, "DOWN_LEFT", Toast.LENGTH_SHORT).show();
        }
        if (ti == 6) {
            Log.d("TEST", "DOWN");
            //Toast.makeText(MainActivity.this, "DOWN", Toast.LENGTH_SHORT).show();
        }
        if (ti == 7) {
            Log.d("TEST", "DOWN_RIGHT");
            // Toast.makeText(MainActivity.this, "DOWN_RIGHT", Toast.LENGTH_SHORT).show();
        }
    }

}
