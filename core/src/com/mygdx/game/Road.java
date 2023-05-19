package com.mygdx.game;

public class Road extends RoadObject {

    public Road(float y) {
        super(0, y, HotWheels.SCR_WIDTH, HotWheels.SCR_HEIGHT);
        vy = -1;
    }

    @Override
    void move() {
        super.move();
        outOfScreen();
    }

    void outOfScreen() {
        if(y<-height) {
            y = height;
        }
    }
}
