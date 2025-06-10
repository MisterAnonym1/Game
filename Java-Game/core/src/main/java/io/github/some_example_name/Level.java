package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    MyTile[][] tiles;
    MyTile[] walls;
    static ArrayList<Teleporter>  teleporters= new ArrayList<>();
    static ArrayList<Testentity> testentitys= new ArrayList<>();
    static ArrayList<Gegner> gegnerliste= new ArrayList<>();
    static ArrayList<Projectile> projectiles= new ArrayList<>();
    static ArrayList<NPC> npcs= new ArrayList<>();
    static ArrayList<TextureActor> deleteList= new ArrayList<>(); // alle Objekte die hier hinzugefügt werden, werden am Ende von act() aus ihren Listen removed/deleted
    //ArrayList<Item> itemlist = new ArrayList<Item>();
    Main logic;
    int doorsnummer;
    float xcoplayer;
    float ycoplayer;

    Level(String[] rows, Main mainlogic) {
        this.rows = rows;
        this.logic = mainlogic;
        this.tiles = new MyTile[rows.length][getMaxRowLength()];
        doorsnummer = 0;
        xcoplayer = 500;
        ycoplayer = 400;
    }

    /**
     * Zerstört das Level, d.h. alle angezeigten Tiles und Kisten
*/
        public void destroy() {
            for (int row = 0; row < tiles.length; row++) {
                for (int col = 0; col < tiles[row].length; col++) {
                        tiles[row][col].destroy();
                }
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
            for (TextureActor actor : deleteList) {
                actor.destroy();
            }
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
        for (Teleporter testi : teleporters) {
            testi.act(delta);
        }
        //items?
        for (TextureActor actor : deleteList) {
            actor.removeFromLevel();
            actor.destroy();
        }
        deleteList.clear();
    }
    public void draw(Batch batch,ShapeRenderer shape, float delta)
    {

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].draw(batch, delta);
                }
            }
        }
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(4);
        drawShadows(shape);

        batch.begin();

        for (Teleporter testi : teleporters) {
            testi.draw(batch,delta);
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

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(4);
        if(Main.debugging)
        {

            drawHitboxes(shape);
        }
        batch.begin();


        //items.draw
    }
    void drawHitboxes(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1f, 1f, 0f, 1  );

        for (MyTile tile : walls) {
            tile.drawHitbox(shape);
        }
        for (Teleporter tile : teleporters) {
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



    public void load() {
        int wallCount = 0;


        for (int row = 0; row < tiles.length; row++) {
            String rowString = rows[row];

            for (int column = 0; column < tiles[row].length; column++) {
                char tilechar;
                try{
                    tilechar = rowString.charAt(column);
                 }
                catch(Exception e)
                {
                   tilechar='#';
                }
                MyTile tile = createTile(column, row, tilechar);
                tiles[row][column] = tile;

                if (tilechar == '#') {
                    wallCount++;
                } else if (tilechar == 'd') {
                    tile.obstructed=false;
                    //teleporters.add(tile);
                }
            }
        }

        walls = new MyTile[wallCount];


        // Speichert alle Wände & Teleporter direkt in ihre Arrays
        int wallIndex = 0;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                MyTile tile = tiles[row][col];
                if (tile.obstructed) {
                    walls[wallIndex] = tile;
                    wallIndex++;
                }
            }
        }

        setTileNeighbors();
    }


    public MyTile getdefaultTile(int column, int row)
    {
       return  new MyTile(column,row, new TextureRegion(new Texture("Ph.Boden_Tile_1.png")), false);
    }


    private MyTile createTile(int column, int row, char tilechar) {
        switch (tilechar) {
            case ' ':
                int randomn = MathUtils.random(1, 20);
                if(randomn <= 2)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                if(randomn <= 4)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                if(randomn <= 6)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png")));}
                if(randomn <= 8)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                /*else*/return new MyTile(column, row, "Ph.Boden_Tile_1.png", false);


            case '#':
                return new MyTile(column, row, "own Watertile 2.png", true);
            case '@':
                xcoplayer = MyTile.columnToX(column);
                ycoplayer = MyTile.rowToY(row);
                return new MyTile(column, row, "Ph.Boden_Tile_1.png", false);
            case 'n' :
                npcs.add(new NPC(MyTile.columnToX(column), MyTile.rowToY(row), "Al Assad.png", "own Watertile 2.png", 0,0.3f,logic));
                return getdefaultTile(column,row) ;
            case 'd' :
                teleporters.add(new Teleporter(MyTile.columnToX(column),MyTile.rowToY(row),logic));
                break;
            case 'k' :
                gegnerliste.add(new Karltoffelboss(MyTile.columnToX(column), MyTile.rowToY(row),logic));
                break;
            case '=' :
                gegnerliste.add(new Dummy(MyTile.columnToX(column), MyTile.rowToY(row),logic));
                break;
            case 't' :
                testentitys.add(new Testentity(MyTile.columnToX(column), MyTile.rowToY(row), logic));
                break;
            case '$' :
                break;
            case 'm' :
                gegnerliste.add(new Mage(logic,MyTile.columnToX(column), MyTile.rowToY(row) ));
                break;
            case 'g' :
                gegnerliste.add(new Schlange(logic, MyTile.columnToX(column), MyTile.rowToY(row)));
                break;
            default:
                return getdefaultTile(column,row) ;
        }
        return getdefaultTile(column,row) ;
    }

    private void setTileNeighbors() {
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                MyTile tile = tiles[row][col];

                    tile.setNeighbors(getTile(col, row - 1),getTile(col + 1, row),getTile(col, row + 1),getTile(col - 1, row));
            }
        }
    }
     private MyTile getTile(int column, int row) {
        if (row >= 0 && row < tiles.length && column >= 0 && column < tiles[row].length) {
            return tiles[row][column];
        }
        return null;
    }
     private MyTile getnotwallTile(int column, int row) {
        if (row >= 0 && row < tiles.length && column >= 0 && column < tiles[row].length) {
            if(tiles[row][column].obstructed){return null;}
            return tiles[row][column];
        }
        return null;
    }
    MyTile getnotwallTile(float x, float y){
            return  getnotwallTile(MyTile.xToColumn(x),MyTile.yToRow(y));
    }

    private int getMaxRowLength() {
        int max = 0;
        for (String row : rows) {
            if (row.length() > max) {
                max = row.length();
            }
        }
        return max;
    }



    int getLength()
    {

        return tiles[0].length;
    }
    int getHeight()
    {
        //return rows.length;
        return tiles.length;
    }

    MyTile newtileNotwall(int column, int line, TextureRegion tex)
    {
        return new MyTile(column, line, tex, false);

    }



}
