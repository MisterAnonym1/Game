package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class Level {


    String[] rows;
    MyTile[][] tiles;
    boolean[][] isWall;
    private boolean[][] visited; //only for pathfinding
    MyTile[] walls;
    private final ArrayList<TextureActor> renderQueue = new ArrayList<>();
    ArrayList<Teleporter>  teleporters= new ArrayList<>();
    ArrayList<Testentity> testentitys= new ArrayList<>();
    ArrayList<Gegner> gegnerliste= new ArrayList<>();
    ArrayList<Projectile> projectiles= new ArrayList<>();
    ArrayList<PartikelSprite> particles= new ArrayList<>();
    Stage indicators;
    ArrayList<NPC> npcs= new ArrayList<>();
    ArrayList<TextureActor> objects= new ArrayList<>();//temporäre objekte die ganz hinten gedrawt werden
    ArrayList<TextureActor> deleteList= new ArrayList<>(); // alle Objekte die hier hinzugefügt werden, werden am Ende von act() aus ihren Listen removed/deleted
    //ArrayList<Item> itemlist = new ArrayList<Item>();
    Main logic;
    float xcoplayer;
    float ycoplayer;
    float leveltime=0f;
    
    // Performance optimization: cache render queue capacity
    private int lastRenderQueueSize = 0;
    
    // Performance optimization: object pools for frequently used objects
    private final ArrayList<Vector2> vector2Pool = new ArrayList<>();
    private final ArrayList<Polygon> polygonPool = new ArrayList<>();
    
    // Performance optimization: spatial partitioning for collision detection
    private static final int GRID_SIZE = 128; // Size of each grid cell
    private final ArrayList<ArrayList<TextureActor>> spatialGrid = new ArrayList<>();
    private int gridWidth, gridHeight;
    
    // Performance optimization: frustum culling for off-screen objects
    private float cameraLeft, cameraRight, cameraTop, cameraBottom;
    private final Rectangle cameraBounds = new Rectangle();
    Level(String[] rows, Main mainlogic) {
        this.rows = rows;
        this.logic = mainlogic;
        this.tiles = new MyTile[rows.length][getMaxRowLength()];
        isWall=new boolean[rows.length][getMaxRowLength()];
        visited=new boolean[rows.length][getMaxRowLength()];
        xcoplayer = 500;
        ycoplayer = 400;
        indicators= new Stage(mainlogic.viewport);
        
        // Initialize spatial grid
        initializeSpatialGrid();
    }

    /**
     * Zerstört das Level, d.h. alle angezeigten Tiles und Kisten
*/
        public void destroy() {


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
            
            // Performance optimization: clear object pools
            vector2Pool.clear();
            polygonPool.clear();
        }


    public void act(float delta) {
         leveltime+=delta;
         
         // Performance optimization: update spatial grid for collision detection
         updateSpatialGrid();

        // Performance optimization: combine loops where possible
        for (Testentity testi : testentitys) {
            testi.act(delta);
        }
        
        if(leveltime>0.6f)
        {
            for (Gegner gegner : gegnerliste) {
               gegner.act(delta);
            }
        }
        
        // Combine object and NPC loops
        int objectCount = objects.size();
        int npcCount = npcs.size();
        for(int i = 0; i < Math.max(objectCount, npcCount); i++) {
            if(i < objectCount) {
                objects.get(i).act(delta);
            }
            if(i < npcCount) {
                npcs.get(i).act(delta);
            }
        }
        
        for (Projectile projec : projectiles) {
            projec.act(delta);
        }
        indicators.act();
        for( PartikelSprite particle : particles) {
            particle.act(delta);
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
        // Performance optimization: update camera bounds for frustum culling
        updateCameraBounds();

        // Performance optimization: frustum culling for tile rendering
        int startRow = Math.max(0, (int) (-cameraTop / 64) - 1);
        int endRow = Math.min(tiles.length, (int) (-cameraBottom / 64) + 2);
        int startCol = Math.max(0, (int) (cameraLeft / 64) - 1);
        int endCol = Math.min(tiles[0].length, (int) (cameraRight / 64) + 2);
        
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
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

        // Performance optimization: frustum culling for entity rendering
        // Group similar texture rendering with culling
        for (Teleporter teleporter : teleporters) {
            if (isOnScreen(teleporter)) {
                teleporter.draw(batch,delta);
            }
        }
        for(TextureActor object: objects) {
            if (isOnScreen(object)) {
                object.draw(batch,delta);
            }
        }

        renderQueue.clear();
        
        // Performance optimization: ensure capacity to avoid reallocations
        int expectedSize = npcs.size() + testentitys.size() + gegnerliste.size();
        if (expectedSize > lastRenderQueueSize) {
            renderQueue.ensureCapacity(expectedSize);
            lastRenderQueueSize = expectedSize;
        }
        
        // Performance optimization: only add on-screen entities to render queue
        for(NPC npc : npcs) {
            if (isOnScreen(npc)) {
                renderQueue.add(npc);
            }
        }
        for(Testentity testi : testentitys) {
            if (isOnScreen(testi)) {
                renderQueue.add(testi);
            }
        }
        for(Gegner gegner : gegnerliste) {
            if (isOnScreen(gegner)) {
                renderQueue.add(gegner);
            }
        }
        
        // **Sortieren nach Y-Koordinate (kleinster zuerst)**
        if (!renderQueue.isEmpty()) {
            Collections.sort(renderQueue, Comparator.comparing(obj -> -obj.hitbox.y));
            for(TextureActor actor : renderQueue) {
                actor.draw(batch, delta);
            }
        }
        
        logic.Player.draw(batch,shape, delta==0&&logic.gamestate!=Gamestate.paused?Gdx.graphics.getDeltaTime():delta);

        // Performance optimization: frustum culling for projectiles
        for (Projectile projec : projectiles) {
            if (isOnScreen(projec)) {
                projec.draw(batch,delta);
            }
        }
        
        // Performance optimization: frustum culling for particles
        for(PartikelSprite particle : particles) {
            if (isOnScreen(particle)) {
                particle.draw(batch,delta);
            }
        }
        
        // Performance optimization: reuse ArrayList for enemies with Y-offset
        ArrayList<Gegner> gegnerMitOffset = new ArrayList<>(gegnerliste.size());
        for (Gegner geg : gegnerliste) {
            if (geg.textureYoffset >0 && isOnScreen(geg)) {
                gegnerMitOffset.add(geg);
            }
        }
        if (!gegnerMitOffset.isEmpty()) {
            gegnerMitOffset.sort(Comparator.comparing(geg -> geg.textureYoffset));
            for (Gegner geg : gegnerMitOffset) {
                geg.draw(batch, delta);
            }
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
    void drawHitboxes(ShapeRenderer shape) {
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
    void drawShadows(ShapeRenderer shape) {
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
                    tilechar=' ';
                }
                if (tilechar == '#') {
                    isWall[row][column]=true;
                }
            }
        }

        for (int row = 0; row < tiles.length; row++) {
            String rowString = rows[row];

            for (int column = 0; column < tiles[row].length; column++) {
                char tilechar;
                try{
                    tilechar = rowString.charAt(column);
                 }
                catch(Exception e)
                {
                   tilechar=' ';
                }
                MyTile tile = createTile(column, row, tilechar);
                tiles[row][column] = tile;
                if (tilechar == '#') {
                    wallCount++;
                } else if (tilechar == 'd') {
                    tile.obstructed=false;
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

    }


    public MyTile getdefaultTile(int column, int row) {
      return new MyTile(column, row, TileManager.getTile("grass",MathUtils.random(0,31)), false);
    }


    private MyTile createTile(int column, int row, char tilechar) {
        switch (tilechar) {
            case ' ':
                int randomn = MathUtils.random(0, 31);
                /*if(randomn <= 2)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                if(randomn <= 4)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                if(randomn <= 6)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png")));}
                if(randomn <= 8)
                { return newtileNotwall(column, row, new TextureRegion(new Texture("Ph.Boden_Tile_2.png"))); }

                /*else*///return new MyTile(column, row, "Ph.Boden_Tile_1.png", false);*/
                return new MyTile(column, row, TileManager.getTile("grass",randomn), false);

            case '#':
                // Nachbarn prüfen, um die passende tilenumber zu bestimmen
                boolean wallTop = isWall(column, row - 1);
                boolean wallRight = isWall(column + 1, row);
                boolean wallBottom = isWall(column, row + 1);
                boolean wallLeft = isWall(column - 1, row);

                /*int tilenumber = 113; // Standardwert--> einzelne Wand

                aditex.add(TileManager.getTile("grass",  MathUtils.random(0, 3)));
                // Beispiel-Logik für verschiedene Wandtypen:
                if (wallTop && wallBottom && wallLeft && !wallRight) {
                    tilenumber = 42;
                }
                else if (wallTop && wallBottom && !wallLeft && !wallRight) {
                    tilenumber = 25; // Vertikale Wand
                }
                else if (wallTop && wallBottom && !wallLeft && wallRight) {
                    tilenumber = 41;
                }

                else if (!wallTop && !wallBottom && wallLeft && wallRight) {
                    tilenumber = 97; // Horizontale Wand
                } else if (wallTop && !wallBottom && wallLeft && wallRight) {
                    tilenumber = 113; // Wand mit Nachbar oben
                } else if (!wallTop && wallBottom && wallLeft && wallRight) {
                    tilenumber = 97;
                } else if (wallTop && !wallBottom && !wallLeft && wallRight) {
                    tilenumber = 65; // Wand mit Nachbar links
                } else if (wallTop && !wallBottom && wallLeft && !wallRight) {
                    tilenumber = 67; // Wand mit Nachbar rechts
                }
                else if (!wallTop && wallBottom && !wallLeft && wallRight) {
                    tilenumber = 49; // Wand mit Nachbar links
                } else if (!wallTop && wallBottom && wallLeft && !wallRight) {
                    tilenumber = 51; // Wand mit Nachbar rechts
                }*/
                int tilenumber = 0; // Standardwert--> einzelne Wand

                // Beispiel-Logik für verschiedene Wandtypen:
                if (wallTop && !wallBottom && wallLeft && !wallRight) {
                    tilenumber = 1;
                } else if (wallTop && !wallBottom && !wallLeft && wallRight) {
                    tilenumber = 2;
                }else
                if (!wallTop && wallBottom && !wallLeft && wallRight) {
                    tilenumber = 3;
                }
                else if (!wallTop && wallBottom && wallLeft && !wallRight) {
                    tilenumber = 4;
                }



                MyTile walltile=new MyTile(column, row, TileManager.getTile("water", tilenumber), true,tilenumber);
                //walltile.setAdditionallTextures(aditex);
                return walltile;

                //return new MyTile(column, row, "own Watertile 2.png", true);
            case '@':
                setPlayerPosition(MyTile.columnToX(column),MyTile.rowToY(row));
                break;
            case 'n' :
                npcs.add(new NPC(MyTile.columnToX(column), MyTile.rowToY(row), "Al Assad.png", "own Watertile 2.png", 0,0.3f,logic));
                return getdefaultTile(column,row) ;
            case 'd' :
                teleporters.add(new Teleporter(MyTile.columnToX(column),MyTile.rowToY(row),logic));
                break;
            case 'k' :
                gegnerliste.add(new Karltoffelboss(MyTile.columnToX(column), MyTile.rowToY(row),logic));
                break;
            case 'p' :
                objects.add(new Healing_potato(MyTile.columnToX(column), MyTile.rowToY(row), MathUtils.random(8,16),this));
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
                gegnerliste.add(new Mage(MyTile.columnToX(column), MyTile.rowToY(row),logic ));
                break;
            case 'h' :
                npcs.add(new Trader(MyTile.columnToX(column), MyTile.rowToY(row), "Al Assad.png", "own Watertile 2.png", 1,0.3f,logic));
                break;
            case 'g' :
                gegnerliste.add(new Testentity(MyTile.columnToX(column), MyTile.rowToY(row), true,logic));
                break;
            default:
                return getdefaultTile(column,row) ;
        }
        return getdefaultTile(column,row) ;
    }


    public ArrayList<MyTile> getAdjacentWalls(float x, float y) {
        ArrayList<MyTile> adjacentWalls = new ArrayList<>(9); // Max 9 adjacent tiles
        int column = MyTile.xToColumn(x);
        int row = MyTile.yToRow(y);

        // Überprüfe die neun angrenzenden Kacheln
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int neighborColumn = column + dx;
                int neighborRow = row + dy;
                if (isWall(neighborColumn, neighborRow)) {
                    MyTile wallTile = getTile(neighborColumn, neighborRow);
                    if (wallTile != null) {
                        adjacentWalls.add(wallTile);
                    }
                }
            }
        }
        return adjacentWalls;
    }

    public boolean isWall(int x, int y) {
        if (y < 0 || y >=rows.length || x < 0 || x >= getMaxRowLength())
        {
            return true;
        }
        return isWall[y][x];
    }

    public boolean isWall(MyTile tile) {

        return isWall[tile.row][tile.column];
    }

    public boolean visited(int x, int y) {
        return visited[y][x];
    }

    public void setVisited(int x, int y, boolean value) {
        visited[y][x]=value;
    }

    public boolean visited(MyTile tile) {
        return visited[tile.row][tile.column];
    }


    void reload() {
        destroy();
        load();
        leveltime=0;
    }
    void resetObjects()//resets Entities and Player
    {
        leveltime=0;
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



     public MyTile getTile(int column, int row) {
        if (row >= 0 && row < tiles.length && column >= 0 && column < tiles[row].length) {
            return tiles[row][column];
        }
        return null;
    }
     private MyTile getnotwallTile(int column, int row) {
        if (row >= 0 && row < tiles.length && column >= 0 && column < tiles[row].length) {
            if(tiles[row][column]!=null && isWall[row][column]){return null;}
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
    // Performance optimization: spatial partitioning methods
    private void initializeSpatialGrid() {
        gridWidth = (getMaxRowLength() * 64 + GRID_SIZE - 1) / GRID_SIZE;
        gridHeight = (rows.length * 64 + GRID_SIZE - 1) / GRID_SIZE;
        
        spatialGrid.clear();
        for (int i = 0; i < gridWidth * gridHeight; i++) {
            spatialGrid.add(new ArrayList<>());
        }
    }
    
    private void updateSpatialGrid() {
        // Clear grid
        for (ArrayList<TextureActor> cell : spatialGrid) {
            cell.clear();
        }
        
        // Add entities to grid
        addEntityToGrid(testentitys);
        addEntityToGrid(gegnerliste);
        addEntityToGrid(npcs);
        addEntityToGrid(projectiles);
    }
    
    private void addEntityToGrid(ArrayList<? extends TextureActor> entities) {
        for (TextureActor entity : entities) {
            int gridX = (int) (entity.getCenterX() / GRID_SIZE);
            int gridY = (int) (entity.getCenterY() / GRID_SIZE);
            
            if (gridX >= 0 && gridX < gridWidth && gridY >= 0 && gridY < gridHeight) {
                int index = gridY * gridWidth + gridX;
                spatialGrid.get(index).add(entity);
            }
        }
    }
    
    // Performance optimization: frustum culling methods
    private void updateCameraBounds() {
        if (logic != null && logic.ocam != null) {
            float camX = logic.ocam.position.x;
            float camY = logic.ocam.position.y;
            float camWidth = logic.ocam.viewportWidth;
            float camHeight = logic.ocam.viewportHeight;
            
            cameraLeft = camX - camWidth / 2;
            cameraRight = camX + camWidth / 2;
            cameraBottom = camY - camHeight / 2;
            cameraTop = camY + camHeight / 2;
            
            cameraBounds.set(cameraLeft, cameraBottom, camWidth, camHeight);
        }
    }
    
    private boolean isOnScreen(TextureActor actor) {
        // Extend bounds slightly to avoid popping
        float margin = 100;
        return actor.hitbox.x + actor.hitbox.width + margin >= cameraLeft &&
               actor.hitbox.x - margin <= cameraRight &&
               actor.hitbox.y + actor.hitbox.height + margin >= cameraBottom &&
               actor.hitbox.y - margin <= cameraTop;
    }
    
    private boolean isTileOnScreen(int row, int col) {
        float tileX = col * 64;
        float tileY = -row * 64;
        return tileX + 64 >= cameraLeft && tileX <= cameraRight &&
               tileY - 64 >= cameraBottom && tileY <= cameraTop;
    }
    private Vector2 getPooledVector2() {
        if (vector2Pool.isEmpty()) {
            return new Vector2();
        }
        return vector2Pool.remove(vector2Pool.size() - 1);
    }
    
    private void returnVector2ToPool(Vector2 vector) {
        if (vector2Pool.size() < 50) { // Limit pool size
            vector.set(0, 0);
            vector2Pool.add(vector);
        }
    }
    
    void applySeperation(float delta) {
        // Separation-Parameter
        float separationRadius = 80f; // Abstand, ab dem abgestoßen wird
        float separationStrength = 2000f; // Stärke der Abstoßung
        
        // Performance optimization: get entities once and reuse
        ArrayList<Entity> entities = getEntities();
        int entityCount = entities.size();
        
        for (int i = 0; i < entityCount; i++) {
            Entity enti = entities.get(i);
            // --- Separation-Kraft berechnen ---
            Vector2 separation = getPooledVector2();
            int count = 0;
            
            // Performance optimization: avoid redundant getEntities() call
            for (int j = 0; j < entityCount; j++) {
                if (i == j) continue;
                Entity other = entities.get(j);
                float dist = enti.getdistance(other);
                if (dist < separationRadius && dist > 0.00001f) {
                    Vector2 diff = getPooledVector2();
                    diff.set(other.getDistanceVector(enti));
                    diff.nor();//normalise (setzt die Länge auf 1)
                    diff.scl(1f / dist); // Je näher, desto stärker
                    separation.add(diff);
                    returnVector2ToPool(diff);
                    count++;
                }
            }
            if (count > 0) {
                separation.scl(separationStrength / count*delta);
                //enti.direction.add(separation);
                enti.moveatAngle(separation);
            }
            returnVector2ToPool(separation);
        }
    }

    MyTile newtileNotwall(int column, int line, TextureRegion tex)
    {
        return new MyTile(column, line, tex, false);

    }



}
