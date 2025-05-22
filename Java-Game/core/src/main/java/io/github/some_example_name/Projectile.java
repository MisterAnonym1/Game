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

    PartikelSprite(float x, float y, String filepath, float vanishInSecs)
    {

        super(filepath);
       centerAt(x,y);

        delay =  vanishInSecs;
    }
    PartikelSprite(float x,float y,String filepath ,float vanishInSecs,float xTexture,float yTexture,float width,float heigth)
    {
        super(filepath, xTexture, yTexture, width, heigth);
       centerAt(x,y);

        delay =  vanishInSecs;
    }
    @Override
    public void act(float delta)
    {
        super.act(delta);
        delay-=delta;
        if(delay <= 0)
        {
            Level.deleteList.add(this);
        }
    }

    @Override
    public void removeFromLevel() {
        Level.projectiles.remove(this);
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

    void reducemovement(float delta){}

}

class FireBall extends Projectile
{
    static float speed=400;
    float animationstateTime;
    static Animation<TextureRegion> explosion=Animator.getAnimation("Explosions.png",9,1,1,9,0.1f);; //Variable zum speichern der letzten abgespielten animation
    FireBall(float x,float y, Vector2 vel)
    {
        super(x,y,"Fireball.png",vel,20);
        movement.setLength(400);
        scale(0.7f);
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
        TextureRegion currentFrame = explosion.getKeyFrame(animationstateTime, true);
        batch.draw(currentFrame,getCenterX()-getWidth()*2,getCenterY()-getHeight()*2,getOriginX(),getOriginY(),getWidth()*4,getHeight()*4,getScaleX(),getScaleY(),getRotation());}
        else{super.draw(batch,delta);}

    }

    @Override
    void onHit(Entity enti) {
        FireBall fireBall = this;
        movement.setLength(0);
        addAction(Actions.delay(0.2f));
        addAction(Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                enti.damageby(damage);
                return true;
            }
        }));

        addAction(Actions.after(Actions.delay(0.3f)) );
        addAction(Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                Level.deleteList.add(fireBall);
                return true;
            }
        }));

        /// Explosions animation
        //super.onHit(enti);

    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX=0;
        hitboxOffsetY=0;
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth()/1.7f, getHeight()/1.7f);

    }
}
