package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Iterator;

interface ConfettiAction {
    void draw(ShapeRenderer shape);
    void act(float delta);
    void clear();
}
public class ConfettiPartikel extends Actor {
    Vector2 vec;
    float width=8;
    float lebensdauer;           // lebensdauer in 1/30 s
    static Color[] colors= {Color.BLUE,Color.SALMON,Color.GREEN,Color.YELLOW,Color.PINK,Color.ORANGE,Color.CYAN,Color.MAGENTA,Color.PURPLE,Color.GOLD};
    ConfettiPartikel(float centerx, float cy, float lebensdauer,float angle) {
        super();
        //float winkel = (float)Math.random() * 360;
        float v = (float) (Math.random() * 300 + 100);
        //vec = new Vector2((float) (Math.cos(winkel) * v), (float) (Math.sin(winkel) * v));
        vec= new Vector2(v,0);
        vec.rotateDeg(angle);
        setColor(colors[(int) (Math.random() * colors.length)]);
        setPosition(centerx, cy);
        this.lebensdauer = lebensdauer;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(getColor().r,getColor().g,getColor().b,getColor().a/*lebensdauer*/);
        shape.rect(getX()-width/2, getY()-width/2, width, width);
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        lebensdauer-=delta;
        moveBy(vec.x*delta, vec.y*delta);
        vec.y = vec.y - 120f*delta;
        if(lebensdauer <= 0.3) {
           addAction(Actions.sequence(Actions.fadeOut(0.3f)));
        }
    }
}
class ConfettiExplosion implements ConfettiAction {

    ArrayList<ConfettiPartikel> confetti=new ArrayList<>();
    ConfettiExplosion(int count, float centerx, float cy)
    {
        this(count, centerx, cy, 270, 40); // Default angle and range
    }
    ConfettiExplosion(int count, float centerx, float cy, float angle, float angleRange)
    {
        // angle is the base angle, anglerange is the range of angles to choose from

        for (int i = 0; i < count; i++)
        {
            float winkel = angle + (float) (Math.random() * angleRange)-angleRange/2; // Random angle within the specified range
            ConfettiPartikel confetto = new ConfettiPartikel(centerx, cy, (float) (0.8f + Math.random() * 1.5f), winkel);
            confetto.width = (float) (Math.random() * 5 + 5);
            confetti.add(confetto);
            //Main.uiStage.addActor(confetti[i]);

        }

    }

    public void draw(ShapeRenderer shape) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (ConfettiPartikel partikel : confetti) {
            partikel.draw(shape);
        }
    }
    public void act(float delta) {

        for(ConfettiPartikel partikel : confetti) {
            partikel.act(delta);
        }

        Iterator<ConfettiPartikel> it = confetti.iterator();
        while (it.hasNext()) {
            ConfettiPartikel partikel = it.next();
            if (partikel.lebensdauer <= 0) {
                it.remove(); // Sicheres Entfernen während des Durchlaufs
            }
        }
    }
    public void clear() {
        confetti.clear();
    }
}


class ConfettiRain implements ConfettiAction {

    ArrayList<ConfettiPartikel> partikels = new ArrayList<>();
    float spawndelay;
    float spawnrate;
    float centerx, cy;

    public ConfettiRain(float spawndelay) {
        this.spawnrate=spawndelay;
        this.centerx = 512;
        this.cy = 288;
    }

    public void draw(ShapeRenderer shape) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (ConfettiPartikel partikel : partikels) {
            partikel.draw(shape);
        }
    }

    public void act(float delta) {
        spawndelay -= delta;
        if (spawndelay <= 0) {
            ConfettiPartikel partikel = new ConfettiPartikel((float)Math.random()*1024, 576, (float) (0.8f + Math.random() * 1.5f), 270);
            partikel.width = (float) (Math.random() * 5 + 5);
            partikels.add(partikel);
            spawndelay = spawnrate; // Reset the spawn delay
        }

        for (ConfettiPartikel partikel : partikels) {
            partikel.act(delta);
        }

        Iterator<ConfettiPartikel> it = partikels.iterator();
        while (it.hasNext()) {
            ConfettiPartikel partikel = it.next();
            if (partikel.lebensdauer <= 0) {
                it.remove(); // Sicheres Entfernen während des Durchlaufs
            }
        }

    }

    public void clear() {
        partikels.clear();
    }
}


class ConfettiManager {
    ArrayList<ConfettiAction> effects = new ArrayList<>();

    public void add(ConfettiAction confettieffect) {
        effects.add(confettieffect);
    }

    public void draw(ShapeRenderer shape) {
        for (ConfettiAction effect : effects) {
          effect.draw(shape);
        }
    }

    public void act(float delta) {
        for (ConfettiAction  effect : effects) {
            effect.act(delta);
        }
    }

    public void clear() {
        effects.clear();
    }
}

