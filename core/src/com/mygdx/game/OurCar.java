package com.mygdx.game;

public class OurCar extends RoadObject {
    int lives = 1; // количество жизней

    public OurCar(float x, float y, float width, float height) {
        super(x, y, width, height); // передаём параметры в родительский класс
    }

    @Override
    void move() {
        super.move(); // вызываем move родителя
        outOfScreen(); // проверка на вылет за пределы экрана
    }

    void outOfScreen() {
        if(x<0 + width/2){
            vx = 0;
            x = 0 + width/2;
        }
        if(x>HotWheels.SCR_WIDTH - width/2) {
            vx = 0;
            x = HotWheels.SCR_WIDTH - width/2;
        }
    }
}
