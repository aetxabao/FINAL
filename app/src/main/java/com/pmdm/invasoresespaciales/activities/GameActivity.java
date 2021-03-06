package com.pmdm.invasoresespaciales.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.pmdm.invasoresespaciales.game.Game;

/**
 * Activity en el que se desarrolla el juego
 */
public class GameActivity extends AppCompatActivity {

    Game gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gv = new Game(this);
        setContentView(gv);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        gv.startGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        gv.stopGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gv.destroy();
    }
}
