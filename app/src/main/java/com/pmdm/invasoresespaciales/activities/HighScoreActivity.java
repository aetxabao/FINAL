package com.pmdm.invasoresespaciales.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.game.MyApplication;

public class HighScoreActivity extends AppCompatActivity implements View.OnTouchListener{

    MyApplication myApp;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        layout = (LinearLayout) findViewById(R.id.LayoutHighScore);
        layout.setOnTouchListener(this);

        myApp = (MyApplication) getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] records = myApp.highScore.getHighScores();
        try {
            ((TextView) findViewById(R.id.highScore1)).setText(records[0]);
            ((TextView) findViewById(R.id.highScore2)).setText(records[1]);
            ((TextView) findViewById(R.id.highScore3)).setText(records[2]);
            ((TextView) findViewById(R.id.highScore4)).setText(records[3]);
            ((TextView) findViewById(R.id.highScore5)).setText(records[4]);
        }catch (Exception e){ }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        HighScoreActivity.this.finish();
        return false;
    }

}
