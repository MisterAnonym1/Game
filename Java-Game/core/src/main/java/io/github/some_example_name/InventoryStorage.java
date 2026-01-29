package io.github.some_example_name;

/**
 * Abstraktion für Inventar-Speicher.
 * Implementierungen können Dateien (desktop) oder Preferences/LocalStorage (html) benutzen.
 */
public interface InventoryStorage {
    String[] readInventory();
    void resetInventory();
    int getValueByKey(String key);
    void setValueByKey(String key, int newValue);
    void addValueByKey(String key, int valueToAdd);
    String getRawContent();
}

