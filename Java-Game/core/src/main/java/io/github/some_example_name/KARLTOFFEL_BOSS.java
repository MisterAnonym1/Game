package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class KARLTOFFEL_BOSS extends Boss{
    KARLTOFFEL_BOSS(float x, float y, Main logic, String filepath){
        super(x, y, logic,"El_Karltoffel.png");
        acceleration = 100;
        maxspeed = 100;
        spawnx = x;
        spawny = y;
        maxhealth = 1000;
        curhealth = 1000;
        hitboxOffsetX = 25;
        hitboxOffsetY = 35;
        scale(0.3f);

    }
    public boolean update(float delta){
        playerinview();
        if(curhealth <= 0) {

                sterben();
                return true;
        }

            engagePlayer(delta);

        return false;
    };
    public void engagePlayer(float delta){
        attackdelay+=delta;

        if (attackdelay>=2)// sobald das attackdelay auf 2 ist sind 2 sekunden vergangen und ein Feuerball wird geschossen
        {
            //line of sight
            attackdelay=0;
            Vector2 vec= new Vector2(player.getCenterX()-getCenterX(), player.getCenterY()-getCenterY());
            vec.setLength(this.getHeight()/2);
            logic.currentlevel.projectiles.add(new FireBall(getCenterX()+vec.x,getCenterY()+vec.y,vec));
        }
    };
    public void sterben(){
        destroy();
    };
}
