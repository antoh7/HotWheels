package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    HotWheels gs;

    Texture imgCross;
    Texture imgSpaceSky;
    Texture imgShip;
    Texture imgEnemy;
    Sound sndExplosion;

    ImageButton btnExit;

    Road[] sky = new Road[2];
    OurCar ship;
    ArrayList<OtherCar> enemy = new ArrayList<>();

    public static final int TYPE_ENEMY = 0, TYPE_SHIP = 1;

    long timeEnemySpawn, timeEnemyInterval = 1500;
    long timeShipDestory, timeShipAliveInterval = 5000;

    int kills;
    boolean isShipAlive;
    boolean isGameOver;

    public ScreenGame(HotWheels galaxyShooter){
        gs = galaxyShooter;

        imgCross = new Texture("cross.png");
        imgSpaceSky = new Texture("stars.png");
        imgShip = new Texture("our_car.png");
        imgEnemy = new Texture("other_car.png");

        btnExit = new ImageButton(imgCross, SCR_WIDTH-40, SCR_HEIGHT-40, 30, 30);

        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        sky[0] = new Road(0);
        sky[1] = new Road(SCR_HEIGHT);

        startGame();
    }

    @Override
    public void show() {
        startGame();
    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.isTouched()){
            gs.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gs.camera.unproject(gs.touch);
            ship.vx = (gs.touch.x - ship.x)/50;
            ship.vy = (gs.touch.y - ship.y)/50;
            if(btnExit.hit(gs.touch.x, gs.touch.y)){
                gs.setScreen(gs.screenIntro);
            }
        }

        //*****************************  события игры  *********************************************
        // движение неба
        for (int i = 0; i < sky.length; i++) {
            sky[i].move();
        }

        // вражеские корабли
        spawnEnemy();
        for (int i = 0; i < enemy.size(); i++) {
            enemy.get(i).move();
            if(enemy.get(i).outOfScreen()){
                enemy.remove(i);
                if(isShipAlive) {
                    killOurCar();
                }
                i--;
            }
        }

        // наш космический корабль
        if(isShipAlive){
            ship.move();
        } else if(!isGameOver){
            if(timeShipDestory+timeShipAliveInterval<TimeUtils.millis()){
                isShipAlive = true;
                ship.x = SCR_WIDTH/2;
            }
        }

        // **********************  вывод всех изображений  *****************************************
        gs.camera.update();
        gs.batch.setProjectionMatrix(gs.camera.combined); // пересчёт всех размеров вывода картинок под размеры экрана
        gs.batch.begin(); // начало вывода изображений
        for (int i = 0; i < sky.length; i++) {
            gs.batch.draw(imgSpaceSky, sky[i].x, sky[i].y, sky[i].width, sky[i].height);
        }
        for (int i = 0; i < enemy.size(); i++) {
            gs.batch.draw(imgEnemy, enemy.get(i).getX(), enemy.get(i).getY(), enemy.get(i).width, enemy.get(i).height);
        }

        if(isShipAlive){
            gs.batch.draw(imgShip, ship.getX(), ship.getY(), ship.width, ship.height);
        }
        gs.fontSmall.draw(gs.batch, "KILLS: "+kills, 10, SCR_HEIGHT-10);
        // кнопка дла выхода - крестик
        gs.batch.draw(btnExit.img, btnExit.x, btnExit.y, btnExit.width, btnExit.height);
        // жизни - маленькие самолётики в правом нижнем углу
        for (int i = 0; i < ship.lives; i++) {
            gs.batch.draw(imgShip, SCR_WIDTH-40-i*40, 10, 30, 30);
        }
        // вывод GameOver
        if(isGameOver) {
            gs.fontLarge.draw(gs.batch, "GAME OVER", 0, SCR_HEIGHT / 2, SCR_WIDTH, Align.center, true);
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
        imgSpaceSky.dispose();
        imgShip.dispose();
        imgEnemy.dispose();
    }

    void spawnEnemy() {
        if(timeEnemySpawn+timeEnemyInterval < TimeUtils.millis()) {
            enemy.add(new OtherCar(100, 100));
            timeEnemySpawn = TimeUtils.millis();
        }
    }

    // гибель нашего корабля
    void killOurCar() {
        if(gs.sound) sndExplosion.play();
        isShipAlive = false;
        timeShipDestory = TimeUtils.millis();
        ship.lives--;
        if(ship.lives == 0) {
            gameOver();
        }
    }

    void gameOver(){
        isGameOver = true;
    }

    void startGame(){
        enemy.clear();
        ship = new OurCar(SCR_WIDTH/2, 100, 100, 100);
        kills = 0;
        isShipAlive = true;
        isGameOver = false;
    }
}
