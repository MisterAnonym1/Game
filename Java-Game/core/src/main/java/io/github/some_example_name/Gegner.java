package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    int counter = 0;
    int attackdelay = 0;
    Player player;
    Main logic;
    ArrayList<MyTile> queue = new ArrayList<>();
    ArrayList<MyTile> goalfields = new ArrayList<>();
    ArrayList<MyTile> visitedfields = new ArrayList<>();
    MyTile targettile;
    Polygon lineofsight;
    boolean isatdestination = false;
    boolean collides = false;
    int delay;
    abstract void attack();// diese Methoden müssen in einer Unterklasse definiert werden
    abstract boolean update(float delta);// soll acten zurückgeben ob gegner aus liste entfernt werden soll
    abstract void engagePlayer(float delta);
    abstract void sterben();

    Gegner(float x, float y, Main logic, String filepath) {
        super(x, y, filepath, logic.Player);
        acceleration = 10;
        maxspeed = 10;
        spawnx = x;
        spawny = y;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.player = logic.Player;
        hitboxOffsetX = 30;
        hitboxOffsetY = 5;


        float[] vertices = {hitbox.getX(), hitbox.getY(), hitbox.getX(), hitbox.getY()+ getHeight(),1,hitbox.getY(),1,hitbox.getY()+ getHeight()};
        lineofsight = new Polygon(vertices );
        lineofsight.setOrigin(hitbox.getX()+hitbox.getWidth()/2.0f, hitbox.getY()+ hitbox.getHeight()/2.0f);
    }

    @Override
    public void drawHitbox(ShapeRenderer shape) {
        super.drawHitbox(shape);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.scale(2,2,1);
        shape.polygon(lineofsight.getVertices());
    }

    boolean playerinview()
    {   Vector2 vec = new Vector2(player.getCenterX() - getCenterX(), player.getCenterY() - getCenterY());
        float[] vertices = {hitbox.getX(), hitbox.getY(), hitbox.getX(), hitbox.getY()+ getHeight(),vec.len()/*+hitbox.getWidth()/2.0f*/,hitbox.getY(),vec.len()/*+hitbox.getWidth()/2.0f*/,hitbox.getY()+ getHeight()};
        lineofsight.setVertices(vertices);
        //+lineofsight.setRotation(vec.angleDeg());
        //float[] vertices = {(hitbox.getX()- lineofsight.getOriginX())*cos - sin * hitbox.getY(), hitbox.getY(), hitbox.getX(), hitbox.getY()+ getHeight(),vec.len(),hitbox.getY(),vec.len(),hitbox.getY()+ getHeight()};
        //+lineofsight.setPosition(hitbox.getX()+hitbox.getWidth()/2, hitbox.getY()+hitbox.getWidth()/2);


        /*if(logic.DevMenu.onscreen) {
            lineofsight.setAlpha(0.5);
        }
        else {
            lineofsight.setAlpha(0);
        }*/

        /*+for (MyTile tile : logic.loadedwalls) {
            if(lineofsight.collidesWith(tile.hitbox))
            {
                //lineofsight.destroy();
                return false;
            }
        }*/

        return true;
    }

    void checkPathToPlayer(float delta)
    {
        counter = 0;
        if(playerinview()) {

            movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
            ismoving = true;
            updatemovement(movement,delta);
        }
    }

    void followPath(float delta)
    {
        if(goalfields.size() <= 0)
        {
            checkPathToPlayer(delta);
            return;
        }
        movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
        if(movement.len() <= maxspeed / 2) {
            //goalfields.get(0).tint(Color.white);
            counter--;

            goalfields.remove(0);

            if(goalfields.size() <= 0)
            {
                checkPathToPlayer(delta);
                return;
            }
            else {
                movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
            }
        }
      /*if(logic.DevMenu.onscreen) {
         goalfields.get(0).tint(Color.blue);
      }*/
        ismoving = true;
        updatemovement(movement, delta);
    }


    void setPath(MyTile start, MyTile target, Vector2 vec)
    {
        queue.clear();
        if(start == null) {
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
            currenttile = target;
            while (currenttile.previoustile != null)//Felder zum Start zurück verfolgen
            {

                goalfields.add(0, currenttile);
                currenttile = currenttile.previoustile;

            }

            //followPath();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    void locateplayer(float mindistance, float maxdistance)
    {
        for (MyTile tile : visitedfields)
        {
            //tile.tint(Color.white);
        }
        visitedfields.clear();
        movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
        if(movement.len() >= mindistance && movement.len() <= maxdistance) {
            setPath(curlevel.getnotwallTile(Math.round(getCenterX() / 129.2f), Math.round(getCenterY() / 129.2f)), curlevel.getnotwallTile(Math.round(player.getCenterX() / 129.2f), Math.round(player.getCenterY() / 129.2f)), movement);

          }
        for (MyTile tile : visitedfields)
        {
            tile.visited = false;
            tile.previoustile = null;
        }

    }



    ArrayList<MyTile> getneighbours(MyTile feld)
    {
        ArrayList<MyTile> neighbors = new ArrayList<>();

        if(feld.southNeighbour != null && !feld.southNeighbour.visited) {
            neighbors.add(feld.southNeighbour);
            feld.southNeighbour.visited = true;
            visitedfields.add(feld.southNeighbour);
            feld.southNeighbour.previoustile = feld;
        }
        if(feld.eastNeighbour != null && !feld.eastNeighbour.visited) {
            neighbors.add(feld.eastNeighbour);
            feld.eastNeighbour.visited = true;
            visitedfields.add(feld.eastNeighbour);
            feld.eastNeighbour.previoustile = feld;
        }
        if(feld.northNeighbour != null && !feld.northNeighbour.visited) {
            neighbors.add(feld.northNeighbour);
            feld.northNeighbour.visited = true;
            visitedfields.add(feld.northNeighbour);
            feld.northNeighbour.previoustile = feld;
        }
        if(feld.westNeighbour != null && !feld.westNeighbour.visited) {
            neighbors.add(feld.westNeighbour);
            feld.westNeighbour.visited = true;
            visitedfields.add(feld.westNeighbour);
            feld.westNeighbour.previoustile = feld;
        }
        return neighbors;

    }

}
class SightRay //Klasse als Sicht Linie für Gegner
{


    Ray topRay,lowRay;
    Rectangle hitbox;
    Player player;
    Gegner gegner;
    SightRay(Gegner gegner, Player play)
    {
        this.hitbox=gegner.hitbox;
        player=play;
        lowRay = new Ray(new Vector3(hitbox.getX(),hitbox.getY(),0), new Vector3(0,0,0));
        topRay = new Ray(new Vector3(hitbox.getX()+hitbox.getWidth(),hitbox.getY()+hitbox.getHeight(),0),new Vector3(0,0,0));
    }
    void update()
    {
        if(gegner==null||gegner.isdead()){return;}
        if(player==null)
        {
            throw new NullPointerException("Spieler nicht definiert/ ==null in Ray");
        }
        lowRay.set(hitbox.getX(),hitbox.getY(),0, player.getCenterX(), player.getCenterY(), 0);
        topRay.set(hitbox.getX()+hitbox.getWidth(),hitbox.getY()+hitbox.getHeight(),0,player.getCenterX(), player.getCenterY(),0);
    }
    void draw(ShapeRenderer shape)
    {
        shape.line(lowRay.origin, lowRay.direction);
        shape.line(topRay.origin, topRay.direction);
    }
    boolean overlaps(Rectangle shape)
    {
        //Intersector.intersectRayBoundsFast(lowRay,shape);

        return false;
    }
}
