package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


    class MyTile extends Sprite {


        boolean visited = false;
        MyTile previoustile;
        MyTile northNeighbour;
        MyTile eastNeighbour;
        MyTile southNeighbour;
        MyTile westNeighbour;


        int column;    // Wievieltes Tile von links her (0 bedeutet: ganz links)
        int row;       // Wievieltes Tile von obern her (0 bedeutet: ganz oben)
        double destinationX;
        double destinationY;
        boolean isCurrentlyMoving = false;
        boolean isMirrored = false;
        Rectangle hitbox;
        static float hitboxalpha = 0;
        //TextureRegion texture;

        public MyTile(int column, int row, String filepath,boolean hasHitbox) {
            super(new TextureRegion(new Texture(filepath)));
            setPosition(columnToX(column), rowToY(row));
            //scale(6.46f);
            this.column = column;
            this.row = row;
            if(hasHitbox) {
                hitbox = new Rectangle(columnToX(column) - getWidth() / 2, rowToY(row) - getHeight() / 2, getWidth(), getHeight());
            }
        }
        public MyTile(int column, int row, TextureRegion texreg,boolean hasHitbox) {
            super(texreg);
            setPosition(columnToX(column), rowToY(row));
            //scale(6.46f);
            this.column = column;
            this.row = row;
            if(hasHitbox) {
                hitbox = new Rectangle(columnToX(column) - getWidth() / 2, rowToY(row) - getHeight() / 2, getWidth(), getHeight());}
        }

        int columnToX(int column) {
            return 10 * column;
        }
        public int rowToY(int row) {
            return 10 * row;
        }

        void setNorth(MyTile neighboor)
        {
            if(neighboor == null) {
                return;
            }
            northNeighbour = neighboor;
            neighboor.southNeighbour = this;
        }
        void setEast(MyTile neighboor)
        { if(neighboor == null) {
            return;
        }
            eastNeighbour = neighboor;
            neighboor.westNeighbour = this;
        }
        void setSouth(MyTile neighboor)
        { if(neighboor == null) {
            return;
        }
            southNeighbour = neighboor;
            neighboor.northNeighbour = this;
        }
        void setWest(MyTile neighboor)
        { if(neighboor == null) {
            return;
        }
            westNeighbour = neighboor;
            neighboor.eastNeighbour = this;
        }

        public float getCenterX()
        {
            return getX()+getWidth()/2;
        }
        public float getCenterY()
        {
            return getY()+getHeight()/2;
        }

        public void setPosition(int column, int row) {

            super.setPosition(columnToX(column), rowToY(row));
            this.column = column;
            this.row = row;
        }

       void destroy()
       {
           getTexture().dispose();
       }
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
            getTexture().dispose();
        }
    }
