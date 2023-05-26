package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    HotWheels hw;
    Texture imgBackGround;
    TextButton btnBack;
    String textAboutGame =
                            "Цель игры - объез-\n" +
                            "жать препятствия в \n" +
                            "виде машин. Большее \n" +
                            "время попадает в \n" +
                            "таблицу рекордов.\n "+
                            "\n"+
                            "Управление: \n"+
                             "A - влево,D - вправо.\n"+
                            "X (кнопка вверху) - \n"+
                            "выход на главное\n"+
                                    "меню.\n";



    public ScreenAbout(HotWheels hotWheels){
        hw = hotWheels;
        imgBackGround = new Texture("about.jpg");
        btnBack = new TextButton(hw.fontLarge, "Back", 230, 100);
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

            if(btnBack.hit(hw.touch.x, hw.touch.y)){
                hw.setScreen(hw.screenIntro);
            }
        }



        // вывод изображений
        hw.camera.update();
        hw.batch.setProjectionMatrix(hw.camera.combined);
        hw.batch.begin();
        hw.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        hw.fontLarge.draw(hw.batch, textAboutGame, 20, SCR_HEIGHT-100);
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
}
