package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.game.Game;

import java.util.List;

/**
 * Los enemigos se desplazan como un conjunto
 */
public class EnemyGroup extends Enemy {

    private Game gv;
    List<Enemy> group;

    public EnemyGroup(Game gv, List<Enemy> enemies) {
        super(gv, null, 1);
        this.gv = gv;
        group = enemies;
        setXY();
        setWidth();
        setHeight();
    }

    public void setXY(){
        x = gv.getGameRect().width();
        y = gv.getGameRect().height();
        for(Enemy e : group){
            if (e.x < x) x = e.x;
            if (e.y < y) y = e.y;
        }
    }

    public void setWidth(){
        int x1 = gv.getGameRect().width();
        int x2 = 0;
        for(Enemy e : group){
            if (e.x < x1) x1 = e.x;
            if (e.x + e.width > x2) x2 = e.x + e.width;
        }
        this.width = x2 - x1;
    }

    public void setHeight(){
        int y1 = gv.getGameRect().height();
        int y2 = 0;
        for(Enemy e : group){
            if (e.y < y1) y1 = e.y;
            if (e.y + e.height > y2) y2 = e.y + e.height;
        }
        this.height = y2 - y1;
    }

    public List<Enemy> getEnemyList(){
        return group;
    }

    @Override
    public Rect[] getRects(){
        int i = 0;
        Rect[] rects = new Rect[group.size()];
        for(Enemy e : group){
            rects[i++] = e.getRect();
        }
        return rects;
    }

    @Override
    public void update() {
        setWidth();
        setHeight();
        if (x > gv.getGameRect().right - width - xSpeed || x + xSpeed < gv.getGameRect().left) {
            xSpeed = -xSpeed;
            y = y + group.get(0).height;
            for(Enemy e : group){
                e.y = e.y + e.height;
            }
        }
        x = x + xSpeed;
        for(Enemy e : group){
            e.x = e.x + xSpeed;
        }
        updateFrame();
    }

    public void updateFrame(){
        for(Enemy e : group){
            e.updateFrame();
        }
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        for(Enemy e : group){
            e.onDraw(canvas);
        }
    }

}

