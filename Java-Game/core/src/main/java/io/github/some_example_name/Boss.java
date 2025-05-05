package io.github.some_example_name;

import com.badlogic.gdx.math.Polygon;

abstract class Boss extends Gegner {

    Boss (float x, float y, Main logic, String filepath) {
        super(x, y, logic, filepath);
        acceleration = 100;
        maxspeed = 100;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 30;
        hitboxOffsetY = 5;
    }
}

