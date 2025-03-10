package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TimeScaleAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

class Player extends Entity
{
    boolean isxmoving;
    boolean isymoving;
    boolean invincible = false;
    MeeleWeapon weapon;
    int swingduration = 0;
    String anistatus;
    HealthBar healthbar;
    ArrayList<Entity> gegnerhitliste = new ArrayList<>();

    boolean isattacking;
    Player(float x, float y, float speed, int leben, Viewport view) {


        super(x, y,"Al Assad.png",null);
        toBack();
        player=this;
        weight = 0.5f;
        maxspeed = speed;
        acceleration = speed;
        curhealth = leben;
        maxhealth = leben;
        healthbar = new HealthBar(100, 400, maxhealth, 1, view);
        weapon=new Pipe(x,y);
        //healthbar.setVisible(false);
        setSize(100, 200);
        scale(1f);


    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;
        hitbox = new Rectangle(getX() +getWidth()/2, getY() +getHeight()/2, getWidth(), getHeight());

    }


    public void draw(Batch batch,ShapeRenderer shape,float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        animationstateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        //TextureRegion currentFrame = walkAnimation.getKeyFrame(animationstateTime, true);

        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
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


    public void move(float dx, float dy)
    {
        super.moveBy(dx, dy);
    }

    void destroy()
    {
        super.destroy();

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
            anistatus = firstpic + "" + lastpic + "" + mirrored + fps;

        }
        else {
            resumeAnimation();
        }
    }*/

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
                //playAnimationrepeat(15, 17, (int) (3 + Math.round(maxspeed)), true);
            }
            else if(vecup.angleDeg() > 10 && vecup.angleDeg() < 180) {
                //playAnimationrepeat(12, 14, (int) (3 + Math.round(maxspeed)), false);
            }
            else if(Math.round(vecup.angleDeg()) == 0) {
                //playAnimationrepeat(18, 20, (int) (3 + Math.round(maxspeed)), false);
            }
            else if(Math.round(vecup.angleDeg()) == 180) {
                //playAnimationrepeat(18, 20, (int) (3 + Math.round(maxspeed)), true);
            }
        }
        else {
            //pauseAnimation();
        }

        vecup.setLength((float) (maxspeed));
        //vecup.setLength((float) maxspeed);

        updatemovement(vecup,deltatime);


    }
}
