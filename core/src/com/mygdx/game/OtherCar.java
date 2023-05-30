package com.mygdx.game;

import static com.mygdx.game.HotWheels.SCR_HEIGHT;

import com.badlogic.gdx.math.MathUtils;

public class OtherCar extends RoadObject {
    int type; // 0 - чёрная, 1 - зелёная

    public OtherCar(float width, float height) {
        super(0, 0, width, height);
        x = randomX(99, 284, 479);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-5f, -3f);
        type = MathUtils.random(0, 1);
    }

    boolean outOfScreen() {
       return y < - height/2;
    }
    // рандомный спавн по Х координате
    int randomX(int... nX){
        return nX[MathUtils.random(0,nX.length-1)];
    }
    // проверка на столкновение вражеских машин
    void otherCarsOverlapping(OtherCar car) {
       if ((this.x == car.x ) && (this.y - this.height <= car.y && this.y - this.height >= car.y-height )) {
           this.vy+=Math.abs(this.vy/3);
        }

    }

}
