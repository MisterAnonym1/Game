package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

class Gegner extends Entity
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


    Gegner(Main logic, double x, double y,String filepath) {
        super(x, y, filepath); //viewport war hier mal das Problem

        acceleration = 5;
        maxspeed = 10;
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

    void setplayer(Player player)
    {
        this.spieler = player;
    }
    void act() {

    }

    public boolean update() {
        //Diese Methode wird später diffiniert
        if(curhealth <= 0) {
            counter--;
            if(counter <= 0) {
                sterben();
                return true;
            }
        }
        else {
            engagePlayer();
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

    void checkPathToPlayer()
    {
        counter = 0;
        movement = new Vector2(-getCenterX() + spieler.getCenterX(), getCenterY() - spieler.getCenterY());
        ismoving = true;
        updatemovement(movement);
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
            //goalfields.get(0).tint(Color.white);
            counter--;

            goalfields.removeFirst();
            if(goalfields.size() <= 0)
            {
                checkPathToPlayer();
                return;
            }
            else {
                movement = new Vector2(goalfields.get(0).getCenterX() - getCenterX(), -goalfields.get(0).getCenterY() + getCenterY());
            }
        }
        if(logic.devmenu.onscreen) {
            goalfields.get(0).tint(Color.blue);
        }
        ismoving = true;
        updatemovement(movement);
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
        movement = new Vector2(-getCenterX() + spieler.getCenterX(), getCenterY() - spieler.getCenterY());
        if(movement.len() >= mindistance && movement.getLength() <= maxdistance) {
            queue.clear();
            MyTile currenttile = curlevel.getnotwallTile(Math.round(getCenterX() / 128), Math.round(getCenterY() / 128));
            targettile = curlevel.getnotwallTile(Math.round(spieler.getCenterX() / 128), Math.round(spieler.getCenterY() / 128));
            currenttile.visited = true;
            currenttile.previoustile = null;
            visitedfields.add(currenttile);
            while (currenttile != null && inradiusof(currenttile, 1264))
            {
                if(currenttile == targettile) {
                    break
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
                    if(logic.devmenu.onscreen) {
                        //currenttile.tint(Color.blueviolet);
                    }
                    goalfields.add(0, currenttile);
                    currenttile = currenttile.previoustile;

                }

                followPath();

            }

     }
    }

    public void engagePlayer()

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

            followPath();

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
