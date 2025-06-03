package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

class Mage extends Gegner
{



    Mage(Main logic, float x, float y) {
        super( x, y,logic, "bucket.png");
        acceleration = 12;
        maxspeed = 12;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 30;
        hitboxOffsetY = 5;
        //setSize(50,50);
        //hitbox = new Rectangle(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY, 50, 35);
        //hitbox.setAlpha(0);
    }

    void sterben() {
        Level.deleteList.add(this);
    }


    public void act(float delta) {
     super.act(delta);
        if(curhealth <= 0) {
            Level.deleteList.add(this);
        }
        else {
            engagePlayer(delta);
        }
    }



    public void attack() {
        Vector2 attackvec1=new Vector2(-getCenterX() + player.getCenterX(), -getCenterY() + player.getCenterY());

        if(player.ismoving) {

            Vector2 attackvec2 = player.movement;
            //attackvec2.setLength(attackvec2.len()*attackvec1.len()/FireBall.speed); ///brauche es für Triangulation
            attackvec2.setLength(   (float) Math.sqrt((attackvec1.len()*attackvec1.len()*player.movement.len()*player.movement.len())/(FireBall.speed*FireBall.speed-player.movement.len()*player.movement.len())       )  ); ///brauche es für Triangulation

            attackvec1.add(attackvec2);
            //attackvec=new Vector2(-getCenterX() + player.getCenterX()+player.movement.x*15, -getCenterY() + player.getCenterY()+player.movement.y*15);
        }

        attackvec1.setLength(this.getHeight()/2);


        Projectile expose = new FireBall(getCenterX(),getCenterY(),  attackvec1);
        //expose.scale(1);
        expose.setdamage(20);
        logic.currentlevel.projectiles.add(expose);

    }



    public void engagePlayer(float delta)

    {
        attackdelay-=delta;
        if(!inradiusof(player, 1600)) {
            return;
        }
        if(attackdelay <= 0 && playerinview()) {
            //läuft direkt gerade zum Spieler
            attackdelay = 0;
            if(inradiusof(player, 400))
            { attackdelay-=delta;
                if(attackdelay <= 0) {
                    attackdelay=1;
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
