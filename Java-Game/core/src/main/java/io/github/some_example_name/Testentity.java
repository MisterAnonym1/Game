package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Testentity extends Entity {
    Vector2 direction;
    int spawnx=0;
    int spawny=0;
    float delay=0;
    Animation<TextureRegion> walkAnimation;
    boolean collides = false;
    Main logic;

    Position targetpos;
    Testentity(float x, float y,  Main log) {
        super(x, y, new TextureRegion(new Texture("slime_move.png"),1,1,80,72),log.Player);
        acceleration = 100;
        maxspeed = 100;
        setSize(200,200);
        hitboxOffsetX=10;
        hitboxOffsetY=10;
        spawnx = (int)x;
        spawny = (int)y;
        logic = log;
        scale(1.4f);
        walkAnimation= Animator.getAnimation("slime_move.png",7,7,22,28,0.5f);
        direction = new Vector2(0, 0);
        targetpos = new Position(spawnx , spawny );
        gotopoint(targetpos.x, targetpos.y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        animationstateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = walkAnimation.getKeyFrame(animationstateTime, true);

        batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    @Override
    void initializeHitbox() {
        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth()/4.1f, getHeight()/4.62f);
        hitboxOffsetX=10;
        hitboxOffsetY=10;
    }

    void setrandompoint(float centerx, float centery, float radius)
    {
        delay = MathUtils.round((float) (MathUtils.random() * 1.0 + 0.6f));
        boolean collides=false;
        while (true) {
            float angle = MathUtils.random(0,360);
            float length = MathUtils.random((float) (radius * 0.1), radius);
            float x = (float) (length * Math.cos(angle * Math.PI / 180));
            float y = (float) ( length * Math.sin((angle * Math.PI) / 180));
            Vector2 pos = new Vector2(x,y);
            collides = false;
            targetpos = new Position(Math.round(centerx + pos.x), Math.round(centery + pos.y));
            //new Rectangle(targetpos.x, targetpos.y, 20, 20).setAlpha(1);
            Vector2 vec = new Vector2(targetpos.x - getCenterX(), targetpos.y - getCenterY());
            Rectangle line2 = new Rectangle(getCenterX(), getCenterY() - getHeight() / 2, vec.len(), Math.max(getWidth(), getHeight()));
            //line2.setOrigin(getCenterX(), getCenterY());

            //line2.rotate(vec.getAngleDeg());

         /*for (MyTile tile : logic.loadedwalls) {
            if(line2.collidesWith(tile.hitbox))
            {
               collides = true;
               break;
            }
         }*/
            //line2.destroy();
            if(!collides) {
                return;
            }

        }



    }
    void gotopoint(float x, float y)
    {
        direction = new Vector2(x - getCenterX(), -getCenterY() + y);

    }
    boolean isatdestination() {
        if(collides || (Math.abs(Math.round(getCenterX()) - targetpos.x) < 10 && Math.abs(Math.round(getCenterY()) - targetpos.y) < 10))
        {
            collides = false;
            return true;
            //gotopoint(targetpos.x, targetpos.y);

        }
        return false;
    }
    void moveatrelativeAngle(float length, float angle)
    {
        super.moveatAngle(length, angle);

    }
    @Override
    public void act(float delta) {
        super.act(delta);
        if(isatdestination())
        {
            delay-=delta;
            if(delay <= 0) {
                while (isatdestination())
                {
                    setrandompoint(spawnx, spawny, 140f);
                }

            }
        }
        else {

            //checknewpos();
            gotopoint(targetpos.x, targetpos.y);
            ismoving = true;
            updatemovement(direction,delta);
        }
    }
}


 class Position {
    int x;
    int y;
    Position(int x, int y)
    {
        this.x=x;
        this.y=y;

    }
}
