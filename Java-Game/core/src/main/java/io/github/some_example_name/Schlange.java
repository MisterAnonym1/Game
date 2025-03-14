package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

class Schlange extends Gegner
{



    Schlange(Main logic, float x, float y) {
        super(logic,x, y, "El_Karltoffel.png");
        acceleration = 20;
        maxspeed = 12;
        maxhealth = 100;
        curhealth = 100;


        hitboxOffsetX = 30;
        hitboxOffsetY = 5;

    }

    void sterben() {

        player.damageby(40);
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

/*
        counter = 3;
        this.stopActing();
        Sprite expose = new Sprite(getCenterX(), getCenterY(), SpriteLibrary.Space_Shooter_1, 12);
        expose.bringToFront();
        expose.scale(7.5);
        Sound.playSound(Sound.far_bomb);
        Sound.playSound(Sound.far_explosion);
        Sound.playSound(Sound.short_shoot);
        expose.playAnimation(12, 23, RepeatType.once, 27);
        expose.bringToFront();
        sethealth(0, true);
*/
    }



    public void engagePlayer(float delta)

    {
        if(!inradiusof(player,1600)){
            return;
        }
        //counter--;
        if(inradiusof(player, 80)) {
            attack();

        }
        else
        {

            if(playerinview()) { //läuft direkt gerade zum Spieler
                counter = 0;
                movement = new Vector2(-getCenterX() + player.getCenterX(), getCenterY() - player.getCenterY());
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