package com.pmdm.invasoresespaciales.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.sprites.Shot;
import com.pmdm.invasoresespaciales.sprites.Sprite;

/**
 * Para pintar el lienzo del juego.
 */
public class Drawer {

    protected Game gv;

    public int TEXT_SIZE = 30;

    /**
     * Constructor
     * @param gv referencia a la instancia del juego
     */
    public Drawer(Game gv){
        this.gv = gv;
    }

    /**
     * Pintado del canvas
     * @param canvas lienzo sobre el que se dibuja
     */
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(gv.getGameRect(), paint);
        drawControls(canvas);
        gv.ship.onDraw(canvas);
        for (int i=gv.temps.size()-1;i>=0;i--) {
            gv.temps.get(i).onDraw(canvas);
        }
        for (Sprite enemy : gv.enemies) {
            enemy.onDraw(canvas);
        }
        for (Shot shot : gv.shotsUp) {
            shot.onDraw(canvas);
        }
        for (Shot shot : gv.shotsDown) {
            shot.onDraw(canvas);
        }
        drawScore(canvas);
        drawLevel(canvas);
        drawLifes(canvas);
        drawIfYouLoose(canvas);
        drawIfYouWin(canvas);
    }

    /**
     * Dibuja los controles
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawControls(Canvas canvas){
        switch (gv.myApp.controls){
            case MyApplication.CONTROLS_BUTTONS:
                drawButtons(canvas);
                break;
            case MyApplication.CONTROLS_JOYSTICK:
                drawShootButton(canvas);
                drawJoystick(canvas);
                break;
        }
    }

    private void drawButtons(Canvas canvas){
        if (gv.controls.isLeft()){
            drawButton(canvas, gv.controls.rectLeft, Color.YELLOW,true);
        }else{
            drawButton(canvas, gv.controls.rectLeft, Color.YELLOW, false);
        }
        if (gv.controls.isRight()){
            drawButton(canvas, gv.controls.rectRight, Color.YELLOW, true);
        }else{
            drawButton(canvas, gv.controls.rectRight, Color.YELLOW, false);
        }
        if (gv.controls.isShoot()){
            drawButton(canvas, gv.controls.rectShoot, Color.RED, true);
        }else{
            drawButton(canvas, gv.controls.rectShoot, Color.RED, false);
        }
    }

    private void drawShootButton(Canvas canvas){
        Bitmap bmpButton;
        Rect rectSrc, rectDst;
        int wSrc, hSrc, uDst, x, y;
        bmpButton = BitmapFactory.decodeResource(gv.getResources(), R.drawable.red);
        wSrc = bmpButton.getWidth() / 2;
        hSrc = bmpButton.getHeight();
        if (gv.controls.isShoot()){
            rectSrc = new Rect(wSrc, 0, wSrc + wSrc - 1, hSrc);
        }else{
            rectSrc = new Rect(0, 0, wSrc, hSrc);
        }
        uDst = gv.myApp.gameRect.width() / 3;
        x =  gv.myApp.gameRect.left;
        y =  gv.myApp.gameRect.bottom;
        rectDst = new Rect(x + uDst + uDst, y, x + uDst + uDst - 1 + uDst, y + uDst);
        canvas.drawBitmap(bmpButton, rectSrc, rectDst, null);
    }

    private void drawJoystick(Canvas canvas){
        Bitmap bmpButton;
        Rect rectSrc, rectDst;
        int uSrc, uDst, x, y;
        int pos, row, col;
        bmpButton = BitmapFactory.decodeResource(gv.getResources(), R.drawable.joystick);
        uSrc = bmpButton.getWidth() / 3;

        pos = gv.controls.getControlPosition();
        col = pos%3;
        row = (int)Math.floor(pos/3);
        rectSrc = new Rect(col*uSrc, row*uSrc, col*uSrc+uSrc, row*uSrc+uSrc);

        uDst = gv.myApp.gameRect.width() / 3;
        x =  gv.myApp.gameRect.left;
        y =  gv.myApp.gameRect.bottom;
        rectDst = new Rect(x, y, x + uDst, y + uDst);
        canvas.drawBitmap(bmpButton, rectSrc, rectDst, null);
    }

    /**
     * Dibuja un botón sobre el rectángulo indicado en el canvas
     * @param canvas lienzo sobre el que dibujar
     * @param rectDst dónde se dibuja
     * @param color Color.RED, Color.MAGENTA o Color.YELLOW
     * @param isDown si está pulsado
     */
    private void drawButton(Canvas canvas, Rect rectDst, int color, boolean isDown){
        Bitmap bmp;
        Rect rectSrc;
        switch (color){
            case Color.RED:
                bmp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.red);
                break;
            case Color.MAGENTA:
                bmp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.pink);
                break;
            case Color.YELLOW:
            default:
                bmp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.yellow);
                break;
        }
        int width = bmp.getWidth() / 2;
        int height = bmp.getHeight();
        if (isDown){
            rectSrc = new Rect(width, 0, width + width - 1, height);
        }else{
            rectSrc = new Rect(0, 0, width - 1, height);
        }
        canvas.drawBitmap(bmp, rectSrc, rectDst, null);
    }

    /**
     * Dibujar la puntuación
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawScore(Canvas canvas){
        String str = gv.getResources().getString(R.string.points) + ":" + gv.myApp.score;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText(str, gv.myApp.screenWidth - 220, 80, paint);
    }

    /**
     * Dibujar el nivel
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawLevel(Canvas canvas){
        String str = gv.getResources().getString(R.string.level) + ":" + gv.myApp.level;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText(str, 10, 80, paint);
    }

    /**
     * Dibujar las vidas
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawLifes(Canvas canvas){
        gv.hud.onDraw(canvas);
        /*
        String str = String.valueOf(gv.myApp.lifes);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText(str, gv.myApp.screenWidth/2, 80, paint);
        */
    }

    /**
     * Si pierdes, reflejarlo gráficamente
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawIfYouLoose(Canvas canvas){
        if (gv.isYouLoose){
            String str;
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextSize(2*TEXT_SIZE);
            paint.setTypeface(Typeface.MONOSPACE);
            if (gv.myApp.lifes == 0) {//es la última vida
                str = gv.getResources().getString(R.string.gameover);
            }else{
                str = gv.getResources().getString(R.string.tryagain);
            }
            canvas.drawText(str, 40, gv.getHeight() / 2, paint);
        }
    }

    /**
     * Si ganas, reflejarlo gráficamente
     * @param canvas lienzo sobre el que se dibuja
     */
    private void drawIfYouWin(Canvas canvas){
        if (gv.isYouWin){
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextSize(2*TEXT_SIZE);
            paint.setTypeface(Typeface.MONOSPACE);
            canvas.drawText(gv.getResources().getString(R.string.nextlevel), 40, gv.getHeight()/2, paint);
        }
    }


}
