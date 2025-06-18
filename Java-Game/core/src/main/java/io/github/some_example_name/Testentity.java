package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Testentity extends Gegner {
    Vector2 direction;
    TextureRegion sheet;
    Vector2 targetpos;
    Animation<TextureRegion> jumpAnimation;
    boolean abandonedTarget;

    Testentity(float x, float y, Main log) {
        super(x, y, log, new TextureRegion(new Texture("slime_move.png"), 1, 1, 80, 72));
        acceleration = 100;
        maxspeed = 100;
        setSize(200, 200);
        sethealth(100, true);
        spawnx = x;
        spawny = y;
        inview = true;
        scale(1f);
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;
        sheet = new TextureRegion(new Texture("HappySheep_All.png"));
        defaultAnimation = new Animation<>(0.2f, sheet.split(sheet.getRegionWidth() / 8, sheet.getRegionHeight() / 2)[0]);
        jumpAnimation = Animator.getAnimation("HappySheep_All.png", 8, 2, 9, 14, 0.1f);
        direction = new Vector2(0, 0);
        playAnimation(defaultAnimation);
        animationstateTime= (float)Math.random()*0.2f;
        moveBy(1, 1);
        setrandompoint(spawnx, spawny, 140f);
        addAction(Actions.sequence(Actions.delay(5), new Action() {
            @Override
            public boolean act(float delta) {
                setaggressive();
                return true;
            }
        }));
    }

    void reset() {
        super.reset();
        maxspeed = 100;
        setrandompoint(spawnx, spawny, 140f);
        animationstateTime= (float)Math.random()*0.2f;
    }

    @Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor());
        if (movement.angleDeg() > 90 && movement.angleDeg() < 270) {
            ismirrored = true;
        } else {
            ismirrored = false;
        }
        animationstateTime += delta; // Accumulate elapsed animation time
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationstateTime, true);
        batch.draw(currentFrame, getX() + (ismirrored ? getWidth() : 0), getY(), getOriginX(), getOriginY(), ismirrored ? -getWidth() : getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

    }


    @Override
    public void removeFromLevel() {
        Level.testentitys.remove(this);
    }

    @Override
    void initializeHitbox() {

        hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, getWidth() / 4.1f, getHeight() / 4.62f);
    }

    void setrandompoint(float centerx, float centery, float radius) {
        abandonedTarget = false;
        float angle;
        float length;
        for (int i = 0; i < 16; i++) {


            angle = MathUtils.random(0, 360);
            length = MathUtils.random(0, radius + getdistance(centerx, centery) / 1.1f - 20);
            float x = (float) (length * Math.cos(angle * Math.PI / 180));
            float y = (float) (length * Math.sin((angle * Math.PI) / 180));
            collides = false;
            targetpos = new Vector2(centerx + x, centery + y);
            Vector2 vec = getDistanceVector(targetpos.x, targetpos.y);
            if (vec.len() <= hitbox.getWidth() * 1.5f) {
                collides = true;
                continue;
            }

            float[] vertices = {hitbox.width / 2, (hitbox.height - hitbox.width) / 2, hitbox.width / 2, (hitbox.height + hitbox.width) / 2, vec.len(), 0 + hitbox.getWidth(), vec.len(), 0};
            lineofsight.setVertices(vertices);
            lineofsight.setOrigin(hitbox.getWidth() / 2.0f, hitbox.getHeight() / 2.0f);
            lineofsight.setPosition(hitbox.x, hitbox.y);
            lineofsight.setRotation(vec.angleDeg());

            for (MyTile tile : logic.loadedwalls) {
                if (Intersector.overlapConvexPolygons(lineofsight, tile.hitbox)) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                return;
            }

        }
        abandonedTarget = true;
        pathCountdown = 0.4f;

    }

    void gotopoint(float x, float y) {
        direction = new Vector2(x - getHitboxCenterX(), y - getHitboxCenterY());

    }

    boolean isatdestination() {
        if ((Math.abs(getHitboxCenterX() - targetpos.x) < hitbox.getWidth() + 3 && Math.abs(getHitboxCenterY() - targetpos.y) < hitbox.getWidth() + 3)) {
            return true;

        }
        return false;
    }

    void setaggressive() {
        status = EntityStatus.engaging;
        attackStatus = AttackStatus.exploding;
        maxspeed = 200;
        playAnimation(jumpAnimation);
        animationstateTime= (float)Math.random()*0.2f;
    }

    @Override
    void onDeath() {
        super.onDeath();
        if(attackStatus==AttackStatus.exploding) {
            PartikelSprite explosion= new PartikelSprite(getHitboxCenterX(), getHitboxCenterY(), Animator.getAnimation("Explosions.png",9,1,1,9,0.1f), true);
            explosion.scaleBy(0.7f);
            Level.particles.add(explosion);

        }}

    @Override
    public void act(float delta) {
        super.act(delta);

        if (status==EntityStatus.engaging)
        {engagePlayer(delta);}
        else{

            ismoving = false;
            if (collides && !abandonedTarget) {
               pathCountdown *= 0.3f;
               abandonedTarget = true;

            }
            if (isatdestination()) {
                abandonedTarget = true;
            }
            if (abandonedTarget)
            {

                pathCountdown -= delta;
                if (pathCountdown <= 0) {
                    pathCountdown = MathUtils.random() * 4.0f + 1.5f;
                    setrandompoint(spawnx, spawny, 140f);
                    collides = false;
                }
            }
            else {

                //checknewpos();
                gotopoint(targetpos.x, targetpos.y);
                ismoving = true;

            }
            updatemovement(direction, delta);
        }
    }

    @Override
    public void engagePlayer(float delta) {
        if(!inradiusof(player,900)){
            //playAnimation(defaultAnimation);
            return;
        }
        pathCountdown-=delta;
        if(playerinview() ) {
            pathCountdown = 0;
            goDirectlyToPlayer(delta,40);
            if(inradiusof(player,50))
            {
                //explode();
                applyknockbackOn(player, 230);
                onDeath();
            }

        }
        else
        {
            if(pathCountdown <= 0)
            {
                locateplayer(40, 1200);
                pathCountdown = 1;
            }

            followPath(delta);


        }
    }


}

