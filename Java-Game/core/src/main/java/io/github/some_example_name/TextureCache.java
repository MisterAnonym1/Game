package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<String, TextureRegion> cache = new HashMap<>();

    public static TextureRegion getRegion(String filepath) {
        if (!cache.containsKey(filepath)) {
            cache.put(filepath, new TextureRegion(new Texture(filepath)));
        }
        return cache.get(filepath);
    }

    public static void disposeAll() {
        for (TextureRegion region : cache.values()) {
            region.getTexture().dispose();
        }
        cache.clear();
    }
}

