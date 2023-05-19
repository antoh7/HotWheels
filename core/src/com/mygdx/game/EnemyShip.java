package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class EnemyShip extends GalaxyObject{
    public EnemyShip(float width, float height) {
        super(0, 0, width, height);
        x = MathUtils.random(0+width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-5f, -2f);
    }

    boolean outOfScreen() {
       return y < - height/2;
    }
}
