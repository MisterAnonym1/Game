package io.github.some_example_name;

public class KARLTOFFEL_BOSS extends Boss{
    KARLTOFFEL_BOSS(float x, float y, Main logic, String filepath){
        super(x, y, logic,"El_Karltoffel.png");
        acceleration = 100;
        maxspeed = 100;
        spawnx = x;
        spawny = y;
        curlevel = logic.currentlevel;
        this.logic = logic;
        maxhealth = 1000;
        curhealth = 1000;
        this.player = logic.Player;
        hitboxOffsetX = 25;
        hitboxOffsetY = 35;
        scale(5.0f);

    }
    public boolean update(float delta){
        playerinview();
        if(curhealth <= 0) {
            counter--;
            if(counter <= 0) {
                sterben();
                return true;
            }
        }
        else {
            engagePlayer(delta);
        }
        return false;
    };
    public void engagePlayer(float delta){

    };
    public void sterben(){
        destroy();
    };
}
