package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    HotWheels hw;

    Texture imgCross;
    Texture imgRoad;
    Texture carOur;
    Texture[] enemyCars = new Texture[2];
    Music sndExplosion;
    Music carSound;

    ImageButton btnExit;

    Road[] roads = new Road[2];
    OurCar ourCar;
    ArrayList<OtherCar> otherCars = new ArrayList<>();
    public Preferences prefs = Gdx.app.getPreferences("DrivingGame");

    long timeEnemySpawn, timeEnemyInterval = 2400,timeStart,currentTime;
    int carsOvertook;
    boolean isCarAlive,isGameOver;

    public ScreenGame(HotWheels hotWheels){
        hw = hotWheels;

        imgCross = new Texture("cross.png");
        imgRoad = new Texture("road2.png");
        carOur = new Texture("car1.png");
        enemyCars[0] = new Texture("enemycar0.png");
        enemyCars[1] = new Texture("enemycar1.png");

        btnExit = new ImageButton(imgCross, SCR_WIDTH-40, SCR_HEIGHT-40, 30, 30);

        sndExplosion = Gdx.audio.newMusic(Gdx.files.internal("explosion.mp3"));
        sndExplosion.setLooping(false);
        carSound = Gdx.audio.newMusic(Gdx.files.internal("carroar.mp3"));

        roads[0] = new Road(0);
        roads[1] = new Road(SCR_HEIGHT);
        startGame();
    }

    @Override
    public void show() {
        startGame();
        if(hw.music) {
            carSound.play();
        }
        carSound.setLooping(true);
    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.isTouched()) {
            hw.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            hw.camera.unproject(hw.touch);
            if (hw.touch.x > ourCar.x) {
                ourCar.x += 5;
            }
            if (hw.touch.x < ourCar.x) {
                ourCar.x -= 5;
            }
            if (hw.touch.y > ourCar.y) {
                ourCar.y += 4;
            }
            if (hw.touch.y < ourCar.y) {
                ourCar.y -= 4;
            }
        }
        // обработка экранных кнопок
        if(Gdx.input.justTouched()){
            if(btnExit.hit(hw.touch.x, hw.touch.y)){
                hw.setScreen(hw.screenIntro);
                //остановка всех звуков
                carSound.stop();
                sndExplosion.stop();
            }
        }
        // обработка нажатия клавиш на клавиатуре
        if(isCarAlive) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                ourCar.x -= 5;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                ourCar.x += 5;
            }
        }

        //*****************************  события игры  *********************************************
        // движение дороги
        for (int i = 0; i < roads.length; i++) {
            roads[i].move();
        }


        // вражеские машины
        spawnEnemy();
        for (int i = 0; i < otherCars.size(); i++) {
            otherCars.get(i).move();

            // проверка на столкновение вражеских машин
            for (int j = 0; j < otherCars.size()-1; j++) {
                if (j != i) {
                    otherCars.get(i).otherCarsOverlapping(otherCars.get(j));
                }
            }

            if(overlap(otherCars.get(i)) && isCarAlive){
                killOurCar();
            }
            if(otherCars.get(i).outOfScreen()){
                otherCars.remove(i);
                if(isCarAlive) {
                    carsOvertook++;
                }
                i--;
            }
        }

        // машина двигается
        if(isCarAlive){
            ourCar.move();
            //игровое время
            currentTime = TimeUtils.millis() - timeStart;
        }



        // **********************  вывод всех изображений  *****************************************
        hw.camera.update();
        hw.batch.setProjectionMatrix(hw.camera.combined); // пересчёт всех размеров вывода картинок под размеры экрана
        hw.batch.begin();// начало вывода изображений

        for (int i = 0; i < roads.length; i++) {
            hw.batch.draw(imgRoad, roads[i].x, roads[i].y, roads[i].width, roads[i].height);
        }

        for (int i = 0; i < otherCars.size(); i++) {
            hw.batch.draw(enemyCars[otherCars.get(i).type], otherCars.get(i).getX(), otherCars.get(i).getY(), otherCars.get(i).width, otherCars.get(i).height);
        }

        if(isCarAlive){
            hw.batch.draw(carOur, ourCar.getX(), ourCar.getY(), ourCar.width, ourCar.height);
        }
        // кнопка дла выхода - крестик
        hw.batch.draw(btnExit.img, btnExit.x, btnExit.y, btnExit.width, btnExit.height);
        //время в игре
        hw.fontSmall.draw(hw.batch,"ВРЕМЯ: "+timeToString(currentTime),10,SCR_HEIGHT - 10);


        // вывод рекордов
        if(isGameOver) {
            hw.fontLarge.draw(hw.batch, "GAME OVER", 0, SCR_HEIGHT / 2, SCR_WIDTH, Align.center, true);
            hw.fontSmall.draw(hw.batch,"машин обогнал: "+carsOvertook,0,SCR_HEIGHT/2-90,SCR_HEIGHT-410,Align.center,true);
            hw.fontSmall.draw(hw.batch,"лучшее время: "+ timeToString(prefs.getLong("time")),0,SCR_HEIGHT/2 - 150,SCR_HEIGHT-410,Align.center,true);
        }
        hw.batch.end(); // завершение вывода изображений

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgRoad.dispose();
        carOur.dispose();
        enemyCars[0].dispose();
        enemyCars[1].dispose();
    }

    void spawnEnemy() {
        if(timeEnemySpawn+timeEnemyInterval < TimeUtils.millis()) {
            otherCars.add(new OtherCar(90, 200));
            timeEnemySpawn = TimeUtils.millis();
        }
    }

    // гибель машины
    void killOurCar() {
        if(hw.sound) sndExplosion.play();
        isCarAlive = false;
        gameOver();

    }

    void gameOver(){
        isGameOver = true;
        carSound.stop();
        saveRecords();
    }

    void startGame(){
        otherCars.clear();
        ourCar = new OurCar(SCR_WIDTH/2, 100, 75, 150);
        // время начала игры
        timeStart = TimeUtils.millis();
        isCarAlive = true;
        isGameOver = false;
        carsOvertook = 0;
    }

    boolean overlap(OtherCar enemy) {
        return Math.abs(ourCar.x-enemy.x)<ourCar.width/2+enemy.width/2 & Math.abs(ourCar.y-enemy.y)<ourCar.height/2+enemy.height/2;
    }

    String timeToString(long t){
        String sec = "" + t/1000%60/10 + t/1000%60%10;
        String min = "" + t/1000/60/10 + t/1000/60%10;
        return min+":"+sec;
    }

    void saveRecords(){
        if(!prefs.contains("time")){
            prefs.putLong("time",currentTime);
            prefs.flush();
        }
        if(currentTime > prefs.getLong("time")){
            prefs.putLong("time",currentTime);
            prefs.flush();
        }

    }
}
