package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Dummy extends Gegner
{
    OwnText damageText;
    Dummy(float x, float y, Main main)
    {
        super(x,y,main,"debug_dummy.png");
        damageText=new OwnText("!",getX()+getWidth()/2, getY()+getHeight()*2,1, Color.WHITE,Color.BLACK);
        scale(2);
    }
    Dummy(float x, float y, Main main,float width, float height)
    {
        this(x,y,main);
        setSize(width,height);
    }

    @Override
    public void act(float delta) {
       super.act(delta);
       damageText.act(delta);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        damageText.draw(batch);
    }

    @Override
    public void drawShadow(ShapeRenderer shape) {
         if(Main.debugging){
            drawHitbox(shape);}
    }

    @Override
    void sterben() {
        Level.deleteList.add(this);
    }

    @Override
    public int getSignature() {
        return 99;
    }

    @Override
    boolean damageby(float damage) {
        damageText.setText(""+damage);
        damageText.setScale(1);
        damageText.clearActions();
        damageText.addAction(Actions.fadeIn(0.5f));
        damageText.addAction(new SequenceAction(Actions.scaleBy(1.5f, 1.5f, 0.4f), Actions.delay(2),Actions.fadeOut(1) ,new Action() {
            @Override
            public boolean act(float delta) {
                damageText.setScale(1);
                return true;
            }
        }));
        return false;
    }
}
