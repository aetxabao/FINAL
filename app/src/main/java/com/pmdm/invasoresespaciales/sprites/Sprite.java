package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Base para crear distintos tipos de sprites.
 */
public abstract class Sprite {
    protected int rows;
    protected int cols;

    public int x = 0;
    public int y = 0;
    public int xSpeed = 0;
    public int ySpeed = 0;
    protected Bitmap bmp;

    public int currentFrame = 0;
    public int width = 0;
    public int height = 0;

    public void setBmpRowsColsWidthHeight(Bitmap bmp, int rows, int cols){
        if (bmp == null) return;
        this.bmp = bmp;
        this.rows = rows;
        this.cols = cols;
        this.width = bmp.getWidth() / cols;
        this.height = bmp.getHeight() / rows;
    }

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}

    public void setXSpeed(int xSpeed){
        this.xSpeed = xSpeed;
    }

    public void setYSpeed(int ySpeed){
        this.ySpeed = ySpeed;
    }

    public Rect getRect(){
        return new Rect(x,y,x+width,y+height);
    }

    public Rect[] getRects(){
        Rect[] rects = new Rect[1];
        rects[0] = getRect();
        return rects;
    }

    public boolean collides(Sprite sprite){
        for(Rect rA : this.getRects()){
            for(Rect rB : sprite.getRects()){
                if (Rect.intersects(rA,rB) || rB.contains(rA) || rA.contains(rB)) return true;
            }
        }
        return false;
    }

    @SuppressLint("DrawAllocation")
    public abstract void onDraw(Canvas canvas);

    public abstract void update();

}
