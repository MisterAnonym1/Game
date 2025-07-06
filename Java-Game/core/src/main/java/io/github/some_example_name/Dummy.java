package io.github.some_example_name;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Dummy extends Gegner
{
    Dummy(float x, float y, Main main)
    {
        super(x,y,main,"debug_dummy.png");
        collisionOn=true;
    }
    Dummy(float x, float y, Main main,float width, float height)
    {
        this(x,y,main);
        setSize(width,height);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    @Override
    void initializeOtherThings() {
        super.initializeOtherThings();
    }

    @Override
    public void act(float delta) {
       super.act(delta);
       //updatemovement(new Vector2(0,0),delta);

    }


    @Override
    public void drawShadow(ShapeRenderer shape) {

            //drawHitbox(shape);}
    }


    @Override
    public void drawHitbox(ShapeRenderer shape) {
    }

    @Override
    public int getSignature() {
        return 99;
    }

    @Override
    boolean damageby(float damage) {
        damageEffect();
        Level.indicators.addActor(new PopUpText("-" + (int) damage, (player.getCenterX()<getCenterX()?hitbox.x:hitbox.x+hitbox.width), getHitboxCenterY()));
        return false;
    }
}
