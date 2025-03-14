package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

/*class Gegner extends Entity
{
    double spawnx;
    double spawny;
    Level curlevel;
    float targetx;
    float targety;
    int counter = 0;
    Player spieler;
    Main logic;
    ArrayList<MyTile> queue = new ArrayList<>();
    ArrayList<MyTile> goalfields = new ArrayList<>();
    ArrayList<MyTile> visitedfields = new ArrayList<>();

    MyTile targettile;
    boolean isatdestination = false;
    boolean collides = false;
    int delay;


    Gegner(Main logic, float x, float y,String filepath) {
        super(x, y, filepath, logic.Player); //viewport war hier mal das Problem

        acceleration = 6;
        maxspeed = 11;
        spawnx = x;
        spawny = y;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 100;
        curhealth = 100;
        this.spieler = logic.Player;

        hitboxOffsetX = 30;
        hitboxOffsetY = 5;
        hitbox = new Rectangle(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY, 50, 35);

    }

    void sterben() {
        spieler.damageby(40);
        destroy();

    }



    public void act(float delta) {
    super.act(delta);
    update(delta);
    }

    public boolean update(float delta) {
        //Diese Methode wird später diffiniert
        if(curhealth <= 0) {
            counter--;
            if(counter <= 0) {
                sterben();
                return true;
            }
        }
        else {
            engagePlayer(delta);
        }
        return false;
    }

    public void attack() {


        sethealth(0, true);
        counter = 3;
        //this.stopActing();

        //Sprite expose = new Sprite(getCenterX(), getCenterY(), SpriteLibrary.Space_Shooter_1, 12);


        //Sound einfügen

    }


    boolean playerinview()
    {
        return false;
    }

    void checkPathToPlayer(float delta)
    {
        counter = 0;
        movement = new Vector2(-getCenterX() + spieler.getCenterX(), getCenterY() - spieler.getCenterY());
        ismoving = true;
        updatemovement(movement, delta);
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

            goalfields.removeFirst();
            if(goalfields.size() <= 0)
            {
                checkPathToPlayer(delta);
                return;
            }
            else {
                movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
            }
        }
        /*if(logic.devmenu.onscreen) {
            goalfields.get(0).tint(Color.blue);
        }*
        ismoving = true;
        updatemovement(movement,delta);
    }

    void setPath(MyTile start, MyTile target)
    {

        MyTile currenttile = start;
        currenttile.visited = true;
        currenttile.previoustile = null;
        visitedfields.add(currenttile);

        while (queue.size() > 0 && inradiusof(currenttile, 1264))
        {
            currenttile = queue.remove(0);
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
        }
    }
    void followplayer(float mindistance, float maxdistance)
    {
        for (MyTile tile : visitedfields)
        {
            tile.visited = false;
            tile.previoustile = null;

            //tile.tint(Color.white);
        }
        visitedfields.clear();
        if(targettile != null) {
            //targettile.tint(Color.white);
        }
        movement = new Vector2(-getCenterX() + Player.getCenterX(), getCenterY() - Player.getCenterY());
        if(movement.len() >= mindistance && movement.len() <= maxdistance) {
            queue.clear();
            MyTile currenttile = curlevel.getnotwallTile(Math.round(getCenterX() / 128), Math.round(getCenterY() / 128));
            targettile = curlevel.getnotwallTile(Math.round(Player.getCenterX() / 128), Math.round(Player.getCenterY() / 128));
            currenttile.visited = true;
            currenttile.previoustile = null;
            visitedfields.add(currenttile);
            while (currenttile != null && inradiusof(currenttile, 1264))
            {
                if(currenttile == targettile) {
                    break;
                }
                queue.addAll(getneighbours(currenttile));
                queue.remove(currenttile);
                if(queue.size() <= 0) {
                    return;
                }
                currenttile = queue.get(0);
            }



            if(currenttile == null || currenttile != targettile) {
                deactivate();
            }

            else {

                goalfields.clear();
                while (currenttile.previoustile != null)//Felder zum Start zurück verfolgen
                {
                    /*if(logic.devmenu.onscreen) {
                        //currenttile.tint(Color.blueviolet);
                    }*
                    goalfields.add(0, currenttile);
                    currenttile = currenttile.previoustile;

                }

                //followPath(); eigentlich aktiv

            }

     }
    }

    public void engagePlayer(float delta)

    {
        //counter--;
        if(inradiusof(spieler, 80)) {
            attack();

        }
        else if(counter <= 0) {
            counter = 2;
            followplayer(40, 1200);
        }
        else {

            followPath(delta);

        }

    }


    ArrayList<MyTile> getneighbours(MyTile feld)
    {
        ArrayList<MyTile> neighbors = new ArrayList<>();
        for (int x = 0; x < 4; x++)
        {
            int col = feld.column;
            int row = feld.row;
            if(x % 2 == 0) {
                col += x - 1;
            }
            else {
                row += x - 2;
            }
            MyTile neigh = curlevel.getnotwallTile(col, row);
            if(neigh != null && !neigh.visited)//checked ob das Feld frei ist
            {
                neigh.visited = true;
                visitedfields.add(neigh);
                neigh.previoustile = feld;
                neighbors.add(neigh);

            }
        }
        return neighbors;

    }

}
*/

