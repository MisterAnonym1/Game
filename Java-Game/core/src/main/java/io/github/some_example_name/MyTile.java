package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


public class MyTile extends Sprite {
    int column;
    int row;
    Polygon hitbox;
    boolean visited = false;
    MyTile previoustile;
    MyTile northNeighbour;
    MyTile eastNeighbour;
    MyTile southNeighbour;
    MyTile westNeighbour;
    boolean obstructed;
    public MyTile(int column, int row, String filepath, boolean hasHitbox) {
        this(column, row, TextureCache.getRegion(filepath), hasHitbox);
    }

    public MyTile(int column, int row, TextureRegion texreg, boolean hasHitbox,boolean iswall){
        this(column, row, texreg, hasHitbox);
        if (iswall){setToWall();}
    }
    public MyTile(int column, int row, TextureRegion texreg, boolean hasHitbox) {
        super(texreg);
        if (hasHitbox){setToWall();}
        this.column = column;
        this.row = row;
        setPosition(columnToX(column), rowToY(row));
        setSize(columnToX(1), rowToY(-1));
        //setColor(Color.PURPLE);
        //setColor(Color.LIME);
        if (hasHitbox) {
            float[] vertices = {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()};
            hitbox = new Polygon(vertices);
            hitbox.setPosition(getX(), getY());
        }
    }
    void setToWall()
    {
        obstructed=true;
    }
    public static int columnToX(int column) {
        return 64 * column;
    }

    public static int rowToY(int row) {
        return -64 * row;
    }
    public static int xToColumn(float x) { return (int) x/64; }
    public static int yToRow(float y) { return (int) (y/-64)+1; }

    @Override
    public void draw(Batch batch, float delta) {
        super.draw(batch);
    }

    void drawHitbox(ShapeRenderer shape) {
        //shape.setColor(1, 1, 0, 1);
        shape.polygon(hitbox.getTransformedVertices());
    }



    public void setNeighbors(MyTile north, MyTile east, MyTile south, MyTile west) {
        this.northNeighbour = north;
        this.eastNeighbour = east;
        this.southNeighbour = south;
        this.westNeighbour = west;
    }

    public float getCenterX() { return getX()+getWidth()/2; }
    public float getCenterY() { return getY()+getHeight()/2; }
    void destroy() { /* getTexture().dispose(); entfernt, da zentral */ }

}


/**
     * Kiste mit Sprengstoff
     */
    class Box extends MyTile {

        // isAtDestination == true genau dann, wenn sich die Box auf einem Zielpunkt befindet
        boolean isexploding;
        Sprite explosion;
        Box(int column, int row, boolean isexplosion) {
            super(column, row, "Fireball.png", true);
            isexploding = isexplosion;

        }

        void destroy()
        {
            if(isexploding == true)
            {
                /*explosion = new Sprite(getCenterX(), getCenterY(), SpriteLibrary.Space_Shooter_1, 12);
                explosion.scale(4);
                explosion.playAnimation(12, 21, RepeatType.once, 40);
                Sound.playSound(Sound.cannon_boom);*/
            }
            // getTexture().dispose(); entfernt, da zentral
        }
    }
