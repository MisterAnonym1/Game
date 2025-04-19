package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

class Player extends Entity
{
    boolean isxmoving;
    boolean isymoving;
    boolean invincible = false;
    MeeleWeapon weapon;
    int swingduration = 0;
    String anistatus="";
    HealthBar healthbar;
    ArrayList<Entity> gegnerhitliste = new ArrayList<>();
    Animation<TextureRegion> walkAnimation;

    boolean isattacking;
    Player(float x, float y, float speed, int leben, Viewport view) {


        super(x, y,"Se_Player_ja.jpg",null);
        toBack();
        player=this;
        weight = 0.5f;
        maxspeed = speed;
        acceleration = speed;
        curhealth = leben;
        maxhealth = leben;
        healthbar = new HealthBar(100, 400, maxhealth, 1, view);
        weapon=new Pipe(x,y,this);
        //healthbar.setVisible(false);
        setSize(200, 180);
        scale(1f);
        texture.flip(true,false);
        walkAnimation= Animator.getAnimation("Se_Player_ja.jpg",3,2,1,5,0.2f);


    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;
        hitbox = new Rectangle(getX() +getWidth()/2, getY() +getHeight()/2, getWidth(), getHeight());

    }


    public void draw(Batch batch,ShapeRenderer shape,float parentAlpha) {
        //super.draw(batch, parentAlpha);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        animationstateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = walkAnimation.getKeyFrame(animationstateTime, true);

        batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        batch.end();
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1,1,1,0.6f);
        weapon.setDebug(true);
        weapon.drawDebug(shape);
        shape.end();
        batch.begin();

        weapon.draw(batch,parentAlpha);
        batch.end();
        healthbar.draw(shape);
        batch.begin();
    }

    boolean damageby(int damage)
    {
        if(invincible) {
            return false;
        }
        curhealth -= damage;
        healthbar.takeDamage(damage);
        if(curhealth > maxhealth) {
            curhealth = maxhealth;
        }
        if(curhealth <= 0) {
            return true;
        }
        if(damage >= 25) {
        }
        return false;
    }
    void sethealth(int health, boolean ignoremax)
    {
        super.sethealth(health, ignoremax);
        healthbar.healTo(health);
    }

    @Override
    public void destroy() {
        super.destroy();
        weapon.destroy();
    }

    public void move(float dx, float dy)
    {
        super.moveBy(dx, dy);
    }


    /*public void playAnimationrepeat(int firstpic, int lastpic, int fps, boolean mirrored)
    {
        if(anistatus != firstpic + "" + lastpic + "" + mirrored + fps)
        {
            if((mirrored && !ismirrored) || (!mirrored && ismirrored))
            {

                texture.flip(true,false);
                if(ismirrored)
                {
                    ismirrored = false;
                    swingduration = 0;

                }
                else
                {
                    ismirrored = true;
                    swingduration = 0;

                }
            }
            //waffe.rotateTo((ismirrored ? -20 : 20));

            playAnimation(firstpic, lastpic, RepeatType.loop, fps);
            anistatus = firstpic + " " + lastpic + "" + mirrored + fps;

        }
        else {
            resumeAnimation();
        }
    }*/
    void flip(boolean shouldBeMirrored)
    {
        if(( ismirrored!=shouldBeMirrored))
        {  texture.flip(true,false); ismirrored=true;
            weapon.mirror();
            ismirrored=shouldBeMirrored;
        }
    }

    @Override

    public void act(float deltatime)
    {

        super.act(deltatime);
        weapon.act(deltatime);
            //Math.sin(((float)swingduration / 5) * 3.14159) * 30 * (ismirrored ? -1 : 1)

            //print(" " + Math.sin(((float)swingduration / 50) * 3.14159) * 360 + " ");

        if(!weapon.hasActions())
        { isattacking = false;}

            if(!isattacking&&Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                weapon.attack();
                gegnerhitliste.clear();
                isattacking = true;

            }


        isxmoving = false;
        isymoving = false;
        ismoving = false;
        Vector2 vecup = new Vector2(1, 0);
        Vector2 vecright = new Vector2(1, 0);
        //zwei Vectoren werden erschaffen um die Laufrichtung des Spielers zu definieren

        if((Gdx.input.isKeyPressed(Input.Keys.UP))|| Gdx.input.isKeyPressed(Input.Keys.W))
        {

            vecup = vecup.rotateDeg(90);
            isymoving = true;
        }
        else if((Gdx.input.isKeyPressed(Input.Keys.DOWN))|| Gdx.input.isKeyPressed(Input.Keys.S))
        {

            vecup = vecup.rotateDeg(-90);
            isymoving = true;
        }
        if( Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.A))
        {
            vecright = vecright.rotateDeg(180);
            isxmoving = true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)|| Gdx.input.isKeyPressed(Input.Keys.D))
        {
            isxmoving = true;
        }
        if(isxmoving || isymoving) {
            ismoving = true;

        }

        if(isxmoving && isymoving) {
            vecup = vecup.add(vecright);
        }
        else if(isxmoving && !isymoving) {
            vecup = vecright;
        }

        if(ismoving) {

            if(vecup.angleDeg() > 180) {
                flip(true);
                //playAnimationrepeat(15, 17, (int) (3 + Math.round(maxspeed)), true);
            }
            else if(vecup.angleDeg() > 10 && vecup.angleDeg() < 180) {
                flip(false);

                //playAnimationrepeat(12, 14, (int) (3 + Math.round(maxspeed)), false);
            }
            else if(Math.round(vecup.angleDeg()) == 0) {
                flip(false);
                //playAnimationrepeat(18, 20, (int) (3 + Math.round(maxspeed)), false);
            }
            else if(Math.round(vecup.angleDeg()) == 180) {
                flip(true);
                //playAnimationrepeat(18, 20, (int) (3 + Math.round(maxspeed)), true);
            }
        }
        else {
            //pauseAnimation();
        }

        vecup.setLength((float) (maxspeed));
        //vecup.setLength((float) maxspeed);

        updatemovement(vecup,deltatime);
        if(!isattacking){  weapon.rotateTo((ismirrored ? 30 : -30)); }
        else ismoving = false;
        //weapon.setOrigin((ismirrored ? 100 : 0),0);
        //weapon.moveTo(getCenterX() + (ismirrored ? -20-weapon.hitbox.width : 20), getCenterY()-40);
        stayinWorldbounds();
        weapon.moveTo(getCenterX() + (ismirrored ? -20 : 20),getCenterY()-40);
    }
}
