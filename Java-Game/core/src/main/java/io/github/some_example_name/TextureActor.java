package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import static java.lang.Float.NaN;


public class TextureActor extends Actor
{
    TextureRegion texture;
    Rectangle hitbox;
    float hitboxOffsetX=0, hitboxOffsetY=0;
    boolean collisionOn=true;
    TextureActor(String filepath)
    {
        super();
        texture= new TextureRegion(new Texture(filepath));
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
        //setOrigin(getCenterX(), getCenterY());

    }
    TextureActor(TextureRegion texture)
    {
        super();
        this.texture= texture;
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
        initializeOtherThings();
        //setOrigin(getCenterX(), getCenterY());

    }
    TextureActor(String filepath ,float xTexture,float yTexture,float width,float heigth)
    {
        super();
        texture= new TextureRegion(new Texture(filepath),xTexture,yTexture,width,heigth);
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
        initializeOtherThings();
        //setOrigin(hitbox.getX()+hitbox.getWidth(), hitbox.getY()+hitbox.getHeight());

    }

    @Override
    public void draw(Batch batch, float delta) {
        //Animation und so
        batch.setColor(getColor().r,getColor().g,getColor().b,getColor().a);
        //super.draw(batch,1);
        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }
    public void drawHitbox(ShapeRenderer shape)
    {
        super.drawDebug(shape);
        shape.rect(hitbox.getX(),hitbox.getY(),hitbox.getWidth(),hitbox.getHeight());

        shape.circle(getHitboxCenterX(),getHitboxCenterY(),5);//Hitboxcenter

        shape.setColor(0.5f,1,0.5f,1);//green= coordinates
        shape.circle(getX(),getY(),4);

        shape.setColor(0f, 0.2f, 1f, 1  );//blue=Hitboxcenter

        //shape.setColor(0.5f,0.5f,1f,1);//blue=origin
        //shape.circle(getX()+getOriginX(),getY()+getOriginY(),5);

    }
    void initializeHitbox()
    {
        //hitbox=new Rectangle(getX(),getY(),texture.getRegionWidth(),texture.getRegionHeight());
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth(), getHeight());
    }
    void initializeOtherThings()
    {

    }
    public float getCenterX()
    {
        return getX()+getWidth()/2;
    }
    public float getCenterY()
    {
        return getY()+getHeight()/2;
    }

    public float getHitboxCenterX()
    {
        return hitbox.x+hitbox.width/2;
    }
    public float getHitboxCenterY()
    {
        return hitbox.y+hitbox.height/2;
    }
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x+hitboxOffsetX+(-getWidth()+hitbox.getWidth())/2, y+hitboxOffsetY+(-getHeight()+hitbox.getHeight())/2);
    }

    /*public float getdistance(TextureActor other)
    {
        float disx = other.getHitboxCenterX() - getHitboxCenterX();
        float disy = other.getHitboxCenterY() - getHitboxCenterY();
        return Vector2.len(disx, disy);
       // return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));

    }

    public float getdistance(float x, float y)
    {
        float disx = x - getCenterX();
        float disy = y - getCenterY();
        return Vector2.len(disx, disy);
        //return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));

    }*/
    public float getdistance(TextureActor other) {
        return getDistanceVector(other).len();
    }

    public float getdistance(float x, float y) {
        return getDistanceVector(x, y).len();
    }

    public Vector2 getDistanceVector(float x, float y) {
        return new Vector2(x - getHitboxCenterX(), y - getHitboxCenterY());
    }

    public Vector2 getDistanceVector(TextureActor other) {
        return getDistanceVector(other.getHitboxCenterX(), other.getHitboxCenterY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        hitbox.setPosition(getCenterX()-hitbox.getWidth()/2- hitboxOffsetX, getCenterY()-hitbox.getHeight()/2 - hitboxOffsetY);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
    }

    void centerAt(TextureActor other)
    {
        setPosition(other.getCenterX()-getWidth()/2, other.getCenterY()-getHeight()/2);
    }
    /*void centerAt(float x, float y)
    {
        setPosition(hitbox.x-hitbox.getWidth()/2, hitbox.y-hitbox.getHeight()/2);
    }*/


    public void destroy()
    {

        texture.getTexture().dispose();
        if(getStage()!=null)
        {
           clear();
            //addAction(Actions.removeActor())
        }
        remove();
    }
    public void removeFromLevel()
    {
        //throw  new IllegalArgumentException("Die Methode der super klasse muss überschrieben werden);
    }
    @Override
    public void setSize(float width, float height) {
        if(getWidth()==0||getHeight()==0||getWidth()==NaN||getHeight()==NaN)
        {
            hitbox.setSize(width,height);
            super.setSize(width, height);
            return;
        }
        //hitbox.setSize((float) (hitbox.getWidth()* factorw), (float) (hitbox.getHeight()*factorh));
        float factorw=width/getWidth();
        float factorh =height/getHeight();
        hitbox.setSize((float) (hitbox.getWidth()* factorw), (float) (hitbox.getHeight()*factorh));
        hitboxOffsetX*=factorw;
        hitboxOffsetY*= factorh;
        super.setSize(width, height);
        //setOrigin(getCenterX(), getCenterY());
    }
    void scale(float factor)
    {
        setSize(getWidth()*factor,getHeight()*factor);
    }
}
