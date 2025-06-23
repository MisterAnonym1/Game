package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static java.lang.Float.NaN;

class Player extends Entity
{
    boolean isxmoving;
    boolean isymoving;
    MeeleWeapon weapon;
    HealthBar healthbar;
    ArrayList<Entity> gegnerhitliste = new ArrayList<>();
    //Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> sideAttackAnimation;
    Animation<TextureRegion> frontAttackAnimation;
    Animation<TextureRegion> backAttackAnimation;
    //Animation<TextureRegion> defaultAnimation;
    Animation<TextureRegion> deadAnimation;
    boolean isattacking;
    Viewport viewport;
    Vector2 attackline;
    Player(float x, float y, float speed, int leben, Viewport view) {


        super(x, y,new TextureRegion(new Texture("Se_Player_ja.jpg"),0,0,200,180),null);
        toBack();
        player=this;
        weight = 0.5f;
        viewport=view;
        maxspeed = speed;
        acceleration = speed;
        curhealth = leben;
        maxhealth = leben;
        healthbar = new HealthBar(20, 20, maxhealth, 1f, 0.8f,Main.uiStage.getViewport());
        weapon=new Pipe(this);
        Main.uiStage.addActor(healthbar);
        scale(1f);

        texture.flip(true,false);
        walkAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,7,12,0.15f);
        defaultAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,1,6,0.12f);

        sideAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,13,18,0.08f);
        frontAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,25,30,0.08f);
        backAttackAnimation=Animator.getAnimation("Warrior_Blue.png", 6,8,37,42,0.08f);
        deadAnimation=Animator.getAnimation("Dead.png", 7,2,1,14,0.082f);

    }

    @Override
    void initializeHitbox() {
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;
        hitbox = new Rectangle(getX() +getWidth()/2, getY() +getHeight()/2, getWidth()/4f, getHeight()/3f);

    }
    void normalise()
    {
        clearActions();
        setColor(1,1,1,1);
        setScale(1);
        setRotation(0);
        animationstateTime=0;
    }

    float getfootDistance(float x, float y)
    {
        return new Vector2(x - getHitboxCenterX(), y - hitbox.y).len();
    }
    float getfootDistance(TextureActor other)
    {
        return new Vector2(other.getHitboxCenterX() - getHitboxCenterX(), other.getHitboxCenterY() - hitbox.y).len();
    }

    public void draw(Batch batch,ShapeRenderer shape,float delta, float parentAlpha) {
        if(status==EntityStatus.dead){playAnimation(deadAnimation);}

        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha*getColor().a);
        animationstateTime += delta; // Accumulate elapsed animation time
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationstateTime, status==EntityStatus.dead? false:true);

        batch.draw(currentFrame,getX()+ (ismirrored?getWidth():0),getY(),getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());

        if(Main.debugging){
        batch.end();
        //shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.RED);
           drawHitbox(shape);
        shape.end();

        batch.begin();}
    }

    @Override
    public void drawHitbox(ShapeRenderer shape) {
        Vector2 worldPosition = attackline.add(new Vector2(getCenterX(),getCenterY()));
        shape.line(new Vector2(getCenterX(),getCenterY()), worldPosition);
        Vector2 direction= new Vector2(100,0);
        direction.rotateDeg(directionline);
        shape.line(new Vector2(getCenterX(),getCenterY()),new Vector2(getCenterX()+direction.x,getCenterY()+direction.y) );
        super.drawHitbox(shape);

    }

    @Override
    boolean damageby(float damage)
    {
        if(invincible) {
            return false;
        }
        damageEffect();
        curhealth -= damage;
        healthbar.takeDamage(damage);
        if(curhealth > maxhealth) {
            curhealth = maxhealth;
        }
        if(curhealth <= 0) {
            return true;
        }
        return false;
    }
    void sethealth(float health, boolean ignoremax)
    {
        super.sethealth(health, ignoremax);
        healthbar.healTo(health);
    }







    void flip(boolean shouldBeMirrored)
    {
        if(( ismirrored!=shouldBeMirrored))
        {  texture.flip(true,false);
            ismirrored=shouldBeMirrored;
        }
    }
    boolean handleAttack(Entity enti)
    {
        if(gegnerhitliste.contains(enti)){return false;}

         Vector2 line =new Vector2(enti.getHitboxCenterX() - getHitboxCenterX(), enti.getHitboxCenterY() - getHitboxCenterY());
        if(line.len()>(Main.DevMode?85:1000)+enti.hitbox.getWidth()/2){return false;}
        //if(line.angleDeg()>(directionline+50+360)%360||line.angleDeg()<(directionline-50+360)%360){return false;}

        if(player.currentAnimation==player.sideAttackAnimation){
            if(MathHelper.isAngleOutOfBounds(line,directionline,40)){return false;}
            line.setLength(85);}
        else{
            if(MathHelper.isAngleOutOfBounds(line,directionline,50)){return false;}
            line.setLength(65);
        }
        if(Main.DevMode)
        {
            line.setLength(1000);
        }
        //line.add(new Vector2(getCenterX(),getCenterY()));
        if(!MathHelper.isLineIntersectingRectangle(getHitboxCenterX(),getHitboxCenterY(),line.x+getHitboxCenterX(),line.y+getHitboxCenterY(),enti.hitbox)){return false;}
        //System.out.println(line.x+getHitboxCenterX()+"X "+line.y+getHitboxCenterY()+"Y");
        gegnerhitliste.add(enti);
        knockbackFromPlayer(enti,200);
        if(enti.damageby(weapon.damage)) {
           enti.onDeath();
            return true;
        }
        return false;

    }

    boolean handleAttack(TextureActor actor, boolean nix)
    {
        if(gegnerhitliste.contains(actor)){return false;}

        Vector2 line =new Vector2(actor.getHitboxCenterX() - getHitboxCenterX(), actor.getHitboxCenterY() - getHitboxCenterY());
        if(line.len()>90+actor.getWidth()/2){return false;}
        //if(line.angleDeg()>(directionline+50+360)%360||line.angleDeg()<(directionline-50+360)%360){return false;}
        if(MathHelper.isAngleOutOfBounds(line,directionline,50)){return false;}
        if(player.currentAnimation==player.sideAttackAnimation){line.setLength(90+actor.getWidth()/2);}else{line.setLength(72+actor.getWidth()/2);}
        if(!MathHelper.isLineIntersectingRectangle(getHitboxCenterX(),getHitboxCenterY(),line.x+getHitboxCenterX(),line.y+getHitboxCenterY(),actor.hitbox)){return false;}
        //System.out.println(line.x+getHitboxCenterX()+"X "+line.y+getHitboxCenterY()+"Y");
        return true;

    }

    void knockbackFromPlayer(Entity enti, float strength/*100 is medium*/) {
        /*Vector2 knockback = getDistanceVector(enti);
        knockback.setLength(strength);
        enti.applyForce(knockback);*/
        applyknockbackOn(enti,strength);
    }

    @Override
    void moveatdirection(float delta) {
        additionalForce.clamp(0,additionalForce.len()-delta*(350)/**weight*/);
        if(additionalForce.len()<100){additionalForce.setLength(0);}
        if(additionalForce.x!=0||additionalForce.y!=0){
            moveBy((additionalForce.x)*delta, (additionalForce.y)*delta);}
        else
        {
            moveBy((movement.x)*delta, (movement.y)*delta);
        }
    }

    @Override
    public void act(float deltatime)
    {
        super.act(deltatime);
        attackline = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        attackline.add(new Vector2(-getCenterX(),-getCenterY()));

        weapon.act(deltatime);

        if(!weapon.hasActions())
        { isattacking = false;}

        if(!isattacking/*&&Gdx.input.isKeyPressed(Input.Keys.SPACE)*/&&Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                weapon.attack(0.45f);
                gegnerhitliste.clear();
                isattacking = true;
            float angle1= attackline.angleDeg();
            if (angle1>= 225&&angle1<=315) {
                //flip(false);
                playAnimation(frontAttackAnimation);

            } else if ( angle1>=45&&angle1<=135) {
                //flip(false);
                playAnimation(backAttackAnimation);


            } else if (angle1<45||angle1>315) {
                flip(false);
                playAnimation(sideAttackAnimation);

            } else if (angle1>135&&angle1<225) {
                flip(true);
                playAnimation(sideAttackAnimation);
            }
            directionline=Math.round(angle1);
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
        }

        updatemovement(vecup,deltatime);

        if(getY()==NaN||getX()==NaN){
            System.out.println("bug detected");
        }

    }

}
