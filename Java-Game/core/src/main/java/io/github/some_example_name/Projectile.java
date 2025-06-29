package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class PartikelSprite extends TextureActor
{
    float delay;
     Animation<TextureRegion> animation;
    float animationstateTime;
    private boolean useAnimation = false;

    PartikelSprite(float x, float y, String filepath, float vanishInSecs)
    {
        super(filepath);
        //setPosition(x,y);
        centerAt(x,y);
        delay =  vanishInSecs;
    }
    PartikelSprite(float x, float y, TextureRegion tex, float vanishInSecs)
    {
        super(tex);
        //setPosition(x,y);
        centerAt(x,y);
        delay =  vanishInSecs;
    }
    PartikelSprite(float x,float y,String filepath ,float vanishInSecs,float xTexture,float yTexture,float width,float heigth)
    {
        super(filepath, xTexture, yTexture, width, heigth);
        //setPosition(x,y);
        centerAt(x,y);
        delay =  vanishInSecs;
    }
    PartikelSprite(float centerx, float centery, Animation<TextureRegion> animation, float vanishInSecs) {
        super(animation.getKeyFrame(0f));
        this.animation = animation;
        this.useAnimation = true;
        centerAt(centerx, centery);
        setOrigin(centerx-getX(), centery-getY());
        delay = vanishInSecs;
    }
    PartikelSprite(float centerx, float centery, Animation<TextureRegion> animation,boolean playOnce) {
        this(centerx, centery, animation, playOnce ? animation.getAnimationDuration() : Integer.MAX_VALUE); // Default delay if not specified
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
            Level.deleteList.add(this);
        }
    }
    @Override
    public void removeFromLevel() {
        Level.particles.remove(this);
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
    int damage = 0;
    boolean collisionOn=true,isacting =false;
    static float hitboxalpha = 0;;
    Projectile(float x, float y, String filepath, Vector2 velocity)
    {
        super(x, y,filepath, 3);


        movement = velocity;
        //move(movement.x, movement.y);
        setdamage(20);
    }
    Projectile(float centerx, float centery, String filepath, Vector2 velocity, int dmg)
    {
        super(centerx, centery, filepath, 3);

        movement = velocity;
        //move(movement.x, movement.y);

        setdamage(dmg);
    }
    Projectile(float centerx, float centery, Animation<TextureRegion> animation, Vector2 velocity, int dmg)
    {
        super(centerx, centery, animation, true);

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
        Level.deleteList.add(this);
    }
    void onHit(Entity enti)
    {
        Level.deleteList.add(this);
    }
    @Override
    public void removeFromLevel() {
        Level.projectiles.remove(this);
    }

    void reducemovement(float delta){}

}

class FireBall extends Projectile
{
    static float speed=300;

    static Animation<TextureRegion> explosion=Animator.getAnimation("Explosions.png",9,1,1,9,0.1f); //Variable zum speichern der letzten abgespielten animation
    FireBall(float x,float y, Vector2 vel)
    {
        super(x,y,"potato.png",vel,20);
        movement.setLength(speed);
        scale(2.5f);
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
        FireBall fireBall = this;
        movement.setLength(0);
        collisionOn=false;
        enti.damageby(damage);
        addAction(Actions.after(Actions.delay(0.5f)) );
        addAction(Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                Level.deleteList.add(fireBall);
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

    Shockwave(float centerx, float centery) {
        super(centerx, centery,  Animator.getAnimation("small shockwave.png", 9, 9, 8, 78, 0.02f), true);
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
       Level.objects.remove(this);
    }
}
