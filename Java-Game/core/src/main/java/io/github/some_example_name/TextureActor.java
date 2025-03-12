package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.Text;


public class TextureActor extends Actor
{
    TextureRegion texture;
    Rectangle hitbox;
    float hitboxOffsetX, hitboxOffsetY;
    TextureActor(String filepath)
    {
        super();
        toFront();
        texture= new TextureRegion(new Texture(filepath));
        initializeHitbox();
        setSize(texture.getRegionWidth(),texture.getRegionHeight());

    }
    TextureActor(TextureRegion texture)
    {
        super();
        toFront();
        this.texture= texture;
        initializeHitbox();
        setSize(texture.getRegionWidth(),texture.getRegionHeight());

    }
    TextureActor(String filepath ,float xTexture,float yTexture,float width,float heigth)
    {
        super();
        texture= new TextureRegion(new Texture(filepath),xTexture,yTexture,width,heigth);
        initializeHitbox();
        setSize(texture.getRegionWidth(),texture.getRegionHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.setColor(getColor().r,getColor().g,getColor().b,parentAlpha);
        batch.draw(texture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        System.out.println("oe eheuewu \n");
    }
    void initializeHitbox()
    {
        hitbox=new Rectangle(getX(),getY(),texture.getRegionWidth(),texture.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hitbox.setPosition(getX()-hitboxOffsetX,getY()-hitboxOffsetY);
    }
    public void destroy()
    {

        texture.getTexture().dispose();
        if(getStage()!=null)
        {
           clear();
        }
        remove();
    }
    public void setSize(float width, float height) {

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
