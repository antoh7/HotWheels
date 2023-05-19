package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    HotWheels gs;
    Texture imgBackGround; // фоновое изображение
    TextButton btnBack;
    String textAboutGame =
                            "Эта игра создана в\n" +
                            "рамках курса Mbile\n" +
                            "Game Development на\n" +
                            "языке java с исполь-\n" +
                            "зованием фреймворка\n" +
                            "LibGDX.\n\n" +
                            "Цель игры - сбивать\n" +
                            "космические корабли\n" +
                            "пришельцев. Победи-\n" +
                            "тель попадает в\n" +
                            "таблицу рекордов.";

    public ScreenAbout(HotWheels galaxyShooter){
        gs = galaxyShooter;
        imgBackGround = new Texture("space02.jpg");
        btnBack = new TextButton(gs.fontLarge, "Back", 230, 100);
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

            if(btnBack.hit(gs.touch.x, gs.touch.y)){
                gs.setScreen(gs.screenIntro);
            }
        }

        // события игры


        // вывод изображений
        gs.camera.update();
        gs.batch.setProjectionMatrix(gs.camera.combined);
        gs.batch.begin();
        gs.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gs.fontLarge.draw(gs.batch, textAboutGame, 20, SCR_HEIGHT-100);
        btnBack.font.draw(gs.batch, btnBack.text, btnBack.x, btnBack.y);
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
}
