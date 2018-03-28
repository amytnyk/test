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
    public List<Pair<Integer, Integer>> green;
    public List<Pair<Integer, Integer>> red;
    public List<Pair<Integer, Integer>> toys;
    public int size;
    public Step step;
    public int width = getResources().getDisplayMetrics().widthPixels;
    public int height = getResources().getDisplayMetrics().heightPixels;
    public Rect start_rect;
    public Rect ok_rect;
    public Rect settings_rect;
    public boolean show;
    public int language;
    public boolean stopped;
    public Game game;
    public int field_size;
    public int p;

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
        text_paint.setTextSize(height / 25);
        text_paint.setTextAlign(Paint.Align.CENTER);
        show = true;
        p = height / 128;
    }


    public void update(int size, boolean stopped, int language, Step step, List<Pair<Integer, Integer>> toys, List<Pair<Integer, Integer>> green, List<Pair<Integer, Integer>> red, int field_size) {
        this.toys = toys;
        this.green = green;
        this.red = red;
        this.field_size = field_size;
        this.step = step;
        this.language = language;
        this.stopped = stopped;
        this.size = size;
    }

    public void touch(MotionEvent event) {
        if (ok_rect.contains((int) event.getX(), (int) event.getY())) {
            if (step == Step.Playing) {
                if (stopped)
                    game.cont();
                else
                    game.stop();
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
                game.start();
                return;
            }
            if (step == Step.Playing) {
                show = show ? false : true;
                postInvalidate();
                return;
            }
            if (step == Step.End2) {
                step = Step.None;
                game.step = step;
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
                game.touch(pos_x, pos_y);
                break;
            case End2:
                break;
        }

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
        canvas.drawText((language == 0) ? "Settings" : (language == 1) ? "Налаштування" : "Настройки", settings_rect.centerX(), settings_rect.centerY() + p, text_paint);
        switch (step) {
            case None:
                // Draw Start button
                canvas.drawRect(start_rect, ok_paint);
                canvas.drawText((language == 0) ? "Start" : (language == 1) ? "Старт" : "Пуск", start_rect.centerX(), start_rect.centerY() + p, text_paint);
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

                canvas.drawText(show ? ((language == 0) ? "hide" : (language == 1) ? "Скрити" : "Скрыть") : ((language == 0) ? "show" : (language == 1) ? "Показати" : "Показать"), start_rect.centerX(), start_rect.centerY() + p, text_paint);

                canvas.drawText(stopped ? ((language == 0) ? "continue" : (language == 1) ? "Продовжити" : "Продолжить") : ((language == 0) ? "stop" : (language == 1) ? "Стоп" : "Стоп"), ok_rect.centerX(), ok_rect.centerY() + p, text_paint);
                break;
            case End2:
                // Draw OK button
                canvas.drawRect(start_rect, ok_paint);
                canvas.drawText("OK", start_rect.centerX(), start_rect.centerY() + p, text_paint);
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
