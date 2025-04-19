package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Matrix {
    GlyphLayout layout;
    BitmapFont font;
    String string="B#}][{ÆYA0Ø¢|+/ 0*+¡¢£¤¥¦¿";
    ArrayList <VerticalRevtext> texts= new ArrayList<>();
    Matrix(Viewport port)
    {
        texts.add(new VerticalRevtext(0,500,1,string,0.1f));
        layout = texts.get(0).layout;
        for(int i=1; i<port.getScreenWidth()/(layout.width*1.1);i++)
        {
            mixString();
            texts.add(new VerticalRevtext(i*layout.width*1.1f,500,1,string,0.1f));
            texts.get(texts.size()-1).nummer=MathUtils.random(0,string.length());
        }
    }
    void mixString()
    {
        //int end=MathUtils.random(0,string.length()-2);
        int end = (int) Math.round(Math.random()*(string.length()-2));
        System.out.println(end+"end \n");
        System.out.println(string.length()+"length \n");
        String s1=string.substring(Math.min(end+1,string.length()-1),string.length());
        String s2=string.substring(0,end+1);
        string=  s1+ s2;
        System.out.println(string);

    }
    public void actAndDraw( SpriteBatch batch, float delta) {
        for (VerticalRevtext rev:texts)
        {
            rev.act(delta);
            rev.draw(batch);
        }

    }
}

class VerticalRevtext extends Sprite
{
    String maintext="B#}][{ÆYA0Ø¢|+/ \\¡¢£¤¥¦¿";
    int nummer=0;
    float counter;
    float centerX;
    float centerY;
    float charDelay=0.3f;

    BitmapFont font;
     GlyphLayout layout;
    VerticalRevtext(float centerx, float centery, float fontsize, String mainText, float chardelay)
    {

        font = new BitmapFont();

        font.getData().setScale(fontsize);
        layout = new GlyphLayout();

        charDelay=chardelay;
        maintext = mainText;
        setCenter(centerx,centery);

        //font.setColor(Color.WHITE);
        //setBorderColor(256 * 256 * 256 + 256 * 256 + 256);
        //setBorderWid setAlpha(1);

    }
    void draw(SpriteBatch sbatch)
    {


        for(int i=0;i<5; i++)
        {
            //i=nummer%maintext.length();
            int x=(nummer+i)%maintext.length();
            font.draw(sbatch, ""+maintext.charAt(x), getX(), getY()-x*1.5f*(font.getCapHeight()));
        }
    }
    void newText(String newtext)
    {
        center(newtext);
        counter=0;
        nummer = 0;
        maintext = newtext;
    }
    public void act(float delta)
    {
        counter+=delta;
        if(counter >= nummer*charDelay)
        {
            nummer++;
            if(nummer>=maintext.length())
            {
                nummer=nummer%maintext.length();
                counter=0;
            }

        }

    }
    void center(String text)
    {
        layout.setText(font, "X");
        setX(centerX- layout.width / 2.0f);// Zentriere den Text horizontal
        setY(centerY);
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
