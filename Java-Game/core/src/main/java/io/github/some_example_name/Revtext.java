package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.*;
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

class Revtext extends Sprite {
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
    BitmapFont font;
    private GlyphLayout layout;
    Revtext(float centerx, float centery, /*float fontsize,*/ float chardelay, String mainText)
    {
        //super(centerx, centery, fontsize, "");
        font = new BitmapFont();
        font.getData().setScale(4);
        layout = new GlyphLayout();

        charDelay=chardelay;
        maintext = mainText;
        showtext = "";

        lineDelay=(0.1f-chardelay)*mainText.length()+0.2f;
        setCenter(centerx,centery);
        linefinished = false;

        //font.setColor(Color.WHITE);
        //setBorderColor(256 * 256 * 256 + 256 * 256 + 256);
        //setBorderWid setAlpha(1);

    }
    @Override
    public void draw(Batch sbatch) {

        font.draw(sbatch, showtext, getX(), getY());
    }
    void setVisible(boolean visible) {
        setAlpha(visible?1:0);
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
        center(newtext);
        maintext = newtext;
    }
    public void act(float delta)
    {
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
        setY(centerY-layout.height / 2.0f);
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }
    public void reset() {
        maintext="";
        showtext="";
        counter=0;
        nummer=0;
        linefinished = false;
        waitingforLine=false;
    }

    void center()
    {
        center(maintext);
    }
    @Override
   public void setCenter(float x, float y)
    {
        this.centerX = x;
        this.centerY = y;
        center();
    }


}



