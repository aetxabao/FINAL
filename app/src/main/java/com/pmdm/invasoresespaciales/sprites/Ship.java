package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.game.Game;

/**
 * Nave que se desplaza horizontalmente para disparar a los enemigos
 */
public class Ship extends Sprite {
    int ROWS = 3;
    int COLS = 3;
    private static final int MAX_SPEED = 5;
    static final int UP = 0;
    static final int DOWN = 0;
    static final int RIGHT = 1;
    static final int LEFT = 2;
    int side;

    private Game gv;

    public Ship(Game gv, Rect gameRect, Bitmap bmp) {
        this.gv = gv;
        this.setBmpRowsColsWidthHeight(bmp, ROWS, COLS);
        xSpeed = MAX_SPEED;
        ySpeed = MAX_SPEED;
        x = gameRect.centerX()-width/2;
        y = (int)(gameRect.bottom-1.2*height);
    }

    public void update() {
        if (x <= gv.getGameRect().right - width - xSpeed && x + xSpeed >= gv.getGameRect().left) {
            x = x + xSpeed;
        }
        if (y <= gv.getGameRect().bottom - height - ySpeed && y + ySpeed >= gv.getGameRect().top) {
            y = y + ySpeed;
        }
    }

    public void updateFrame(){
        currentFrame = ++currentFrame%cols;
    }

    public void moveRight(){
        xSpeed = MAX_SPEED;
        side = RIGHT;
        updateFrame();
    }

    public void moveLeft(){
        xSpeed = -MAX_SPEED;
        side = LEFT;
        updateFrame();
    }

    public void moveUp(){
        ySpeed = -MAX_SPEED;
        side = UP;
        updateFrame();
    }

    public void moveDown(){
        ySpeed = MAX_SPEED;
        side = DOWN;
        updateFrame();
    }

    public void moveNoWhere(){
        xSpeed = 0;
        ySpeed = 0;
        side = UP;
        updateFrame();
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        int srcX = currentFrame * width;
        int srcY = side * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x+width, y+height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    public Rect[] getRects(){
        Rect[] rects;
        if (xSpeed==0){
            rects = new Rect[3];
            rects[0] = new Rect((int)(x+(0.47*width)),y,(int)(x+(0.53*width)),y+height);
            rects[1] = new Rect((int)(x+(0.37*width)),(int)(y+(0.22*height)),(int)(x+(0.63*width)),y+height);
            rects[2] = new Rect(x,(int)(y+(0.4*height)),x+width,(int)(y+(0.55*height)));
        }else{
            rects = new Rect[2];
            rects[0] = new Rect(x+width/3,y,x+2*width/3,y+height);
            rects[1] = new Rect((int)(x+(0.3*width)),(int)(y+(0.4*height)),(int)(x+(0.7*width)),y+height);
        }
        return rects;
    }

}
