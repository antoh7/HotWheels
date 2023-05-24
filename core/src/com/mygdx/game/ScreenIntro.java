package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    HotWheels gs;
    Texture imgBackGround; // фоновое изображение
    TextButton btnPlay;
    TextButton btnSettings;
    TextButton btnAbout;
    TextButton btnExit;

    public ScreenIntro(HotWheels galaxyShooter){
        gs = galaxyShooter;
        imgBackGround = new Texture("menu.jpg");
        btnPlay = new TextButton(gs.fontLarge, "Play", 100, 500);
        btnSettings = new TextButton(gs.fontLarge, "Settings", 100, 400);
        btnAbout = new TextButton(gs.fontLarge, "About", 100, 300);
        btnExit = new TextButton(gs.fontLarge, "Exit", 100, 200);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.justTouched()){
            gs.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gs.camera.unproject(gs.touch);
            if(btnPlay.hit(gs.touch.x, gs.touch.y)){
                sleep(100);
                gs.setScreen(gs.screenGame);
            }
            if(btnSettings.hit(gs.touch.x, gs.touch.y)){
                gs.setScreen(gs.screenSettings);
            }
            if(btnAbout.hit(gs.touch.x, gs.touch.y)){
                gs.setScreen(gs.screenAbout);
            }
            if(btnExit.hit(gs.touch.x, gs.touch.y)){
                Gdx.app.exit();
            }
        }

        // события игры


        // вывод изображений
        gs.camera.update();
        gs.batch.setProjectionMatrix(gs.camera.combined);
        gs.batch.begin();
        gs.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(gs.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(gs.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(gs.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(gs.batch, btnExit.text, btnExit.x, btnExit.y);
        gs.batch.end();
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
        imgBackGround.dispose();
    }

    void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e){

        }
    }
}
