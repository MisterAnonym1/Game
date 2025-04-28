package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
        delay-=delta;
        if(delay <= 0)
        {
            destroy();
        }
        super.act(delta);
    }

    @Override
    public void destroy() {
        super.destroy();
        Level.projectiles.remove(this);
    }
}
// Sprite der herunterzählt und sich nach angegebener Zeit zerstört


class Projectile extends PartikelSprite
{

    Vector2 movement;
    Rectangle hitbox;
    int damage = 0;
    boolean collisionOn=true,isacting =false;
    static float hitboxalpha = 0;
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

        delay-=delta;
        if(delay <= 0)
        {
            onHit();
            return;
        }
        moveBy(movement.x*delta, movement.y*delta);
        reducemovement(delta);
        delay+=delta;
        super.act(delta);

    }

    void onHit()
    {
        destroy();
    }
    void reducemovement(float delta)
    {

    }
}

class FireBall extends Projectile
{
    FireBall(float x,float y, Vector2 vel)
    {
        super(x,y,"Fireball.png",vel,20);
        movement.setLength(400);
        scale(0.8f);
    }
    @Override
    void reducemovement(float delta)
    {
        movement.x+=(2*delta);
        movement.y+=(2*delta);
    }

}
