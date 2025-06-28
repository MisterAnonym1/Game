package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends PartikelSprite implements Groundobject {
    private int amount;
    public Coin(float cx, float cy,int amount) {
        super(cx,cy, Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 8, 1, 0.07f),false);
        this.amount = amount;
        //coinAnimation = Animator.getAnimation(new TextureRegion(new Texture("Coin-rotating.png")), 1, 1, 0.5f);
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

    @Override
    public void onTouch(Entity enti) {
        if (enti instanceof Player) {
            Main.invManager.addValueByKey("Coins", amount);
            SoundManager.play("coin_pickup",1,1);
            Level.deleteList.add(this);
        }
    }
}
