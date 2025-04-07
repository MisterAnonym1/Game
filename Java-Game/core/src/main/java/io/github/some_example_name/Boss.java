package io.github.some_example_name;

import com.badlogic.gdx.math.Polygon;

abstract class Boss extends Gegner {

    Boss (float x, float y, Main logic, String filepath) {
        super(x, y, filepath, logic.Player);
        acceleration = 10;
        maxspeed = 10;
        spawnx = x;
        spawny = y;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.player = logic.Player;
        hitboxOffsetX = 30;
        hitboxOffsetY = 5;

        float[] vertices = {hitbox.getX(), hitbox.getY(), hitbox.getX(), hitbox.getY()+ getHeight(),1,hitbox.getY(),1,hitbox.getY()+ getHeight()};
        lineofsight = new Polygon(vertices );
        lineofsight.setOrigin(hitbox.getX()+hitbox.getWidth()/2.0f, hitbox.getY()+ hitbox.getHeight()/2.0f);
    }
}

