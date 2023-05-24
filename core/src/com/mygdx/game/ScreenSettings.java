package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    HotWheels gs;
    Texture imgBackGround;

    TextButton btnSound;
    TextButton btnMusic;
    TextButton btnClearTable;
    TextButton btnBack;
    ScreenGame game;


    public ScreenSettings(HotWheels galaxyShooter){
        gs = galaxyShooter;
        imgBackGround = new Texture("settings.jpg");
        btnSound = new TextButton(gs.fontLarge, "Sound on", 100, 600);
        btnMusic = new TextButton(gs.fontLarge, "Music on", 100, 500);
        btnClearTable = new TextButton(gs.fontLarge, "Clear records", 100, 400);
        btnBack = new TextButton(gs.fontLarge, "Back", 100, 100);
        //объект игрового класса
        game = new ScreenGame(gs);
        loadSettings();
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
            if (btnSound.hit(gs.touch.x, gs.touch.y)) {
                gs.sound = !gs.sound;
                updateButtons();
            }
            if (btnMusic.hit(gs.touch.x, gs.touch.y)) {
                gs.music = !gs.music;
                updateButtons();
            }
            if (btnClearTable.hit(gs.touch.x, gs.touch.y)) {
                btnClearTable.setText("Records cleared");
                //game.prefs.putLong("time",0);
                game.prefs.remove("time");
            }
            if (btnBack.hit(gs.touch.x, gs.touch.y)) {
                gs.setScreen(gs.screenIntro);
                saveSettings();
                btnClearTable.setText("Clear records");
            }
        }


        // вывод изображений
        gs.camera.update();
        gs.batch.setProjectionMatrix(gs.camera.combined);
        gs.batch.begin();
        gs.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSound.font.draw(gs.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(gs.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearTable.font.draw(gs.batch, btnClearTable.text, btnClearTable.x, btnClearTable.y);
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

    void saveSettings() {
        game.prefs.putBoolean("Sound", gs.sound);
        game.prefs.putBoolean("Music", gs.music);
        game.prefs.flush();
    }

    void loadSettings() {
        if(game.prefs.contains("Sound")) gs.sound = game.prefs.getBoolean("Sound");
        if(game.prefs.contains("Music")) gs.music = game.prefs.getBoolean("Music");
        updateButtons();
    }

    void updateButtons() {
        if (gs.sound) {
            btnSound.setText("Sound on");
        } else {
            btnSound.setText("Sound off");
        }
        if (gs.music) {
            btnMusic.setText("Music on");
        } else {
            btnMusic.setText("Music off");
        }
    }
}
