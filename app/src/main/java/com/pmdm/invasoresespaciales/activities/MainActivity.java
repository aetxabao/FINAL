package com.pmdm.invasoresespaciales.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.game.MyApplication;

/**
 * Activity para acceder al juego. Inicia myApp y reproduce música mientras tanto.
 * Al pulsar sobre el activity se accede al activity de juego.
 */
public class MainActivity extends AppCompatActivity {

    MyApplication myApp;
    LinearLayout layout;
    MediaPlayer mp;
    boolean isMusicLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApp = (MyApplication) getApplication();
        myApp.loadHighScore();

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        layout = (LinearLayout) findViewById(R.id.LayoutMain);

        mp = MediaPlayer.create(this, R.raw.start);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!mp.isPlaying() && myApp.sound) {
                    mp.start();
                }
                if (mp.isPlaying() && !myApp.sound) {
                    mp.pause();
                }
                isMusicLoaded = true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        myApp.level = 1;
        myApp.lifes = myApp.LIFES;
        myApp.score = 0;

        if (isMusicLoaded){
            if (!mp.isPlaying() && myApp.sound) {
                mp.start();
            }
            if (mp.isPlaying() && !myApp.sound) {
                mp.pause();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApp.saveHighScore();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }

    /**
     * Define el área de juego y da paso al activity de juego
     * @param v la vista
     * @return devuelve false
     */
    public boolean btnPlay(View v) {
        int w = layout.getWidth();
        int h = layout.getHeight();
        myApp.screenWidth = w;
        myApp.screenHeight = h;
        //myApp.gameRect = new Rect(0,(int)(myApp.screenHeight*0.1),myApp.screenWidth,(int)(myApp.screenHeight*0.85));
        myApp.gameRect = new Rect(0, 120, w, h-w/3);
        Log.d("LOG-MainActivity", myApp.gameRect.width() + "x" + myApp.gameRect.height());
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        return false;
    }

    /**
     * Accede al activity con los mejores resultados
     * @param v la vista
     * @return devuelve false
     */
    public boolean btnHighScore(View v) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        return false;
    }

    /**
     * Accede al activity con la configuración
     * @param v la vista
     * @return devuelve false
     */
    public boolean btnSetUp(View v) {
        Intent intent = new Intent(this, SetUpActivity.class);
        startActivity(intent);
        return false;
    }
}
