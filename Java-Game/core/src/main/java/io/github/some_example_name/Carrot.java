package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Carrot extends Gegner
{

    //Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> spinattackAnimation;
    //Animation<TextureRegion> defaultAnimation;
    //Animation<TextureRegion> currentAnimation; //Variable zum speichern der letzten abgespielten animation
    Animation<TextureRegion> deadAnimation;
    Animation<TextureRegion> damageAnimation;
    Carrot( float x, float y, Main logic) {
        super(x, y,logic, "Carrot_Idle.png");
        speed = 150;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 0;
        hitboxOffsetY = 5;
        attackdelay=3;
        weight=1f;
        defaultAnimation= Animator.getAnimation("Carrot-sheet.png",7,6,1,5,0.15f);
        playAnimation(defaultAnimation);
        spinattackAnimation = Animator.connectAnimations(
            Animator.getAnimation("Carrot-sheet.png",7,6,15,28,0.1f),
            Animator.getAnimation("Carrot-sheet.png",7,6,17,31,0.1f),0.1f);
        walkAnimation= Animator.getAnimation("Carrot-sheet.png",7,6,6,13,0.1f);
        deadAnimation= Animator.getAnimation("Carrot-sheet.png",7,6,35,42,0.1f);
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
        final Carrot car=this;
        clearActions();
        setColor(1,1,1,1);
        addAction(Actions.sequence(
            Actions.delay(0.65f),
            new Action() {
                @Override
                public boolean act(float delta) {
                    Level.deleteList.add(car);
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


    @Override
    void onPlayertouch() {
        if(attackStatus==AttackStatus.spin){
        player.applyknockbackOn(this,230);
        applyknockbackOn(player,110);
        player.damageby(10);
        }
    }

    @Override
    void reset() {
        super.reset();
        attackdelay=3;
        hitbox.width=getWidth()/4;
        speed=150;
    }

    public void engagePlayer(float delta)

    {

        if(!inradiusof(player,1000)){
            //playAnimation(defaultAnimation);
            return;
        }

        pathCountdown-=delta;
        if(playerinview() ) {
            pathCountdown = 0;
            goDirectlyToPlayer(delta,40);
            if(inradiusof(player,100))
            {
                attackdelay=0;
            }
            if(attackStatus==AttackStatus.inactive){
            attackdelay-=delta;
            if(attackdelay<=0)
            {
                spinattack();
                attackdelay=3;
            }}
        }
        else
        {
            if(pathCountdown <= 0)
            {
                locateplayer(40, 1200);
                pathCountdown = 1;
            }

            followPath(delta);


        }
        if(currentAnimation!=spinattackAnimation&&currentAnimation!=deadAnimation)
        {
            playAnimation(walkAnimation);
        }

    }
    public void spinattack()
    {
        playAnimation(spinattackAnimation);
        attackStatus= AttackStatus.spin;
        speed=260;
        hitbox.width*=1.3f;
        addAction(Actions.sequence(Actions.delay(2.9f),
            new Action() {
            @Override
            public boolean act(float delta) {
                speed = 150;
                hitbox.width=getWidth()/4;
                playAnimation(walkAnimation);
                attackStatus= AttackStatus.inactive;
                return true;
            }
        }));
        //------
    }

}
