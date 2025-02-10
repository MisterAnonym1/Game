package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

class MyTile extends Sprite {



    // Position im Tile-Raster:
    int column;    // Wievieltes Tile von links her (0 bedeutet: ganz links)
    int row;       // Wievieltes Tile von obern her (0 bedeutet: ganz oben)
    double destinationX;
    double destinationY;
    boolean isCurrentlyMoving = false;
    boolean isMirrored = false;
    Rectangle hitbox;
    TextureRegion texture;


    public MyTile(int column, int row, String filepath,boolean hasHitbox) {
        super();
        setPosition(columnToX(column), rowToY(row));
        texture=new TextureRegion(new Texture(filepath));


        scale(6.46F);


        this.column = column;
        this.row = row;



        if(hasHitbox){
            hitbox = new Rectangle(columnToX(column) - getWidth() / 2, rowToY(row) - getHeight() / 2, getWidth(), getHeight());

        }
    }

    float columnToX(int column) {
        return 128 * column;
    }


    public float rowToY(int row) {
        return 128 * row;
    }

    public float getCenterX()
    {
        return getX()+getWidth()/2;
    }
    public float getCenterY()
    {
        return getY()+getHeight()/2;
    }


public void destroy(){
        texture.getTexture().dispose();
    }

    /*
     * Verschiebt das Tile ohne Animation sofort zur Tile-Position (column, row).
     */
    public void moveTo(int column, int row) {

        setPosition(columnToX(column), rowToY(row));
        this.column = column;
        this.row = row;
    }

}

/**
 * Kiste mit Sprengstoff
 */
class Box extends MyTile {

    // isAtDestination == true genau dann, wenn sich die Box auf einem Zielpunkt befindet
    boolean isexploding;
    Sprite explosion;
    Box(int column, int row,  boolean isexplosion) {
        super(column, row, "own Watertile 2.png" ,true);
        isexploding = isexplosion;

    }


    void act()
    {
        if(isexploding == true)
        {

        }
    }
}
