package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

class Mage extends Gegner
{



    Mage(Main logic, float x, float y, String filepath) {
        super( x, y,logic, filepath);
        acceleration = 12;
        maxspeed = 12;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 30;
        hitboxOffsetY = 5;
        scale(2);
        //hitbox = new Rectangle(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY, 50, 35);
        //hitbox.setAlpha(0);
    }

    void sterben() {

        destroy();

    }


    public boolean update(float delta) {

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
    }



    public void attack() {


        attackdelay = 30;
        Vector2 attackvec = new Vector2(-getCenterX() + player.getCenterX(), -getCenterY() + player.getCenterY());

        if(player.ismoving) {
            //attackvec = player.movement;
            //attackvec.setLength(0);
            attackvec=new Vector2(-getCenterX() + player.getCenterX()+player.movement.x*15, -getCenterY() + player.getCenterY()+player.movement.y*15);
            attackvec.setLength(20);
            System.out.println(1);
        }



        Projectile expose = new FireBall(getCenterX(), getCenterY(),  attackvec);
        expose.scale(10);
        expose.setdamage(20);
        logic.currentlevel.projectiles.addActor(expose);

    }



    public void engagePlayer(float delta)

    {
        //counter--;
        if(!inradiusof(player, 1600)) {
            return;
        }
        if(counter <= 0 && playerinview()) {
            //läuft direkt gerade zum Spieler
            counter = 0;
            if(inradiusof(player, 400))
            { attackdelay--;
                if(attackdelay <= 0) {
                    attack();
                }

            }
            else
            {
                movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
                ismoving = true;
                updatemovement(movement, delta);

            }
        }

    }


}
