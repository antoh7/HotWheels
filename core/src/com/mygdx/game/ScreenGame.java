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
    Texture imgShot;
    Texture imgAtlasFragments;
    TextureRegion[][] imgFragment = new TextureRegion[2][4];
    Sound sndShoot;
    Sound sndExplosion;

    ImageButton btnExit;

    SpaceSky[] sky = new SpaceSky[2];
    SpaceShip ship;
    ArrayList<EnemyShip> enemy = new ArrayList<>();
    ArrayList<ShipShot> shots = new ArrayList<>();
    ArrayList<FragmentShip> fragments = new ArrayList<>();

    public static final int TYPE_ENEMY = 0, TYPE_SHIP = 1;

    long timeEnemySpawn, timeEnemyInterval = 1500;
    long timeShotSpawn, timeShotInterval = 500;
    long timeShipDestory, timeShipAliveInterval = 5000;

    int kills;
    boolean isShipAlive;
    boolean isGameOver;

    public ScreenGame(HotWheels galaxyShooter){
        gs = galaxyShooter;

        imgCross = new Texture("cross.png");
        imgSpaceSky = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shipshot.png");
        imgAtlasFragments = new Texture("atlasfragment.png");
        for (int i = 0; i < imgFragment[0].length; i++) {
            imgFragment[0][i] = new TextureRegion(imgAtlasFragments, i*200, 0, 200, 200);
            imgFragment[1][i] = new TextureRegion(imgAtlasFragments, i*200, 200, 200, 200);
        }

        btnExit = new ImageButton(imgCross, SCR_WIDTH-40, SCR_HEIGHT-40, 30, 30);

        sndShoot = Gdx.audio.newSound(Gdx.files.internal("shoot2.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        sky[0] = new SpaceSky(0);
        sky[1] = new SpaceSky(SCR_HEIGHT);

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
                    killShip();
                }
                i--;
            }
        }

        // выстрелы
        if(isShipAlive) {
            spawnShots();
        }
        for (int i = 0; i < shots.size(); i++) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                i--;
                continue;
            }
            for (int j = 0; j < enemy.size(); j++) {
                if(enemy.get(j).x<SCR_HEIGHT && shots.get(i).overlap(enemy.get(j))) {
                    // взрыв вражеского корабля
                    spawnFragments(enemy.get(j).x, enemy.get(j).y, enemy.get(j).width, TYPE_ENEMY);
                    shots.remove(i);
                    enemy.remove(j);
                    if(gs.sound) sndExplosion.play();
                    i--;
                    kills++;
                    break;
                }
            }
        }

        // обломки кораблей
        for (int i = 0; i < fragments.size(); i++) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()){
                fragments.remove(i);
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
        for (int i = 0; i < fragments.size(); i++) {
            gs.batch.draw(imgFragment[fragments.get(i).typeShip][fragments.get(i).typeFragment],
                    fragments.get(i).getX(), fragments.get(i).getY(),
                    fragments.get(i).width/2, fragments.get(i).height/2,
                    fragments.get(i).width, fragments.get(i).height,
                    1, 1, fragments.get(i).angle);
        }
        for (int i = 0; i < enemy.size(); i++) {
            gs.batch.draw(imgEnemy, enemy.get(i).getX(), enemy.get(i).getY(), enemy.get(i).width, enemy.get(i).height);
        }
        for (int i = 0; i < shots.size(); i++) {
            gs.batch.draw(imgShot, shots.get(i).getX(), shots.get(i).getY(), shots.get(i).width, shots.get(i).height);
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
        imgShot.dispose();
    }

    void spawnEnemy() {
        if(timeEnemySpawn+timeEnemyInterval < TimeUtils.millis()) {
            enemy.add(new EnemyShip(100, 100));
            timeEnemySpawn = TimeUtils.millis();
        }
    }

    void spawnShots() {
        if(timeShotSpawn+timeShotInterval < TimeUtils.millis()) {
            shots.add(new ShipShot(ship.x, ship.y, ship.width, ship.height));
            timeShotSpawn = TimeUtils.millis();
            if(gs.sound) {
                sndShoot.play();
            }
        }
    }

    void spawnFragments(float x, float y, float shipSize, int typeShip) {
        for (int i = 0; i < 100; i++) {
            fragments.add(new FragmentShip(x, y, shipSize, typeShip));
        }
    }

    // гибель нашего корабля
    void killShip() {
        spawnFragments(ship.x, ship.y, ship.width, TYPE_SHIP);
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
        fragments.clear();
        shots.clear();
        enemy.clear();
        ship = new SpaceShip(SCR_WIDTH/2, 100, 100, 100);
        kills = 0;
        isShipAlive = true;
        isGameOver = false;
    }
}
