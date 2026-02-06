package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import javax.sound.sampled.SourceDataLine;

public class Healing_potato extends TextureActor {
    int healingAmount;
    Level level;
    public Healing_potato(float x, float y,int healingAmount, Level level) {
        super("healing-potato.png");
        this.level=level;
        this.healingAmount = healingAmount;
        setColor(0.95f, 0.95f, 0.95f, 0f);
        centerAt(x,y);
        if(level.isWall(MyTile.yToRow(y),MyTile.xToColumn(x))) {
            removeFromLevel();
            return;
        }
        setOrigin( x-getX(),y-getY());
       scaleBy(1.5f);
       addAction(Actions.fadeIn(0.9f));
       addAction(Actions.after(Actions.forever(
               Actions.parallel(
            Actions.sequence(
                Actions.scaleTo(1.6f+ this.healingAmount /20f, 1.6f+ this.healingAmount /20f, MathUtils.random(0.4f,0.6f)),
                Actions.scaleTo(1.4f+ this.healingAmount /20f, 1.4f+ this.healingAmount /20f, 0.5f)
            ),
            Actions.sequence(
                 Actions.color(new Color(0.9f, 1f, 0.9f, 1f), 0.5f),
                    Actions.color(new Color(0.75f, 0.95f, 0.75f, 1f), 0.5f)
               )
        ))));
        initializeHitbox();
        collisionOn = true;
        act((float) Math.random());
    }
    public Healing_potato(float x, float y,Level level) {
        this(x, y,  MathUtils.random(10,20),level);
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
        level.objects.remove(this);
    }

    void collect(Entity entity) {
            SoundManager.play("crunch", 1f, MathUtils.random(0.9f, 1.1f));
            entity.sethealth(entity.curhealth+ healingAmount, false);
            level.indicators.addActor(new PopUpText("+" + healingAmount, getX() + getWidth() / 2, getY() + getHeight(), 1, Color.GREEN, Color.BLACK));
            level.deleteList.add(this);

    }
}
