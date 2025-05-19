package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.w3c.dom.Text;

import java.util.ArrayList;



class Level {


    String[] rows;
    ArrayList<MyTile> notWallsTiles= new ArrayList<>();
    ArrayList<MyTile> walls = new ArrayList<>();
    ArrayList<MyTile> teleporters = new ArrayList<>();
    static ArrayList<Testentity> testentitys= new ArrayList<>();
    static ArrayList<Gegner> gegnerliste= new ArrayList<>();
    static ArrayList<Projectile> projectiles= new ArrayList<>();
    static ArrayList<NPC> npcs= new ArrayList<>();
    static ArrayList<TextureActor> deleteList= new ArrayList<>(); // alle Objekte die hier hinzugefügt werden, werden am Ende von act() aus ihren Listen removed/deleted
    //ArrayList<Item> itemlist = new ArrayList<Item>();
    Main logic;
    SpriteBatch batch;
    int[]rownotwalls;
    int doorsnummer;


    float xcoplayer;
    float ycoplayer;

    Level(String[] rows, Main mainlogic) {
        this.rows = rows;
        this.logic=mainlogic;
        /*testentitys = new Stage(log.viewport,log.spriteBatch);
        gegnerliste= new Stage(log.viewport,log.spriteBatch);
        projectiles= new Stage(log.viewport,log.spriteBatch);
        npcs = new Stage(log.viewport,log.spriteBatch);*/

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

        for (Testentity testentity : testentitys) {
            testentity.destroy();
        }
        testentitys.clear();

        for (Gegner geg : gegnerliste) {
                geg.destroy();
        }
        gegnerliste.clear();

        for (NPC npc : npcs) {
            npc.destroy();
        }
        npcs.clear();

        for (Projectile pro : projectiles) {
            pro.destroy();
        }
        projectiles.clear();

        //itemlist.clear();


        deleteList.clear();
    }


    public void act(float delta) {
        for (Testentity testi : testentitys) {
            testi.act(delta);
        }
        for (Gegner testi : gegnerliste) {
            testi.act(delta);
        }
        for (Projectile projec : projectiles) {
            projec.act(delta);
        }
        for (NPC npc : npcs) {
            npc.act(delta);
        }

        //items?


        for (TextureActor actor : deleteList) {
            actor.removeFromLevel();
        }
        deleteList.clear();
    }
    public void draw(Batch batch,ShapeRenderer shape, float delta)
    {

        for (MyTile tile : notWallsTiles) {
           tile.draw(batch);
        }

        for (MyTile tile : teleporters) {
            tile.draw(batch);
        }
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawShadows(shape);
        if(Main.debugging)
        {
            drawHitboxes(shape);
        }
        batch.begin();

        for (MyTile tile : walls) {
            tile.draw(batch);
        }
        for (Testentity testi : testentitys) {
            testi.draw(batch,delta);
        }
        for (Gegner testi : gegnerliste) {
            testi.draw(batch,delta);
        }
        for (Projectile projec : projectiles) {
            projec.draw(batch,delta);
        }
        for (NPC npc : npcs) {
            npc.draw(batch,delta);
        }


        //items.draw
    }
    void drawHitboxes(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1f, 1f, 0f, 1  );

