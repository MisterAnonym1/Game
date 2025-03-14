package io.github.some_example_name;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class MeeleWeapon extends TextureActor
{
    int damage = 40;
    Player player;
    boolean ismirrored=false;
    MeeleWeapon (float x, float y, String filepath,Player pl) {
        super(filepath);
        setPosition(x, y);
        player=pl;
        //hitbox.rotate(90);
    }
    MeeleWeapon (float x, float y, TextureRegion texture,Player pl) {
        super(texture);
        setPosition(x, y);
        player=pl;
        //hitbox.rotate(90);
    }

    @Override
    void initializeHitbox() {
        hitbox = new Rectangle(getX() , getY(), getWidth(), getHeight());
    }

    void rotateTo(float angle)
    {

        rotateBy(-getRotation() - angle);

    }

    void defineCenter(float x, float y)
    {
        super.setOrigin(x, y);
        //hitbox.moveTo(getCenterX(), getCenterY());
        //hitbox.setOrigin(x, y);
    }
    void moveTo(float x, float y)
    {
        super.setPosition((ismirrored ? x-hitbox.width : x), y);
        hitbox.setPosition(x - getWidth() / 2.0f, y + getWidth() / 1.8f);
    }

    void attack() {
        addAction(Actions.timeScale(0.9f,Actions.rotateBy((ismirrored ? 60 :-60 ),0.2f)));
        addAction(Actions.after(Actions.rotateBy((ismirrored ? -60 :60 ),0.2f)));
    }
    void mirror()
    {
        ismirrored=!ismirrored;
        texture.flip(true,false);
        setOrigin((ismirrored ? 100 : 0),0);
        moveTo(getX() + (ismirrored ? -hitbox.width : hitbox.width), getY());
        act(99999);
        player.isattacking=false;
    }
}




class Pipe extends MeeleWeapon {

    Pipe(float x, float y, Player pl) {
        super(x, y, new TextureRegion(new Texture("steel-weapons.png"),384/24*7,0,384/24-1,320/20),pl);
        hitbox = new Rectangle(x - getWidth() / 2, y, getWidth(), -getHeight());
        damage=50;
        scale(7);
        texture.flip(true,false);
        //hitbox.rotate(90);

    }

    @Override
    void attack() {
        super.attack();
    }
}
