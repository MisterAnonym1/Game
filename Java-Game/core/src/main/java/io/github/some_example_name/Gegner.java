package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;


abstract class Gegner extends Entity
{

    Level curlevel;
    float pathCountdown= 0;
    float attackdelay = 0;
    float attackdelay2 = 0;
    Player player;
    Main logic;
    float textureYoffset;
    ArrayList<MyTile> queue = new ArrayList<>();
    ArrayList<MyTile> goalfields = new ArrayList<>();
    ArrayList<MyTile> visitedfields = new ArrayList<>();
    MyTile targettile;
    Polygon lineofsight;
    AttackStatus attackStatus= AttackStatus.inactive;
    Vector2 directiontoTile = new Vector2(0,0);
    boolean inview = false;
    Vector2 savedVector= new Vector2(0,0);
    Animation<TextureRegion> explosionAnimation;
    boolean exploded=false;

    public enum AttackStatus {inactive, dash, strike,exploding,spin,projectile_storm, shockwave, inair, repositioning }

    Gegner(float x, float y, Main logic, String filepath) {
        this(x, y,  logic,new TextureRegion(new Texture(filepath)));

    }
    Gegner(float x, float y, Main logic, TextureRegion texture)
    {
        super(x, y, texture, logic.Player);
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.player = logic.Player;
    }



    @Override
    protected void scaleChanged() {
        super.scaleChanged();
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight(),100,0+ hitbox.getHeight(),100,0};
        lineofsight.setVertices(vertices);
        lineofsight.setOrigin(hitbox.getWidth()/2.0f, hitbox.getHeight()/2.0f);
    }

