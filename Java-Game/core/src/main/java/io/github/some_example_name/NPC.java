package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;

public class NPC extends Entity
{
    Revtext text;
    Texture backround;
    String backroundfilepath;
    int currentline;
    int scriptIndex;
    int maxline;
    int line;
    boolean statementfinished,inradius;
    boolean inConversation=false;
    Viewport viewport;
    Animation<Texture> bubble;
    NPC(float x, float y, String filepath, String fileBackround, int scriptindex,Main log)
    {
        super(x, y, filepath,log.Player);
        //addAction(Actions.delay(1));
        backroundfilepath=fileBackround;
        viewport=Main.uiStage.getViewport();
        text = new Revtext(512, 120, 3,0.04f, "Diese Zeile sollte nicht sichtbar sein");
        this.scriptIndex = scriptindex;
        line = 0;
        maxline = Script.npcscript[scriptIndex][0].length;;
        hitboxOffsetX = 25;
        hitboxOffsetY = 35;


        Array<Texture> frames = new Array<>();
        for (int i = 1; i <= 4; i++) {
            //frames.add(new Texture(Gdx.files.internal("assets/speech-bubble/speech" + i + ".png")));
        }
        bubble=new Animation<>(0.4f, frames);
    }
    NPC(float x, float y, String filepath, String fileBackround, int scriptindex,float scale,Main log)
    {
        this(x,y,filepath,fileBackround,scriptindex,log);
        scale(scale);
    }
    NPC(NpcData data,float x, float y,Main log)
    {
        this(x,y,data.filepath,data.fileBackround,data.scriptindex,log);
        scale(data.scale);
        hitboxOffsetX= data.offsetx;
        hitboxOffsetY=data.offsety;
        if(data.width!=0){
        hitbox = new Rectangle(getX() - data.offsetx, getY() - data.offsety, data.width, data.height);}
    }

    @Override
    public void draw(Batch batch, float delta)
    {
        super.draw(batch, delta);

        if(inradius)
        {
            animationstateTime+=delta;
            //batch.setColor(0.7f,0.7f,0.7f,1);
            //batch.draw(bubble.getKeyFrame(animationstateTime,true),hitbox.x+hitbox.width-20,hitbox.y+hitbox.height-40,40,44);
        }


    }

    @Override
    public void removeFromLevel() {
        Level.npcs.remove(this);
    }

    public void drawInConversation(Batch batch)
    {
        batch.begin();
        batch.setColor(1,1,1,1);
        if(inConversation){
            batch.draw(backround, 0,0, viewport.getWorldWidth(), viewport.getWorldHeight());
            batch.draw(texture,1024/2.0f-hitbox.getWidth(), 0+576/2.0f-hitbox.getHeight(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX()*2,getScaleY()*2,getRotation());
            text.draw(batch,1);
        }
        batch.end();
    }

    public void act(float delta)
    {
        super.act(delta);
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

class NpcData
{
    String filepath, fileBackround;
    int scriptindex;
    float scale, offsetx,offsety,width, height;
    NpcData(String filepath, String fileBackround, int scriptindex, float scale, float xoffset, float yoffset, float width, float height)
    {
       this.filepath=filepath;
       this.fileBackround= fileBackround;
       this.scriptindex=scriptindex;
       this.scale=scale;
    }
    NpcData(String filepath, String fileBackround, int scriptindex, float scale)
    {
        this(filepath,fileBackround,scriptindex,scale,0,0,100,100);

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


    void interact(){
        //inConversation = true;
        onPress();
        System.out.println("Entered trader menu succesfully");
        //nextline();


    }

}
