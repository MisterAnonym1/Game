package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.awt.*;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

class Revtext extends Actor {
    String maintext;
    CharSequence showtext;
    boolean linefinished;
    int toline;
    int nummer=0;
    float counter;
    float centerX;
    float centerY;
    float charDelay=0.3f;
    float lineDelay=0.1f;
    boolean waitingforLine;
    boolean centered;
    BitmapFont font;
    private GlyphLayout layout;
    Revtext(float centerx, float centery, float fontsize, float chardelay, String mainText)
    {
        font = new BitmapFont();
        //font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(fontsize);
        layout = new GlyphLayout();
        centered=true;
        charDelay=chardelay;
        maintext = mainText;
        showtext = "";
        setColor(1,1,1,1);
        lineDelay=(0.1f-chardelay)*mainText.length()+0.2f;
        setCenter(centerx,centery);
        linefinished = false;
        toFront();
        //font.setColor(Color.WHITE);
        //setBorderColor(256 * 256 * 256 + 256 * 256 + 256);
        //setBorderWid setAlpha(1);

    }
    Revtext(float leftx, float bottomy, float fontsize, String mainText)
    {
        font = new BitmapFont();
        font.getData().setScale(fontsize);
        layout = new GlyphLayout();
        centered=false;
        charDelay=0;
        maintext = mainText;
        showtext = mainText;
        //counter=9999;
        setColor(1,1,1,1);
        setPosition(leftx,bottomy);
        linefinished=true;
        lineDelay=2;
        toFront();
    }
    @Override
    public void draw(Batch sbatch,float parentalpha) {
        float x= centerX;
        float y= centerY;
        sbatch.setTransformMatrix(new Matrix4().translate(x, y, 0).rotate(0, 0, 1, getRotation()).translate(-x, -y, 0));
        font.setColor(getColor().r,getColor().g,getColor().b,parentalpha);
        font.draw(sbatch, showtext, getX(), getY());
        //sbatch.setTransformMatrix(Main.uiStage.getViewport().getCamera().combined);
        sbatch.setTransformMatrix(new Matrix4());


    }

    CharSequence reveal()
    {
        showtext = showtext.toString() + maintext.charAt(nummer);
        nummer++;
        return showtext;
    }
    void skip()
    {
        showtext=maintext;
        nummer = maintext.length();
        counter=0;
        waitingforLine=true;
    }
    void newText(String newtext)
    {
        reset();
        if(centered){
        center(newtext);}
        maintext = newtext;
        linefinished=false;
    }
    void setText(String text)
    {
        showtext=text;
        maintext=text;
        linefinished=true;
    }
    public void act(float delta)
    {
        super.act(delta);
       counter+=delta;
        if(!linefinished&&!waitingforLine&&counter >= nummer*charDelay && nummer < maintext.length())
        {
            reveal();
            //counter = 0;
        }
        if (nummer >= maintext.length())
        {
            if (!waitingforLine)
            {
             waitingforLine=true;
             counter=0;
            }
        }
        if(waitingforLine&&counter >= lineDelay )
        {
            waitingforLine=false;
            linefinished = true;
        }
    }
    void center(String text)
    {
        layout.setText(font, text);
        setX(centerX- layout.width / 2.0f);// Zentriere den Text horizontal
        //setY(centerY-layout.height / 2.0f);
        setY(centerY);
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }
    public void reset() {
        maintext="";
        showtext="";
        counter=0;
        nummer=0;
        if(charDelay!=0){
        linefinished = false;}
        waitingforLine=false;
    }

    void center()
    {
        center(maintext);
    }
   public void setCenter(float x, float y)
    {
        this.centerX = x;
        this.centerY = y;
        center();
    }



}



