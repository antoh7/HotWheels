package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    HotWheels gs;

    Texture imgCross;
    Texture imgRoad;
    Texture carOur;
    Texture enemyCars;
    Music sndExplosion;

    ImageButton btnExit;

    Road[] roads = new Road[2];
    OurCar ourCar;
    ArrayList<OtherCar> otherCars = new ArrayList<>();
    Preferences prefs = Gdx.app.getPreferences("DrivingGame");

    long timeEnemySpawn, timeEnemyInterval = 2400;
    int carsOvertook;
    long timeStart,currentTime;

    boolean isCarAlive;
    boolean isGameOver;

    public ScreenGame(HotWheels galaxyShooter){
        gs = galaxyShooter;

        imgCross = new Texture("cross.png");
        imgRoad = new Texture("road.png");
        carOur = new Texture("car.png");
        enemyCars = new Texture("obstr.png");

        btnExit = new ImageButton(imgCross, SCR_WIDTH-40, SCR_HEIGHT-40, 30, 30);

        sndExplosion = Gdx.audio.newMusic(Gdx.files.internal("explosion.mp3"));
        sndExplosion.setLooping(false);

        roads[0] = new Road(0);
        roads[1] = new Road(SCR_HEIGHT);

        startGame();
    }

    @Override
    public void show() {
        startGame();
    }

    @Override
    public void render(float delta) {
        // касания экрана
        gs.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        gs.camera.unproject(gs.touch);
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            ourCar.x -= 5;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            ourCar.x += 5;
        }
        if(Gdx.input.justTouched()){
            if(btnExit.hit(gs.touch.x, gs.touch.y)){
                gs.setScreen(gs.screenIntro);
            }

        }

        //*****************************  события игры  *********************************************
        // движение неба
        for (int i = 0; i < roads.length; i++) {
            roads[i].move();
        }

        // вражеские корабли
        spawnEnemy();
        for (int i = 0; i < otherCars.size(); i++) {
            otherCars.get(i).move();
            if(overlap(otherCars.get(i)) && isCarAlive){
                killOurCar();
            }
            if(otherCars.get(i).outOfScreen() && isCarAlive){
                otherCars.remove(i);
                carsOvertook++;
                i--;
            }


        }

        // наш космический корабль
        if(isCarAlive){
            ourCar.move();
            //игровое время
            currentTime = TimeUtils.millis() - timeStart;
        }



        // **********************  вывод всех изображений  *****************************************
        gs.camera.update();
        gs.batch.setProjectionMatrix(gs.camera.combined); // пересчёт всех размеров вывода картинок под размеры экрана
        gs.batch.begin();// начало вывода изображений

        for (int i = 0; i < roads.length; i++) {
            gs.batch.draw(imgRoad, roads[i].x, roads[i].y, roads[i].width, roads[i].height);
        }

        for (int i = 0; i < otherCars.size(); i++) {
            gs.batch.draw(enemyCars, otherCars.get(i).getX(), otherCars.get(i).getY(), otherCars.get(i).width, otherCars.get(i).height);
        }

        if(isCarAlive){
            gs.batch.draw(carOur, ourCar.getX(), ourCar.getY(), ourCar.width, ourCar.height);
        }
        // кнопка дла выхода - крестик
        gs.batch.draw(btnExit.img, btnExit.x, btnExit.y, btnExit.width, btnExit.height);
        //время в игре
        gs.fontSmall.draw(gs.batch,"ВРЕМЯ: "+timeToString(currentTime),10,SCR_HEIGHT - 10);


        // вывод рекордов
        if(isGameOver) {
            gs.fontLarge.draw(gs.batch, "GAME OVER", 0, SCR_HEIGHT / 2, SCR_WIDTH, Align.center, true);
            gs.fontSmall.draw(gs.batch,"машин обогнал: "+carsOvertook,0,SCR_HEIGHT/2-70,SCR_HEIGHT-410,Align.center,true);
            gs.fontSmall.draw(gs.batch,"лучшее время: "+ timeToString(prefs.getLong("time")),0,SCR_HEIGHT/2 - 130,SCR_HEIGHT-410,Align.center,true);
        }
        gs.batch.end(); // завершение вывода изображений

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
        enemyCars.dispose();
    }

    void spawnEnemy() {
        if(timeEnemySpawn+timeEnemyInterval < TimeUtils.millis()) {
            otherCars.add(new OtherCar(100, 100));
            timeEnemySpawn = TimeUtils.millis();
        }
    }

    // гибель нашего корабля
    void killOurCar() {
        if(gs.sound) sndExplosion.play();
        isCarAlive = false;
        gameOver();

    }

    void gameOver(){
        isGameOver = true;
        if(!prefs.contains("time")){
            prefs.putLong("time",currentTime);
            prefs.flush();
        }
        if(currentTime < prefs.getLong("time")){
            prefs.putLong("time",currentTime);
            prefs.flush();
        }
    }

    void startGame(){
        otherCars.clear();
        ourCar = new OurCar(SCR_WIDTH/2, 100, 100, 100);
        // время начала игры
        timeStart = TimeUtils.millis();
        isCarAlive = true;
        isGameOver = false;
    }

    boolean overlap(OtherCar enemy) {
        return Math.abs(ourCar.x-enemy.x)<ourCar.width/2+enemy.width/2 & Math.abs(ourCar.y-enemy.y)<ourCar.height/2+enemy.height/2;
    }

    String timeToString(long t){
        String sec = "" + t/1000%60/10 + t/1000%60%10;
        String min = "" + t/1000/60/10 + t/1000/60%10;
        return min+":"+sec;
    }

}
