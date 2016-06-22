package com.pmdm.invasoresespaciales.sprites;

import android.graphics.Bitmap;

import com.pmdm.invasoresespaciales.game.Game;

/**
 * Enemigo disparador
 */

public class EnemyOneWay extends Enemy {

    public EnemyOneWay(Game gv, Bitmap bmp, int N) {
        super(gv, bmp, N);
    }

    public void update() {
        //Desplazamiento hacia la derecha
        if (xSpeed>0){
            if (x > gv.getGameRect().right - width - xSpeed) {
                x = gv.getGameRect().left;
                y = y + height;
            }
        }
        //Desplazamiento hacia la izquierda
        if (xSpeed<0){
            if (x < gv.getGameRect().left - width + xSpeed ) {
                x = gv.getGameRect().right;
                y = y + height;
            }
        }
        x = x + xSpeed;
        updateFrame();
    }

}
