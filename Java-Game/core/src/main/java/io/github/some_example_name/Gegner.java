package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;


abstract class Gegner extends Entity
{

    Level curlevel;
    float pathCountdown= 0;
    float attackdelay = 0;
    float attackdelay2 = 0;
    Player player;
    Main logic;
    ArrayList<MyTile> queue = new ArrayList<>();
    ArrayList<MyTile> goalfields = new ArrayList<>();
    ArrayList<MyTile> visitedfields = new ArrayList<>();
    MyTile targettile;
    Polygon lineofsight;
    AttackStatus attackStatus= AttackStatus.inactiv;
    Vector2 directiontoTile = new Vector2(0,0);
    boolean inview = false;
    //abstract void attack();// diese Methoden müssen in einer Unterklasse definiert werden
    // soll acten zurückgeben ob gegner aus liste entfernt werden soll
    Animation<TextureRegion> explosionAnimation;
    public enum AttackStatus { inactiv, dash, strike,exploding }

    Gegner(float x, float y, Main logic, String filepath) {
        this(x, y,  logic,new TextureRegion(new Texture(filepath)));

    }
    Gegner(float x, float y, Main logic, TextureRegion texture)
    {
        super(x, y, texture, logic.Player);
        acceleration = 100;
        maxspeed = 100;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.player = logic.Player;
        hitboxOffsetX = 0;
        hitboxOffsetY =0;


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
        goalfields.clear();
        visitedfields.clear();
        queue.clear();
        targettile = null;
        attackStatus = AttackStatus.inactiv;
    }


    @Override
    void initializeOtherThings() {
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight()/2,100,0+ hitbox.getHeight()/2,100,0};
        lineofsight = new Polygon(vertices );
        lineofsight.setOrigin(hitbox.getWidth(), hitbox.getHeight());
        lineofsight.setPosition(hitbox.x,hitbox.y);
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

    void goDirectlyToPlayer(float delta)
    {
            ismoving = true;
            movement=getDistanceVector(player);
            updatemovement(movement,delta);
    }

    void followPath(float delta)
    {
        if(goalfields.size() <= 0)
        {
            pathCountdown=Math.min(pathCountdown,.3f);
            return;
        }
        directiontoTile= new Vector2(goalfields.get(0).getCenterX() - getCenterX(), goalfields.get(0).getCenterY() - getCenterY());
        if(directiontoTile.len() <= targettile.getWidth()/2-hitbox.height*getScaleY()/2 || directiontoTile.len() >= 200){

            goalfields.remove(0);

            if(goalfields.size() <= 0) //keine Felder mehr zum Folgen
            {
                pathCountdown=0;// neuen Pfad berechnen
                return;
            }
        }
        ismoving = true;
        updatemovement(directiontoTile, delta);
    }


    void setPath(MyTile start, MyTile target, Vector2 vec)
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
            while (currenttile.previoustile != null)//Felder zum Start zurück verfolgen
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
    };

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
    }


    @Override
    public void removeFromLevel() {
        Level.gegnerliste.remove(this);
    }

    void locateplayer(float mindistance, float maxdistance)
    {


        movement = new Vector2(-getCenterX() + player.getCenterX(), -getCenterY() + player.getCenterY());
        if(movement.len() >= mindistance && movement.len() <= maxdistance) {
            setPath(curlevel.getnotwallTile( getCenterX(),getCenterY() ), curlevel.getnotwallTile(player.getCenterX(), player.getCenterY()), movement);

          }


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
            player.damageby(30);
        }
    };
    public void bombattack(){
        if (getdistance(player)<= 20) {
            explosionAnimation = Animator.getAnimation("Se_Player_ja.jpg",3,2,1,5,0.2f);
            player.damageby(50);
            this.destroy();
        }
    };
    public void fireballattack(){

            Vector2 vec= new Vector2(player.getCenterX()-getCenterX(), player.getCenterY()-getCenterY());
            vec.setLength(this.getHeight()/2);
            Level.projectiles.add(new FireBall(getCenterX()+vec.x,getCenterY()+vec.y,vec));

    };
    public void dashattack (float delta) {
        if (getdistance(player) <= 20 && getdistance(player) >= 5) {//läuft direkt gerade zum Spieler
            acceleration = 600;
            maxspeed = 600;
            movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
            ismoving = true;
            //player.damageby(30);
        }
    }

    void fireballringattack(float angle) //
    {
        fireballringattack(angle,0);
    }
    void fireballringattack(float angle,float angleoffset) //
    {
        Vector2 vec= new Vector2(hitbox.width/3f,0);
        vec.rotateDeg(angleoffset);
        for(int i=0; i<=360-angle;i+=angle)
        {
            Level.projectiles.add( new FireBall(getHitboxCenterX()+vec.x,getHitboxCenterY()+vec.y,new Vector2(vec.x,vec.y)));
            vec.rotateDeg(angle);
        }
    }



}


