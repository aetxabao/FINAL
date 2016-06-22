package com.pmdm.invasoresespaciales.game;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.activities.MainActivity;
import com.pmdm.invasoresespaciales.activities.NewScoreActivity;
import com.pmdm.invasoresespaciales.sprites.Enemy;
import com.pmdm.invasoresespaciales.sprites.EnemyGroup;
import com.pmdm.invasoresespaciales.sprites.Hud;
import com.pmdm.invasoresespaciales.sprites.Ship;
import com.pmdm.invasoresespaciales.sprites.Shot;
import com.pmdm.invasoresespaciales.sprites.Sprite;
import com.pmdm.invasoresespaciales.sprites.SpriteTemp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Lógica del juego.
 */
@SuppressLint("WrongCall")
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    public MyApplication myApp;
    protected GameLoopThread thread;
    protected Ship ship;
    protected List<Shot> shotsUp = new ArrayList<>();
    protected List<Shot> shotsDown = new ArrayList<>();
    protected List<Sprite> enemies = new ArrayList<>();
    protected List<SpriteTemp> temps = new ArrayList<>();
    public Hud hud;
    protected long lastClick;
    protected Bitmap bmpLifes;
    protected Bitmap bmpExplo9;
    protected Bitmap bmpExplo12;
    protected Context context;
    protected long time = 0;
    protected boolean isYouLoose = false;
    protected boolean isYouWin = false;
    protected SparseArray<PointF> mActivePointers;
    protected ControlSet controls;
    protected long lastFire = 0;
    protected long firingInterval = 333;

    protected static final int PTS = 20;
    protected static final int TMP_UPDS = 12;

    protected MediaPlayer mp;
    protected boolean isMusicLoaded = false;

    protected SoundPool soundPool;
    protected int idShoot, idAh;

    protected Drawer drawer;

    protected Handler handler;

	public Game(final Context context) {
        //Defición del contexto
		super(context);
        this.context = context;

        //Definición del contexto de la aplicación
        myApp = (MyApplication) getContext().getApplicationContext();
        //Referencia a la vista de juego
        myApp.gv = this;

        //Definición de los controles: izquierda, derecha, disparo
        configurarControles();

        //Definición de la reproducción de audio
        configurarMediaPlayer();

        //Definición del objeto pintor
        drawer = new Drawer(this);

        //Imagen y objeto que representa las vidas
        bmpLifes = BitmapFactory.decodeResource(getResources(), R.drawable.lifes);
        hud = new Hud(this, bmpLifes);

        //Imagen de explosiones
        bmpExplo9 = BitmapFactory.decodeResource(getResources(), R.drawable.explo9);
        bmpExplo12 = BitmapFactory.decodeResource(getResources(), R.drawable.explo12);

        //Callbacks
        getHolder().addCallback(this);
        //Gestión de mensajes
        configurarGestorMensajes();
	}

    /**
     * Acciones a realizar cuando se recibe un mensaje en base a los flags
     *  isYouLoose: es porque has perdido, vuelve al MainActivity
     *  isYouWin: es porque has superado la fase, elimina rastros e inicia siguiente nivel
     */
    public void configurarGestorMensajes(){
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1==1){
                    Log.d("LOG-Game", "msg 1");
                    if (isYouLoose){
                        Intent intent = new Intent(context, MainActivity.class);
                        myApp.lifes--;
                        if (myApp.lifes < 0) {
                            if (myApp.highScore.getPosition(myApp.score)<=5) {
                                intent = new Intent(context, NewScoreActivity.class);
                            }
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else{
                            clearElements();
                            startGame();
                        }
                    }
                    if (isYouWin){
                        myApp.level++;
                        clearElements();
                        if(myApp.level%myApp.LEVELS==0){
                            myApp.lifes++;
                            myApp.vx++;
                            myApp.vy++;
                        }
                        startGame();
                    }
                }
                return false;
            }
        });
    }

    private void clearElements(){
        for(Iterator<SpriteTemp> it=temps.iterator();it.hasNext();){
            it.next();
            it.remove();
        }
        for(Iterator<Shot> it=shotsUp.iterator();it.hasNext();){
            it.next();
            it.remove();
        }
        for(Iterator<Shot> it=shotsDown.iterator();it.hasNext();){
            it.next();
            it.remove();
        }
    }

    /**
     * Definición de los controles
     */
    private void configurarControles(){
        mActivePointers = new SparseArray<PointF>();
        controls = new ControlSet();
        controls.setActivePointers(mActivePointers);
        switch (myApp.controls){
            case MyApplication.CONTROLS_BUTTONS:
                configurarBotones();
                break;
            case MyApplication.CONTROLS_JOYSTICK:
                configurarJoystick();
                break;
        }
    }

    /**
     * Definición de los botones
     */
    private void configurarBotones(){
        int x, y, w, h;
        w = (int)(myApp.screenWidth/3);
        h = myApp.screenHeight - myApp.gameRect.bottom;
        x = myApp.gameRect.left;
        y = myApp.gameRect.bottom;
        controls.setRect(new Rect(x, y, x + w, y + h), controls.LEFT);
        controls.setRect(new Rect(x + 2 * w, y, x + 3 * w, y + h), controls.RIGHT);
        controls.setRect(new Rect(x + w, y, x + 2 * w, y + h),controls.SHOOT);
    }

    /**
     * Definición del joystick
     */
    private void configurarJoystick(){
        int u,a,b,x,y;
        u = myApp.gameRect.width()/3;
        b = (int)(0.25*u);
        a = (u-b)/2;
        x = myApp.gameRect.left;
        y = myApp.gameRect.bottom;
        controls.setRect(new Rect(x, y, x+a, y+a), controls.UPLEFT);
        controls.setRect(new Rect(x+a, y, x+a+b, y+a), controls.UP);
        controls.setRect(new Rect(x+a+b, y, x+a+b+a, y+a), controls.UPRIGHT);
        controls.setRect(new Rect(x, y+a, x+a, y+a+b), controls.LEFT);
        controls.setRect(new Rect(x+a+b, y+a, x+a+b+a, y+a+b), controls.RIGHT);
        controls.setRect(new Rect(x, y+a+b, x+a, y+a+b+a), controls.DOWNLEFT);
        controls.setRect(new Rect(x+a, y+a+b, x+a+b, y+a+b+a), controls.DOWN);
        controls.setRect(new Rect(x+a+b, y+a+b, x+a+b+a, y+a+b+a), controls.DOWNRIGHT);
        controls.setRect(new Rect(u+u, y, u+u+u-1, y+u), controls.SHOOT);
    }

    /**
     * Reproductor de música y sonidos
     */
    private void configurarMediaPlayer(){
        mp = MediaPlayer.create(context, R.raw.game);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!mp.isPlaying() && myApp.sound) {
                    mp.start();
                }
                if (mp.isPlaying() && !myApp.sound) {
                    mp.pause();
                }
                isMusicLoaded = true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else {
            createOldSoundPool();
        }
        idShoot = soundPool.load(context, R.raw.shoot, 0);
        idAh = soundPool.load(context, R.raw.ah, 0);
    }

    /**
     * Pool de sonidos para versiones nuevas de Android
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        Log.d("LOG-Game", "createNewSoundPool");
    }

    /**
     * Pool de sonidos para versiones antiguas de Android
     */
    @SuppressWarnings("deprecation")
    private void createOldSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        Log.d("LOG-Game", "createOldSoundPool");
    }

    /**
     * Área de juego
     * @return rectángulo que define la zona de juego
     */
    public Rect getGameRect(){
        return myApp.gameRect;
    }

    /**
     * Iniciación de las circunstancias del juego y el hilo
     */
	public void startGame() {
		if (thread == null) {
            Log.d("LOG-Game", "startGame");
            initiateGameView();
			thread = new GameLoopThread(this);
			thread.startThread();
            try{
                if (isMusicLoaded){
                    if (!mp.isPlaying() && myApp.sound) {
                        mp.start();
                    }
                    if (mp.isPlaying() && !myApp.sound) {
                        mp.pause();
                    }
                }
            }catch(Exception ignored){}
		}
	}

    /**
     * Inicializa variables, enemigos y protagonista
     */
    public void initiateGameView(){
        isYouWin = false;
        isYouLoose = false;
        time = 0;
        enemies = myApp.createEnemies();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ship0);
        ship = new Ship(this, myApp.gameRect, bmp);
        hud.update();
    }

    /**
     * Para la reproducción de sonidos
     * y el hilo de ejecución
     */
    public void stopGame() {
        try{
            if (mp.isPlaying()) {
                mp.pause();
            }
        }catch(Exception ignored){}
        try{
            soundPool.stop(idShoot);
            soundPool.stop(idAh);
        }catch(Exception ignored){}
        try{
            if (thread != null) {
                Log.d("LOG-GameView", "stopGame");
                thread.stopThread();
                thread = null;
            }
        }catch(Exception ignored){}
	}

    /**
     * Actualización de la lógica del juego
     */
    public void update() {
        //Desplazamiento del protagonista
        if(controls.isUp()){
            ship.moveUp();
        }
        if(controls.isDown()){
            ship.moveDown();
        }
        if(controls.isLeft()){
            ship.moveLeft();
        }
        if(controls.isRight()){
            ship.moveRight();
        }
        if(controls.isNowhere()){
            ship.moveNoWhere();
        }


        //Si el protagonista quiere disparar
        if(controls.isShoot()){
            //Control del disparo
            if (System.currentTimeMillis() - lastFire >= firingInterval) {
                lastFire = System.currentTimeMillis();
                Bitmap bmpShotUp = BitmapFactory.decodeResource(getResources(), R.drawable.shot_up);
                Shot shotUp = new Shot(this, myApp.gameRect, bmpShotUp);
                shotUp.setPos(ship.x + (ship.width - shotUp.width) / 2, ship.y - shotUp.height);
                shotsUp.add(shotUp);
                if (myApp.sound) soundPool.play(idShoot, 1, 1, 1, 0, 1);

                //Por cada disparo propio uno de un enemigo disparador
                List<Sprite> shooters = new ArrayList<>();
                for(Sprite sprite : enemies){
                    if(((Enemy)sprite).isShooter()){
                        shooters.add(sprite);
                    }
                }
                int n = shooters.size();
                Random rnd = new Random();
                if(n>0){
                    Sprite sprite = shooters.get(rnd.nextInt(n));
                    Bitmap bmpShotDown = BitmapFactory.decodeResource(getResources(), R.drawable.shot_down);
                    Shot shotDown = new Shot(this, myApp.gameRect, bmpShotDown);
                    shotDown.setPos(sprite.x + (sprite.width - shotDown.width) / 2, sprite.y+sprite.height);
                    shotDown.ySpeed = -shotDown.ySpeed;
                    shotsDown.add(shotDown);
                }

            }
        }

        //Detección de colisión entre balas
OuterLoop1: for (Iterator<Shot> itShotUp = shotsUp.iterator(); itShotUp.hasNext(); ) {
            Shot shotUp = itShotUp.next();
            for (Iterator<Shot> itShotDown = shotsDown.iterator(); itShotDown.hasNext(); ) {
                Shot shotDown = itShotDown.next();
                if (shotUp.collides(shotDown)) {
                    itShotUp.remove();
                    itShotDown.remove();
                    temps.add(new SpriteTemp(temps, this, shotDown.x + shotDown.width / 2, shotDown.y + shotDown.height / 2, bmpExplo12, 12));
                    continue OuterLoop1;
                }
            }
        }

        //Detección de colisiones de los enemigos con los disparos del protagonista
OuterLoop2: for (Iterator<Shot> itBullet = shotsUp.iterator(); itBullet.hasNext(); ) {
            Shot shot = itBullet.next();
            for (Iterator<Sprite> itSprite = enemies.iterator(); itSprite.hasNext(); ) {
                Sprite sprite = itSprite.next();
                if (shot.collides(sprite)){
                    if (sprite instanceof EnemyGroup){
                        List<Enemy> enemyList = ((EnemyGroup) sprite).getEnemyList();
                        for (Iterator<Enemy> itEnemy = enemyList.iterator(); itEnemy.hasNext(); ) {
                            Enemy enemy = itEnemy.next();
                            if (shot.collides(enemy)){
                                temps.add(new SpriteTemp(temps, this, enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, bmpExplo9, 9));
                                itEnemy.remove();
                                if (enemyList.size()==0){
                                    itSprite.remove();
                                }else{
                                    ((EnemyGroup) sprite).setXY();
                                    ((EnemyGroup) sprite).setWidth();
                                    ((EnemyGroup) sprite).setHeight();
                                }
                                break;
                            }
                        }
                    }else{
                        temps.add(new SpriteTemp(temps, this, sprite.x + sprite.width / 2, sprite.y + sprite.height / 2, bmpExplo9, 9));
                        itSprite.remove();
                    }
                    if (myApp.sound) soundPool.play(idAh, 1, 1, 1, 0, 1);
                    myApp.score += PTS;
                    itBullet.remove();
                    continue OuterLoop2;
                }
            }
            if (myApp.gameRect.contains(shot.getRect())){
                shot.update();
            }else{
                itBullet.remove();
            }
        }
        //Actualización del protagonista
        ship.update();
        //Actualización de los sprites temporales
        for (int i=temps.size()-1;i>=0;i--) {
            temps.get(i).update();
        }
        //Actualización de los enemigos
        for (Sprite enemy : enemies) {
            enemy.update();
        }
        //Comprobación del estado de la partida
        if ((enemies.size()==0)&&(temps.size()==0)){
            Log.d("LOG-Game", "You win!");
            isYouWin = true;
            stopGame();
        }
        //Detección si los disparos de los enemigos han impactado con el protagonista
        for (Iterator<Shot> itBullet = shotsDown.iterator(); itBullet.hasNext(); ) {
            Shot shot = itBullet.next();
            if (shot.collides(ship)) {
                Log.d("LOG-Game", "You have been killed!");
                isYouLoose = true;
                stopGame();
            }else {
                if (myApp.gameRect.contains(shot.getRect())) {
                    shot.update();
                } else {
                    itBullet.remove();
                }
            }
        }
        //Detección si los enemigos han impactado con el protagonista
        //o se han salido del área se juego
        for (Iterator<Sprite> itSprite = enemies.iterator(); itSprite.hasNext(); ) {
            Sprite sprite = itSprite.next();
            if(sprite.collides(ship)){
                Log.d("LOG-Game", "You loose!");
                isYouLoose = true;
                stopGame();
            }
            if (myApp.gameRect.bottom < sprite.y - sprite.height ) {
                itSprite.remove();
            }
        }
    }

    /**
     * Pintado del canvas
     * @param canvas lienzo sobre el que se dibuja
     */
	public void onDraw(Canvas canvas) {
        drawer.draw(canvas);
    }

    /**
     * Gestiona las pulsaciones
     * @param event objeto con los parámetros del evento
     * @return devuelve siempre true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();
        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);
        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }
        return true;
    }

    /**
     * En caso de cambiar la superficie
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    /**
     * En caso de crearse la superficie
     */
	public void surfaceCreated(SurfaceHolder holder) {
		startGame();
	}

    /**
     * En caso de destruirse la superficie
     */
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopGame();
	}

    /**
     * Liberar el reproductor multimedia
     */
    public void destroy(){
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }
}
