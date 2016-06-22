package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.game.Game;

public class Hud extends Sprite {
    int ROWS = 4;
    int COLS = 1;
    double ALFA = 0.75;

    protected Game gv;

    public Hud(Game gv, Bitmap bmp) {
        this.gv = gv;
        this.setBmpRowsColsWidthHeight(bmp, ROWS, COLS);

        x = (gv.getGameRect().width()-width)/2;
        y = 50;
    }

    public void update() {
        updateFrame();
    }

    public void updateFrame(){
        if (gv.myApp.lifes >= 3){
            currentFrame = 0;
        }else{
            currentFrame = 3 - gv.myApp.lifes;
        }
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        int srcX = 0;
        int srcY = currentFrame * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, (int)(x+ALFA*width), (int)(y+ALFA*height));
        canvas.drawBitmap(bmp, src, dst, null);
    }

}
