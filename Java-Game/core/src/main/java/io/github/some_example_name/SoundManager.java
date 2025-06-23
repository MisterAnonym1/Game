
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

