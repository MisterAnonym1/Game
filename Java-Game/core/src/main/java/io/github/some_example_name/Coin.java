package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Coin extends PartikelSprite {
    private int amount;
    public Coin(float cx, float cy,int amount,Level level) {
        super(cx,cy, Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 8, 1, 0.07f),false,level);
        this.amount = amount;
        setColor(1,1,1,0.1f);
        addAction(Actions.fadeIn(0.2f));
        //coinAnimation = Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 1, 1, 0.5f);
        scaleBy(0.3f);
        animationstateTime+= (float) (Math.random()*0.3f);
    }


    public void add(int value) {
        this.amount += value;
    }
    @Override
    public void removeFromLevel() {
        level.objects.remove(this);
    }


    public void onTouch(Entity enti) {
        if (enti instanceof Player) {
            Main.invManager.addValueByKey("Coins", amount);
            SoundManager.stop("coin_pickup");
            SoundManager.play("coin_pickup",0.11f, MathUtils.random(0.9f,1.1f));
            level.indicators.addActor(new PopUpText("+" + amount, getX() + getWidth() /2, getY() + getHeight(), 1, Color.YELLOW));
            level.deleteList.add(this);
        }
    }
}

