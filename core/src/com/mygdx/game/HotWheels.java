package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class HotWheels extends Game {
    // ширина и высота экрана
    public static final float SCR_WIDTH = 576, SCR_HEIGHT = 1000;

    // системные объекты
    SpriteBatch batch; // ссылка на объект, отвечающий за вывод изображений
    OrthographicCamera camera; // пересчитывает размеры для различных экранов
    Vector3 touch; // этот объект хранит координаты касания экрана
    BitmapFont fontLarge, fontSmall; // шрифт

    ScreenIntro screenIntro;
    ScreenGame screenGame;
    ScreenSettings screenSettings;
    ScreenAbout screenAbout;

    boolean sound = true;
    boolean music = true;

    @Override
    public void create () {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        createFont();

        screenIntro = new ScreenIntro(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenAbout = new ScreenAbout(this);

        setScreen(screenIntro);
    }

    void createFont(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Halogen_0.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        parameter.size = 50;
        parameter.color = Color.TEAL;
        parameter.borderWidth = 3;
        parameter.borderColor = Color.BLACK;
        fontLarge = generator.generateFont(parameter);
        parameter.size = 30;
        fontSmall = generator.generateFont(parameter);
    }

    @Override
    public void dispose () {
        batch.dispose();
        fontLarge.dispose();
    }
}
