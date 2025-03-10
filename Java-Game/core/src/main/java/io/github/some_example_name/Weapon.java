package io.github.some_example_name;
import com.badlogic.gdx.math.Rectangle;

class MeeleWeapon extends TextureActor
{
    int damage = 40;

    MeeleWeapon (float x, float y, String filepath) {
        super(filepath);
        setPosition(x, y);
        //hitbox.rotate(90);
    }

    @Override
    void initializeHitbox() {
        hitbox = new Rectangle(getX() , getY(), getWidth(), getHeight());
    }

    void rotateTo(float angle)
    {

        rotate(-getRotation() - angle);

    }
    void rotate(float angle)
    {
        super.rotateBy(angle);
        //hitbox.rotateBy(angle);
    }
    void defineCenter(float x, float y)
    {
        super.setOrigin(x, y);
        //hitbox.moveTo(getCenterX(), getCenterY());
        //hitbox.setOrigin(x, y);
    }
    void moveTo(float x, float y)
    {
        super.setPosition(x, y);
        hitbox.setPosition(x - getWidth() / 2.0f, y + getWidth() / 1.8f);
    }

    void schlagen() {
      /*rotate(20);
      SystemTools.pause(100);
      rotate(-20);*/

    }
}




class Pipe extends MeeleWeapon {

    Pipe(float x, float y) {
        super(x, y, "bucket.png");
        hitbox = new Rectangle(x - getWidth() / 2, y, getWidth(), -getHeight());
        damage=50;
        //hitbox.rotate(90);

    }




}