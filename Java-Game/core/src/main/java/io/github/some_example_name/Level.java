package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;



class Level {


    String[] rows;
    ArrayList<MyTile> notWallsTiles= new ArrayList<>();;
    ArrayList<MyTile> walls = new ArrayList<>();
    ArrayList<MyTile> teleporters = new ArrayList<>();
    Stage testentitys;
    Stage gegnerliste;
    Stage projectiles;
    Stage npcs;
    //ArrayList<Item> itemlist = new ArrayList<Item>();
    Main log;

    int[]rownotwalls;
    int doorsnummer;


    double xcoplayer;
    double ycoplayer;

    Level(String[] rows, Main log) {
        this.rows = rows;
        this.log=log;
        testentitys = new Stage(log.viewport,log.spriteBatch);
        gegnerliste= new Stage(log.viewport,log.spriteBatch);
        projectiles= new Stage(log.viewport,log.spriteBatch);
        npcs = new Stage(log.viewport,log.spriteBatch);

        rownotwalls = new int[rows.length];
        doorsnummer = 0;
        xcoplayer = 500;
        ycoplayer = 400;
    }

    /**
     * Zerstört das Level, d.h. alle angezeigten Tiles und Kisten
*/
    public void destroy() {
        for (MyTile wall : walls) {
            wall.destroy();
        }
        walls.clear();

        for (MyTile notwall : notWallsTiles) {
            notwall.destroy();
        }
        notWallsTiles.clear();

        for (MyTile teleport : teleporters) {
            teleport.destroy();
        }
        teleporters.clear();

        for (Actor testen : testentitys.getActors()) {
            Testentity testentity= (Testentity) testen; //casten da alle Gegner in Stage zurück in Actors umgewandelt werden
            testentity.destroy();
        }
        testentitys.clear();

        for (Actor gegner : gegnerliste.getActors()) {
            Gegner geg = (Gegner) gegner;
                geg.destroy();
        }
        gegnerliste.clear();

        for (Actor np : npcs.getActors()) {
            NPC npc = (NPC) np;
            npc.destroy();
        }
        npcs.clear();
        for (Actor prc : projectiles.getActors()) {
            Projectile pro= (Projectile) prc;
                pro.destroy();
        }
        projectiles.clear();
        //itemlist.clear();

    }


    public void act(float delta) {
        testentitys.act(delta);
        gegnerliste.act(delta);
        projectiles.act(delta);
        npcs.act();
        //items?
    }
    public void draw(Batch batch)
    {
        for (MyTile tile : notWallsTiles) {
           tile.draw(batch);
        }
        for (MyTile tile : walls) {
            tile.draw(batch);
        }
        for (MyTile tile : teleporters) {
            tile.draw(batch);
        }
        batch.end();
        testentitys.draw();
        gegnerliste.draw();
        projectiles.draw();
        npcs.draw();
        batch.begin();
        //items.draw
    }
    void showHitboxes()
    {
        MyTile.hitboxalpha = 1;
        Entity.hitboxalpha = 0.5f;
        Projectile.hitboxalpha = 0.5f;
        Testentity.hitboxalpha = 0.5f;
        NPC.hitboxalpha=0.5f;
    }


    void hideHitboxes()
    {
        MyTile.hitboxalpha = 0;
        Entity.hitboxalpha = 0;
        Projectile.hitboxalpha = 0;
        Testentity.hitboxalpha = 0;
        NPC.hitboxalpha=0;
    }


    public void render() {
        // Gehe von oben her alle Zeilen durch:
        for (int line = 0; line < rows.length; line++) {
            // Hole den String-Wert, in dem die aktuelle Zeile kodiert ist
            String row = rows[line];

            // Gehe von links her alle Spalten durch:
            for (int column = 0; column < row.length(); column++) {

                // Zeichen, das die aktuelle Kachel repräsentiert:
                char tilechar = row.charAt(column);

                switch(tilechar) {
                    case '#' : // wall

                        int randomn = MathUtils.random(1, 20);
                        if(randomn <= 2)
                        { newtilewall(column, line, new TextureRegion(new Texture("bucket.png"))); }
                        else
                        {
                            if(randomn <= 4)
                            { newtilewall(column, line, new TextureRegion(new Texture("bucket.png"))); }
                            else
                            { if(randomn <= 6)
                            { newtilewall(column, line, new TextureRegion(new Texture("bucket.png")));
                            }
                            else
                            { if(randomn <= 8)
                            { newtilewall(column, line, new TextureRegion(new Texture("bucket.png"))); }
                            else
                            { MyTile tili = new MyTile(column, line, "bucket.png", true);
                                //print(tili.getWidth()/2+"\n");
                                walls.add(tili);

                            }
                            }
                            }
                        }
                        break;

                    case 'n' :
                        ///npcs.add(new NPC(column * 128, line * 128, "", 2, 0));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 'd' :
                        teleporters.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), true));

                        break;
                    case 'i' :
                  /*ItemType type= Item.getrandomtype();
                  itemlist.add(new Item(column * 128, line * 128, Item.getInType(type),type ));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;*/

                        break;
                    case ' ' :
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 't' :
                        testentitys.addActor(new Testentity(column * 128, line * 128, log));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;
                        break;
                    case '$' :
                        break;
                    case '@' :
                        xcoplayer = column * 128;
                        ycoplayer = line * 128;
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 'm' :
                        ///gegnerliste.add(new Mage(log, column * 128, line * 128));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        break;
                    case 'k' :
                        //notWallsTiles.add(new MyTile(column, line, SpriteLibrary.Hamster, 2));
                        break;
                    case 'g' :
                        ///gegnerliste.add(new Schlange(log, column * 128, line * 128));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;
                        break;
                    default :
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;

                }

            }
        }



        for (MyTile tile : notWallsTiles) {
            int row = tile.row;
            int column = tile.column;

            tile.setNorth(getnotwallTile(column, row - 1));
            tile.setEast(getnotwallTile(column + 1, row));
            tile.setSouth(getnotwallTile(column, row + 1));
            tile.setWest(getnotwallTile(column - 1, row));
        }
        for (MyTile tile : teleporters) {

        }
        //System.out.print(testentitys.size());
    }



    public MyTile getnotwallTile2(int column, int row) {
        for (MyTile tile : notWallsTiles) {

            if(tile.column == column && tile.row == row) {
                return tile;
            }
        }
        for (MyTile tile : teleporters) {

            if(tile.column == column && tile.row == row) {
                return tile;
            }
        }

        return null;

    }
    public MyTile getnotwallTile(int column, int row) {
        if(row < 0 || column < 0) {
            return null;
        }
        if(row + 1 > rows.length || column > rows[row].length()) {
            return null;
        }
        int listnr = 0;
        for (int i = 0; i < row; i++) {
            listnr += rownotwalls[i];
        }
        MyTile tile;
        for (int i = listnr; i < listnr + rownotwalls[row]; i++) {
            tile = notWallsTiles.get(i);
            if(tile.column == column && tile.row == row) {
                return tile;
            }
        }
        // listnr += column;
        return null;

    }
    int getLength()
    {
        int length = 0;
        for (int i = 0; i < rows.length; i++) {
            if(rows[i].length() > length) {
                length = rows[i].length();
            }
        }
        return length;
    }
    int getHeight()
    {
        return rows.length;
    }

    void newtilewall(int column, int line, TextureRegion tex)
    {
        MyTile tili = new MyTile(column, line, tex, true);

        walls.add(tili);

    }



}
