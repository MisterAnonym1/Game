package io.github.some_example_name;


public class InventoryManager {

    private static final boolean USE_PREFERENCES_FOR_HTML = false; // <-- bei HTML-Build auf true setzen

    private final InventoryStorage storage;

    public InventoryManager() {
        if (USE_PREFERENCES_FOR_HTML) {
            this.storage = new PreferencesInventoryStorage();
        } else {
            this.storage = new FileInventoryStorage();
        }
    }

    // Delegierte Methoden
    public void resetInventory() {
        storage.resetInventory();
    }

    public String[] readInventory() {
        return storage.readInventory();
    }

    public int getValueByKey(String key) {
        return storage.getValueByKey(key);
    }

    public void setValueByKey(String key, int newValue) {
        storage.setValueByKey(key, newValue);
    }

    public void addValueByKey(String key, int valueToAdd) {
        storage.addValueByKey(key, valueToAdd);
    }

    public String getRawContent() {
        return storage.getRawContent();
    }

}
