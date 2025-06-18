
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class SoundManager {
    private static final HashMap<String, Sound> sounds = new HashMap<>();

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

    public static void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
}

