package com.pmdm.invasoresespaciales.sprites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.game.Game;

import java.util.List;

/**
 * Sprite temporal que tras N actualizaciones se autoelimina.
 */
public class SpriteTemp extends Sprite {

	private float x;
	private float y;
	private Bitmap bmp;
    private static int N = 16;
	private int life = N;
	private List<SpriteTemp> temps;
    Paint paint;

	public SpriteTemp(List<SpriteTemp> temps, Game game, float x, float y, Bitmap bmp, int N) {
        this.setBmpRowsColsWidthHeight(bmp, 1, N);
		this.x = x - width/2;
		this.y = y - height/2;
		this.bmp = bmp;
		this.temps = temps;
        paint = new Paint();
        life = N;
	}

	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		int srcX = currentFrame * width;
		int srcY = 0;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)x, (int)y, (int)x+width, (int)y+height);
		canvas.drawBitmap(bmp, src, dst, null);
	}

	public void update() {
		currentFrame = ++currentFrame % cols;
		if (--life < 1) {
			temps.remove(this);
		}
	}

	public void remove() {
			temps.remove(this);
	}

}
