package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import static java.lang.Float.NaN;

public class Karltoffelboss extends Boss{
    int recenthits=0;
    Karltoffelboss(float x, float y, Main logic){
        super(x, y, logic,"El_Karltoffel.png");
        speed = 200;
        weight=20;
        sethealth(1000,true);
        scale(0.3f);
        setPosition(x,y);
        setBossName("KARLTOFFEL DER SCHRECKLICHE");
        bossTitel.setColor(Color.YELLOW);
        positionChanged();
        collisionOn=true;
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
        speed=200;
        //collisionOn=false;
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
        if(invincible){return false;}
        recenthits++;
        if (attackStatus != AttackStatus.dash&&recenthits>=3)
        {
            addAction(Actions.sequence(Actions.delay((float) (Math.random()*0.15)), new Action() {
                @Override
                public boolean act(float delta) {
                    shockwaveAttack(0.3f,120);
                    recenthits-= MathUtils.random(1,2);
                    return true;
                }
            }));
        }
        return super.damageby(damage);
    }

    @Override
    void onPlayertouch() {
        super.onPlayertouch();
        if(attackStatus == AttackStatus.dash) {
            Vector2 knockback = player.getDistanceVector(getHitboxCenterX(), hitbox.y).scl(-1);
            knockback.setLength(160);
            player.setAdditionalForce(knockback);
            player.damageby(15);
            collisionOn=true;
            //invincible = true;
            attackStatus = AttackStatus.inactive;
        }
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

            if(collisionOn&&player.collisionOn&& attackdelay<=0.25f) {

                float ellipseDistance= (float) (1.3*Math.pow(getHitboxCenterX()-player.getHitboxCenterX(),2)+4*Math.pow(hitbox.y-player.hitbox.y,2));
                if ( ellipseDistance<Math.pow(300,2)*attackdelay/0.3f) {
                    Vector2 knockback = player.getDistanceVector(getHitboxCenterX(), hitbox.y).scl(-1);
                    knockback.setLength(160);
                    player.setAdditionalForce(knockback);
                    float damage = (float) (Math.min(Math.pow(300, 2) / ellipseDistance,100));
                    if (damage == NaN ||Float.isInfinite(damage)) {
                        damage = 100;
                    }
                    player.damageby(damage);
                    collisionOn=false;
                }
            }
        }
        else{
            attackdelay2 += delta;


            if (attackStatus == AttackStatus.inactive) {
                if (attackdelay2 >= 15) {

                    if (getdistance(spawnx,spawny)<=100) {

                        if(getdistance(player)>=250){
                        attackdelay2 = 0;
                        attackdelay = 0;
                        fireStormattack();
                        recenthits=0;}
                        else{shockwaveAttack(0.3f,150);
                            recenthits= Math.max(recenthits-1,0);}

                    }
                    else{
                    attackStatus=AttackStatus.repositioning;}

                }else if (getdistance(player) >= 400) {
                    //shockwaveAttack(0.3f,150);
                    dashattack();
                    recenthits=0;
                }



                if (attackdelay >= 2)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
                {
                    attackdelay = 0;
                    if (getdistance(player) <= 500 && getdistance(player) >= 100) {
                        fireballringattack(45, (float) Math.random() * 35f);
                        recenthits= Math.max(recenthits-1,0);
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
            } else if (attackStatus==AttackStatus.dash) {
                if(collides)
                {
                    attackStatus = AttackStatus.inactive;
                    speed=200;
                }
                else{
                ismoving=true;
                speed+=160*delta;
                updatemovement(savedVector, delta);}

            }
            else if (attackStatus == AttackStatus.repositioning) {
                if(getdistance(spawnx,spawny)>100)
                {
                   updatemovement(getDistanceVector(spawnx, spawny), delta);
                }
                else {attackStatus = AttackStatus.inactive;
                     attackdelay = 0;
                }

            }


        }
        collides=false;
        //-----
    }


}
