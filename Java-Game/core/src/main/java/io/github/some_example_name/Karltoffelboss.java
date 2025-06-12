package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Karltoffelboss extends Boss{
    HealthBar bossbar;
    Karltoffelboss(float x, float y, Main logic){
        super(x, y, logic,"El_Karltoffel.png");
        acceleration = 100;
        maxspeed = 100;
        spawnx = x;
        spawny = y;
        maxhealth = 1000;
        curhealth = 1000;
        bossbar = new HealthBar(230, 500, maxhealth, 2f, 1f, Main.uiStage.getViewport());
        scale(0.3f);

    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 320;
        hitboxOffsetY = 200;
        hitbox = new Rectangle(getCenterX(), getCenterY(), getWidth()/4, getHeight()/2);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        batch.end();
        bossbar.draw();
        batch.begin();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    public void act(float delta){
        super.act(delta);
        engagePlayer(delta);
    };

    @Override
    boolean damageby(float damage){
        bossbar.takeDamage(damage);
        curhealth-=damage;
        if(curhealth <= 0) {
            return true;
        } else{
            return false;
        }
    }

    public void engagePlayer(float delta){
        attackdelay+=delta;
        attackdelay2+=delta;
        if (attackdelay>=1)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
        {
            //line of sight
            attackdelay=0;
            if(getdistance(player)<= 500) {
            //fireballattack();
                fireballringattack(45,(float)Math.random()*22.5f);
            }
        }
        if (getdistance(player) <= 250 & attackdelay2 >= 2){
            dashattack(delta);
            attackdelay2 = 0;
        }
    }

}
