package io.github.some_example_name;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static java.util.Collections.rotate;

class Entity extends Actor
{
    int maxhealth, curhealth;
    TextureRegion texture;
    double maxspeed, curspeed, acceleration;
    boolean collisionOn, ismoving;
    Rectangle hitbox;//hitbox kann in den Unterklassen unterschiedliche Formen haben
    Vector2 movement;
    FitViewport viewport;
    Arrow ar;
    float hitboxOffsetX, weight;
    float hitboxOffsetY;
    EntityStatus status;
    enum EntityStatus { inactiv, idle, engaging }
    Entity(float x, float y, String filepath, FitViewport viewport)
    {
        super();
        setPosition(x,y);
        texture=new TextureRegion(new Texture(filepath));
        collisionOn = true;
        weight = 1;
        this.viewport=viewport;
        curspeed = 0;
        curhealth = 1;
        ismoving = false;
        movement = new Vector2(1, 0);
        ar = new Arrow(x, y, 1, 0);
        status = EntityStatus.inactiv;
        hitboxOffsetX = getWidth();
        hitboxOffsetY = getHeight();
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
    void updatemovement(Vector2 direction)
    {
        if(ismoving) {
            if(Math.round(direction.angleDeg()) == Math.round(movement.angleDeg()))
            {
                curspeed += acceleration;
                //kontrolliert, dass die Geschwindigkeit nie die maximale Geschwindigkeit überschreitet
                if(curspeed > maxspeed) {
                    curspeed = maxspeed;
                }
            }
            else {
                reducespeed();
            }
            movement = direction;
            //definiert, dass der Berechnete Vektor zur Bewegungsrichtung wird
        }
        else
        {
            reducespeed();
        }
        moveatdirection();
    }
    void moveatdirection()
    {
        moveatrelativeAngle(curspeed, movement.angleDeg());
        ar.rotateTo(-movement.angleDeg());
        //print(acceleration+"\n");
    }

    void reducespeed()//methode zum überschreiben
    {
        curspeed = 0;
    }
    public void movetoright(float right)
    {
        float x = (float) (right * Math.cos((getRotation() + 90) * Math.PI / 180));
        float y = (float) (- right * Math.sin((getRotation() + 90) * Math.PI / 180));
        moveBy(x, y);


    }
   public float getCenterX()
   {
       return getX()+getWidth()/2;
   }
    public float getCenterY()
    {
        return getY()+getHeight()/2;
    }
    public void moveatrelativeAngle(double length, float angle)
    {
        float x = (float) (length * Math.cos((/*getAngle() +*/ angle) * Math.PI / 180));
        float y = (float) (- length * Math.sin((/*getAngle() +*/ angle) * Math.PI / 180));
        moveBy(x, y);
        hitbox.setPosition(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY);
        ar.translateX(x);
        ar.translateY(y);
        //hitbox.move(x, y);
        //mit dieser Methode kann man einen genauen Winkel angeben, in welchen sich die Entity bewegen soll

    }
    public boolean inradiusof(Entity other, float radius)
    {

        if(getdistance(other) <= radius)
        {
            return true;
        }
        return false;
    }
    public float getdistance(Entity other)
    {
        float distancex = other.getCenterX() - getCenterX();
        float distancey = other.getCenterY() - getCenterY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));
        //Überprüft ob etwas in einem gewissen Radius von dieser Entity ist
    }
    public float getdistance(float x, float y)
    {
        float distancex = x - getCenterX();
        float distancey = y - getCenterY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));
        //Überprüft, ob etwas in einem gewissen Radius von diesen Koordinaten ist
    }

    boolean outofsight()
    {
        return false;
    }

    boolean inloadedworld()
    {

        //checkt ob dieses Entity vorhanden ist in
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
class Arrow extends Sprite {



    MeshSpawnShapeValue.Triangle tri;

    float shaperotation=0;
     float dborderwidth = 10;
    Arrow(float x1, float y1, float length, float angle)
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
       shape.setColor(0.1F, 0.1F, 0.8F, 0.1F);
        // shape.identity();
        //  //shape.translate(getOriginX(),getOriginY() ,1);
        //  //shape.rotate(0,0,1,getRotation()-shaperotation);
        //  //shaperotation=getRotation();

        shape.rect(getX(),getY(),dborderwidth*10,dborderwidth);
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
