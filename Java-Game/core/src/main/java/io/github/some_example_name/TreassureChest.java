package io.github.some_example_name;

public class TreassureChest extends Trader{
    float spawnx;
    float spawny;
    Level curlevel;
    //boolean opened;

    TreassureChest(float x, float y, Main logic){
        super(x, y, "El_Kartoffel.png", "sand.png", 1, 1,logic);
        spawnx = x;
        spawny = y;
        curlevel=logic.currentlevel;
        //this.logic = logic;

    }

    void open(){
        interact();
    }

    @Override
    public void onLeave() {
        super.onLeave();
        destroy();
    }
}

