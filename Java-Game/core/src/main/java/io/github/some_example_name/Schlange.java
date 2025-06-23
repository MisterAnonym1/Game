package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

class Schlange extends Gegner
{



    Schlange(Main logic, float x, float y) {
        super(x, y,logic, "bucket.png");
        acceleration = 200;
        maxspeed = 120;
        maxhealth = 100;
        curhealth = 100;
        scale(1.0f);

        hitboxOffsetX = 30;
        hitboxOffsetY = 5;

    }

    void sterben() {

        player.damageby(40);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        playerinview();
        if(curhealth <= 0) {
              Level.deleteList.add(this);

        }
        else {
            engagePlayer(delta);
        }
    }





    public void engagePlayer(float delta)

    {
        if(!inradiusof(player,1600)){
            return;
        }
        pathCountdown-=delta;
        if(inradiusof(player, 64)) {
            simpleattack();
            pathCountdown = 0;
            goDirectlyToPlayer(delta,5);
            //updatemovement(movement, delta);
            ismoving = true;

        }
        else
        {

            /*if(playerinview()) { //läuft direkt gerade zum Spieler
                counter = 0;
                movement = new Vector2(-getCenterX() + player.getCenterX(), -getCenterY()  +player.getCenterY());
                //locateplayer(0, 1600);
                ismoving = true;
                updatemovement(movement, delta);
            }*/
            if(pathCountdown <= 0)
            {
                locateplayer(40, 1200);
                pathCountdown = 2;
            }
            else {

                followPath(delta);
            }
        }

    }


}
