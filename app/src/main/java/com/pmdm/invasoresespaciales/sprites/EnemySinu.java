package com.pmdm.invasoresespaciales.sprites;

import android.graphics.Bitmap;
import android.util.Log;

import com.pmdm.invasoresespaciales.game.Game;

/**
 * Los enemigos se desplazan de forma sinusoidal.
 */
public class EnemySinu extends Enemy {

    int yRef;

    public EnemySinu(Game gv, Bitmap bmp, int N) {
        super(gv, bmp, N);
    }

    @Override
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
        yRef = y;
    }

    @Override
    public void update() {
        if (x > gv.getGameRect().right - width - xSpeed || x + xSpeed < gv.getGameRect().left) {
            xSpeed = -xSpeed;
            yRef = yRef + height;
        }
        x = x + xSpeed;
        y = (int) (yRef + height*Math.sin(2*(double)x/(double)(width)));
        Log.d("LOG-EnemySinu",x+","+y);
        updateFrame();
    }

}
