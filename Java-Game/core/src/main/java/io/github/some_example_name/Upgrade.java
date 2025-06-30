package io.github.some_example_name;

public class Upgrade {
    private String name;
    private String description;
    private int cost;
    private int level;

    public Upgrade(String name, String description, int cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.level = 0; // Initial level is 0
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    public void upgrade() {
        level++;
        cost += 100; // Example increment for each upgrade
    }
}
