package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Healing_potato extends TextureActor {
    int healingAmount;
    public Healing_potato(float x, float y,int healingamount) {
        super("healing-potato.png");
        healingAmount = healingamount;
        setColor(0.95f, 0.95f, 0.95f, 0f);
        centerAt(x,y);
        setOrigin( x-getX(),y-getY());
       scaleBy(1.5f);
       addAction(Actions.fadeIn(0.3f));
       addAction(Actions.forever(
               Actions.parallel(
            Actions.sequence(
                Actions.scaleTo(1.6f+healingAmount/20f, 1.6f+healingAmount/20f, 0.5f),
                Actions.scaleTo(1.4f+healingAmount/20f, 1.4f+healingAmount/20f, 0.5f)
            ),
            Actions.sequence(
                 Actions.color(new Color(0.9f, 1f, 0.9f, 1f), 0.5f),
                    Actions.color(new Color(0.75f, 0.95f, 0.75f, 1f), 0.5f)
               )
        )));
        initializeHitbox();
        collisionOn = true;
        act((float) Math.random());
    }
    public Healing_potato(float x, float y) {
        this(x, y, 5);
    }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void removeFromLevel() {
        Level.objects.remove(this);
    }

    void collect(Entity entity) {
            entity.sethealth(entity.curhealth+ healingAmount, false);
            Level.indicators.addActor(new PopUpText("+" + healingAmount, getX() + getWidth() / 2, getY() + getHeight(), 19, Color.GREEN, Color.BLACK));
            Level.deleteList.add(this);

    }
}
