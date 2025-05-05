package io.github.some_example_name;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import com.badlogic.gdx.math.*;
public class LibgdxHelperClass {
}

 class Animator{

    static public Animation<TextureRegion> getAnimation(String sheet_path, int sheet_cols, int sheet_rows, int firstFrame, int lastFrame, float frameDuration)
    {
        lastFrame=Math.min(lastFrame, sheet_cols*sheet_rows-1);
        int frames=lastFrame-firstFrame +1;
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
        for (int i = (int) Math.ceil((float)firstFrame/sheet_cols)-1; i <= Math.ceil((float)lastFrame/sheet_cols)-1; i++) {
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

         SpriteBatch spriteBatch;


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

