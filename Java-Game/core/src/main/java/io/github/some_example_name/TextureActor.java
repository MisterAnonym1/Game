package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.Text;

import static java.lang.Float.NaN;


public class TextureActor extends Actor
{
    TextureRegion texture;
    Rectangle hitbox;
    float hitboxOffsetX=0, hitboxOffsetY=0;
    TextureActor(String filepath)
    {
        super();
        toFront();
        texture= new TextureRegion(new Texture(filepath));
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
    }
    TextureActor(TextureRegion texture)
    {
        super();
        toFront();
        this.texture= texture;
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
    }
    TextureActor(String filepath ,float xTexture,float yTexture,float width,float heigth)
    {
        super();
        texture= new TextureRegion(new Texture(filepath),xTexture,yTexture,width,heigth);
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }
    void initializeHitbox()
    {
        //hitbox=new Rectangle(getX(),getY(),texture.getRegionWidth(),texture.getRegionHeight());
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth(), getHeight());

    }
    public float getCenterX()
    {
        return getX()+getWidth()/2;
    }
    public float getCenterY()
    {
        return getY()+getHeight()/2;
    }
    public float getdistance(TextureActor other)
    {
        float disx = other.getCenterX() - getCenterX();
        float disy = other.getCenterY() - getCenterY();
        return Vector2.len(disx, disy);
       // return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));

    }
    /*public float getdistance(Sprite other)
    {
        float distancex = other.getX() - getX();
        float distancey = other.getY() - getY();
        return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));

    }*/
    public float getdistance(float x, float y)
    {
        float disx = x - getCenterX();
        float disy = y - getCenterY();
        return Vector2.len(disx, disy);
        //return (float) Math.sqrt(Math.pow(distancex, 2) + Math.pow(distancey, 2));

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hitbox.setPosition(getX() - hitboxOffsetX, getY() - hitboxOffsetY);
    }
    void centerAt(TextureActor other)
    {
        setPosition(other.getCenterX()-getWidth()/2, other.getCenterY()-getHeight()/2);
    }
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
    @Override
    public void setSize(float width, float height) {
        if(getWidth()==0||getHeight()==0||getWidth()==NaN||getHeight()==NaN)
        {
            super.setSize(width, height);
            hitbox.setSize(width,height);
            return;
        }
        //hitbox.setSize((float) (hitbox.getWidth()* factorw), (float) (hitbox.getHeight()*factorh));
        float factorw=width/getWidth();
        float factorh =height/getHeight();
        super.setSize(width, height);
        hitbox.setSize((float) (hitbox.getWidth()* factorw), (float) (hitbox.getHeight()*factorh));
        hitboxOffsetX*=factorw;
        hitboxOffsetY*= factorh;
    }
    void scale(float factor)
    {
        setSize(getWidth()*factor,getHeight()*factor);
    }
}
