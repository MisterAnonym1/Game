package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class Mage extends Gegner
{
    Animation<TextureRegion> deadAnimation;

    Mage(float x, float y,Main logic) {
        super( x, y,logic, "Carrot_Idle.png");
        speed = 150;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 0;
        hitboxOffsetY = 5;
        attackdelay= (float) Math.random();
        defaultAnimation= Animator.getAnimation("Rettich-sheet.png",7,6,1,5,0.15f);
        playAnimation(defaultAnimation);
        walkAnimation= Animator.getAnimation("Rettich-sheet.png",7,6,6,13,0.1f);
        deadAnimation= Animator.getAnimation("Rettich-sheet.png",7,6,35,42,0.1f);
        deadAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        playAnimation(defaultAnimation);
        scale(2f);
    }

    @Override
    void initializeHitbox() {
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth()/4, getHeight()/3);
    }

    @Override
    void onDeath() {
        playAnimation(deadAnimation);
        status=EntityStatus.dead;
        final Mage mag=this;
        clearActions();
        setColor(1,1,1,1);
        addAction(Actions.sequence(
                Actions.delay(0.65f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        Level.deleteList.add(mag);
                        spawnCoins();

                        return true;
                    }
                }
        ));

    }


    @Override
    public void drawShadow(ShapeRenderer shape) {
        if(status!=EntityStatus.dead){
            super.drawShadow(shape);}
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(status!=EntityStatus.dead)
        {engagePlayer(delta);}
    }




    public void attack() {
        Vector2 distancevec=getDistanceVector(player);

        if(player.ismoving&&Math.random()<0.5f) {

            Vector2 attackvec2 = player.movement.cpy();
            attackvec2.setLength(   (float) Math.sqrt((distancevec.len()*distancevec.len()*player.speed*player.speed)/Math.abs(WeedyBall.speed*WeedyBall.speed-((player.speed-20)*(player.speed-20))))  ); ///brauche es für Triangulation
            distancevec.add(attackvec2);
        }

        distancevec.setLength(this.getHeight()/2);


        //Projectile expose = new FireBall(getCenterX(),getCenterY(),  distancevec,this);
        Projectile expose = new WeedyBall(getHitboxCenterX(),getHitboxCenterY(),  distancevec,this);

        logic.currentlevel.projectiles.add(expose);

    }


    public void engagePlayer(float delta)
    {
        if(!inradiusof(player,1000)){
            playAnimation(defaultAnimation);
            return;
        }

        pathCountdown-=delta;
        if(playerinview() ) {
            if(attackStatus==AttackStatus.inactive)
            {
                if(inradiusof(player,370))
                {
                    pathCountdown = 0;
                    attackdelay-=delta;
                    if(attackdelay<=0)
                    {
                        if(!inradiusof(player,100)){
                        attack();
                        attackdelay= MathUtils.random(1.6f,2.1f);}
                        else
                        {
                            attackdelay=0.3f;
                        }
                    }
                    playAnimation(defaultAnimation);
                    ismoving=false;
                    updatemovement(movement,delta);
            }
                else
                {
                    attackStatus = AttackStatus.repositioning;
                }
            }
            if(attackStatus== AttackStatus.repositioning)
            {
                pathCountdown = 0;
                goDirectlyToPlayer(delta,40);
                playAnimation(walkAnimation);
                if(inradiusof(player,280))
                {
                    attackStatus=AttackStatus.inactive;
                }
            }

        }
        else
        {
            if(pathCountdown <= 0)
            {
                locateplayer(40, 1200);
                pathCountdown = 1;
            }

            followPath(delta);
            playAnimation(walkAnimation);
        }

        /*if(!inradiusof(player,1000)){
            playAnimation(defaultAnimation);
            return;
        }
        pathCountdown -= delta;
        if(attackStatus==AttackStatus.inactive) {

            if (!inradiusof(player, 400)||inradiusof(player,100)||!playerinview()) {
                attackStatus = AttackStatus.repositioning;
            }
            else{
                attackdelay -= delta;
                pathCountdown = 0;
                playAnimation(defaultAnimation);
                if (attackdelay >= 1.2f) {
                    attackdelay = 0;
                    attack();
                }
                     else {
                        playAnimation(deadAnimation);
                        attackStatus = AttackStatus.repositioning;
                    }

                }

        }
        else
        {
            if(playerinview()){
             if (!inradiusof(player, 300)) {
                   goDirectlyToPlayer(delta, 40);
                   playAnimation(walkAnimation);
                }
             else{attackStatus= AttackStatus.inactive;}
            }
            else{ if(pathCountdown <= 0)
            {
                locateplayer(40, 1200);
                pathCountdown = 1;
            }

            followPath(delta);
            playAnimation(walkAnimation);}
        }*/



    }




}
