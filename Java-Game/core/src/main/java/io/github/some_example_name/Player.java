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
    UpgradeManager upgradeManager;

    Animation<TextureRegion> sideAttackAnimation;
    Animation<TextureRegion> frontAttackAnimation;
    Animation<TextureRegion> backAttackAnimation;
    Animation<TextureRegion> deadAnimation;
    Animation<TextureRegion> runSmoke;
    Viewport viewport;
    Vector2 attackline;
    StorySpeechBox speechbox;
    Displaytext coindisplay;
    float smokedelay;
    Player(float x, float y, Viewport view) {


        super(x, y,new TextureRegion(new Texture("Se_Player_ja.jpg"),0,0,200,180),null);
        toBack();
        player=this;
        weight = 0.5f;
        viewport=view;
        this.speed = 215;
        curhealth = 100;
        maxhealth = 100;
        healthbar = new HealthBar(20, 20, maxhealth, 1f, 0.8f,Main.uiStage.getViewport());
        weapon=new Sword(this);
        Main.uiStage.addActor(healthbar);

        speechbox=new StorySpeechBox(512+150,60,300+300,100);
        ///+Main.uiStage.addActor(speechbox);
        speechbox.toBack();
        speechbox.setVisible(false);

        coindisplay=new Displaytext("Coins:"+Main.invManager.getValueByKey("Coins"),20,570,28,Color.YELLOW,Color.BLACK,false,"Coins");
        Main.uiStage.addActor(coindisplay);
        coindisplay.setZIndex(Integer.MAX_VALUE-10);//ist wie toFront()

        texture.flip(true,false);

        walkAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,7,12,0.1f);
        defaultAnimation= Animator.getAnimation("Warrior_Blue.png",6,8,1,6,0.12f);

        sideAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,14,18,0.092f);
        frontAttackAnimation=Animator.getAnimation("Warrior_Blue.png",6,8,26,30,0.092f);
        backAttackAnimation=Animator.getAnimation("Warrior_Blue.png", 6,8,38,42,0.092f);
        deadAnimation=Animator.getAnimation("Dead.png", 7,2,1,14,0.082f);

        runSmoke=Animator.getAnimation("Smoke5.png",11,15,154,164,0.08f);

        upgradeManager = new UpgradeManager(this);
        upgradeManager.addnewPlayerUpgrade("Health", "Increases the maximum health of the player", (forlevel) -> forlevel * 10, (level) -> {
            maxhealth = 100+level * 10;
            healthbar.setMaxHealth(maxhealth);
        }, Main.invManager.getValueByKey("Upgrade_Health"));
        upgradeManager.addnewPlayerUpgrade("Speed", "Increases the speed of the player", (forlevel) -> forlevel * 5, (level) -> {
            speed = 250 + level * 20;
        },Main.invManager.getValueByKey("Upgrade_Speed"));
        upgradeManager.addnewPlayerUpgrade("Damage", "Increases the damage of the player's weapon", (forlevel) -> 5+forlevel *15 , (level) -> {
            weapon.damage= 40+level * 5;
        },Main.invManager.getValueByKey("Upgrade_Damage"));
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

    public void draw(Batch batch,ShapeRenderer shape,float delta) {
        if(status==EntityStatus.dead){playAnimation(deadAnimation);}

        batch.setColor(getColor().r,getColor().g,getColor().b,getColor().a);
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

    void reAddUiElements()
    {
        Main.uiStage.addActor(healthbar);
        Main.uiStage.addActor(speechbox);
        Main.uiStage.addActor(coindisplay);
        coindisplay.setZIndex(Integer.MAX_VALUE-1);//ist wie toFront()
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
        if(enti.invincible){return false;}
         Vector2 line =new Vector2(enti.getHitboxCenterX() - getHitboxCenterX(), enti.getHitboxCenterY() - getHitboxCenterY());
        if(line.len()>(Main.DevMode?1000:85)+enti.hitbox.getWidth()/2){return false;}

        if(player.currentAnimation==player.sideAttackAnimation){
            if(MathHelper.isAngleOutOfBounds(line,directionline,40)){return false;}
            line.setLength(Main.DevMode?1000:85);}
        else{
            if(MathHelper.isAngleOutOfBounds(line,directionline,50)){return false;}
            line.setLength(Main.DevMode?1000:65);
        }
        if(Main.DevMode)
        {
            line.setLength(1000);
        }
        if(!MathHelper.isLineIntersectingRectangle(getHitboxCenterX(),getHitboxCenterY(),line.x+getHitboxCenterX(),line.y+getHitboxCenterY(),enti.hitbox)){return false;}
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
        if(line.len()>(Main.DevMode?1000:85)+actor.hitbox.getWidth()/2){return false;}

        if(player.currentAnimation==player.sideAttackAnimation){
            if(MathHelper.isAngleOutOfBounds(line,directionline,40)){return false;}
            line.setLength(Main.DevMode?1000:85);}
        else{
            if(MathHelper.isAngleOutOfBounds(line,directionline,50)){return false;}
            line.setLength(Main.DevMode?1000:65);
        }
        if(Main.DevMode)
        {
            line.setLength(1000);
        }
        if(!MathHelper.isLineIntersectingRectangle(getHitboxCenterX(),getHitboxCenterY(),line.x+getHitboxCenterX(),line.y+getHitboxCenterY(),actor.hitbox)){return false;}
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
    boolean isAttacking()
    {
        return weapon.isAttacking;
    }

    @Override
    public void act(float deltatime)
    {
        super.act(deltatime);
        attackline = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        attackline.add(new Vector2(-getCenterX(),-getCenterY()));

        weapon.act(deltatime);


        if(!weapon.preparingAttack()/*&&Gdx.input.isKeyPressed(Input.Keys.SPACE)*/&&Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                //weapon.attack(0.45f);
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
            weapon.attack(currentAnimation, 2, 3);
            gegnerhitliste.clear();
            directionline=Math.round(angle1);
        }


        isxmoving = false;
        isymoving = false;
        ismoving = false;
        Vector2 vecup = new Vector2(1, 0);
        Vector2 vecright = new Vector2(1, 0);
        //zwei Vectoren werden erschaffen um die Laufrichtung des Spielers zu definieren
        if(!weapon.preparingAttack()) {
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
                walkAnimation.setFrameDuration(25/speed);
                smokedelay+=deltatime;
                if(smokedelay>25/speed*3)
                {
                    smokedelay=0;
                    /*PartikelSprite par=new PartikelSprite(getHitboxCenterX(),getHitboxCenterY(),runSmoke,true);
                    //par.setSize(hitbox.width,hitbox.height*0.1f);
                    par.scaleBy(2);
                    par.centerAt(getHitboxCenterX(),hitbox.y+par.getHeight()/2);
                    Level.particles.add(par);*/
                }
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

            vecup.setLength(speed);
        }

        updatemovement(vecup,deltatime);

        if(getY()==NaN||getX()==NaN){
            System.out.println("bug detected");
        }

    }

}
