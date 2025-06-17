package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Karltoffelboss extends Boss{

    Karltoffelboss(float x, float y, Main logic){
        super(x, y, logic,"El_Karltoffel.png");
        acceleration = 100;
        maxspeed = 100;
        sethealth(1000,true);
        scale(0.3f);
        setPosition(x,y);
        setBossName("KARLOFF DER SCHRECKLICHE");
        bossTitel.setColor(Color.YELLOW);
    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 5;
        hitboxOffsetY = 30;
        hitbox = new Rectangle(getX(), getX(), getWidth()/3, getHeight()/1.5f);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
    }

    @Override
    void reset() {
        super.reset();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    public void act(float delta){
        super.act(delta);
        engagePlayer(delta);
    };





    public void engagePlayer(float delta){
        attackdelay+=delta;
        attackdelay2+=delta;
        if(attackStatus!=AttackStatus.projectile_storm)
        {
            if (attackdelay2>=15)
            {
                if(getdistance(player)<= 500)
                {
                    attackdelay2=0;
                    attackdelay=0;
                    fireStormattack();

                }
            }
            if (attackdelay>=1)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
            {
                //line of sight
                attackdelay=0;

                if(getdistance(player)<= 500&&getdistance(player)>= 100) {
                    //fireballattack();
                    fireballringattack(45,(float)Math.random()*35f);
                }
            }
        }
        else{
                if(attackdelay2>=10){attackStatus=AttackStatus.inactiv; attackdelay2=0; attackdelay=0;}
                if(attackdelay>=0.08f)
                {
                fireballringattack(45,(float)Math.random()*10f+attackdelay2/10*180);
                attackdelay=0;
                }
            }


        if (getdistance(player) >= 300 & attackdelay2 >= 2){
            //dashattack(delta);
        }
    }

}
