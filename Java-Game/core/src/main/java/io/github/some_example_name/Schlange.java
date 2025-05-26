package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

class Schlange extends Gegner
{



    Schlange(Main logic, float x, float y) {
        super(x, y,logic, "bucket.png");
        acceleration = 20;
        maxspeed = 12;
        maxhealth = 100;
        curhealth = 100;
        scale(1.0f);

        hitboxOffsetX = 30;
        hitboxOffsetY = 5;

    }

    void sterben() {

        player.damageby(40);
        Level.deleteList.add(this);
    }


    public boolean update(float delta) {
        playerinview();
        if(curhealth <= 0) {
              Level.deleteList.add(this);
                return true;
        }
        else {
            engagePlayer(delta);
        }
        return false;
    }





    public void engagePlayer(float delta)

    {
        if(!inradiusof(player,1600)){
            return;
        }
        //counter--;
        if(inradiusof(player, 800)) {
            simpleattack();
            locateplayer(30, 800);
            //updatemovement(movement, delta);
            ismoving = true;

        }
        else
        {
            //this.dashattack(delta);
            if(playerinview()) { //läuft direkt gerade zum Spieler
                counter = 0;
                movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
                //locateplayer(0, 1600);
                ismoving = true;
                updatemovement(movement, delta);
            }

            else if(counter <= 0)
            { counter = 2;
                /*locateplayer(40, 1200);*/
            }
            else {

                followPath(delta);
            }
        }

    }


}
