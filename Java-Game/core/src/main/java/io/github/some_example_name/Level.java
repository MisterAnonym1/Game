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
import com.badlogic.gdx.math.Vector2;
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
    static ArrayList<PartikelSprite> particles= new ArrayList<>();
    static Stage indicators;
    static ArrayList<NPC> npcs= new ArrayList<>();
    static ArrayList<TextureActor> objects= new ArrayList<>();//temporäre objekte die ganz hinten gedrawt werden
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
        indicators= new Stage(mainlogic.viewport);
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

            indicators.clear();
            for (Projectile pro : projectiles) {
                pro.destroy();
            }
            projectiles.clear();
            for(TextureActor actor : objects) {
                actor.destroy();
            }
            objects.clear();
            for (PartikelSprite par : particles) {
                par.destroy();
            }
            particles.clear();

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
        for (Gegner gegner : gegnerliste) {
            gegner.act(delta);
        }
        for(TextureActor object: objects) {
            object.act(delta);
        }
        for (Projectile projec : projectiles) {
            projec.act(delta);
        }
        indicators.act(delta);
        for( PartikelSprite particle : particles) {
            particle.act(delta);
        }
        for (NPC npc : npcs) {
            npc.act(delta);
        }
        applySeperation(delta);
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

        for (Teleporter teleporter : teleporters) {
            teleporter.draw(batch,delta);
        }
        for(TextureActor object: objects) {
            object.draw(batch,delta);
        }
        for (NPC npc : npcs) {
            npc.draw(batch,delta);
        }
        for (Testentity testi : testentitys) {
            testi.draw(batch,delta);
        }
        for (Gegner gegner : gegnerliste) {
            gegner.draw(batch,delta);
        }
        logic.Player.draw(batch,shape, delta==0?Gdx.graphics.getDeltaTime():delta,1.0f);
        for (Projectile projec : projectiles) {
            projec.draw(batch,delta);
        }
        for(PartikelSprite particle : particles) {
            particle.draw(batch,delta);
        }


        batch.end();
        indicators.draw();
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
        for(TextureActor object: objects) {
            object.drawHitbox(shape);
        }
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
        for(PartikelSprite particle : particles) {
            particle.drawHitbox(shape);
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
                setPlayerPosition(MyTile.columnToX(column),MyTile.rowToY(row));
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
                /*for (int j = 0; j <8 ; j++) {
                for (int i = 0; i <8 ; i++) {
                    gegnerliste.add(new Dummy(MyTile.columnToX(column)+j*8, MyTile.rowToY(row)+i*8,logic,8,8));

                }}*/
                gegnerliste.add(new Dummy(MyTile.columnToX(column), MyTile.rowToY(row),logic,64,64));

                break;
            case 't' :
                testentitys.add(new Testentity(MyTile.columnToX(column), MyTile.rowToY(row), false,logic));
                break;
            case '$' :
                break;
            case 'c' :
                for (int i = 0; i < 1; i++) {
                    gegnerliste.add(new Carrot( MyTile.columnToX(column), MyTile.rowToY(row),logic));
                }
                break;
            case 'm' :
                gegnerliste.add(new Mage(logic,MyTile.columnToX(column), MyTile.rowToY(row) ));
                break;
            case 'h' :
                npcs.add(new Trader(MyTile.columnToX(column), MyTile.rowToY(row), "Al Assad.png", "own Watertile 2.png", 0,0.3f,logic));
                break;
            case 'g' :
                gegnerliste.add(new Testentity(MyTile.columnToX(column), MyTile.rowToY(row), true,logic));
                break;
            default:
                return getdefaultTile(column,row) ;
        }
        return getdefaultTile(column,row) ;
    }


    void reload() {
        destroy();
        load();
    }
    void resetObjects()//resets Entities and Player
    {
        for (Testentity testi : testentitys) {
            testi.reset();
        }
        for(TextureActor object: objects) {
            //object.reset();
        }
        objects.clear();
        for (Gegner gegner : gegnerliste) {
            gegner.reset();
        }

        for (NPC npc : npcs) {
            npc.reset();
        }
        for (Teleporter teleporter : teleporters) {
            teleporter.deactivate();
        }
        for (Projectile projec : projectiles) {
            projec.destroy();
        }
        projectiles.clear();
        for (PartikelSprite par : particles) {
            par.destroy();
        }
        particles.clear();
        for (TextureActor actor : deleteList) {
            actor.removeFromLevel();
            actor.destroy();
        }
        indicators.clear();
        deleteList.clear();
        logic.Player.reset();
        logic.Player.normalise();
    }

    void setPlayerPosition(float x, float y) {
        xcoplayer = x;
        ycoplayer = y;
        logic.Player.spawnx =x;
        logic.Player.spawny =y;
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
    ArrayList<Entity> getEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.addAll(testentitys);
        entities.addAll(gegnerliste);
        entities.addAll(npcs);
        return entities;
    }
    void applySeperation(float delta) {
        // Separation-Parameter
        float separationRadius = 80f; // Abstand, ab dem abgestoßen wird
        float separationStrength = 2000f; // Stärke der Abstoßung
        for (Entity enti : getEntities()) {
            // --- Separation-Kraft berechnen ---
            Vector2 separation = new Vector2();
            int count = 0;
            for (Entity other : getEntities()) {
                if (other == enti) continue;
                float dist = enti.getdistance(other);
                if (dist < separationRadius && dist > 0.00001f) {
                    Vector2 diff = new Vector2(other.getDistanceVector(enti));
                    diff.nor();//normalise (setzt die Länge auf 1)
                    diff.scl(1f / dist); // Je näher, desto stärker
                    separation.add(diff);
                    count++;
                }
            }
            if (count > 0) {
                separation.scl(separationStrength / count*delta);
                //enti.direction.add(separation);
                enti.moveatAngle(separation);
            }

        }
    }

    MyTile newtileNotwall(int column, int line, TextureRegion tex)
    {
        return new MyTile(column, line, tex, false);

    }



}
