package alexm.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.jar.Attributes;

/**
 * Created by alexm on 26.03.2018.
 */

public class MyView extends View {
    public Paint paint;
    public Paint ok_paint;
    public Paint start_paint;
    public Paint grid_paint;
    public Paint text_paint;
    public int field_size;
    public List<Pair<Integer, Integer>> green;
    public List<Pair<Integer, Integer>> red;
    public boolean s;
    List<Pair<Integer, Integer>> toys;
    List<Pair<Integer, Integer>> moves;
    List<Pair<Integer, Integer>> d_moves;
    List<Pair<Integer, Integer>> wd_moves;
    public int turns;
    public int sleeptime;
    public Thread thread;
    public int size;
    public int toys_count;
    public Step step;
    public Logic logic;
    public int touch_count;
    public int width = getResources().getDisplayMetrics().widthPixels;
    public int height = getResources().getDisplayMetrics().heightPixels;
    public Rect start_rect;
    public Rect ok_rect;
    public Rect settings_rect;
    public boolean show;
    public int g = 0;
    public int last_index;
    public boolean d;
    public int language;
    public boolean stopped;

    public int[] colors = {
            Color.BLUE, Color.rgb(0, 0, 200), Color.rgb(0, 0, 150), Color.rgb(0, 0, 100), Color.rgb(0, 100, 150), Color.rgb(100, 0, 200)
    };


    public MyView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(8);
        grid_paint = new Paint();
        grid_paint.setColor(Color.DKGRAY);
        grid_paint.setStrokeWidth(8);
        ok_paint = new Paint(Color.GREEN);
        ok_paint.setColor(Color.LTGRAY);
        start_paint = new Paint(Color.GRAY);
        start_paint.setColor(Color.LTGRAY);
        start_rect = new Rect(width / 2 - width / 3 / 2, height / 12 * 8, width / 2 + width / 3 / 2, height / 12 * 9);
        ok_rect = new Rect(width / 2 - width / 3 / 2, height / 12 * 9, width / 2 + width / 3 / 2, height / 12 * 10);
        settings_rect = new Rect(0, height / 12 * 11, width / 2, height);
        text_paint = new Paint();
        text_paint.setColor(Color.rgb(0, 200, 0));
        text_paint.setTextSize(50);
        text_paint.setTextAlign(Paint.Align.CENTER);
        green = new ArrayList<>();
        red = new ArrayList<>();
        s = false;
        field_size = 200;
        loadSettings();
        if (field_size * size > width)
            field_size = width / size - 5;
        init();
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

    public void loadSettings() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
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
            field_size = width / size - 5;
    }

    public void cont() {
        stopped = false;
    }

    public void touch(MotionEvent event) {
        if (ok_rect.contains((int) event.getX(), (int) event.getY())) {
            if (step == Step.Playing) {
                if (stopped)
                    cont();
                else
                    stop();
                return;
            }
        }
        if (settings_rect.contains((int) event.getX(), (int) event.getY())) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
        if (start_rect.contains((int) event.getX(), (int) event.getY())) {
            if (step == Step.None) {
                start();
                return;
            }
            if (step == Step.Playing) {
                show = show ? false : true;
                postInvalidate();
                return;
            }
            if (step == Step.End2) {
                step = Step.None;
                postInvalidate();
                return;
            }
        }
        switch (step) {
            case None:
                break;
            case Playing:
                break;
            case End:
                int pos_x = (int) (event.getX() / field_size);
                int pos_y = (int) (event.getY() / field_size);
                if (toys.get(touch_count).first == pos_x && toys.get(touch_count).second == pos_y) {
                    green.add(Pair.create(pos_x, pos_y));
                } else {
                    red.add(Pair.create(toys.get(touch_count).first, toys.get(touch_count).second));
                }
                invalidate();
                touch_count++;
                if (touch_count == toys.size()) {
                    step = Step.End2;
                }
                break;
            case End2:
                break;
        }

    }

    public void stop() {
        stopped = true;
        postInvalidate();
    }

    public void start() {
        loadSettings();
        touch_count = 0;
        last_index = 0;
        step = Step.Playing;
        green.clear();
        red.clear();
        stopped = false;
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
                        logic.playSound(ti, d, language, index, getContext());
                        toys.set(index, Pair.create(toys.get(index).first + moves.get(ti).first, toys.get(index).second + moves.get(ti).second));
                        //invalidate();
                        postInvalidate();
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
                logic.all(getContext(), language);
                step = Step.End;
                postInvalidate();
            }
        });
        thread.start();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        // Draw grid
        for (int i = 0;i < size + 1;i++) {
            canvas.drawLine(field_size * i, 0, field_size * i, field_size * size, grid_paint);
        }
        for (int i = 0;i < size + 1;i++) {
            canvas.drawLine(0, field_size * i, field_size * size, field_size * i, grid_paint);
        }
        // Draw settings button
        canvas.drawRect(settings_rect, start_paint);
        canvas.drawText((language == 0) ? "Settings" : (language == 1) ? "Налаштування" : "Настройки", settings_rect.centerX(), settings_rect.centerY(), text_paint);
        switch (step) {
            case None:
                // Draw Start button
                canvas.drawRect(start_rect, ok_paint);
                canvas.drawText((language == 0) ? "Start" : (language == 1) ? "Старт" : "Пуск", start_rect.centerX(), start_rect.centerY(), text_paint);
                break;
            case Playing:
                if (show) {
                    for (int i = 0; i < toys.size(); i++) {
                        paint.setColor(colors[i]);
                        canvas.drawCircle(toys.get(i).first * field_size + field_size / 2, toys.get(i).second * field_size + field_size / 2, field_size / 2, paint);
                    }
                }
                // Draw show / hide button
                canvas.drawRect(start_rect, ok_paint);
                // Draw stop button
                canvas.drawRect(ok_rect, ok_paint);

                canvas.drawText(show ? ((language == 0) ? "hide" : (language == 1) ? "Скрити" : "Скрыть") : ((language == 0) ? "show" : (language == 1) ? "Показати" : "Показать"), start_rect.centerX(), start_rect.centerY(), text_paint);

                canvas.drawText(stopped ? ((language == 0) ? "continue" : (language == 1) ? "Продовжити" : "Продолжить") : ((language == 0) ? "stop" : (language == 1) ? "Стоп" : "Стоп"), ok_rect.centerX(), ok_rect.centerY(), text_paint);
                break;
            case End2:
                // Draw OK button
                canvas.drawRect(start_rect, ok_paint);
                canvas.drawText("OK", start_rect.centerX(), start_rect.centerY(), text_paint);
            case End:
                for (int i = 0; i < green.size(); i++) {
                    paint.setColor(Color.GREEN);
                    canvas.drawCircle(green.get(i).first * field_size +field_size / 2, green.get(i).second * field_size + field_size / 2, field_size / 2, paint);
                }
                for (int i = 0; i < red.size(); i++) {
                    paint.setColor(Color.RED);
                    canvas.drawCircle(red.get(i).first * field_size + field_size / 2, red.get(i).second * field_size + field_size / 2, field_size / 2, paint);
                }
                break;
            default:
                break;
        }
    }
}