        for (MyTile tile : walls) {
            tile.drawHitbox(shape);
        }
        for (MyTile tile : teleporters) {
            tile.drawHitbox(shape);
        }
        shape.setColor(0f, 0.2f, 1f, 1  );
        for (Testentity testi : testentitys) {
            testi.drawHitbox(shape);
        }
        for (Gegner testi : gegnerliste) {
            testi.drawHitbox(shape);
        }
        for (Projectile projec : projectiles) {
            projec.drawHitbox(shape);
        }
        for (NPC npc : npcs) {
            npc.drawHitbox(shape);
        }
        shape.end();
    }
    void drawShadows(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.1f, 0.4f  );
        for (Testentity testi : testentitys) {
            testi.drawShadow(shape);
        }
        for (Gegner testi : gegnerliste) {
            testi.drawShadow(shape);
        }
        for (Projectile projec : projectiles) {
            //projec.drawShadow(shape);
        }
        for (NPC npc : npcs) {
            npc.drawShadow(shape);
        }
        shape.end();
    }
    void showHitboxes()
    {
        /*MyTile.hitboxalpha = 1;
        Entity.hitboxalpha = 1f;
        Projectile.hitboxalpha = 1f;
        Testentity.hitboxalpha = 0.5f;
        NPC.hitboxalpha=0.5f;*/
    }


    void hideHitboxes()
    {
        /*MyTile.hitboxalpha = 0;
        Entity.hitboxalpha = 0;
        Projectile.hitboxalpha = 0;
        Testentity.hitboxalpha = 0;
        NPC.hitboxalpha=0;*/
    }


    public void load() {
        // Gehe von oben her alle Zeilen durch:
        for (int line = 0; line <rows.length ; line++) {
            // Hole den String-Wert, in dem die aktuelle Zeile kodiert ist
            String row = rows[line];

            // Gehe von links her alle Spalten durch:
            for (int column = 0; column < row.length(); column++) {

                // Zeichen, das die aktuelle Kachel repräsentiert:
                char tilechar = row.charAt(column);

                switch(tilechar) {
                    case ' ' : // wall

                        int randomn = MathUtils.random(1, 20);
                        if(randomn <= 2)
                        { newtileNotwall(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }
                        else
                        {
                            if(randomn <= 4)
                            { newtileNotwall(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }
                            else
                            { if(randomn <= 6)
                            { newtileNotwall(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_2.png")));
                            }
                            else
                            { if(randomn <= 8)
                            { newtileNotwall(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }
                            else
                            { notWallsTiles.add(new MyTile(column, line, "Ph.Boden_Tile_1.png", false));


                            }
                            }
                            }
                        }
                        break;

                    case 'n' :
                        npcs.add(new NPC(MyTile.columnToX(column), MyTile.rowToY(line), "Al Assad.png", "own Watertile 2.png", 0,0.3f,logic));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 'd' :
                        teleporters.add(new MyTile(column, line, new TextureRegion(new Texture("drop.png")), true));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 'i' :
                  /*ItemType type= Item.getrandomtype();
                  itemlist.add(new Item(column * 128, line * 128, Item.getInType(type),type ));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("bucket.png")), false));
                        rownotwalls[line]++;*/

                        break;
                    case '#' :
                        walls.add(new MyTile(column, line, new TextureRegion(new Texture("own Watertile 2.png")), true));

                        int i=1;
                        break;
                    case 't' :
                        testentitys.add(new Testentity(MyTile.columnToX(column), MyTile.rowToY(line), logic));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        rownotwalls[line]++;
                        break;
                    case '$' :
                        break;
                    case '@' :
                        xcoplayer = MyTile.columnToX(column);
                        ycoplayer = MyTile.rowToY(line);
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        rownotwalls[line]++;
                        break;
                    case 'm' :
                        //gegnerliste.add(new Mage(logic,MyTile.columnToX(column), MyTile.rowToY(line) ));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        break;
                    case 'k' :
                        //notWallsTiles.add(new MyTile(column, line, SpriteLibrary.Hamster, 2));
                        break;
                    case 'g' :
                        gegnerliste.add(new Schlange(logic, MyTile.columnToX(column), MyTile.rowToY(line)));
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
                        rownotwalls[line]++;
                        break;
                    default :
                        notWallsTiles.add(new MyTile(column, line, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false));
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

    void newtileNotwall(int column, int line, TextureRegion tex)
    {
        MyTile tili = new MyTile(column, line, tex, false);
        notWallsTiles.add(tili);
        rownotwalls[line]++;
    }



}
