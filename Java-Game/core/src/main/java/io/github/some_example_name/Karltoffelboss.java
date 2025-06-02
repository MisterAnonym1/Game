package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Karltoffelboss extends Boss{
    Karltoffelboss(float x, float y, Main logic){
        super(x, y, logic,"El_Karltoffel.png");
        acceleration = 100;
        maxspeed = 100;
        spawnx = x;
        spawny = y;
        maxhealth = 1000;
        curhealth = 1000;

        scale(0.3f);

    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 320;
        hitboxOffsetY = 200;
        hitbox = new Rectangle(getCenterX(), getCenterY(), getWidth()/4, getHeight()/2);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    public void act(float delta){
        super.act(delta);
        playerinview();
        engagePlayer(delta);
    };
    public void engagePlayer(float delta){
        attackdelay+=delta;

        if (attackdelay>=2)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
        {
            //line of sight
            attackdelay=0;
            if(getdistance(player)<= 500) {
            //fireballattack();
                fireballringattack(45,(float)Math.random()*22.5f);
            }
        }
    };
    public void sterben(){
        Level.deleteList.add(this);
    };

    @Override
    public void dashattack(float delta) {
        super.dashattack(delta);
    }

}
