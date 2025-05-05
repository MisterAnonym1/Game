package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NPC extends Entity
{
    Revtext text;
    Texture backround;
    String backroundfilepath;
    int currentline;
    int scriptIndex;
    int maxline;
    int line;
    boolean statementfinished;
    boolean inConversation=false;
    Viewport viewport;
    NPC(float x, float y, String filepath, String fileBackround, int scriptindex,Main log)
    {
        super(x, y, filepath,log.Player);
        //addAction(Actions.delay(1));
        backroundfilepath=fileBackround;
        viewport=log.viewport;
        text = new Revtext(400, 200, 3,0.1f, "\n");
        this.scriptIndex = scriptindex;
        line = 0;
        maxline = Script.npcscript[scriptIndex][0].length;;
        hitboxOffsetX = 25;
        hitboxOffsetY = 35;
        hitboxalpha=0.5f;

        //hitbox.setAlpha(1); //Aus kommentieren um die Hitbox sichtbar zu mahcen. Achtung macht dei Hitbox durchlässig wenn auskommentiert.
    }

    @Override
    public void draw(Batch batch, float delta)
    {
        super.draw(batch, delta);
    }

    @Override
    public void destroy() {
        super.destroy();
        Level.npcs.remove(this);
    }

    public void drawInConversation(SpriteBatch batch)
    {
        if(inConversation){
            batch.draw(backround, viewport.getScreenX(), viewport.getScreenY(), viewport.getWorldWidth(), viewport.getWorldHeight());
            batch.draw(texture,viewport.getScreenX()+viewport.getScreenWidth()/2.0f-hitbox.getWidth()/1f, viewport.getScreenY()+viewport.getScreenHeight()/2.0f-hitbox.getHeight()/1.0f,getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX()*2,getScaleY()*2,getRotation());
            System.out.println("Conversation started succesfully");
            text.draw(batch);}
    }

    public void act(float delta)
    {
        if(inConversation&&!statementfinished)
        {
            if(text.linefinished)
            {
                line++;

                 nextline();
                if(line >= maxline)
                {
                    statementfinished=true;
                }
                else{}

            }
            text.act(delta);
        }

    }
    void skip()
    {

        if(text.nummer >= text.maintext.length())
        {
            line++;
            text.linefinished=true;
            if(line <= maxline)
            {
                nextline();
            }
        }
        else {
            text.skip();
        }
    }

    @Override
    public void destroy() {
        backround.dispose();
        super.destroy();

    }

    void onPress() {
        inConversation=true;
        if(backround==null)
        {
            backround= new Texture(backroundfilepath);
        }
        //toFront();
        //moveTo(getWorld().getLeft() + getWorld().getWidth() / 2, getWorld().getTop() + getWorld().getHeight() / 2);
        //hintergrund.setVisible(true);
        //restartActing();
        nextline();

    }
    void nextline()
    {
        if(line < maxline)
        {
            text.newText(Script.npcscript[scriptIndex][0][line]);
        }
    }

    void onLeave() {
        inConversation=false;
        text.reset();
    }
}
 class Trader extends NPC {
    Revtext text;
    Sprite hintergrund;
    double lastx;
    double lasty;
    int currentline;
    int lineindex;
    int maxline;
    int line;
    Trader(float x, float y, String filepath, String fileBackground, int lineindex,Main log){
        super(x, y, filepath, fileBackground, lineindex, log);
        addAction(Actions.delay(1));
        //hintergrund = new Sprite(new Texture(fileBackground));
        //hintergrund.setPosition(log.viewport.getScreenX(), log.viewport.getScreenY());
    }

    @Override void nextline()
    {
        if(line < maxline)
        {
            text.newText(Script.npcscript[scriptIndex][1][line]);
        }
    }

    void interact(){
        //inConversation = true;
        onPress();
        System.out.println("Entered trader menu succesfully");
        //nextline();


    }

}
