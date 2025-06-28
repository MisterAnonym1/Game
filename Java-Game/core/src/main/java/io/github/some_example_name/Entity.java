package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.Viewport;



class Entity extends TextureActor
{
    float maxhealth, curhealth;
    float speed, directionline=0,shadowscale=1;
    boolean  ismoving, isattacking;
    Vector2 movement;
    Vector2 additionalForce;
    float spawnx;
    float spawny;
    boolean ismirrored;
    boolean invincible = false;
    float animationstateTime=0f, weight;
    EntityStatus status;
    Rectangle worldbounds;
    boolean collides = false;
    Player player;
    Animation<TextureRegion> defaultAnimation;
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> currentAnimation; //Variable zum speichern der letzten abgespielten animation
    public enum EntityStatus { inactiv, idle, engaging,dead }

    Entity(float x, float y, TextureRegion tex, Player player)
    {
        super(tex);
        spawnx = x;
        spawny = y;
        this.player=player;
        additionalForce = new Vector2(0, 0);
        movement = new Vector2(0, 0);
        curhealth = 100;
        maxhealth=100;
        weight = 1;
        status = EntityStatus.inactiv;
        setPosition(x,y);
    }
    Entity(float x, float y, String filepath,Player player)
    {
         this(x,y,new TextureRegion(new Texture(filepath)), player);
    }

void reset()
    {
        setPosition(spawnx,spawny);
        sethealth(maxhealth,true);
        collisionOn=true;
        invincible=false;
        additionalForce.set(0,0);
        status = EntityStatus.inactiv;
        animationstateTime=0;
        currentAnimation=defaultAnimation;
        ismirrored=false;
        clearActions();
        //super.act(999);
        setColor(1,1,1,1);
    }


