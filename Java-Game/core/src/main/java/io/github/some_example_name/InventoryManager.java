package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class InventoryManager {

    private final FileHandle inventoryFile;

    public InventoryManager() {
        // Erstelle oder lade die Datei im lokalen Verzeichnis
        this.inventoryFile = Gdx.files.local("inventory.txt");
        createInventoryFileIfMissing();
    }

    // Überprüft, ob die Datei existiert, wenn nicht, wird sie mit Standardinhalt im lokalen Verzeichnis erstellt
    private void createInventoryFileIfMissing() {
        if (!inventoryFile.exists()) {
            // Standardinhalt für das Inventar
            String defaultContent = "#Coins 0\n#Potions 0\n#Armor 50\n#Sword 1\n"; // Beispielwerte
            inventoryFile.writeString(defaultContent, false); // Erstelle die Datei mit Standardwerten
        }
    }

    // Lese den Inhalt der Datei und gib ihn als Array von Zeilen zurück
    public String[] readInventory() {
        String text = inventoryFile.readString();
        return text.split("\\r?\\n"); // Aufteilen nach Zeilen
    }

    // Hole den Wert für einen bestimmten Schlüssel (z.B. Coins, Armor)
    public int getValueByKey(String key) {
        String[] lines = inventoryFile.readString().split("\\r?\\n");

        for (String line : lines) {
            if (line.startsWith("#" + key)) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    return Integer.parseInt(parts[1].trim()); // Den Wert für den Schlüssel zurückgeben
                }
            }
        }
        return 0; // Falls der Schlüssel nicht gefunden wird
    }

    // Setze einen neuen Wert für einen bestimmten Schlüssel (überschreibt den alten Wert)
    public void setValueByKey(String key, int newValue) {
        String[] lines = readInventory();
        boolean updated = false;
        StringBuilder builder = new StringBuilder();

        // Durch alle Zeilen gehen und nach dem passenden Schlüssel suchen
        for (String line : lines) {
            if (line.startsWith("#" + key)) {
                builder.append("#").append(key).append(" ").append(newValue).append("\n");
                updated = true;
            } else {
                builder.append(line).append("\n");
            }
        }

        // Falls der Schlüssel noch nicht vorhanden ist, füge ihn hinzu
        if (!updated) {
            builder.append("#").append(key).append(" ").append(newValue).append("\n");
        }

        // Die Datei mit den neuen Werten überschreiben
        inventoryFile.writeString(builder.toString(), false);
    }
    public void addValueByKey(String key, int valueToAdd) {
        int currentValue = getValueByKey(key);
        setValueByKey(key, currentValue + valueToAdd);
    }

    // Optional: Speichern erzwingen, falls du keine "automatische Speicherung" im Code verwendest
    //public void saveInventory() {
    //    inventoryFile.flush(); // Stellt sicher, dass alle Daten gespeichert sind
    //}
}

