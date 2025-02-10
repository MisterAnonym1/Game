package io.github.some_example_name;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

class Level {


    String[] rows;
    // Bagimongame game;
    ArrayList<MyTile> notWallsTiles = new ArrayList<MyTile>();
    ArrayList<MyTile> walls = new ArrayList<>();
    ArrayList<MyTile> teleporters = new ArrayList<MyTile>();
    ArrayList<Testentity> testentitys = new ArrayList<>();
    ArrayList<Gegner> gegnerliste = new ArrayList<>();
    ArrayList<NPC> npcs = new ArrayList<>();
    //ArrayList<Item> itemlist = new ArrayList<Item>();

    int doorsnummer;


    double xcoplayer;
    double ycoplayer;

    Level(String[] rows) {
        this.rows = rows;



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

        for (Testentity testen : testentitys) {
            testen.destroy();
        }
        testentitys.clear();

        for (Gegner gegner : gegnerliste) {
            gegner.destroy();
        }
        gegnerliste.clear();

        for (NPC npc : npcs) {
            npc.destroy();
        }
        npcs.clear();
        //itemlist.clear();

    }


    /**
     * Erzeugt aus dem String-kodierten Level-Format (siehe http://sokobano.de/wiki/index.php?title=Level_format)
     * alle benötigten Kachel-Sprites und die Kisten und setzt die Spielerfigur auf die Startposition (Zeichen @).
     */
    public void render(Main log) {
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
                        { newtilewall(column, line, 3); }
                        else
                        {
                            if(randomn <= 4)
                            { newtilewall(column, line, 4); }
                            else
                            { if(randomn <= 6)
                            { newtilewall(column, line, 5); }
                            else
                            { if(randomn <= 8)
                            { newtilewall(column, line, 8); }
                            else
                            { MyTile tili = new MyTile(column, line, SpriteLibrary.pixelmon, 7, true);
                                walls.add(tili);

                            }
                            }
                            }
                        }



                        break;
                    case 'n' :
                        npcs.add(new NPC(column * 128, line * 128, SpriteLibrary.Characters_1, 0, 2, 0));
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    case 'd' :
                        teleporters.add(new MyTile(column, line, SpriteLibrary.pixelmon, 31, true));
                        break;
                    case 'i' :
                  /*ItemType type= Item.getrandomtype();
                  itemlist.add(new Item(column * 128, line * 128, Item.getInType(type),type ));*/
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    case ' ' :
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    case 't' :
                        testentitys.add(new Testetity(column * 128, line * 128, log));
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    case '$' :
                        break;
                    case '@' :
                        xcoplayer = column * 128;
                        ycoplayer = line * 128;
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    case 'x' :

                        break;
                    case 'k' :
                        //notWallsTiles.add(new MyTile(column, line, SpriteLibrary.Hamster, 2));
                        break;
                    case 'g' :
                        gegnerliste.add(new Gegner(log, column * 128, line * 128));
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));
                        break;
                    default :
                        notWallsTiles.add(new MyTile(column, line, SpriteLibrary.pixelmon, 24, false));

                }

            }
        }
        System.out.print(testentitys.size());
    }


    public MyTile getnotwallTile(int column, int row) {
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
    /**

     *
     * column/row: Aktuelle Kachel-Position der Spielerfigur
     * dx: Um so viele Kacheln soll die Spielerfigur nach rechts bewegt werden.
     * dy: Um so viele Kacheln soll die Spielerfigur nach unten bewegt werden.
     */
    void newtilewall(int column, int line, int num)
    {
        MyTile tili = new MyTile(column, line, SpriteLibrary.pixelmon, num, true);

        walls.add(tili);

    }



}
