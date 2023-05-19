package com.mygdx.game;

public class GalaxyObject {
    float x, y; // координаты
    float width, height; // ширина и высота
    float vx, vy; // скорость (приращение координаты)

    public GalaxyObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void move() {
        x += vx;
        y += vy;
    }

    float getX() { // экранная координата х
        return x - width/2;
    }

    float getY() { // экранная координата y
        return y - height/2;
    }
}
