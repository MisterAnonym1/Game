package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Speichert das Inventar in einer lokalen Textdatei (für Desktop/Android).
 */
public class FileInventoryStorage implements InventoryStorage {
    private final FileHandle inventoryFile;
    private final String defaultContent = "#Coins 0\n#Potions 0\n#Armor 50\n#Sword 1\n";

    public FileInventoryStorage() {
        this.inventoryFile = Gdx.files.local("inventory.txt");
        if (!inventoryFile.exists()) {
            inventoryFile.writeString(defaultContent, false);
        }
    }

    @Override
    public String[] readInventory() {
        return inventoryFile.readString().split("\\r?\\n");
    }

    @Override
    public void resetInventory() {
        inventoryFile.writeString(defaultContent, false);
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

        inventoryFile.writeString(builder.toString(), false);
    }

    @Override
    public void addValueByKey(String key, int valueToAdd) {
        setValueByKey(key, getValueByKey(key) + valueToAdd);
    }

    @Override
    public String getRawContent() {
        return inventoryFile.readString();
    }
}

