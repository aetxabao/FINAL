package com.pmdm.invasoresespaciales.sprites;

import android.graphics.Bitmap;

import com.pmdm.invasoresespaciales.game.Game;

public class EnemyRocket extends Enemy {

    public EnemyRocket(Game gv, Bitmap bmp, int N) {
        super(gv, bmp, N);
    }

    @Override
    public void update() {
        if (x > gv.getGameRect().right - width - xSpeed || x + xSpeed < gv.getGameRect().left) {
            xSpeed = -xSpeed;
        }
        if (y > gv.getGameRect().bottom - height) {
            y = gv.getGameRect().top + height;
        }
        x = x + xSpeed;
        y = y + ySpeed;
        updateFrame();
    }

}

