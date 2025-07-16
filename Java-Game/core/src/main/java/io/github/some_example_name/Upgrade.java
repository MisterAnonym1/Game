package io.github.some_example_name;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Upgrade {
    private String name;
    private String description;
    private int powerlevel=0;
    CostFunction costFunction;
    ApplyFunction applyfunction;
    public Upgrade(String name, String description, CostFunction cost,ApplyFunction applyfun) {
        this.name = name;
        this.description = description;
        costFunction = cost;
        applyfunction = applyfun;
    }
    public Upgrade(String name, CostFunction cost,ApplyFunction applyfun) {
        this(name, "", cost,applyfun);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost(int forlevel) {
        if(costFunction != null) {
            return costFunction.getCost(forlevel);
        }
        return forlevel*1;
    }

    public void setPowerlevel(int powerlevel) {
        this.powerlevel = powerlevel;
    }

    public int getLevel() {
        return powerlevel;
    }

    public void upgrade() {
        powerlevel++;
    }
    public void apply() {
        applyfunction.apply(getLevel());
    }
}
class PlayerUpgrade extends Upgrade {
    Player player;
    public PlayerUpgrade(String name, String description, CostFunction cost ,ApplyFunction applyfun,Player player) {
        super(name, description, cost,applyfun);
        this.player = player;

    }
    public PlayerUpgrade(String name, CostFunction cost, ApplyFunction applyfun,Player player) {
        super(name, cost,applyfun);
        this.player = player;

    }

    @Override
    public void upgrade() {
        super.upgrade();
        applyfunction.apply(getLevel());
    }



}

@FunctionalInterface
interface CostFunction {
    int getCost(int forlevel);
}
@FunctionalInterface
interface ApplyFunction {
    void apply(int forlevel);
}

class UpgradeManager {
    ArrayList<Upgrade> upgrades = new ArrayList<>();
    Player player;
    public UpgradeManager(Player player) {
        this.player = player;
    }
    public void add(Upgrade upgrade) {
        upgrades.add(upgrade);
    }
    public Upgrade getUpgrade(String name) {
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getName().equals(name)) {
                return upgrade;
            }
        }
        return null;
    }
    public void addnewPlayerUpgrade(String name, CostFunction cost, ApplyFunction applyfun) {
        upgrades.add(new PlayerUpgrade(name, "", cost, applyfun, player));
    }
    public void addnewPlayerUpgrade(String name,String description, CostFunction cost, ApplyFunction applyfun) {
        upgrades.add(new PlayerUpgrade(name, description, cost, applyfun, player));
    }
    public void reset() {
        for (Upgrade upgrade : upgrades) {
            upgrade.setPowerlevel(0);
        }
    }

}