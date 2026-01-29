package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Implementierung für den HTML/Browser-Build: speichert Inventar in LibGDX Preferences (lokal im Browser).
 */
public class PreferencesInventoryStorage implements InventoryStorage {
    private final Preferences prefs;
    private final String prefsKey = "inventory.raw";
    private final String defaultContent = "#Coins 0\n#Potions 0\n#Armor 50\n#Sword 1\n";

    public PreferencesInventoryStorage() {
        this.prefs = Gdx.app.getPreferences("game_inventory");
        if (!prefs.contains(prefsKey)) {
            prefs.putString(prefsKey, defaultContent);
            prefs.flush();
        }
    }

    private String raw() {
        return prefs.getString(prefsKey, defaultContent);
    }

    @Override
    public String[] readInventory() {
        return raw().split("\\r?\\n");
    }

    @Override
    public void resetInventory() {
        prefs.putString(prefsKey, defaultContent);
        prefs.flush();
    }

    @Override
    public int getValueByKey(String key) {
        String[] lines = readInventory();
        for (String line : lines) {
            if (line.startsWith("#" + key)) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    try {
                        return Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void setValueByKey(String key, int newValue) {
        String[] lines = readInventory();
        boolean updated = false;
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("#" + key)) {
                builder.append("#").append(key).append(" ").append(newValue).append("\n");
                updated = true;
            } else {
                builder.append(line).append("\n");
            }
        }

        if (!updated) {
            builder.append("#").append(key).append(" ").append(newValue).append("\n");
        }

        prefs.putString(prefsKey, builder.toString());
        prefs.flush();
    }

    @Override
    public void addValueByKey(String key, int valueToAdd) {
        setValueByKey(key, getValueByKey(key) + valueToAdd);
    }

    @Override
    public String getRawContent() {
        return raw();
    }
}

