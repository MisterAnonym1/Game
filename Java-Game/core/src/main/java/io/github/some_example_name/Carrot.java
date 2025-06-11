package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Carrot extends Gegner
{

    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> attackAnimation;
    Animation<TextureRegion> defaultAnimation;
    Animation<TextureRegion> currentAnimation; //Variable zum speichern der letzten abgespielten animation
    Animation<TextureRegion> deadAnimation;
    Animation<TextureRegion> damageAnimation;
    Carrot(Main logic, float x, float y) {
        super(x, y,logic, "Carrot_Idle.png");
        acceleration = 120;
        maxspeed = 120;
        maxhealth = 100;
        curhealth = 100;
        hitboxOffsetX = 0;
        hitboxOffsetY = 0;


        defaultAnimation= Animator.getAnimation("Carrot-sheet.png",7,6,1,5,0.15f);
        playAnimation(defaultAnimation);
        walkAnimation= Animator.getAnimation("Carrot-sheet.png",7,6,6,13,0.15f);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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
            pathCountdown = 0;
            goDirectlyToPlayer(delta);
            //updatemovement(movement, delta);
            ismoving = true;

        }
        else
        {
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
