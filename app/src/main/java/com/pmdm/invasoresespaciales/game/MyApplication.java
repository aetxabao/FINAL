package com.pmdm.invasoresespaciales.game;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.sprites.Enemy;
import com.pmdm.invasoresespaciales.sprites.EnemyDiagonal;
import com.pmdm.invasoresespaciales.sprites.EnemyGroup;
import com.pmdm.invasoresespaciales.sprites.EnemyRocket;
import com.pmdm.invasoresespaciales.sprites.EnemySinu;
import com.pmdm.invasoresespaciales.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

//<application android:name="com.pmdm.MyApplication" ...>
//
//MyApplication myApp = (MyApplication) getApplication();
//myApp.doSomething();
//
//MyApplication myApp = (MyApplication) getContext().getApplicationContext();
//myApp.doSomething();
//

/**
 * Recoge variables globales sobre el desarrollo y la configuración del juego.
 * Define el área de juego, los tipos de enemigos y la ubicación de estos según el nivel.
 */
public class MyApplication extends Application {

    public static Game gv;

    public static final boolean SOUND_OFF = false;
    public static final boolean SOUND_ON = true;

    public static final int CONTROLS_BUTTONS = 0;
    public static final int CONTROLS_JOYSTICK = 1;

    public static final boolean NOSHOT = false;
    public static final boolean SHOT = true;

    public static final int NORMAL = 1;
    public static final int DIAGONAL = 2;
    public static final int SINU = 3;
    public static final int ROCKET = 4;

    public boolean sound;
    public int controls;

    public int screenWidth = 0;
    public int screenHeight = 0;
    public Rect gameRect = null;
    public int lifes = 2;//vidas pendientes
    public int LIFES = 2;
    public int level = 1;
    public int LEVELS = 6;
    public int score = 0;

    public int TICKSxFRAME = 3;

    public int n = 8;
    public int m = 16;

    public int vx = 8;
    public int vy = 3;

    public HighScore highScore;

    public void loadHighScore(){
        highScore = new HighScore();
        String record;
        SharedPreferences sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        sound = sharedPref.getBoolean("Sound", false);
        controls = sharedPref.getInt("Controls", CONTROLS_BUTTONS);
        for(int i=1;i<=5;i++){
            record = sharedPref.getString("HS"+i, " AAA   00   00000");
            highScore.putScore(new Score(record));
        }
    }

    public void saveHighScore(){
        SharedPreferences sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String[] records = highScore.getHighScores();
        editor.putBoolean("Sound", sound);
        editor.putInt("Controls", controls);
        for(int i=1;i<=records.length;i++){
            editor.putString("HS" + i, records[i - 1]);
        }
        editor.commit();
    }

    /**
     * Transforma una coordenada en una posición
     * @param i coordenada de 0 a n eje horizontal
     * @return posicion x
     */
    public int getX(int i){
        return gameRect.left + i*screenWidth/n;
    }

    /**
     * Transforma una coordenada en una posición
     * @param j coordenada de 0 a m eje vertical
     * @return posicion y
     */
    public int getY(int j){
        return gameRect.top + j*screenHeight/m;
    }


    public List<Sprite> createEnemies() {
        List<Sprite> enemies = new ArrayList<>();
        int level = this.level % LEVELS;
        switch (level){
            case 1:
                enemies = crearEnemigosNivelBarricada();
                break;
            case 2:
                enemies = crearEnemigosNivelDonut();
                break;
            case 3:
                enemies = crearEnemigosNivelPaquito();
                break;
            case 4:
                enemies = crearEnemigosNivelLambada();
                break;
            case 5:
                enemies = crearEnemigosNivelPulpo();
                break;
            case 0:
                enemies = crearEnemigosNivelRock();
                break;
        }
        return enemies;
    }

