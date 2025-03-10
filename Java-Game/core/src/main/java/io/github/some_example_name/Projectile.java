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
        setPosition(x,y);

        delay =  vanishInSecs;
    }
    PartikelSprite(float x,float y,String filepath ,float vanishInSecs,float xTexture,float yTexture,float width,float heigth)
    {
        super(filepath, xTexture, yTexture, width, heigth);
        setPosition(x,y);

        delay =  vanishInSecs;
    }
    @Override
    public void act(float delta)
    {
        super.act(delta);
        delay-=delta;
        if(delay <= 0)
        {
            destroy();
        }
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
    Projectile(float x, float y, String filepath, Vector2 velocity, int dmg)
    {
        super(x, y, filepath, 3);

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
        reducemovement();
        delay+=delta;
        super.act(delta);

    }

    void onHit()
    {
        destroy();
    }
    void reducemovement()
    {

    }
}

class FireBall extends Projectile
{
    FireBall(float x,float y, Vector2 vel)
    {
        super(x,y,"Fireball.png",vel,20);
        vel.setLength(300);
        scale(2);
    }

}
