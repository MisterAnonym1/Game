package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ConfettiPartikel extends Actor {
    Vector2 vec;
    float width=10;
    float lebensdauer;           // lebensdauer in 1/30 s
    static Color[] colors= {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW,Color.PINK,Color.ORANGE,Color.CYAN,Color.MAGENTA,Color.PURPLE,Color.GOLD,Color.FIREBRICK};
    ConfettiPartikel(float centerx, float cy, float lebensdauer,float angle) {
        super();
        //float winkel = (float)Math.random() * 360;
        float v = (float) (Math.random() * 50 + 20);
        //vec = new Vector2((float) (Math.cos(winkel) * v), (float) (Math.sin(winkel) * v));
        vec= new Vector2(v,0);
        vec.rotateDeg(angle);
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
class ConfettiRain {
    ConfettiPartikel[] confetti;
    ConfettiRain(int count, float centerx, float cy)
    {
        this(count, centerx, cy, 270, 40); // Default angle and range
    }
    ConfettiRain(int count, float centerx, float cy, float angle, float angleRange)
    {
        // angle is the base angle, anglerange is the range of angles to choose from
        confetti = new ConfettiPartikel[count];
        for (int i = 0; i < count; i++)
        {
            float winkel = angle + (float) (Math.random() * angleRange/2);
            confetti[i] = new ConfettiPartikel(centerx, cy, (float) (2 + Math.random() * 2), winkel);
            confetti[i].width = (float) (Math.random() * 10 + 5);
        }

    }

    public void draw(ShapeRenderer shape) {
        for (int i = 0; i < confetti.length; i++) {
            confetti[i].act();
            confetti[i].draw(shape);
        }
    }
}
