package alexm.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alexm on 28.03.2018.
 */

public class Game {
    public int language;
    public Context context;
    List<Pair<Integer, Integer>> toys;
    List<Pair<Integer, Integer>> moves;
    List<Pair<Integer, Integer>> d_moves;
    List<Pair<Integer, Integer>> wd_moves;
    public int field_size;
    public Step step;
    public int toys_count;
    public boolean d;
    public Logic logic;
    public int touch_count;
    public List<Pair<Integer, Integer>> green;
    public List<Pair<Integer, Integer>> red;
    public Thread thread;
    public boolean stopped;
    public int last_index;
    public int size;
    public int turns;
    public int sleeptime;
    public MyView mv;
    public int g;
    public int width;
    public int height;

    public void set() {
        mv.game = this;
        height = mv.height;
        width = mv.width;
        field_size = width / 4;
        loadSettings();
        inval();
    }

    public Game(Context context) {
        this.context = context;
        green = new ArrayList<>();
        red = new ArrayList<>();
        loadSettings();
        init();

    }

    public void inval() {
        mv.update(size, stopped, language, step, toys, green, red, field_size);
        mv.postInvalidate();
    }

    public void touch(int x, int y) {
        if (step == Step.End) {
            if (toys.get(touch_count).first == x && toys.get(touch_count).second == y) {
                green.add(Pair.create(x, y));
            } else {
                red.add(Pair.create(toys.get(touch_count).first, toys.get(touch_count).second));
            }
            inval();
            touch_count++;
            if (touch_count == toys.size()) {
                step = Step.End2;
                inval();
            }
        }
    }

    public void cont() {
        stopped = false;
    }

    public void ok() {

    }

    public void stop() {
        stopped = true;
        inval();
    }

    public void pause() {

    }


    public void init() {
        wd_moves = new ArrayList<>();
        step = Step.None;
        wd_moves.add(Pair.create(-1, -1));
        wd_moves.add(Pair.create(0, -1));
        wd_moves.add(Pair.create(1, -1));
        wd_moves.add(Pair.create(-1, 0));
        wd_moves.add(Pair.create(1, 0));
        wd_moves.add(Pair.create(-1, 1));
        wd_moves.add(Pair.create(0, 1));
        wd_moves.add(Pair.create(1, 1));
        d_moves = new ArrayList<>();
        d_moves.add(Pair.create(0, -1));
        d_moves.add(Pair.create(-1, 0));
        d_moves.add(Pair.create(1, 0));
        d_moves.add(Pair.create(0, 1));
        toys = new ArrayList<>();
        for (int i = 0; i < toys_count; i++) {
            toys.add(Pair.create(0, 0));
        }

        if (d)
            moves = wd_moves;
        else
            moves = d_moves;
    }

    public void start() {
        loadSettings();
        touch_count = 0;
        last_index = 0;
        step = Step.Playing;
        green.clear();
        red.clear();
        stopped = false;
        toys = new ArrayList<>();
        for (int i = 0; i < toys_count; i++) {
            toys.add(Pair.create(0, 0));
        }
        logic =  new Logic(toys, moves, size);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                for (int i = 0; i < turns; i++) {
                    if (!stopped) {
                        Random r = new Random();
                        int index = Math.abs(r.nextInt() % toys.size());
                        if (index == last_index)
                            g++;
                        else
                            g = 0;
                        if (g > 2) {
                            r = new Random();
                            index = Math.abs(r.nextInt() % toys.size());
                            g = 0;
                        }
                        last_index = index;
                        int ti = logic.makeTurn(index);
                        logic.playSound(ti, d, language, index, context);
                        toys.set(index, Pair.create(toys.get(index).first + moves.get(ti).first, toys.get(index).second + moves.get(ti).second));
                        //invalidate();
                        inval();
                        try {
                            Thread.sleep(sleeptime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        i--;
                    }
                }
                logic.all(context, language);
                step = Step.End;
                inval();
            }
        });
        thread.start();
    }

    public void loadSettings() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String p_turns = prefs.getString("turns", "10");
        turns = Integer.parseInt(p_turns);
        String p_toys = prefs.getString("toys", "1");
        toys_count = Integer.parseInt(p_toys);
        String p_sleeptime = prefs.getString("sleeptime", "2000");
        sleeptime = Integer.parseInt(p_sleeptime);
        String p_map = prefs.getString("map", "3");
        size = Integer.parseInt(p_map);
        d = prefs.getBoolean("d", true);
        String v = prefs.getString("language", "English");
        switch (v) {
            case "English":
                language = 0;
                break;
            case "Ukrainian":
                language = 1;
                break;
            case "Russian":
                language = 2;
                break;
        }
        if (v == "English")
            language = 0;
        if (v == "Ukrainian")
            language = 1;
        if (v == "Russian")
            language = 2;
        if (field_size * size > width)
            field_size = width / size;
    }

}
