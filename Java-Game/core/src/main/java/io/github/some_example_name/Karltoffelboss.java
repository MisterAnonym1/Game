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
        weight=20;
        sethealth(1000,true);
        scale(0.3f);
        setPosition(x,y);
        setBossName("KARLOFF 0123456789:DER SCHRECKLICHE");
        bossTitel.setColor(Color.YELLOW);
        positionChanged();
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

    @Override
    boolean damageby(float damage) {
        if(!collisionOn){return false;}
        shockwaveAttack(0.3f,120);
        return super.damageby(damage);
    }

    public void engagePlayer(float delta) {
        if(attackStatus == AttackStatus.inair)
        {
                ismoving=true;
                updatemovement(savedVector, delta);
                return;
        }
        //player.getdistance(getHitboxCenterX(), hitbox.y)<400
        attackdelay += delta;
        if (attackStatus == AttackStatus.shockwave) {

            if (attackdelay <= 0.2f) {
                logic.randomcamerashake(100*delta,100*delta);
            }

            if(collisionOn&&player.collisionOn&& attackdelay<=0.27f) {

                float ellipseDistance= (float) (1.3*Math.pow(getHitboxCenterX()-player.getHitboxCenterX(),2)+4*Math.pow(hitbox.y-player.hitbox.y,2));
                if ( ellipseDistance<Math.pow(300,2)*attackdelay/0.3f) {
                    Vector2 knockback = player.getDistanceVector(getHitboxCenterX(), hitbox.y).scl(-1);
                    knockback.setLength(160);
                    player.setAdditionalForce(knockback);
                    player.damageby((float) (Math.pow(340,2)/ellipseDistance));
                    System.out.println((float) (Math.pow(340,2)/ellipseDistance));
                    collisionOn=false;
                }
            }
        }
        else{
            attackdelay2 += delta;


            if (attackStatus == AttackStatus.inactive) {
                if (attackdelay2 >= 15) {
                    if (getdistance(player) <= 500) {
                        attackdelay2 = 0;
                        attackdelay = 0;
                        fireStormattack();

                    }
                }else if (getdistance(player) >= 400) {
                    shockwaveAttack(0.3f,150);
                    //dashattack(delta);
                }

                if (attackdelay >= 2)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
                {
                    attackdelay = 0;
                    if (getdistance(player) <= 500 && getdistance(player) >= 100) {
                        fireballringattack(45, (float) Math.random() * 35f);
                        //shockwaveAttack(0.3f,200);
                    }
                }
            }
            else if(attackStatus==AttackStatus.projectile_storm){
                if (attackdelay2 >= 10) {
                    attackStatus = AttackStatus.inactive;
                    attackdelay2 = 0;
                    attackdelay = 0;
                }
                if (attackdelay >= 0.08f) {
                    fireballringattack(45, (float) Math.random() * 10f + attackdelay2 / 10 * 180);
                    attackdelay = 0;
                }
            }



        }
        //-----
    }


}
