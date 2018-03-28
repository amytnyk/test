package alexm.testproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        game = new Game(getApplicationContext());

        game.mv = new MyView(getApplicationContext());
        game.mv.step = Step.None;
        setContentView(game.mv);

        game.mv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               game.mv.touch(motionEvent);
                return false;
            }
        });

        game.set();
    }

    @Override
    public void onResume() {
        super.onResume();
        game.loadSettings();
    }

    @Override
    public void onPause() {
        super.onPause();game.pause();
    }
}
