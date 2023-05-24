package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;
import static com.mygdx.game.HotWheels.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class OtherCar extends RoadObject {
    public OtherCar(float width, float height) {
        super(0, 0, width, height);
        x = randomX(99, 284, 479);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-5f, -2f);
    }

    boolean outOfScreen() {
       return y < - height/2;
    }

    int randomX(int... nX){
        return nX[MathUtils.random(0,nX.length-1)];
    }
}
