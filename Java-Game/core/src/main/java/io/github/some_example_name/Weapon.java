package io.github.some_example_name;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class MeeleWeapon extends Actor
{
    int damage = 40;
    Player player;
    boolean isAttacking = false;
    MeeleWeapon (Player pl) {
        player=pl;

    }




    void attackcom(float attackduration) {
        if (isAttacking) return; // Prevent multiple attacks at once
        isAttacking = true;
        addAction(Actions.sequence(
                Actions.delay(attackduration),
                Actions.run(() -> {
                    isAttacking = false; // Reset attacking state after the attack duration
                })
        ));
    }
    void attack(Animation<TextureRegion> attackAnimation, int fromFrame, int toFrame) {
        if (isAttacking||hasActions()) return; // Prevent multiple attacks at once
        float delay= fromFrame*attackAnimation.getFrameDuration();
        float duration= (toFrame-fromFrame+1)*attackAnimation.getFrameDuration();
        addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.run(() -> {
                    isAttacking = true; // Reset attacking state after the attack duration
                }),
                Actions.delay(duration),
                Actions.run(() -> {
                    isAttacking = false; // Reset attacking state after the attack duration
                })
        ));
    }
    boolean preparingAttack() {
        return hasActions();
    }

}




class Sword extends MeeleWeapon {

    Sword( Player pl) {
        super(pl);
        damage=40;


    }

}
