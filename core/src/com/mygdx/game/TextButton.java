package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextButton {
    float x, y;
    float width, height;
    String text;
    BitmapFont font;

    public TextButton(BitmapFont font, String text, float x, float y){
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
    }

    void setText(String text) {
        this.text = text;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
    }

    boolean hit(float tx, float ty){
        return x < tx && tx < x+width && y > ty && ty > y-height;
    }
}