void reset()
    {
        super.reset();
        pathCountdown = 0;
        attackdelay = 0;
        attackdelay2 = 0;
        textureYoffset=0;
        goalfields.clear();
        visitedfields.clear();
        queue.clear();
        targettile = null;
        attackStatus = AttackStatus.inactive;
        exploded=false;
    }


    @Override
    void initializeOtherThings() {
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight()/2,100,0+ hitbox.getHeight()/2,100,0};
        lineofsight = new Polygon(vertices );
        lineofsight.setOrigin(hitbox.getWidth(), hitbox.getHeight());
        lineofsight.setPosition(hitbox.x,hitbox.y);
    }

    @Override
    protected void positionChanged() {
        hitbox.setPosition(getCenterX()-hitbox.getWidth()/2- hitboxOffsetX, getCenterY()-hitbox.getHeight()/2 - hitboxOffsetY);
    }

    @Override
    boolean damageby(float damage) {
        Level.indicators.addActor(new PopUpText("-" + (int) damage, (player.getCenterX()<getCenterX()?hitbox.x:hitbox.x+hitbox.width), getHitboxCenterY()));
        return super.damageby(damage);
    }

    @Override
    public void drawHitbox(ShapeRenderer shape) {
        super.drawHitbox(shape);
        if(inview)
        {
            shape.setColor(0.8f,0.2f,1,1);
        }
        else
        {
            if(targettile!=null) {
                shape.setColor(Color.PURPLE);
                shape.rect(targettile.getCenterX()-16+hitbox.height*getScaleY()/2, targettile.getCenterY()-16+hitbox.height*getScaleY()/2, 32-hitbox.height*getScaleY(), 32-hitbox.height*getScaleY());
            }
            if(goalfields.size() > 0)
            {
            shape.setColor(Color.BLUE);
                shape.rect(goalfields.get(0).getCenterX()-16+hitbox.height*getScaleY()/2, goalfields.get(0).getCenterY()-16+hitbox.height*getScaleY()/2, 32-hitbox.height*getScaleY(), 32-hitbox.height*getScaleY());
            }
            shape.setColor(Color.BROWN);
        }

        shape.polygon(lineofsight.getTransformedVertices());
        shape.setColor(Color.BLUE);
    }

    boolean playerinview()
    {   Vector2 vec = getDistanceVector(player);
        //movement=vec;
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight(),vec.len(),0+ hitbox.getHeight(),vec.len(),0};
        lineofsight.setVertices(vertices);
        lineofsight.setOrigin(hitbox.getWidth()/2.0f, hitbox.getHeight()/2.0f);
        lineofsight.setPosition(hitbox.x,hitbox.y);
        lineofsight.setRotation(vec.angleDeg());

        for (MyTile tile : logic.loadedwalls) {
            if(Intersector.overlapConvexPolygons(lineofsight, tile.hitbox))
            {
                inview=false;
                return false;
            }
        }
        inview=true;
        return true;
    }

    void goDirectlyToPlayer(float delta,float minDistance)
    {
            ismoving = true;
            movement=getDistanceVector(player);
            if(movement.len()>=minDistance){
            updatemovement(movement,delta);}
    }

    void followPath(float delta)
    {
        if(goalfields.size() == 0)
        {
            pathCountdown=Math.min(pathCountdown,.3f);
            return;
        }
        directiontoTile= new Vector2(goalfields.get(0).getCenterX() - getCenterX(), goalfields.get(0).getCenterY() - getCenterY());
        if(directiontoTile.len() <= targettile.getWidth()/2-hitbox.height*getScaleY()/2 || directiontoTile.len() >= 200){

            goalfields.remove(0);

            if(goalfields.size() == 0) //keine Felder mehr zum Folgen
            {
                pathCountdown=0;// neuen Pfad berechnen
                return;
            }
        }
        ismoving = true;
        updatemovement(directiontoTile, delta);
    }
    @Override
    public void drawShadow(ShapeRenderer shape) {
        shape.ellipse( hitbox.getX()+hitbox.getWidth()/2-hitbox.getWidth()*shadowscale/2, hitbox.getY()-hitbox.getWidth()/4*shadowscale , hitbox.getWidth()*shadowscale, hitbox.getWidth()*shadowscale / 2);
    }

    @Override
    void onDeath() {
        super.onDeath();
        spawnCoins();
    }
    void spawnCoins()
    {
        int combinedvalue=Math.max(1,(int)(maxhealth/10f));
        int objectcount=Math.min((int)Math.ceil(combinedvalue/5f),5);
        int value=(int) Math.floor((float)combinedvalue/(float)objectcount);
        for(int i=1; i<objectcount; i++)
        {
            combinedvalue-=value;
            Level.objects.add(new Coin(getHitboxCenterX()+MathUtils.random(-hitbox.width/2,hitbox.width/2), getHitboxCenterY()+MathUtils.random(-hitbox.height/2,hitbox.height/2), value));
        }

        Level.objects.add(new Coin(getHitboxCenterX()+MathUtils.random(-hitbox.width/2,hitbox.width/2), getHitboxCenterY()+MathUtils.random(-hitbox.height/2,hitbox.height/2), combinedvalue));


    }

    void setPath(MyTile start, MyTile target)
    {

        visitedfields.clear();
        queue.clear();
        if(start==null|| target==null|| start.obstructed|| target.obstructed) {
            goalfields.clear();
            deactivate();
            return;
        }

        targettile=target;
        MyTile currenttile = start;
        currenttile.visited = true;
        currenttile.previoustile = null;
        visitedfields.add(currenttile);
        queue.add(currenttile);
        while (queue.size() > 0 && inradiusof(currenttile.getCenterX(),currenttile.getCenterY(), 1270))
        {
            currenttile = queue.get(0);
            queue.remove(0);
            if(currenttile == target) {
                break;
            }
            queue.addAll(getneighbours(currenttile));

        }

        if(currenttile != target) {
            deactivate();
        }
        else {

            goalfields.clear();
            while (currenttile.previoustile != null)//Felder zum Start zurückverfolgen
            {

                goalfields.add(0, currenttile);
                currenttile = currenttile.previoustile;

            }
        }
        for (MyTile tile : visitedfields)
        {
            tile.visited = false;
            tile.previoustile = null;
        }
    }

    public void engagePlayer(float delta){
        attackdelay+=delta;

        if (attackdelay>=2)
        {
            //line of sight
            attackdelay=0;
        }
    }

    @Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor().r,getColor().g,getColor().b,getColor().a);
        animationstateTime += delta;
        ismirrored= !(movement.angleDeg() > 90) || !(movement.angleDeg() < 270);
        if (currentAnimation==null){batch.draw(texture,getX()+ (ismirrored?getWidth():0),getY()+textureYoffset,getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        }
        else {
            TextureRegion currentFrame = currentAnimation.getKeyFrame(animationstateTime, true);
            batch.draw(currentFrame,getX()+ (ismirrored?getWidth():0),getY()+textureYoffset,getOriginX(),getOriginY(),ismirrored? -getWidth():getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        }
    }
    void damagePlayer(float damage) {
            player.damageby(damage);
    }

    @Override
    public void removeFromLevel() {
        Level.gegnerliste.remove(this);
    }

    void locateplayer(float mindistance, float maxdistance)
    {


        movement = new Vector2(-getCenterX() + player.getCenterX(), -getCenterY() + player.getCenterY());
        if(movement.len() >= mindistance && movement.len() <= maxdistance) {
            setPath(curlevel.getnotwallTile( getCenterX(),getCenterY() ), curlevel.getnotwallTile(player.getCenterX(), player.getCenterY()));

          }


    }


    void onPlayertouch()
    {

    }


    ArrayList<MyTile> getneighbours(MyTile feld)
    {
        ArrayList<MyTile> neighbors = new ArrayList<>();

        if(feld.southNeighbour!=null && !feld.southNeighbour.obstructed && !feld.southNeighbour.visited) {
            neighbors.add(feld.southNeighbour);
            feld.southNeighbour.visited = true;
            visitedfields.add(feld.southNeighbour);
            feld.southNeighbour.previoustile = feld;
        }
        if(feld.eastNeighbour!=null && !feld.eastNeighbour.obstructed && !feld.eastNeighbour.visited) {
            neighbors.add(feld.eastNeighbour);
            feld.eastNeighbour.visited = true;
            visitedfields.add(feld.eastNeighbour);
            feld.eastNeighbour.previoustile = feld;
        }
        if(feld.northNeighbour!=null && !feld.northNeighbour.obstructed && !feld.northNeighbour.visited) {
            neighbors.add(feld.northNeighbour);
            feld.northNeighbour.visited = true;
            visitedfields.add(feld.northNeighbour);
            feld.northNeighbour.previoustile = feld;
        }
        if(feld.westNeighbour!=null && !feld.westNeighbour.obstructed && !feld.westNeighbour.visited) {
            neighbors.add(feld.westNeighbour);
            feld.westNeighbour.visited = true;
            visitedfields.add(feld.westNeighbour);
            feld.westNeighbour.previoustile = feld;
        }
        return neighbors;

    }
    public int getSignature()
    {
        return 0;
    }

    public void simpleattack (){
        if (getdistance(player)<= 20) {
            damagePlayer(30);
        }
    }
    public void bombattack(){
        if (getdistance(player)<= 20) {
            explosionAnimation = Animator.getAnimation("Se_Player_ja.jpg",3,2,1,5,0.2f);
            damagePlayer(50);
            this.destroy();
        }
    }
    public void fireballattack(){

            Vector2 vec= new Vector2(player.getCenterX()-getCenterX(), player.getCenterY()-getCenterY());
            vec.setLength(this.getHeight()/2);
            Level.projectiles.add(new FireBall(getCenterX()+vec.x,getCenterY()+vec.y,vec,this));

    }
    public void dashattack () {
        final float savedspeed=speed;
        speed=320;
        attackStatus=AttackStatus.dash;
        savedVector=getDistanceVector(player);
        addAction(Actions.sequence(
                Actions.delay(1.5f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        attackStatus=AttackStatus.inactive;
                        speed=savedspeed;
                        return true;
                    }
                }
        ));
    }

    void fireballringattack(float angle) //
    {
        fireballringattack(angle,0);
    }
    void fireballringattack(float angle,float angleoffset) //
    {
        Vector2 vec= new Vector2(hitbox.width/3f,0);
        vec.rotateDeg(angleoffset);
        for(float i=0; i<=360-angle;i+=angle)
        {
            Level.projectiles.add( new FireBall(getHitboxCenterX()+vec.x,getHitboxCenterY()+vec.y,new Vector2(vec.x,vec.y),this));
            vec.rotateDeg(angle);
        }
    }
    void fireStormattack()
    {
        attackStatus=AttackStatus.projectile_storm;
    }

    void explodeAttack(float damage) {

        if(attackStatus==AttackStatus.exploding) {
            PartikelSprite explosion= new PartikelSprite(getHitboxCenterX(), getHitboxCenterY(), Animator.getAnimation("Explosions.png",9,1,1,9,0.1f), true);
            explosion.scaleBy(0.7f);
            Level.particles.add(explosion);
            SoundManager.play("medium-explosion", 1, 1.6f+MathUtils.random(0,0.4f));
        }
        applyknockbackOn(player, 230);
        damagePlayer(damage);
        exploded=true;
        onDeath();
    }

    void shockwaveAttack(float waveduration, float jumpheight) {
        final AttackStatus currentStatus = attackStatus;// Aktuellen Status speichern
        attackdelay=0;
        attackStatus = AttackStatus.inair;
        invincible=true;
        savedVector= new Vector2(player.getHitboxCenterX() - getHitboxCenterX(),  player.hitbox.y-hitbox.y);
        final float maxspeed = speed;
       speed=savedVector.len()/jumpheight*250f/2-5;
        float startValue =1f; // Startwert für shadowscale
        float endValue = (float) Math.exp( -jumpheight/450f); // Endwert für shadowscale, abhängig von jumpheight
        float startValue2 =endValue;
        float endValue2 = 1f;
        addAction(Actions.sequence(
            Actions.parallel(

                new Action()
                {
                    float elapsed = 0;
                    float duration = jumpheight/250; // Dauer in Sekunden
                    @Override
                    public boolean act(float delta) {
                        elapsed += delta;
                        float progress = Math.min(elapsed / duration, 1f);
                        float interpolated = Interpolation.fastSlow.apply(progress);
                        // Benutze 'interpolated' für deinen zeitlichen Verlauf, z.B.:
                        textureYoffset=(jumpheight)*interpolated;
                        shadowscale = startValue + (endValue - startValue) * interpolated;
                        return elapsed >= duration;
                    }
                }),

            Actions.parallel(

                //Actions.moveBy(0, -jumpheight, jumpheight/250, Interpolation.linear),
                new Action()
                {

                    float elapsed = 0;
                    final float duration = jumpheight/250; // Dauer in Sekunden
                    @Override
                    public boolean act(float delta) {
                        elapsed += delta;
                        float progress = Math.min(elapsed / duration, 1f);
                        float interpolated = Interpolation.slowFast.apply(progress);
                        // Benutze 'interpolated' für deinen zeitlichen Verlauf, z.B.:
                        textureYoffset=jumpheight+(0-jumpheight)*interpolated;
                        shadowscale = startValue2 + (endValue2 - startValue2) * interpolated;
                        return elapsed >= duration;
                    }
                }
            ),
            new Action() {
                @Override
                public boolean act(float delta) {
                    invincible=true;
                   speed=maxspeed;
                    Shockwave wave=new Shockwave(getHitboxCenterX(), hitbox.y);
                    wave.scaleBy(0.8f);
                    Level.objects.add(wave);
                    attackStatus = AttackStatus.shockwave;
                    shadowscale=1f;
                    return true;}},

            Actions.delay(waveduration),

            new Action() {
                @Override
                public boolean act(float delta) {
                    attackStatus = AttackStatus.inactive;
                    invincible=false;
                    logic.resetCameraOffset();
                    if(currentStatus==AttackStatus.projectile_storm||currentStatus==AttackStatus.inair||currentStatus==AttackStatus.repositioning){
                    //attackdelay2=0;
                        attackStatus = currentStatus;
                    }
                    return true;}}

        ));


    }

    void shockwavejump(float waveduration, float jumpheight,float centerx, float centery) {
        final AttackStatus currentStatus = attackStatus;// Aktuellen Status speichern
        attackdelay=0;
        attackStatus = AttackStatus.inair;
        invincible=true;
        savedVector= new Vector2(centerx - getHitboxCenterX(),  centery-hitbox.y);
        final float maxspeed = speed;
        speed=savedVector.len()/jumpheight*250f/2-5;
        float startValue =1f; // Startwert für shadowscale
        float endValue = (float) Math.exp( -jumpheight/450f); // Endwert für shadowscale, abhängig von jumpheight
        float startValue2 =endValue;
        float endValue2 = 1f;
        addAction(Actions.sequence(
                Actions.parallel(

                        new Action()
                        {
                            float elapsed = 0;
                            float duration = jumpheight/250; // Dauer in Sekunden
                            @Override
                            public boolean act(float delta) {
                                elapsed += delta;
                                float progress = Math.min(elapsed / duration, 1f);
                                float interpolated = Interpolation.fastSlow.apply(progress);
                                // Benutze 'interpolated' für deinen zeitlichen Verlauf, z.B.:
                                textureYoffset=(jumpheight)*interpolated;
                                shadowscale = startValue + (endValue - startValue) * interpolated;
                                return elapsed >= duration;
                            }
                        }),

                Actions.parallel(

                        //Actions.moveBy(0, -jumpheight, jumpheight/250, Interpolation.linear),
                        new Action()
                        {

                            float elapsed = 0;
                            final float duration = jumpheight/250; // Dauer in Sekunden
                            @Override
                            public boolean act(float delta) {
                                elapsed += delta;
                                float progress = Math.min(elapsed / duration, 1f);
                                float interpolated = Interpolation.slowFast.apply(progress);
                                // Benutze 'interpolated' für deinen zeitlichen Verlauf, z.B.:
                                textureYoffset=jumpheight+(0-jumpheight)*interpolated;
                                shadowscale = startValue2 + (endValue2 - startValue2) * interpolated;
                                return elapsed >= duration;
                            }
                        }
                ),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        speed=maxspeed;
                        Shockwave wave=new Shockwave(getHitboxCenterX(), hitbox.y);
                        wave.scaleBy(0.8f);
                        Level.objects.add(wave);
                        attackStatus = AttackStatus.shockwave;
                        shadowscale=1f;
                        return true;}},

                Actions.delay(waveduration),

                new Action() {
                    @Override
                    public boolean act(float delta) {
                        attackStatus = AttackStatus.inactive;
                        invincible=false;
                        logic.resetCameraOffset();
                        if(currentStatus==AttackStatus.inair||currentStatus==AttackStatus.repositioning){
                            //attackdelay2=0;
                            attackStatus = currentStatus;
                        }
                        return true;}}

        ));


    }





}

