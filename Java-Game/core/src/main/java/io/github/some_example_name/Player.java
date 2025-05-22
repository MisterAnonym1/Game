package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
    HealthBar healthbar;
    ArrayList<Entity> gegnerhitliste = new ArrayList<>();
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> sideAttackAnimation;
    Animation<TextureRegion> frontAttackAnimation;
    Animation<TextureRegion> backAttackAnimation;
    Animation<TextureRegion> defaultAnimation;
    Animation<TextureRegion> currentAnimation; //Variable zum speichern der letzten abgespielten animation
    boolean isattacking;
    Player(float x, float y, float speed, int leben, Viewport view) {


        super(x, y,new TextureRegion(new Texture("Se_Player_ja.jpg"),0,0,200,180),null);
        toBack();
        player=this;
        weight = 0.5f;
        maxspeed = speed;
        acceleration = speed;
        curhealth = leben;
        maxhealth = leben;
        healthbar = new HealthBar(100, 400, maxhealth, 1, Main.uiStage.getViewport());
        weapon=new Pipe(this);
        //healthbar.setVisible(false);
        //setSize(200, 180);
        scale(1f);
        texture.flip(true,false);
        walkAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,7,12,0.15f);
        defaultAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,1,6,0.12f);

        sideAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,13,18,0.08f);
        frontAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,25,30,0.08f);
        backAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,37,42,0.08f);

    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;
        hitbox = new Rectangle(getX() +getWidth()/2, getY() +getHeight()/2, getWidth()/4f, getHeight()/3f);

    }


    public void draw(Batch batch,ShapeRenderer shape,float delta, float parentAlpha) {
        //super.draw(batch, parentAlpha);

        //shape.begin(ShapeRenderer.ShapeType.Filled);
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        animationstateTime += delta; // Accumulate elapsed animation time
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationstateTime, true);

        batch.draw(currentFrame,getX()+ (ismirrored?getWidth():0),getY(),getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());

        if(Main.debugging){
        batch.end();
        //shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);

        shape.end();

        batch.begin();}



        batch.end();

        healthbar.draw();
        batch.begin();
    }

    @Override
    boolean damageby(float damage)
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
    void sethealth(float health, boolean ignoremax)
    {
        super.sethealth(health, ignoremax);
        healthbar.healTo(health);
    }





    public void playAnimation(Animation<TextureRegion> animation)
    {
        if(animation!=currentAnimation)
        {
            animationstateTime=0;
            currentAnimation=animation;
        }
    }

    void flip(boolean shouldBeMirrored)
    {
        if(( ismirrored!=shouldBeMirrored))
        {  texture.flip(true,false);
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
                weapon.attack(0.45f);
                gegnerhitliste.clear();
                isattacking = true;

        }


        isxmoving = false;
        isymoving = false;
        ismoving = false;
        Vector2 vecup = new Vector2(1, 0);
        Vector2 vecright = new Vector2(1, 0);
        //zwei Vectoren werden erschaffen um die Laufrichtung des Spielers zu definieren
        if(!isattacking) {
            if ((Gdx.input.isKeyPressed(Input.Keys.UP)) || Gdx.input.isKeyPressed(Input.Keys.W)) {

                vecup = vecup.rotateDeg(90);
                isymoving = true;
            } else if ((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || Gdx.input.isKeyPressed(Input.Keys.S)) {

                vecup = vecup.rotateDeg(-90);
                isymoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                vecright = vecright.rotateDeg(180);
                isxmoving = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                isxmoving = true;
            }
            if (isxmoving || isymoving) {
                ismoving = true;

            }

            if (isxmoving && isymoving) {
                vecup = vecup.add(vecright);
            } else if (isxmoving && !isymoving) {
                vecup = vecright;
            }

            if (ismoving) {

                playAnimation(walkAnimation);
                if (Math.round(vecup.angleDeg())>95&&Math.round(vecup.angleDeg())<265) {
                    flip(true);
                }
                else if(Math.round(vecup.angleDeg())>275||Math.round(vecup.angleDeg())<85)
                {flip(false);}

            }
            else {
                playAnimation(defaultAnimation);
                /*if (Math.round(directionline)>95&&Math.round(vecup.angleDeg())<265) {
                    flip(true);
                }
                else if(Math.round(directionline)>275||Math.round(vecup.angleDeg())<85)
                {flip(false);}*/
            }

            vecup.setLength(maxspeed);
            updatemovement(vecup,deltatime);
        }

        else{
            ismoving=false;
            if (Math.round(directionline) == 270) {
                //flip(false);
                playAnimation(frontAttackAnimation);

            } else if ( Math.round(directionline) == 90) {
                //flip(false);
                playAnimation(backAttackAnimation);


            } else if (directionline<85||directionline>275) {
                flip(false);
                playAnimation(sideAttackAnimation);

            } else if (directionline>95&&directionline<265) {
                flip(true);
                playAnimation(sideAttackAnimation);
            }
        }

        //+stayinWorldbounds();
        //damageby(8.0f*deltatime);
    }

}
