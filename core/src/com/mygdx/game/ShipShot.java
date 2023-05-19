package com.mygdx.game;


import static com.mygdx.game.HotWheels.SCR_HEIGHT;

public class ShipShot extends GalaxyObject{
    public ShipShot(float x, float y, float width, float height) {
        super(x, y, width, height);
        vy = 8;
    }

    @Override
    void move() {
        super.move();
        outOfScreen();
    }

    boolean outOfScreen() {
       return y > SCR_HEIGHT + height/2;
    }

    boolean overlap(EnemyShip enemy) {
        return Math.abs(x-enemy.x)<width/2+enemy.width/2 & Math.abs(y-enemy.y)<height/2+enemy.height/2;
    }
}
