package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class FragmentShip extends GalaxyObject{
    int typeFragment;
    int typeShip;
    float v, a;
    float speedRotation, angle;

    public FragmentShip(float x, float y, float size, int typeShip) {
        super(x, y, 0, 0);
        width = MathUtils.random(size/10, size/3);
        height = MathUtils.random(size/10, size/3);
        v = MathUtils.random(2f, 5f);
        a = MathUtils.random(0f, 360f);
        vx = MathUtils.sin(a)*v;
        vy = MathUtils.cos(a)*v;
        this.typeShip = typeShip;
        typeFragment = MathUtils.random(0, 3);
        speedRotation = MathUtils.random(-5f, 5f);
    }

    @Override
    void move() {
        super.move();
        angle += speedRotation;
    }

    boolean outOfScreen() {
       return x < -width/2 || x > SCR_WIDTH + width/2 || y < - height/2 || y > SCR_HEIGHT + height/2;
    }
}
