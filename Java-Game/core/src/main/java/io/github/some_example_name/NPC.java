package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/*public class NPC extends Entity
{
    Revtext text;
    Sprite hintergrund;
    double lastx;
    double lasty;
    int currentline;
    int lineindex;
    int maxline;
    int line;
    NPC(float x, float y, String filepath, String fileBackround, int lineindex,Main log)
    {
        super(x, y, filepath,log.Player);
        addAction(Actions.delay(1));
        hintergrund = new Sprite(new Texture(fileBackround));
        hintergrund.setPosition(log.viewport.getScreenX(), log.viewport.getScreenY());

        text = new Revtext(400, 520, 40, "\n");
        text.isfinished = true;
        lineindex = lineindex;
        line = 0;
        lastx = x;
        lasty = y;
        maxline = 5;
        text.setVisible(false);
        hitboxOffsetX = 25;
        hitboxOffsetY = 35;
        hitbox = new Rectangle(getCenterX() - hitboxOffsetX, getCenterY() - hitboxOffsetY, 50, 85);

        hitbox.setAlpha(hitboxalpha);
        hintergrund.scale(3);
        collisionOn = true;
        //hitbox.setAlpha(1); //Aus kommentieren um die Hitbox sichtbar zu mahcen. Achtung macht dei Hitbox durchlässig wenn auskommentiert.
    }
    void act()
    {
        if(text.isfinished)
        {
            line++;
            if(line <= maxline)
            {
                nextline();
            }
        }
    }
    void skip()
    {

        if(text.nummer >= text.maintext.length())
        {
            line++;
            text.isfinished=true;
            if(line <= maxline)
            {
                nextline();
            }
        }
        else {
            text.skip();
        }
    }
    void onPress() {
        hintergrund.bringToFront();
        bringToFront();
        text.bringToFront();
        scale(2);
        moveTo(getWorld().getLeft() + getWorld().getWidth() / 2, getWorld().getTop() + getWorld().getHeight() / 2);
        hintergrund.setVisible(true);
        restartActing();
        text.setVisible(true);
    }
    void nextline()
    {

        text.newText(Script.npcscript[lineindex][line]);

    }
    void onLeave() {
        stopActing();
        scale(1.0 / 2);
        moveTo(lastx, lasty);
        line = 0;
        text.isfinished = true;
        text.setText("");
        hintergrund.setVisible(false);
        text.setVisible(false);
    }
}*/

public class Trader extends NPC {
    Revtext text;
    Sprite hintergrund;
    double lastx;
    double lasty;
    int currentline;
    int lineindex;
    int maxline;
    int line;
    Trader(float x, float y, String filepath, String fileBackround, int lineindex,Main log){
        super(x, y, filepath,log.Player);
        addAction(Actions.delay(1));
        hintergrund = new Sprite(new Texture(fileBackround));
        hintergrund.setPosition(log.viewport.getScreenX(), log.viewport.getScreenY());
    }

    void Trade(){

    }

}
