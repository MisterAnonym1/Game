package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.lang.reflect.Array;

import static java.lang.Float.NaN;

public class Karltoffelboss extends Boss{
    int recenthits=0;
    double aggressionLevel=70;
    float potatodelay=0;
    Level level;
    TextureRegion zwischenform=new TextureRegion(new Texture("Karltoffelzwischenform.png"));
    TextureRegion[] textureRegions= TextureRegion.split(new Texture("Karltoffel-sheet.png"), 324, 411)[0];
    static Animation<TextureRegion> smokeAuraAnimation= Animator.getAnimation("Smoke5.png",11,15,99,109,0.08f);
    Karltoffelboss(float x, float y, Main logic){
        super(x, y, logic,"El_Karltoffel.png");
        level=logic.currentlevel;
        speed = 250;
        weight=20;
        sethealth(1400,true);
        scale(0.3f);
        setPosition(x,y);
        setOrigin(x-hitbox.x,y-hitbox.y);
        setBossName("KARLTOFFEL DER SCHRECKLICHE");
        bossTitel.setColor(Color.YELLOW);
        positionChanged();
        collisionOn=true;
        status= EntityStatus.inactiv;
        addAction(Actions.sequence(Actions.delay(1.5f),
                Actions.run(() -> {
                    status = EntityStatus.engaging;
                })));
    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 5;
        hitboxOffsetY = 30;
        hitbox = new Rectangle(getX(), getX(), getWidth()/3, getHeight()/1.5f);
    }

