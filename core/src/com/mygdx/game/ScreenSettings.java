package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    HotWheels hw;
    Texture imgBackGround;

    TextButton btnSound;
    TextButton btnMusic;
    TextButton btnClearTable;
    TextButton btnBack;
    ScreenGame game;


    public ScreenSettings(HotWheels hotWheels){
        hw = hotWheels;
        imgBackGround = new Texture("settings.jpg");
        btnSound = new TextButton(hw.fontLarge, "Sound on", 100, 600);
        btnMusic = new TextButton(hw.fontLarge, "Music on", 100, 500);
        btnClearTable = new TextButton(hw.fontLarge, "Clear records", 100, 400);
        btnBack = new TextButton(hw.fontLarge, "Back", 100, 100);
        //объект игрового класса
        game = new ScreenGame(hw);
        loadSettings();
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
            if (btnSound.hit(hw.touch.x, hw.touch.y)) {
                hw.sound = !hw.sound;
                updateButtons();
            }
            if (btnMusic.hit(hw.touch.x, hw.touch.y)) {
                hw.music = !hw.music;
                updateButtons();
            }
            if (btnClearTable.hit(hw.touch.x, hw.touch.y)) {
                btnClearTable.setText("Records cleared");
                game.prefs.remove("time");
            }
            if (btnBack.hit(hw.touch.x, hw.touch.y)) {
                hw.setScreen(hw.screenIntro);
                saveSettings();
                btnClearTable.setText("Clear records");
            }
        }


        // вывод изображений
        hw.camera.update();
        hw.batch.setProjectionMatrix(hw.camera.combined);
        hw.batch.begin();
        hw.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSound.font.draw(hw.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(hw.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearTable.font.draw(hw.batch, btnClearTable.text, btnClearTable.x, btnClearTable.y);
        btnBack.font.draw(hw.batch, btnBack.text, btnBack.x, btnBack.y);

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

    void saveSettings() {
        game.prefs.putBoolean("Sound", hw.sound);
        game.prefs.putBoolean("Music", hw.music);
        game.prefs.flush();
    }

    void loadSettings() {
        if(game.prefs.contains("Sound")) hw.sound = game.prefs.getBoolean("Sound");
        if(game.prefs.contains("Music")) hw.music = game.prefs.getBoolean("Music");
        updateButtons();
    }

    void updateButtons() {
        if (hw.sound) {
            btnSound.setText("Sound on");
        } else {
            btnSound.setText("Sound off");
        }
        if (hw.music) {
            btnMusic.setText("Music on");
        } else {
            btnMusic.setText("Music off");
        }
    }
}
