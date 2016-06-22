package com.pmdm.invasoresespaciales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.game.MyApplication;
import com.pmdm.invasoresespaciales.game.Score;

public class NewScoreActivity extends AppCompatActivity implements View.OnTouchListener{

    MyApplication myApp;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_score);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        layout = (RelativeLayout) findViewById(R.id.LayoutNewScore);
        layout.setOnTouchListener(this);

        myApp = (MyApplication) getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.position)).setText("Position: " + myApp.highScore.getPosition(myApp.score));
        ((TextView) findViewById(R.id.level)).setText("Level: " + myApp.level);
        ((TextView) findViewById(R.id.points)).setText("Points: " + myApp.score);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String name = ((EditText)findViewById(R.id.txtName)).getText().toString();
        Score score = new Score(name, myApp.level, myApp.score);
        myApp.highScore.putScore(score);
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        NewScoreActivity.this.finish();
        return false;
    }
}
