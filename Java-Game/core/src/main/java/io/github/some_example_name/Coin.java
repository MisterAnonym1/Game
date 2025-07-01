package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Coin extends PartikelSprite {
    private int amount;
    public Coin(float cx, float cy,int amount) {
        super(cx,cy, Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 8, 1, 0.07f),false);
        this.amount = amount;
        //coinAnimation = Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 1, 1, 0.5f);
        scaleBy(0.4f);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void add(int value) {
        this.amount += value;
    }
    @Override
    public void removeFromLevel() {
        Level.objects.remove(this);
    }


    public void onTouch(Entity enti) {
        if (enti instanceof Player) {
            Main.invManager.addValueByKey("Coins", amount);
            SoundManager.stop("coin_pickup");
            SoundManager.play("coin_pickup",0.11f, MathUtils.random(0.9f,1.1f));
            Level.indicators.addActor(new PopUpText("+" + amount, getX() + getWidth() /2, getY() + getHeight(), 17, Color.YELLOW, Color.BLACK));
            Level.deleteList.add(this);
        }
    }
}
