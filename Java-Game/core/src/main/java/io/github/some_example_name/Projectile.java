package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;
import java.util.Map;

class PartikelSprite extends TextureActor
{
    float delay;
     Animation<TextureRegion> animation;
    float animationstateTime;
    private boolean useAnimation = false;
    Level level;
    PartikelSprite(float x, float y, String filepath, float vanishInSecs,Level level)
    {
        super(filepath);
        this.level=level;
        //setPosition(x,y);
        centerAt(x,y);
        delay =  vanishInSecs;
        /// causes random inexplainable Texture bug
       // addAction(Actions.sequence(Actions.delay(delay*0.8f),Actions.fadeOut(delay*0.2f)));
    }

    PartikelSprite(float centerx, float centery, Animation<TextureRegion> animation, float vanishInSecs,Level level) {
        super(animation.getKeyFrame(0f));
        this.level=level;
        this.animation = animation;
        this.useAnimation = true;
        centerAt(centerx, centery);
        setOrigin(centerx-getX(), centery-getY());
        delay = vanishInSecs;
        addAction(Actions.sequence(Actions.delay(delay*0.8f),Actions.fadeOut(delay*0.2f)));
    }
    PartikelSprite(float centerx, float centery, Animation<TextureRegion> animation,boolean playOnce,Level level) {
        this(centerx, centery, animation, playOnce ? animation.getAnimationDuration() : Integer.MAX_VALUE,level); // Default delay if not specified
    }

    @Override
    public void destroy() {
        clear();
        remove();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        delay-=delta;
        if(useAnimation && animation != null) {
            animationstateTime += delta;
            setRegion(animation.getKeyFrame(animationstateTime, true));
        }
        if(delay <= 0)
        {
            level.deleteList.add(this);
        }
    }
    @Override
    public void removeFromLevel() {
        level.particles.remove(this);
    }
    // Hilfsmethode, um das TextureRegion der Basisklasse zu setzen
    private void setRegion(TextureRegion region) {
        this.texture = region;
        //setWidth(region.getRegionWidth());
        //setHeight(region.getRegionHeight());
    }


}
// Sprite der herunterzählt und sich nach angegebener Zeit zerstört


class Projectile extends PartikelSprite
{

    Vector2 movement;
    int damage = 20;
    boolean collisionOn=true,isacting =false;
    static float hitboxalpha = 0;;
    Projectile(float x, float y, String filepath, Vector2 velocity,Level level)
    {
        super(x, y,filepath, 3,level);


        movement = velocity;
        //move(movement.x, movement.y);
    }
    Projectile(float centerx, float centery, String filepath, Vector2 velocity, int dmg,Level level)
    {
        super(centerx, centery, filepath, 3,level);

        movement = velocity;
        //move(movement.x, movement.y);

        setdamage(dmg);
    }
    Projectile(float centerx, float centery, Animation<TextureRegion> animation, Vector2 velocity, int dmg, Level level)
    {
        super(centerx, centery, animation, true, level);

        movement = velocity;
        //move(movement.x, movement.y);

        setdamage(dmg);
    }
    void setdamage(int dmg)
    {
        damage = dmg;
    }
    @Override
    public void act(float delta)
    {
        super.act(delta);
        moveBy(movement.x*delta, movement.y*delta);
        reducemovement(delta);

    }

    void onHit()
    {
        level.deleteList.add(this);
    }
    void onHit(Entity enti)
    {
        level.deleteList.add(this);
    }
    @Override
    public void removeFromLevel() {
        level.projectiles.remove(this);
    }

    void reducemovement(float delta){}

}

class FireBall extends Projectile
{
    static float speed=370;
    Gegner origin;
    static Animation<TextureRegion> explosion=Animator.getAnimation("Explosions.png",9,1,1,9,0.1f); //Variable zum speichern der letzten abgespielten animation
    FireBall(float x,float y, Vector2 vel, Gegner origin,Level level)
    {
        super(x,y,"potato.png",vel,20,level);
        movement.setLength(speed);
        scale(2.5f);
        this.origin = origin;
    }
    @Override
    void reducemovement(float delta)
    {
        //movement.x+=(2*delta);
        //movement.y+=(2*delta);
    }

    @Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor().r,getColor().g,getColor().b,1);
        if(hasActions()){
        animationstateTime += delta; // Accumulate elapsed animation time
        TextureRegion currentFrame = explosion.getKeyFrame(animationstateTime, false);
        batch.draw(currentFrame,getCenterX()-getWidth()*2,getCenterY()-getHeight()*2,getOriginX(),getOriginY(),getWidth()*4,getHeight()*4,getScaleX(),getScaleY(),getRotation());}
        else{super.draw(batch,delta);}

    }

    @Override
    void onHit(Entity enti) {
        if(enti==origin){return;}
        FireBall fireBall = this;
        movement.setLength(0);
        collisionOn=false;
        if(enti instanceof Player) {
            origin.damagePlayer(damage);
        }else{
        enti.damageby(damage);}
        addAction(Actions.after(Actions.delay(0.5f)) );
        addAction(Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                level.deleteList.add(fireBall);
                return true;
            }
        }));


    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX=0;
        hitboxOffsetY=0;
        super.initializeHitbox();
        //hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth()/1.7f, getHeight()/1.7f);
    }
}
class Shockwave extends PartikelSprite {

    Shockwave(float centerx, float centery,Level  level) {
        super(centerx, centery,  Animator.getAnimation("small shockwave.png", 9, 9, 8, 78, 0.02f), true,level);
        setSize(106*2,106);
        centerAt(centerx, centery);
        setOrigin(centerx-getX(), centery-getY());

    }


    void onHit(Entity enti) {
        if(animationstateTime<=animation.getAnimationDuration()/5) {
            collisionOn=false;
            Vector2 knockback = getDistanceVector(enti);
            knockback.setLength(170);
            enti.setAdditionalForce(knockback);
            enti.damageby(20);
        }

    }


    public void onTouch(Entity enti) {


    }

    @Override
    public void removeFromLevel() {
       level.objects.remove(this);
    }
}


class WeedyBall extends Projectile
{
    static float speed=400;
    Gegner origin;
    static Animation<TextureRegion>  projectileAnimation= Animator.getAnimation("Rettich-sheet.png",7,6,15,15,1.3f);
    WeedyBall(float x,float y, Vector2 vel, Gegner origin,Level level)
    {
        super(x,y,projectileAnimation,vel,8,level);
        movement.setLength(speed);
        scale(1.6f);
        this.origin = origin;
        centerAt(x,y);
        SoundManager.play("bush-cut",0.7f,1.2f);
    }
    @Override
    void reducemovement(float delta)
    {
        //movement.x+=(2*delta);
        //movement.y+=(2*delta);
    }

    /*@Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor().r,getColor().g,getColor().b,1);
        if(hasActions()){
            animationstateTime += delta; // Accumulate elapsed animation time
            TextureRegion currentFrame = projectileAnimation.getKeyFrame(animationstateTime, false);
            batch.draw(currentFrame,getCenterX()-getWidth()*2,getCenterY()-getHeight()*2,getOriginX(),getOriginY(),getWidth()*4,getHeight()*4,getScaleX(),getScaleY(),getRotation());}
        else{super.draw(batch,delta);}

    }*/

    @Override
    void onHit(Entity enti) {
        if(enti==origin){return;}
        if(enti instanceof Player) {
            origin.damagePlayer(damage);
        }else{
            enti.damageby(damage);}
        level.deleteList.add(this);




    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX=2;
        hitboxOffsetY=6;
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth()/4.4f, getHeight()/4.4f);
    }
}