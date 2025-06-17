package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Dummy extends Gegner
{
    OwnText damageText;
    Dummy(float x, float y, Main main)
    {
        super(x,y,main,"debug_dummy.png");
        scale(2);
    }
    Dummy(float x, float y, Main main,float width, float height)
    {
        this(x,y,main);
        setSize(width,height);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        damageText.setPosition(getX() + getWidth() / 2, getY() + getHeight() * 2);
    }

    @Override
    void initializeOtherThings() {
        super.initializeOtherThings();
        damageText=new OwnText("!",getX()+getWidth()/2, getY()+getHeight()*2,1, Color.WHITE,Color.BLACK);
    }

    @Override
    public void act(float delta) {
       super.act(delta);
       updatemovement(new Vector2(0,0),delta);
       damageText.act(delta);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        damageText.draw(batch,1);
    }

    @Override
    public void drawShadow(ShapeRenderer shape) {

            //drawHitbox(shape);}
    }

    @Override
    public void drawHitbox(ShapeRenderer shape) {
        shape.rect(hitbox.getX(),hitbox.getY(),hitbox.getWidth(),hitbox.getHeight());

        shape.circle(getHitboxCenterX(),getHitboxCenterY(),5);//Hitboxcenter

        shape.setColor(0.5f,1,0.5f,1);//green= coordinates
        shape.circle(getX(),getY(),4);

        shape.setColor(0f, 0.2f, 1f, 1  );//blue=Hitboxcenter
    }

    @Override
    public int getSignature() {
        return 99;
    }

    @Override
    boolean damageby(float damage) {
        damageEffect();

        damageText.setText(""+damage);
        damageText.setScale(1);
        damageText.clearActions();
        damageText.addAction(Actions.fadeIn(0.5f));
        damageText.addAction(new SequenceAction(Actions.scaleBy(1f, 1f, 0.4f), Actions.delay(2),Actions.fadeOut(1) ,new Action() {
            @Override
            public boolean act(float delta) {
                damageText.setScale(1);
                return true;
            }
        }));
        return false;
    }
}
