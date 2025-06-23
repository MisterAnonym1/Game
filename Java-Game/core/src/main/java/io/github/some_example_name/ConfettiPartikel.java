package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ConfettiPartikel extends Actor {
    Vector2 vec;
    float width;
    float lebensdauer;           // lebensdauer in 1/30 s
    static Color[] colors= {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW,Color.PINK,Color.ORANGE,Color.CYAN,Color.MAGENTA,Color.PURPLE,Color.GOLD,Color.FIREBRICK};
    ConfettiPartikel(float centerx, float cy, float lebensdauer) {
        super();
        double winkel = Math.random() * 2 * Math.PI;
        double v = Math.random() * 12 + 4;
        vec = new Vector2((float) (Math.cos(winkel) * v), (float) (Math.sin(winkel) * v));
        setColor(colors[(int) (Math.random() * colors.length)]);
        setPosition(centerx, cy);
        this.lebensdauer = lebensdauer;
    }


    public void draw(ShapeRenderer shape) {
        shape.setColor(getColor());
        shape.rect(getX()-width/2, getY()-width/2, width, width);
    }

    public void act() {
        lebensdauer--;
        moveBy(vec.x, vec.y);
        vec.y = vec.y + 0.2f;
        if(lebensdauer < 0) {
           addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.removeActor()));
           lebensdauer=1; // Set to 1 to prevent immediate destruction after fade out
        }
    }
}