    public void draw(Batch batch, float delta) {
        batch.setColor(getColor().r,getColor().g,getColor().b,getColor().a);
        animationstateTime += delta;
        if(movement.angleDeg()>90&&movement.angleDeg()<270)
        {
            ismirrored=false;
        }
        else{ismirrored=true;}
        if (currentAnimation==null){batch.draw(texture,getX()+ (ismirrored?getWidth():0),getY(),getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        }
        else {
            TextureRegion currentFrame = currentAnimation.getKeyFrame(animationstateTime, true);
            batch.draw(currentFrame,getX()+ (ismirrored?getWidth():0),getY(),getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        }

    }

    public void drawShadow(ShapeRenderer shape)
    {
        shape.ellipse( hitbox.getX()+hitbox.getWidth() *0.1f, hitbox.getY()-hitbox.getWidth()/4 , hitbox.getWidth() *0.8f, hitbox.getWidth() / 2);
    }

    public void playAnimation(Animation<TextureRegion> animation)
    {
        if(animation!=currentAnimation)
        {
            animationstateTime=0;
            currentAnimation=animation;
        }
    }

    void centerAtActor(Entity other)
    {
       setPosition(other.getX()+other.hitbox.width/2-getWidth()/2,other.getY()+other.hitbox.height/2-getHeight()/2);
    }
    void sethealth(float health, boolean ignoremax)
    //ignoriert den als Limit für die Max Health gesetzten Wert und setzt maxhealth = health als maximale Health
    {
        if(health > maxhealth) {
            if(ignoremax)
            {
                maxhealth = health;
                curhealth = health;
            }
            else {
                curhealth = maxhealth;
            }
        }
        else {
            curhealth = health;
        }
    }
    void sethealth(float health)
    //ignoriert den als Limit für die Max Health gesetzten Wert und setzt maxhealth = health als maximale Health
    {
       sethealth(health,false);
    }

    boolean damageby(float damage)
    {
        curhealth -= damage;
        damageEffect();
        if(curhealth > maxhealth) {
            curhealth = maxhealth;
            return false;
        }
        if(curhealth <= 0) {
            //destroy();
            status=EntityStatus.dead;
            //Level.deleteList.add(this);
            return true;
        }
        return false;

    }

    void damageEffect() {
        // This method can be used to create a visual effect when the enemy is damaged.
        final Color originalColor = /*getColor().cpy()*/ Color.WHITE;
        setColor(1, 0.4f, 0.4f, 1);
        addAction(new SequenceAction(
            Actions.delay(0.2f),
            new Action() {
                @Override
                public boolean act(float delta) {
                    setColor(originalColor);
                    return true;
                }
            }
        ));


    }
    void onDeath()
    {
        Level.deleteList.add(this);
    }


    void setWorldbounds(Rectangle rec)
    {
        if(worldbounds != null) {
        }
        worldbounds = rec;
    }
    void setWorldbounds(float minX, float maxX, float minY, float maxY)
    {
        if(worldbounds != null) {

        }
        worldbounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);

    }

    boolean inWorldbounds()
    {
        if(worldbounds == null) {
            return true;
        }

        float minX = worldbounds.getX();
        float maxX = worldbounds.getX() + worldbounds.getWidth() ;
        float minY = worldbounds.getY() ;
        float maxY = worldbounds.getY() + worldbounds.getHeight();
        if(hitbox.getX() < minX || hitbox.getX() + hitbox.getWidth() > maxX) {
            return false;
        }
        if(hitbox.getY()  < minY || hitbox.getY() + hitbox.getHeight()  > maxY) {
            return false;
        }
        return true;
    }

    void stayinWorldbounds()
    {
        if(worldbounds == null||getWidth()>worldbounds.getWidth()||getHeight()>worldbounds.getHeight()) {
            return;
        }
        float minX = worldbounds.getX();
        float maxX = worldbounds.getX() + worldbounds.getWidth()-hitbox.getWidth() ;
        float minY = worldbounds.getY();
        float maxY = worldbounds.getY() + worldbounds.getHeight()-hitbox.getHeight();
        setX(MathUtils.clamp(getX(),minX,maxX));
        setY(MathUtils.clamp(getY(),minY,maxY));


    }
    void loopWorldbounds()
    {
        if(worldbounds == null||getWidth()>worldbounds.getWidth()||getHeight()>worldbounds.getHeight()) {
            return;
        }
        float minX = worldbounds.getX();
        float maxX = worldbounds.getX() + worldbounds.getWidth()-hitbox.getWidth() ;
        float minY = worldbounds.getY() ;
        float maxY = worldbounds.getY() + worldbounds.getHeight()-hitbox.getHeight();
        if(hitbox.getX()  < minX) {
            setPosition(maxX,getY());
        }
        else if(hitbox.getX() > maxX) {
            setPosition(minX,getY());
        }

        if(hitbox.getY()  < minY) {
            setPosition(getX(),maxY);


        }
        else if(hitbox.getY()  > maxY) {
            setPosition(getX(),minY);

        }

    }

    void updatemovement(Vector2 direction,float deltatime)
    {
        movement=direction;
        if(ismoving) {

            movement.setLength(speed);

            if(movement.len()>0)
            {
               directionline=movement.angleDeg();
            }
        }
        else
        {
            reduceSpeed(1,1);
        }

        moveatdirection(deltatime);

    }


    void applyForce(Vector2 force)
    {
        force.setLength(force.len() / weight);
        additionalForce = additionalForce.add(force);
    }
    void setAdditionalForce(Vector2 force)
    {
        force.setLength(force.len() / weight);
        additionalForce = force;
    }


    float easeOutCubic(float x)
    {
        return (float) (1 - Math.pow(1 - x, 3));
    }


    void moveatdirection(float delta)
    {
        additionalForce.clamp(0,additionalForce.len()-delta*(200)/**weight*/);
        if(additionalForce.len()<100){additionalForce.setLength(0);}
        if(additionalForce.x!=0||additionalForce.y!=0){
        moveBy((additionalForce.x)*delta, (additionalForce.y)*delta);}
        else
        {
            moveBy((movement.x)*delta, (movement.y)*delta);
        }
    }
    void moveReverse(Vector2 move, int speed)
    {

        moveatAngle(speed, move.angleDeg());

    }

    void reduceSpeed(float haftreibungsKoeffizient,float delta)//Haftreibungs koeffinzient zwischen 0 und 1
    {

        movement.setLength( movement.len() * (1 - haftreibungsKoeffizient*delta));
    }

    public void moveatAngle(float length, float angle)
    {
        float x = (float) (length * Math.cos(angle * Math.PI / 180));
        float y = (float) ( length * Math.sin((angle * Math.PI) / 180));
        moveBy(x, y);
        //mit dieser Methode kann man einen genauen Winkel angeben, in welchen sich die Entity bewegen soll

    }
    public void moveatAngle(Vector2 vector)
    {
        moveBy(vector.x,vector.y);

    }

    void centerAt(float x, float y)
    {
        setPosition(x-hitbox.getWidth()/2+hitboxOffsetX,y-hitbox.getHeight()/2+hitboxOffsetY);
    }




    public boolean inradiusof(TextureActor other, float radius)
    {

        if(getdistance(other) <= radius)
        {
            return true;
        }
        return false;
    }
    public boolean inradiusof(float x, float y,float radius)
    {

        if(getdistance(x,y) <= radius)
        {
            return true;
        }
        return false;
    }




    /*public float getdistance(Sprite other)
    {
        float distancex = other.getX() - getX();
        float distancey = other.getY() - getY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));
        //Überprüft ob etwas in einem gewissen Radius von diesem Entity ist
    }*/
    void applyknockbackOn(Entity enti, float strength/*100 is medium*/) {
        Vector2 knockback = getDistanceVector(enti);
        knockback.setLength(strength);
        //enti.applyForce(knockback);
        enti.setAdditionalForce(knockback);
    }

    boolean isactiv()
    {
        if(status == EntityStatus.inactiv) {
            return false;
        }
        return true;
    }
    boolean isengaging()
    {
        if(status == EntityStatus.engaging) {
            return true;
        }
        return false;
    }



    void activate()
    {
        status = EntityStatus.idle;
    }
    void setengaging()
    {
        status = EntityStatus.engaging;
    }
    void deactivate()
    {
        status = EntityStatus.inactiv;
    }

}






class Arrow1 extends Sprite {





