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
    float alpha;
    ArrayList<TextureActor> array;
    TextureActor(String filepath)
    {
        super();;
        texture= new TextureRegion(new Texture(filepath));
        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());
        initializeHitbox();
    }
    TextureActor(TextureRegion texture)
    {
        super();
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
    public void draw(Batch batch, float fixDenCodeTheoDuKeck) {
        //Animation und so
        //batch.setColor(getColor().r,getColor().g,getColor().b,1);
        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }
    public void drawHitbox(ShapeRenderer shape)
    {
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.rect(hitbox.getX(),hitbox.getY(),hitbox.getWidth(),hitbox.getHeight());

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
    void centerAt(float x, float y)
    {
        setPosition(x-getWidth()/2, y-getHeight()/2);
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
