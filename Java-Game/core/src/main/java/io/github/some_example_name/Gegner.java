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
    float spawnx;
    float spawny;
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
    boolean isatdestination = false;
    int delay;
    AttackStatus attackStatus= AttackStatus.inactiv;
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
        spawnx = x;
        spawny = y;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.player = logic.Player;
        hitboxOffsetX = 0;
        hitboxOffsetY =0;


    }

    @Override
    protected void positionChanged() {
        super.positionChanged();

    }

    @Override
    void initializeHitbox() {
        super.initializeHitbox();

    }

    @Override
    protected void scaleChanged() {
        super.scaleChanged();
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight(),100,0+ hitbox.getHeight(),100,0};
        lineofsight.setVertices(vertices);
        lineofsight.setOrigin(hitbox.getWidth()/2.0f, hitbox.getHeight()/2.0f);
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
        shape.setColor(0.8f,0.2f,1,1);
        shape.polygon(lineofsight.getTransformedVertices());
        shape.setColor(0,0f,1,1);
    }

    boolean playerinview()
    {   Vector2 vec = getDistanceVector(player);
        float[] vertices = {0, 0, 0, 0+ hitbox.getHeight(),vec.len(),0+ hitbox.getHeight(),vec.len(),0};
        lineofsight.setVertices(vertices);
        lineofsight.setOrigin(hitbox.getWidth()/2.0f, hitbox.getHeight()/2.0f);
        lineofsight.setPosition(hitbox.x,hitbox.y);
        lineofsight.setRotation(vec.angleDeg());

        for (MyTile tile : logic.loadedwalls) {
            if(Intersector.overlapConvexPolygons(lineofsight, tile.hitbox))
            {
                return false;
            }
        }

        return true;
    }

    void goDirectlyToPlayer(float delta)
    {
        if(playerinview()) {

            ismoving = true;
            updatemovement(movement,delta);
        }
    }

    void followPath(float delta)
    {
        if(goalfields.size() <= 0)
        {
            pathCountdown=0;
            return;
        }
        if(movement.len() <= (maxspeed / 2)+1) {
            //goalfields.get(0).setColor(Color.WHITE);

            goalfields.remove(0).setColor(Color.WHITE);

            if(goalfields.size() <= 0)
            {
                pathCountdown=0;
                return;
            }
            else {
                movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), goalfields.get(0).getCenterY() - getCenterY());
            }
        }
      if(Main.debugging) {
         goalfields.get(0).setColor(Color.RED);
      }
        ismoving = true;
        updatemovement(movement, delta);
    }


    void setPath(MyTile start, MyTile target, Vector2 vec)
    {

        if(Main.debugging){
            for(MyTile tile: visitedfields)
            {
                tile.setColor(Color.WHITE);
            }
            if(target!=null) target.setColor(Color.WHITE);
        }
        visitedfields.clear();
        queue.clear();
        if(start.obstructed) {
            while (true) {
                goalfields.clear();
                return;
            }
        }
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
            target.setColor(Color.PURPLE);
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


