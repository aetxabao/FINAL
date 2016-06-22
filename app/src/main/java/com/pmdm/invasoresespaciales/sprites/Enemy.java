package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.game.Game;

/**
 * Enemigos normales que se desplazan de lado a lado y en los extremos descienden.
 */
public class Enemy extends Sprite {
	int ROWS = 1;
	int COLS = 6;
	private static final int MAX_SPEED = 5;
	boolean canShoot = false;
    int n;
    int N;//ticks para cambio de frame
	double ALFA = 0.75;

	protected Game gv;

	public Enemy(Game gv, Bitmap bmp, int N) {
		this.gv = gv;
		this.setBmpRowsColsWidthHeight(bmp, ROWS, COLS);
        this.N = N;
        this.n = 0;

		xSpeed = MAX_SPEED;
        x = gv.getGameRect().left+width/2;
	}

    public boolean isShooter(){
        return canShoot;
    }

    public void setShooter(boolean status){
        canShoot = status;
    }

    public void update() {
		if (x > gv.getGameRect().right - width - xSpeed || x + xSpeed < gv.getGameRect().left) {
			xSpeed = -xSpeed;
			y = y + height;
		}
		x = x + xSpeed;
		updateFrame();
	}

    public void updateFrame(){
        if (++n==N) {
            n = 0;
            currentFrame = ++currentFrame % cols;
        }
    }


    public Rect getRect(){
        return new Rect(x,y,(int)(x+ALFA*width),(int)(y+ALFA*height));
    }

    public Rect[] getRects(){
        Rect[] rects = new Rect[1];
        rects[0] = getRect();
        return rects;
    }

    @SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, (int)(x+ALFA*width), (int)(y+ALFA*height));
		canvas.drawBitmap(bmp, src, dst, null);
	}

}