    @Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor());
        if(attackStatus==AttackStatus.projectile_storm) {
            batch.draw(texture,getX()+ (ismirrored?getWidth():0),getY()+textureYoffset,getOriginX(),getOriginY(),ismirrored? -getWidth()/1.2f:getWidth()/1.2f,getHeight(),getScaleX(),getScaleY(),getRotation());
        }
        else{
        batch.draw(texture,getX()+ (ismirrored?getWidth():0),getY()+textureYoffset,getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        }
    }

    @Override
    void reset() {
        super.reset();
        speed=250;
        aggressionLevel=70;
        potatodelay=0;
        status= EntityStatus.inactiv;
        addAction(Actions.sequence(Actions.delay(1.5f),
                Actions.run(() -> {
                    status = EntityStatus.engaging;
                })));
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    public void act(float delta){
        super.act(delta);
        if(status==EntityStatus.engaging) {
            engagePlayer(delta);
        }
    }

    @Override
    boolean damageby(float damage) {
        if(invincible||attackStatus==AttackStatus.inair){
            assert attackStatus==AttackStatus.inair&&!invincible : "Karltoffel should be invincible";
            return false;}
        recenthits++;
        aggressionLevel+=damage*0.5f;

        if (attackStatus != AttackStatus.dash&&attackStatus!=AttackStatus.repositioning &&recenthits>=3)
        {
            recenthits-= MathUtils.random(1,2);
            addAction(Actions.sequence(Actions.delay((float) (Math.random()*0.15)), new Action() {
                @Override
                public boolean act(float delta) {
                    if(Math.random() < 1f-aggressionLevel/200f){
                        shockwaveAttack(0.3f,120);
                    }
                    else{
                        Vector2 vector = getDistanceVector(spawnx, spawny).add(getDistanceVector(player).scl(-1));
                        dash(vector,200,0.8f);
                    }

                    return true;
                }
            }));
        }
        return super.damageby(damage);
    }

    @Override
    void damagePlayer(float damage) {
        super.damagePlayer(damage);
        aggressionLevel-= damage;
        if(potatodelay<=0&& player.curhealth<player.maxhealth/2f&&  aggressionLevel<140)
        {
            for (int i = 0; i < MathUtils.random(4,7); i++) {
                Level.objects.add(new Healing_potato(getHitboxCenterX()+MathUtils.random(-400,400), getHitboxCenterY()+MathUtils.random(-400,400), MathUtils.random(10,20), level));
            }
            potatodelay=20;
        }

    }

    @Override
    void onPlayertouch() {
        super.onPlayertouch();
        System.out.println();
        if(attackStatus == AttackStatus.dash) {
            Vector2 knockback = player.getDistanceVector(getHitboxCenterX(), hitbox.y).scl(-1);
            knockback.setLength(160);
            player.setAdditionalForce(knockback);
            damagePlayer(15);
            attackStatus = AttackStatus.inactive;
        }
    }
    void updateNewTexture(TextureRegion textureRegion) {
        if (textureRegion != null) {
            texture = textureRegion;
            setWidth(textureRegion.getRegionWidth());
            setHeight(textureRegion.getRegionHeight());
            setOrigin(getWidth() / 2, getHeight() / 2);
        }

    }

    public void engagePlayer(float delta) {
        //aggressionLevel=(aggressionLevel+ delta*50.0*0.15)/(1.0+delta*0.15);
        aggressionLevel += MathUtils.clamp(50-aggressionLevel,-5*delta,5*delta);
        potatodelay-=delta;
        if(attackStatus == AttackStatus.inair)
        {
                ismoving=true;
                updatemovement(savedVector, delta);
                return;
        }
        attackdelay += delta;
        if (attackStatus == AttackStatus.shockwave) {

            if (attackdelay <= 0.2f) {
                logic.randomcamerashake(100*delta,100*delta);
            }

            if(invincible&&player.collisionOn&& attackdelay<=0.25f) {

                float ellipseDistance= (float) (1.3*Math.pow(getHitboxCenterX()-player.getHitboxCenterX(),2)+4*Math.pow(hitbox.y-player.hitbox.y,2));
                if ( ellipseDistance<Math.pow(300,2)*attackdelay/0.3f) {
                    Vector2 knockback = player.getDistanceVector(getHitboxCenterX(), hitbox.y).scl(-1);
                    knockback.setLength(160);
                    player.setAdditionalForce(knockback);
                    float damage = (float) (Math.min(Math.pow(300, 2) / ellipseDistance,100));
                    if (damage == NaN ||Float.isInfinite(damage)) {
                        damage = 100;
                    }
                    damagePlayer(damage);
                    invincible=false;
                }
            }

        }
        else{
            attackdelay2 += delta;
            if (aggressionLevel>150&&attackStatus!=AttackStatus.projectile_storm) {
                attackdelay2+= delta*aggressionLevel/200;
            }

            if (attackStatus == AttackStatus.inactive) {
                if (attackdelay2 >= 13) {///15
                    attackStatus=AttackStatus.repositioning;
                }
                else if (getdistance(player) >= 400)
                {
                    if(Math.random() < 1f-aggressionLevel/280f) {
                        dashattack();
                    }
                    else
                    {
                        shockwaveAttack(0.3f, 150);
                    }
                    recenthits=0;

                }

                if (attackdelay >= 1.8f)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
                {
                    if(getdistance(player) <= 170) {
                        attackdelay-=0.5f;
                        if (Math.random() < 0.4f*delta)
                        {
                            Vector2 vector;
                            if(aggressionLevel>360+MathUtils.random(0,80)) {
                                 vector = getDistanceVector(spawnx, spawny).add(getDistanceVector(player).scl(-1));
                            }else{vector= getDistanceVector(spawnx, spawny).add(getDistanceVector(player).scl(-20));}
                            dash(vector,180,0.5f);

                        }

                    }else{
                    attackdelay = 0;
                    if (getdistance(player) <= 800) {
                        fireballringattack(45, (float) Math.random() * 35f);
                        recenthits= Math.max(recenthits-1,0);
                    }}
                }

            }
            else if(attackStatus==AttackStatus.projectile_storm)
            {
                if (!invincible) {
                    System.out.println("args not invincible");
                }
                if (attackdelay2 >= 10) {
                    attackStatus = AttackStatus.inactive;
                    attackdelay2 = 0;
                    attackdelay = 0;
                    invincible=false;
                }
                if (attackdelay >= 0.08f) {
                    fireballringattack(45, (float) Math.random() * 10f + attackdelay2 / 10 * 180);
                    attackdelay = 0;
                }
            } else if (attackStatus==AttackStatus.dash)
            {
                if(collides)
                {
                    attackStatus = AttackStatus.inactive;
                    speed=250;
                }
                else{
                ismoving=true;
                speed+=160*delta;
                updatemovement(savedVector, delta);}

            }
            else if (attackStatus == AttackStatus.repositioning)
            {

                 if(getdistance(spawnx,spawny)>100)
                {
                   if(aggressionLevel>180) {shockwavejump(0.3f,150,spawnx,spawny);
                        System.out.println("jumping to center");
                    }
                    else{
                   updatemovement(getDistanceVector(spawnx, spawny), delta);}
                }
                else {
                         if(getdistance(player)>=250){
                             attackdelay2 = 0;
                             attackdelay = 0;
                             fireStormattack();
                             //texture=new TextureRegion(new Texture("Karltoffelzwischenform.png"));
                             invincible=true;
                             recenthits=0;
                             PartikelSprite smokepar=new PartikelSprite(getHitboxCenterX(),getHitboxCenterY(),smokeAuraAnimation,true);
                             smokepar.setSize(hitbox.width*1.8f,hitbox.height*1.8f);
                             smokepar.centerAt(getHitboxCenterX()-10,getHitboxCenterY()+20);
                             Level.particles.add(smokepar);
                         }
                         else{shockwaveAttack(0.3f,100);
                             recenthits= Math.max(recenthits-1,0);}
                }

            }


        }
        collides=false;
        //-----
    }


}