    float shaperotation=0;
     float dborderwidth = 10;
    Arrow1(float x1, float y1, float length, float angle)
    {
        setOrigin(x1, y1);
        setPosition(x1,y1);
        dborderwidth=length/10;

        //tri = new MeshSpawnShapeValue.Triangle(x1 + dborderwidth * 8, y1 + dborderwidth / 1, x1 + dborderwidth * 8, y1 - dborderwidth / 1, x1 + dborderwidth * 8 + dborderwidth * 2.5, y1);
        rotate(angle);
        //setVisible(false);
    }

    void rotateTo(float angle)
    {
        rotate( (-getRotation() - angle));


    }

    void draw(ShapeRenderer shape)
    {

       shape.begin(ShapeRenderer.ShapeType.Filled);
       shape.setColor(0.1F, 0.1F, 0.8F, 0.5F);
        shape.rect(1,1,dborderwidth*10,dborderwidth);
       shape.triangle(getX() + dborderwidth * 10, getY() + dborderwidth *2, getX() + dborderwidth * 10, getY() - dborderwidth , (float) (getX() + dborderwidth * 10 + dborderwidth * 2.5), getY()+dborderwidth/2);
       shape.end();
    }
    void setVisible(boolean visible)
    {
        if(visible)
        {
            setAlpha(1);
        }
        else
        {
            setAlpha(0);
        }
    }


}

class HealthBar extends Actor {
    Rectangle h1;
    Rectangle h2;
    Rectangle h3;
    float maxHealth;
    float maxLaenge;
    float currentHealth;
    Viewport viewport;
    HealthBar(float xPos, float yPos, float maxhealth, float xscale,float yscale, Viewport view) {
        h1 = new Rectangle(xPos, yPos, 296f*xscale, 50f*yscale);
        h3 = new Rectangle(xPos + 5f*xscale, yPos + 6f*yscale, 286f*xscale, 38f*yscale);
        h2 = new Rectangle(xPos + 5f*xscale, yPos + 6f*yscale, 286f*xscale, 38f*yscale);
       viewport=view;
        setColor(Color.CHARTREUSE);
       /* h1.setFillColor(new Color(80, 74, 74))
        h2.setFillColor(Color.chartreuse);
        h3.setFillColor(Color.chartreuse, 0.2);*/
        maxLaenge = h2.getWidth();
        this.maxHealth = maxhealth;
        currentHealth = maxHealth;
    }
    void centeratX()
    {
        float x1 =h1.getX();
        h1.setX(h1.getX()- h1.width / 2.0f);// Zentriere horizontal
        h2.setX(x1- h2.width / 2.0f);// Zentriere horizontal
        h3.setX(x1- h3.width / 2.0f);// Zentriere horizontal
        //setY(getY());
    }

    void takeDamage(float damage) {
        currentHealth -= damage;
        h2.setWidth((maxLaenge * currentHealth / maxHealth)<0?0:maxLaenge * currentHealth / maxHealth);

    }
    @Override
    public void draw(Batch batch, float alpha)
    {
        batch.end();
        ShapeRenderer shape= new ShapeRenderer();
        shape.setProjectionMatrix(viewport.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f,0.2f,0.2f,1);
        shape.rect(h1.x,+h1.y,h1.width,h1.height);


        //shape.setColor(127.0f/255f,1,0f,1f);
        shape.setColor(100.0f/255f,117.0f/255f,100.0f/255f,1f);
        shape.rect(h3.x,h3.y,h3.width,h3.height);

        //shape.setColor(Color.CHARTREUSE);
        shape.setColor(getColor());
        shape.rect(h2.x,h2.y,h2.width,h2.height);
        shape.end();
        batch.begin();
    }
    void healTo(float health)
    {
        currentHealth = health;
        h2.setWidth(maxLaenge * currentHealth / maxHealth);
    }
    void heal(float healamount)
    {
        currentHealth += healamount;
        //currentHealth = Math.min(currentHealth, maxHealth);
        currentHealth= MathUtils.clamp(currentHealth, 0,maxHealth);
        h2.setWidth(maxLaenge * currentHealth / maxHealth);
    }

    //void setVisible(boolean visible) {
       //setAlpha(visible?1:0);
    //}
    void bringtofront()
    {

    }
}
