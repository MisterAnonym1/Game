package io.github.some_example_name;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;




class Entity extends Actor
{
    int maxhealth, curhealth;
    float maxspeed, acceleration;
    boolean collisionOn, ismoving, isattacking;
    Rectangle hitbox;//hitbox kann in den Unterklassen unterschiedliche Formen haben
    Vector2 movement;
    Vector2 additionalForce;
    TextureRegion texture;
    float hitboxOffsetX=0, weight, hitboxOffsetY=0;
    static float hitboxalpha = 0.5f;
    float animationstateTime=10f;
    EntityStatus status;
    //Ellipse shadow;
    FitViewport viewport;
    Rectangle worldbounds;

    public enum EntityStatus { inactiv, idle, engaging }
    Entity(float x, float y, String filepath, FitViewport viewport)
    {
        super();

        texture=new TextureRegion(new Texture(filepath));
        setWidth(texture.getTexture().getWidth());
        setHeight(texture.getTexture().getHeight());

        collisionOn = true;
        additionalForce = new Vector2(0, 0);
        movement = new Vector2(0, 0);
        curhealth = 100;
        weight = 1;
        this.viewport=viewport;
        status = EntityStatus.inactiv;
        initializeHitbox();
        setPosition(x,y);
    }

    public void draw (Batch batch, float parentAlpha)
    {
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        //batch.setColor(getColor().r,getColor().g,getColor().b, hitboxalpha);
        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());


    }
    public void drawHitbox(ShapeRenderer shape)
    {
        shape.setColor(0, 0, 0.5f, hitboxalpha  );
        shape.rect(hitbox.getX(),hitbox.getY(),hitbox.getWidth(),hitbox.getHeight());

        shape.setColor(0.1f, 0.1f, 0.1f, 0.4f  );
        shape.ellipse( getCenterX()-getWidth() *0.6f, getY()-getWidth()/4 , getWidth() *1.2f, getWidth() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(Math.min(delta,1/30f));
    }

    void sethealth(int health, boolean ignoremax)
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
    void scale(float factor)
    {

        setSize(getWidth()*factor,getHeight()*factor);


    }

    @Override
    public void setSize(float width, float height) {
        float factorw=width/getWidth();
        float factorh =height/getHeight();
        super.setSize(width, height);
        hitbox.setSize((float) (hitbox.getWidth()* factorw), (float) (hitbox.getHeight()*factorh));
    }

    boolean damageby(int damage)
    {

        curhealth -= damage;
        if(curhealth > maxhealth) {
            curhealth = maxhealth;
            return false;
        }
        if(curhealth <= 0) {
            destroy();
            return true;
        }
        return false;

    }
    void initializeHitbox()
    {
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth(), getHeight());

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
        setX(Math.clamp(getX(),minX,maxX));
        setY(Math.clamp(getY(),minY,maxY));


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

        reduceSpeed(1,1);
        direction.setLength(acceleration);
        if(ismoving) {
            movement = movement.add(direction);
        }
            movement.setLength( (float) Math.min(movement.len(), maxspeed));
        if(ismoving) {

            movement = direction;

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


    float easeOutCubic(float x)
    {
        return (float) (1 - Math.pow(1 - x, 3));
    }


    void moveatdirection(float delta)
    {
        moveBy((movement.x+additionalForce.x)*delta, (movement.y+additionalForce.y)*delta);
    }
    void moveReverse(Vector2 move, int speed)
    {

        moveatAngle(speed, move.angleDeg());

    }

    void reduceSpeed(float haftreibungsKoeffizient,float delta)//Haftreibungs koeffinzient zwischen 0 und 1
    {

        movement.setLength( movement.x * (1 - haftreibungsKoeffizient*delta));
    }

    public void moveatAngle(double length, float angle)
    {
        float x = (float) (length * Math.cos(angle * Math.PI / 180));
        float y = (float) ( length * Math.sin((angle * Math.PI) / 180));
        moveBy(x, y);
        //mit dieser Methode kann man einen genauen Winkel angeben, in welchen sich die Entity bewegen soll

    }

    void centerAt(float x, float y)
    {
        setPosition(x-hitbox.getWidth()/2+hitboxOffsetX,y-hitbox.getHeight()/2+hitboxOffsetY);
    }


    @Override
    public void setX(float x) {
        super.setX(x);
        hitbox.setPosition(getX() - hitboxOffsetX, getY() - hitboxOffsetY);}

    @Override
    public void setY(float y) {
        super.setY(y);
        hitbox.setPosition(getX() - hitboxOffsetX, getY() - hitboxOffsetY);}

    @Override
    public void moveBy(float x, float y) {
        super.moveBy(x, y);
        hitbox.setPosition(getX() - hitboxOffsetX, getY() - hitboxOffsetY);}

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        hitbox.setPosition(getX() - hitboxOffsetX, getY() - hitboxOffsetY);}



    public boolean inradiusof(Entity other, float radius)
    {

        if(getdistance(other) <= radius)
        {
            return true;
        }
        return false;
    }

    public float getCenterX()
    {
        return getX()+getWidth()/2;
    }
    public float getCenterY()
    {
        return getY()+getHeight()/2;
    }

    public float getdistance(Entity other)
    {
        float distancex = other.getCenterX() - getCenterX();
        float distancey = other.getCenterY() - getCenterY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));
        //Überprüft ob etwas in einem gewissen Radius von diesem Entity ist
    }
    public float getdistance(float x, float y)
    {
        float distancex = x - getCenterX();
        float distancey = y - getCenterY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));
        //Überprüft ob etwas in einem gewissen Radius von diesen Koordinaten ist
    }

    boolean outofsight()
    {
        return false;
    }
    boolean inloadedworld()
    {
        //checked ob dieses Entity vorhanden ist in
        // einem quadratischen Bereich etwas größer als der Screen

        float wx = viewport.getScreenX();
        float wy = viewport.getScreenY();
        if(getCenterX() > wx - 200 && getCenterX() < wx + 1000) {
            if(getCenterY() > wy - 200 && getCenterY() < wy + 1000)
            {
                return true;
            }
        }
        return false;
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
    void destroy()
    {

        texture.getTexture().dispose();
        if ( getStage()!=null){
            clear();
            //addAction(Actions.removeActor())
        }
        remove();


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



    MeshSpawnShapeValue.Triangle tri;

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
         //shape.identity();
         //shape.translate(getOriginX(),getOriginY() ,0);
        //shape.translate(1,1 ,0);
         //shape.rotate(0,0,1,-getRotation()-shaperotation);
         //shaperotation=getRotation();

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
class HealthBar extends Sprite {
    Rectangle h1;
    Rectangle h2;
    Rectangle h3;
    float maxHealth;
    float maxLaenge;
    float currentHealth;
    HealthBar(int xPos, int yPos, float maxhealth, float size) {
        h1 = new Rectangle(xPos, yPos, 300, 50f);
        h3 = new Rectangle(xPos + 7f, yPos + 6f, 286f, 38f);
        h2 = new Rectangle(xPos + 7f, yPos + 6f, 286f, 38f);
        /*h1.setStatic(true);
        h2.setStatic(true);
        h3.setStatic(true);
        h1.setFillColor(new Color(80, 74, 74));

        h2.setFillColor(Color.chartreuse);
        h3.setFillColor(Color.chartreuse, 0.2);*/
        maxLaenge = h2.getWidth();
        this.maxHealth = maxhealth;
        currentHealth = maxHealth;
    }

    void takeDamage(float damage) {
        currentHealth -= damage;
        h2.setWidth((maxLaenge * currentHealth / maxHealth)<0?0:maxLaenge * currentHealth / maxHealth);

    }
    void draw(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(80/256,74/256,74/256,100/256);
        shape.rect(h1.x,h1.y,h1.width,h1.height);

        shape.setColor(Color.CHARTREUSE);
        shape.rect(h2.x,h2.y,h2.width,h2.height);

        shape.rect(h3.x,h3.y,h3.width,h3.height);
        shape.end();
    }
    void healTo(float health)
    {
        currentHealth = health;
        h2.setWidth(maxLaenge * currentHealth / maxHealth);
    }
    void heal(float heal)
    {
        currentHealth += heal;
        currentHealth = Math.min(currentHealth, maxHealth);
        h2.setWidth(maxLaenge * currentHealth / maxHealth);
    }

    void setVisible(boolean visible) {
       setAlpha(visible?1:0);
    }
    void bringtofront()
    {

    }
}
