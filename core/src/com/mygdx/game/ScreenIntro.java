package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    HotWheels hw;
    Texture imgBackGround; // фоновое изображение
    TextButton btnPlay;
    TextButton btnSettings;
    TextButton btnAbout;
    TextButton btnExit;

    public ScreenIntro(HotWheels hotWheels){
        hw = hotWheels;
        imgBackGround = new Texture("menu.jpg");
        btnPlay = new TextButton(hw.fontLarge, "Play", 100, 500);
        btnSettings = new TextButton(hw.fontLarge, "Settings", 100, 400);
        btnAbout = new TextButton(hw.fontLarge, "About", 100, 300);
        btnExit = new TextButton(hw.fontLarge, "Exit", 100, 200);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.justTouched()){
            hw.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            hw.camera.unproject(hw.touch);
            if(btnPlay.hit(hw.touch.x, hw.touch.y)){
                hw.setScreen(hw.screenGame);
            }
            if(btnSettings.hit(hw.touch.x, hw.touch.y)){
                hw.setScreen(hw.screenSettings);
            }
            if(btnAbout.hit(hw.touch.x, hw.touch.y)){
                hw.setScreen(hw.screenAbout);
            }
            if(btnExit.hit(hw.touch.x, hw.touch.y)){
                Gdx.app.exit();
            }
        }


        // вывод изображений
        hw.camera.update();
        hw.batch.setProjectionMatrix(hw.camera.combined);
        hw.batch.begin();
        hw.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(hw.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(hw.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(hw.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(hw.batch, btnExit.text, btnExit.x, btnExit.y);
        hw.batch.end();
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

}
