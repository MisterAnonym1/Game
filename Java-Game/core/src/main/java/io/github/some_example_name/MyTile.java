package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;


public class MyTile extends Sprite {
    int column;
    int row;
    Polygon hitbox;
    MyTile previoustile;
    boolean obstructed;
    ArrayList<TextureRegion> additionalTextures; // Only create when needed
    
    public MyTile(int column, int row, String filepath, boolean hasHitbox) {
        this(column, row, TextureCache.getRegion(filepath), hasHitbox);
        if("own Watertile 2.png".equals(filepath)) // Performance: use .equals() instead of ==
        {
            setColor(new Color(0.5f,0.5f,0.3f,1));
        }
    }

    public MyTile(int column, int row, TextureRegion region, boolean hasHitbox,int num){
        this(column, row, region, hasHitbox);
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
    void setAdditionalTextures(ArrayList<TextureRegion> additionalTextures) {
        if (this.additionalTextures == null) {
            this.additionalTextures = new ArrayList<>();
        }
        this.additionalTextures = additionalTextures;
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
        if (additionalTextures != null && !additionalTextures.isEmpty()) {
            for (TextureRegion tex : additionalTextures) {
                batch.draw(tex, getX(), getY(), getWidth(), getHeight());
            }
        }
        super.draw(batch);
    }

    void drawHitbox(ShapeRenderer shape) {
        //shape.setColor(1, 1, 0, 1);
        shape.polygon(hitbox.getTransformedVertices());
    }





    public float getCenterX() { return getX()+getWidth()/2; }
    public float getCenterY() { return getY()+getHeight()/2; }

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