abstract class Gegner extends Entity
{
    double spawnx;
    double spawny;
    Level curlevel;
    int counter = 0;
    int attackdelay = 0;
    Player player;
    Main logic;
    ArrayList<MyTile> queue = new ArrayList<>();
    ArrayList<MyTile> goalfields = new ArrayList<>();
    ArrayList<MyTile> visitedfields = new ArrayList<>();
    MyTile targettile;
    Rectangle lineofsight;
    boolean isatdestination = false;
    boolean collides = false;
    int delay;
    abstract void attack();// diese Methoden müssen in einer Unterklasse definiert werden
    abstract boolean update();// soll acten zurückgeben ob gegner aus liste entfernt werden soll
    abstract void engagePlayer();
    abstract void sterben();

    Gegner(float x, float y, Main logic, String filepath) {
        super(x, y, filepath, logic.Player); //viewport war hier mal das Problem

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
        hitbox = new Rectangle(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY, 50, 35);
        //hitbox.setVisible(false);
        hitbox.setAlpha(hitboxalpha);
        lineofsight = new Rectangle(hitbox.getX()+hitbox.getWidth()/2, hitbox.getY()+hitbox.getWidth()/2 - 1.2 * Math.max(hitbox.getWidth(), hitbox.getHeight()) / 2, 1, 1.2 * Math.max(hitbox.getWidth(), hitbox.getHeight()));
        lineofsight.setFillColor(Color.blue, hitboxalpha);
        //lineofsight.defineCenterRelative(hitbox.getCenterX(), hitbox.getCenterY());
    }
    void destroy()
    {
        super.destroy();
        lineofsight.destroy();
    }

    boolean playerinview()
    { Vector2 vec = new Vector2(player.getCenterX() - getCenterX(), -player.getCenterY() + getCenterY());
        lineofsight.destroy();
        lineofsight = new Rectangle(hitbox.getX()+hitbox.getWidth()/2, hitbox.getY()+hitbox.getWidth()/2() -hitbox.getHeight()-lineofsight.getHeight()* - 1.2 * Math.max(hitbox.getWidth(), hitbox.getHeight()) / 2, vec.len()-player.getWidth()/2, 1.2 * Math.max(hitbox.getWidth(), hitbox.getHeight()));
        lineofsight.defineCenter(hitbox.getX()+hitbox.getWidth()/2, hitbox.getY()+hitbox.getWidth()/2);

        lineofsight.rotate(vec.getAngleDeg());
        if(logic.DevMenu.onscreen) {
            lineofsight.setAlpha(0.5);
        }
        else {
            lineofsight.setAlpha(0);
        }

        for (MyTile tile : logic.loadedwalls) {
            if(lineofsight.collidesWith(tile.hitbox))
            {
                //lineofsight.destroy();
                return false;
            }
        }
        //lineofsight.destroy();
        return true;
    }

    void checkPathToPlayer()
    {
        counter = 0;
        if(playerinview()) {

            movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
            ismoving = true;
            updatemovement(movement);
        }
    }

    void followPath()
    {
        if(goalfields.size() <= 0)
        {
            checkPathToPlayer();
            return;
        }
        movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
        if(movement.len() <= maxspeed / 2) {
            goalfields.get(0).tint(Color.white);
            counter--;

            goalfields.remove(0);

            if(goalfields.size() <= 0)
            {
                checkPathToPlayer();
                return;
            }
            else {
                movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
            }
        }
      if(logic.DevMenu.onscreen) {
         goalfields.get(0).tint(Color.blue);
      }
        ismoving = true;
        updatemovement(movement);
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
        while (queue.size() > 0 && inradiusof(currenttile, 1270))
        {
            currenttile = queue.get(0);
            queue.remove(0);
            if(currenttile == target) {
                break
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
                if(logic.DevMenu.onscreen) {
                    currenttile.tint(Color.blueviolet);
                }
                goalfields.add(0, currenttile);
                currenttile = currenttile.previoustile;

            }

            //followPath();
        }
    }


    void locateplayer(float mindistance, float maxdistance)
    {
        for (MyTile tile : visitedfields)
        {
            tile.tint(Color.white);
        }
        visitedfields.clear();
        movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
        if(movement.len() >= mindistance && movement.len() <= maxdistance) {
            setPath(curlevel.getnotwallTile(Math.round(getCenterX() / 129.2), Math.round(getCenterY() / 129.2)), curlevel.getnotwallTile(Math.round(player.getCenterX() / 129.2), Math.round(player.getCenterY() / 129.2)), movement);

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
