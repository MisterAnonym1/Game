package io.github.some_example_name;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class LibgdxHelperClass {
    public static Pixmap createRoundedRect(int width, int height, int radius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(color);

        // Ecken
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius - 1, radius, radius);
        pixmap.fillCircle(radius, height - radius - 1, radius);
        pixmap.fillCircle(width - radius - 1, height - radius - 1, radius);

        // Kanten
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);

        return pixmap;
    }
    public static Pixmap generateFuturisticTriangleBackground(int width, int height, int triangleCount) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);

        // Hintergrundfarbe (dunkles Blau / Schwarz-Blau)
        pixmap.setColor(0.05f, 0.1f, 0.15f, 1f);
        pixmap.fill();

        Random random = new Random();

        for (int i = 0; i < triangleCount; i++) {
            // Zufällige Punkte für das Dreieck
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = x1 + random.nextInt(80) - 50;
            int y2 = y1 + random.nextInt(80) - 50;
            int x3 = x1 + random.nextInt(80) - 50;
            int y3 = y1 + random.nextInt(80) - 50;

            // Leuchtfarbe mit leichtem Transparenz-Effekt
            Color glow = new Color(
                    0.3f + random.nextFloat() * 0.5f, // R
                    0.7f + random.nextFloat() * 0.3f, // G
                    1f,                               // B
                    0.07f + random.nextFloat() * 0.15f // Alpha
            );
            pixmap.setColor(glow);
            pixmap.fillTriangle(x1, y1, x2, y2, x3, y3);
        }

        return pixmap;
    }

}
 class TriangleBackgroundRenderer {
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float time = 0f;

    public TriangleBackgroundRenderer(int triangleCount) {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram("triangle.vert.glsl", "triangle.frag.glsl");

        float[] vertices = new float[triangleCount * 3 * 2]; // 3 Punkte × 2 Koordinaten (x,y) pro Dreieck
        Random rand = new Random();
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = rand.nextFloat() * 2f - 1f; // Normalisierte Koordinaten zwischen -1 und 1
        }

        mesh = new Mesh(true, vertices.length / 2, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"));
        mesh.setVertices(vertices);
    }

    public void render(float delta) {
        time += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shader.bind();
        shader.setUniformf("u_time", time);
        mesh.render( shader,GL20.GL_TRIANGLES);

    }

    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }
}

 class MathHelper//eigene Klasse von mir zum regeln von sachen
{
    public static float minAbs(float a, float b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }
    public static float maxAbs(float a, float b) {
        return Math.abs(a) > Math.abs(b) ? a : b;
    }
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
            return mtv.normal.scl(mtv.depth+0.0001f);// Berechnet den tatsächlichen Korrektur-Vektor
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
        // left, going across first. The Animatio n constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[Math.min(sheet_cols * sheet_rows,frames)];
        //int index = 0;
        for (int i = firstFrameisOne1; i <=lastFrame ; i++) {
            int h=(int)Math.floor((float)(i-1)/sheet_cols);
            int j= ((i-1)%sheet_cols);
            walkFrames[i-firstFrameisOne1]=tmp[h][j];
        }
        /*loop:for (int i = (int) Math.ceil((float)firstFrameisOne1/sheet_cols)-1; i <= Math.ceil((float)lastFrame/sheet_cols)-1; i++) {
            for (int j = 0; j < sheet_cols; j++) {
                walkFrames[index++] = tmp[i][j];

                if (index >= frames) {
                    //System.out.println("\nfirst " + index);
                    break loop;
                }

            }

        }*/


        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(frameduration, walkFrames);

        return walkAnimation;

    }

     static public TextureRegion[] getRegionList(String sheet_path, int sheet_cols, int sheet_rows)
     {

         Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)

         Texture walkSheet = new Texture(Gdx.files.internal(sheet_path));
         // Use the split utility method to create a 2D array of TextureRegions. This is
         // possible because this sprite sheet contains frames of equal size and they are
         // all aligned.
         TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheet_cols, walkSheet.getHeight() / sheet_rows);

         // Place the regions into a 1D array in the correct order, starting from the top
         // left, going across first. The Animatio n constructor requires a 1D array.
         TextureRegion[] walkFrames = new TextureRegion[sheet_cols * sheet_rows];
         int index = 0;
         for (int i = 0; i < sheet_rows; i++) {
             for (int j = 0; j < sheet_cols; j++) {
                 walkFrames[index++] = tmp[i][j];
             }
         }


         return walkFrames;

     }

     static public Animation<TextureRegion> getAnimation(TextureRegion sheet, int sheet_cols, int sheet_rows, float frameDuration)
     {
         int firstFrameisOne1=1;
         int lastFrame=sheet_cols*sheet_rows;
         int frames=lastFrame-firstFrameisOne1 +1;
         float frameduration= frameDuration;
         if (frameduration<=0)
         {
             frameduration=0.025f;
         }
         Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)

         SpriteBatch spriteBatch;

         Texture walkSheet = sheet.getTexture();
         // Use the split utility method to create a 2D array of TextureRegions. This is
         // possible because this sprite sheet contains frames of equal size and they are
         // all aligned.
         TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheet_cols, walkSheet.getHeight() / sheet_rows);

         // Place the regions into a 1D array in the correct order, starting from the top
         // left, going across first. The Animatio n constructor requires a 1D array.
         TextureRegion[] walkFrames = new TextureRegion[Math.min(sheet_cols * sheet_rows,frames)];
         //int index = 0;
         for (int i = firstFrameisOne1; i <=lastFrame ; i++) {
             int h=(int)Math.floor((float)(i-1)/sheet_cols);
             int j= ((i-1)%sheet_cols);
             walkFrames[i-firstFrameisOne1]=tmp[h][j];
         }


         // Initialize the Animation with the frame interval and array of frames
         walkAnimation = new Animation<TextureRegion>(frameduration, walkFrames);

         return walkAnimation;



     }

    /**
         * Verbindet zwei Animationen zu einer einzigen Animation, indem die Frames der zweiten Animation
         * an die der ersten angehängt werden.
         */
        public static Animation<TextureRegion> connectAnimations(Animation<TextureRegion> anim1, Animation<TextureRegion> anim2, float frameDuration) {
            Array<TextureRegion> frames = new Array<>();
            for (TextureRegion frame : anim1.getKeyFrames()) {
                frames.add(frame);
            }
            for (TextureRegion frame : anim2.getKeyFrames()) {
                frames.add(frame);
            }
            return new Animation<>(frameDuration, frames, Animation.PlayMode.NORMAL);
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

 class ZoomInputProcessor extends InputAdapter {
    private final OrthographicCamera camera;

    private float initialDistance = -1;
    private float initialZoom = -1;

    public ZoomInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Mausrad-Zoom (amountY ist auf Desktop relevant)
        camera.zoom += amountY * 0.1f;
        clampZoom();
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Zoom-Start bei 2 Fingern
        if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
            initialDistance = getTouchDistance();
            initialZoom = camera.zoom;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
            float currentDistance = getTouchDistance();
            if (initialDistance > 0) {
                float ratio = initialDistance / currentDistance;
                camera.zoom = initialZoom * ratio;
                clampZoom();
            }
        }
        return false;
    }

    private float getTouchDistance() {
        Vector2 touch1 = new Vector2(Gdx.input.getX(0), Gdx.input.getY(0));
        Vector2 touch2 = new Vector2(Gdx.input.getX(1), Gdx.input.getY(1));
        return touch1.dst(touch2);
    }

    private void clampZoom() {
        camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 5f);
    }
}
