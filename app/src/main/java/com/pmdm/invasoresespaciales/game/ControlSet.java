package com.pmdm.invasoresespaciales.game;

import android.graphics.PointF;
import android.graphics.Rect;
import android.util.SparseArray;

/**
 * Define si se debe desplazar a la izquierda, derecha o disparar.
 */
public class ControlSet {

    public static final int UPLEFT = 0;
    public static final int UP = 1;
    public static final int UPRIGHT = 2;
    public static final int LEFT = 3;
    public static final int NORMAL = 4;
    public static final int RIGHT = 5;
    public static final int DOWNLEFT = 6;
    public static final int DOWN = 7;
    public static final int DOWNRIGHT = 8;
    public static final int SHOOT = 9;

    public Rect rectLeft;
    public Rect rectRight;
    public Rect rectUp;
    public Rect rectDown;
    public Rect rectUpLeft;
    public Rect rectUpRight;
    public Rect rectDownLeft;
    public Rect rectDownRight;
    public Rect rectShoot;
    public SparseArray<PointF> activePointers;

    public void setRect(Rect rect, int POS){
        switch (POS){
            case UPLEFT:
                rectUpLeft = rect;
                break;
            case LEFT:
                rectLeft = rect;
                break;
            case DOWNLEFT:
                rectDownLeft = rect;
                break;
            case UPRIGHT:
                rectUpRight = rect;
                break;
            case RIGHT:
                rectRight = rect;
                break;
            case DOWNRIGHT:
                rectDownRight = rect;
                break;
            case UP:
                rectUp = rect;
                break;
            case DOWN:
                rectDown = rect;
                break;
            case SHOOT:
                rectShoot = rect;
                break;
        }
    }

    public void setActivePointers(SparseArray<PointF> activePointers){
        this.activePointers = activePointers;
    }

    private boolean hasRectActivePoints(Rect rect){
        int key;
        PointF pointF;
        try {
            for (int i = 0; i < activePointers.size(); i++) {
                key = activePointers.keyAt(i);
                pointF = activePointers.get(key);
                if ((pointF != null) && rect.contains((int) pointF.x, (int) pointF.y)) return true;
            }
        }catch (Exception e){}
        return false;
    }

    public int getControlPosition(){
        if (hasRectActivePoints(rectLeft)) return LEFT;
        if (hasRectActivePoints(rectRight)) return RIGHT;
        if (hasRectActivePoints(rectUp)) return UP;
        if (hasRectActivePoints(rectDown)) return DOWN;
        if (hasRectActivePoints(rectUpLeft)) return UPLEFT;
        if (hasRectActivePoints(rectUpRight)) return UPRIGHT;
        if (hasRectActivePoints(rectDownLeft)) return DOWNLEFT;
        if (hasRectActivePoints(rectDownRight)) return DOWNRIGHT;
        return NORMAL;
    }

    public boolean isLeft(){
        return hasRectActivePoints(rectUpLeft) || hasRectActivePoints(rectLeft) || hasRectActivePoints(rectDownLeft);
    }

    public boolean isRight(){
        return hasRectActivePoints(rectUpRight) || hasRectActivePoints(rectRight) || hasRectActivePoints(rectDownRight);
    }

    public boolean isUp(){
        return hasRectActivePoints(rectUpLeft) || hasRectActivePoints(rectUp) || hasRectActivePoints(rectUpRight);
    }

    public boolean isDown(){
        return hasRectActivePoints(rectDownLeft) || hasRectActivePoints(rectDown) || hasRectActivePoints(rectDownRight);
    }

    public boolean isNowhere(){
        return !(hasRectActivePoints(rectUpLeft) || hasRectActivePoints(rectLeft) || hasRectActivePoints(rectDownLeft)||
                 hasRectActivePoints(rectUpRight) || hasRectActivePoints(rectRight) || hasRectActivePoints(rectDownRight)||
                 hasRectActivePoints(rectUp) || hasRectActivePoints(rectDown));
    }

    public boolean isShoot(){
        return hasRectActivePoints(rectShoot);
    }

}
