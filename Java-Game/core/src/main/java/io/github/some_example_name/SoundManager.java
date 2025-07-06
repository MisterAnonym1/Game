
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;

public class SoundManager {
    private static final HashMap<String, Sound> sounds = new HashMap<>();
    private static float masterVolume = 1.0f;
    public static void load(String key, String filePath) {
        if (!sounds.containsKey(key)) {
            sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(filePath)));
        }
    }

    public static void play(String key) {
        Sound sound = sounds.get(key);

        if (sound != null) {
            sound.play();
        }
    }
    public static void play(String key, float volume, float pitch, float pan) {
        Sound sound = sounds.get(key);
        if (sound != null) {
            sound.play(volume * masterVolume, pitch, pan);
        }
    }
    public static void play(String key, float volume, float pitch) {
        play(key, volume, pitch, 0f);
    }
    public static void stop(String key) {
        Sound sound = sounds.get(key);
        if (sound != null) {
            sound.stop();
        }
    }
    public static void setMasterVolume(float volume) {
        masterVolume = MathUtils.clamp(volume, 0f, 1f);
    }


    public static void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
}

 class TileManager {
     private static final HashMap<String, TextureRegion[]> tilesets = new HashMap<>();


     public static void load(String key, String filePath,int cols, int rows) {
         if (!tilesets.containsKey(key)) {
             tilesets.put(key, Animator.getRegionList(filePath, cols, rows));
         }
     }
     public static void load(String key, String filePath,float width, float heigth) {
         if (!tilesets.containsKey(key)) {
             Texture tex= new Texture(filePath);
             tilesets.put(key, Animator.getRegionList(filePath, (int)Math.floor(tex.getWidth()/width), (int)Math.floor(tex.getHeight()/heigth)));
         }
     }
     public static TextureRegion getTile(String key, int index) {
         TextureRegion[] tiles = tilesets.get(key);
         if (tiles != null && index >= 0 && index < tiles.length) {
             return tiles[index];
         }
         throw new IllegalArgumentException("Tile not found: " + key + " at index " + index);
         //return null; // or throw an exception
     }


     public static void dispose() {
        for(TextureRegion[] tiles : tilesets.values()) {
            for (TextureRegion tile : tiles) {
                if (tile.getTexture() != null) {
                    tile.getTexture().dispose();
                }
            }
        }
     }
}

