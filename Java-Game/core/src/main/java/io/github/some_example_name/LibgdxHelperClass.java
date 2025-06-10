package io.github.some_example_name;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import com.badlogic.gdx.math.*;
public class LibgdxHelperClass {
}
 class MathHelper//eigene Klasse von mir zum regeln von sachen
{
    static  boolean isLineIntersectingRectangle(float x1, float y1, float x2, float y2, Rectangle rect) {
        return (Intersector.intersectSegments(x1, y1, x2, y2, rect.x, rect.y, rect.x + rect.width, rect.y,null) ||
            Intersector.intersectSegments(x1, y1, x2, y2, rect.x, rect.y, rect.x, rect.y + rect.height,null) ||
            Intersector.intersectSegments(x1, y1, x2, y2, rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height,null) ||
            Intersector.intersectSegments(x1, y1, x2, y2, rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height,null));

    }
    public static Polygon recToPolygon(Rectangle rect) {
        float[] vertices = {
            rect.x, rect.y,                        // Ecke unten links
            rect.x + rect.width, rect.y,           // Ecke unten rechts
            rect.x + rect.width, rect.y + rect.height, // Ecke oben rechts
            rect.x, rect.y + rect.height           // Ecke oben links
        };
        return new Polygon(vertices);
    }

    public static boolean overlaps(Rectangle rec,Polygon polygon){
        Polygon rectPoly = recToPolygon(rec);
        return Intersector.overlapConvexPolygons(rectPoly, polygon);
    }

    public static Vector2 getCollisionResolutionVector(Rectangle rec, Polygon polygon) {
        Polygon rectPoly = recToPolygon(rec);
        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();

        if (Intersector.overlapConvexPolygons(rectPoly, polygon, mtv)) {
            return mtv.normal.scl(mtv.depth); // Berechnet den tatsächlichen Korrektur-Vektor
        }

        return new Vector2(0, 0); // Falls keine Kollision vorliegt
    }




    public static boolean isAngleOutOfBounds(Vector2 vector, float referenceAngle, float vary) {
        float vectorAngle = vector.angleDeg(); // Winkel des Vektors in Grad
        float lowerBound = (referenceAngle - vary + 360) % 360;
        float upperBound = (referenceAngle + vary) % 360;

        // Prüfe, ob der Winkel außerhalb des erlaubten Bereichs liegt
        if (lowerBound > upperBound) {
            return vectorAngle < lowerBound && vectorAngle > upperBound;
        } else {
            return vectorAngle < lowerBound || vectorAngle > upperBound;
        }
    }

}

 class Animator{

    static public Animation<TextureRegion> getAnimation(String sheet_path, int sheet_cols, int sheet_rows, int firstFrameisOne1, int lastFrame, float frameDuration)
    {
        lastFrame=Math.min(lastFrame, sheet_cols*sheet_rows-1);
        int frames=lastFrame-firstFrameisOne1 +1;
        float frameduration= frameDuration;
        if (frameduration<=0)
        {
            frameduration=0.025f;
        }
        Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)

        SpriteBatch spriteBatch;

        Texture walkSheet = new Texture(Gdx.files.internal(sheet_path));
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheet_cols, walkSheet.getHeight() / sheet_rows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[Math.min(sheet_cols * sheet_rows,frames)];
        int index = 0;
        for (int i = (int) Math.ceil((float)firstFrameisOne1/sheet_cols)-1; i <= Math.ceil((float)lastFrame/sheet_cols)-1; i++) {
            for (int j = 0; j < sheet_cols; j++) {
                walkFrames[index++] = tmp[i][j];

                if (index >= frames) {
                    //System.out.println("\nfirst " + index);
                    break;
                }

            }
        }


        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(frameduration, walkFrames);

        return walkAnimation;

    }
     static public Animation<TextureRegion> getAnimation(TextureRegion sheet, int sheet_cols, int sheet_rows, float frameDuration)
     {

         float frameduration= frameDuration;
         if (frameduration<=0)
         {
             frameduration=0.025f;
         }
         Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)



         // Use the split utility method to create a 2D array of TextureRegions. This is
         // possible because this sprite sheet contains frames of equal size and they are
         // all aligned.
         TextureRegion[][] tmp = sheet.split(sheet.getRegionWidth() / sheet_cols, sheet.getRegionWidth() / sheet_rows);
             //TextureRegion.split(walkSheet, walkSheet.getWidth() / sheet_cols, walkSheet.getHeight() / sheet_rows);

         // Place the regions into a 1D array in the correct order, starting from the top
         // left, going across first. The Animation constructor requires a 1D array.
         TextureRegion[] walkFrames = new TextureRegion[sheet_cols * sheet_rows];

         int index = 0;
         for (int i = 0; i < sheet_rows; i++) {
             for (int j = 0; j < sheet_cols; j++) {
                 walkFrames[index++] = tmp[i][j];
             }
         }

         // Initialize the Animation with the frame interval and array of frames
         walkAnimation = new Animation<TextureRegion>(frameduration, walkFrames);

         return walkAnimation;

     }





    /*public void render() {
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50); // Draw current frame at (50, 50)
        spriteBatch.end();

    }*/


    public void dispose() { // SpriteBatches and Textures must always be disposed
       // spriteBatch.dispose();
       // walkSheet.dispose();
    }
}

