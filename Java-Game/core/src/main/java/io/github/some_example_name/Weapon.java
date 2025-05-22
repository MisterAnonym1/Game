package io.github.some_example_name;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class MeeleWeapon extends Actor
{
    int damage = 40;
    Player player;
    boolean ismirrored=false;
    MeeleWeapon (Player pl) {
        player=pl;

    }




    void attack(float attackduration) {
        //addAction(Actions.timeScale(0.9f,Actions.rotateBy((ismirrored ? 60 :-60 ),0.2f)));
        //addAction(Actions.after(Actions.rotateBy((ismirrored ? -60 :60 ),0.2f)));
        addAction(Actions.delay(attackduration));
    }
}




class Pipe extends MeeleWeapon {

    Pipe( Player pl) {
        super(pl);
        damage=50;


    }

    @Override
    void attack(float attackduration) {
        super.attack(attackduration);
    }
}