    private List<Sprite> crearEnemigosNivelBarricada() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        List<Enemy> el1 = new ArrayList<>();
        el1.add(createEnemy(R.drawable.ei1, 2, 2, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 3, 2, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 4, 2, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 5, 2, 0, 0, NORMAL, NOSHOT));
        EnemyGroup eg1 = new EnemyGroup(gv, el1);
        eg1.setXSpeed(vx);
        enemies.add(eg1);
        List<Enemy> el2 = new ArrayList<>();
        el2.add(createEnemy(R.drawable.ei3, 2, 4, 0, 0, NORMAL, NOSHOT));
        el2.add(createEnemy(R.drawable.ei3, 3, 4, 0, 0, NORMAL, NOSHOT));
        el2.add(createEnemy(R.drawable.ei3, 4, 4, 0, 0, NORMAL, NOSHOT));
        el2.add(createEnemy(R.drawable.ei3, 5, 4, 0, 0, NORMAL, NOSHOT));
        EnemyGroup eg2 = new EnemyGroup(gv, el2);
        eg2.setXSpeed(-vx);
        enemies.add(eg2);
        return enemies;
    }

    private List<Sprite> crearEnemigosNivelDonut() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        List<Enemy> el1 = new ArrayList<>();
        el1.add(createEnemy(R.drawable.ei1, 2, 3, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 3, 2, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 4, 2, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 5, 3, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 2, 4, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 3, 5, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 4, 5, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 5, 4, 0, 0, NORMAL, NOSHOT));
        EnemyGroup eg1 = new EnemyGroup(gv, el1);
        eg1.setXSpeed(vx);
        enemies.add(eg1);
        return enemies;
    }


    private List<Sprite> crearEnemigosNivelPaquito() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 3, 2, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 4, 3, -vx, 0, NORMAL, SHOT));
        List<Enemy> el1 = new ArrayList<>();
        el1.add(createEnemy(R.drawable.ei1, 2, 4, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 3, 4, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 4, 4, 0, 0, NORMAL, NOSHOT));
        el1.add(createEnemy(R.drawable.ei1, 5, 4, 0, 0, NORMAL, NOSHOT));
        EnemyGroup eg1 = new EnemyGroup(gv, el1);
        eg1.setXSpeed(vx);
        enemies.add(eg1);
        return enemies;
    }

    private List<Sprite> crearEnemigosNivelPulpo() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, vy, DIAGONAL, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 0, -vx, vy, DIAGONAL, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 3, 0, -vx, vy, DIAGONAL, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 4, 0, vx, vy, DIAGONAL, NOSHOT));
        return enemies;
    }

    private List<Sprite> crearEnemigosNivelLambada() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 0, 3, vx, vy, SINU, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 6, -vx, vy, SINU, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 3, 3, -vx, vy, SINU, NOSHOT));
        enemies.add(createEnemy(R.drawable.ei2, 4, 6, vx, vy, SINU, NOSHOT));
        return enemies;
    }

    private List<Sprite> crearEnemigosNivelRock() {
        List<Sprite> enemies = new ArrayList<>();
        enemies.add(createEnemy(R.drawable.ei2, 0, 0, vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 1, -vx, 0, NORMAL, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 0, 3, vx, 2*vy, ROCKET, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 7, 6, -vx, 2*vy, ROCKET, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 3, 3, -vx, 2*vy, ROCKET, SHOT));
        enemies.add(createEnemy(R.drawable.ei2, 4, 6, vx, 2*vy, ROCKET, SHOT));
        return enemies;
    }

    /**
     * Crea un enemigo en una coordenada (i,j) con una velocidad (vx,vy)
     * @param resourceId identificador del recourso (la imagen)
     * @param i coordenada horizontal
     * @param j coordenada vertical
     * @param vx velocidad eje x
     * @param vy velocidad eje y
     * @return una instancia del enemigo
     */
    private Enemy createEnemy(int resourceId, int i, int j, int vx, int vy, int type, boolean canShoot) {
        Enemy e = null;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resourceId);
        switch (type){
            case DIAGONAL:
                e = new EnemyDiagonal(gv, bmp, TICKSxFRAME);
                break;
            case SINU:
                e = new EnemySinu(gv, bmp, TICKSxFRAME);
                break;
            case ROCKET:
                e = new EnemyRocket(gv, bmp, TICKSxFRAME);
                break;
            case NORMAL:
                e = new Enemy(gv, bmp, TICKSxFRAME);
                break;
        }
        if (canShoot){
            e.setShooter(true);
        }
        e.setPos(getX(i), getY(j));
        e.setXSpeed(vx);
        e.setYSpeed(vy);
        return e;
    }

}
