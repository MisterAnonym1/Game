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
    String string="B#}][{ÆYA0Ø¢|+/ 0*+¡¢£¤¥¦¿ABCDEFB#}][{ÆYA0Ø¢|+/ 0*+¡¢£¤¥¦¿ABCDEF";
    ArrayList <VerticalRevtext> texts= new ArrayList<>();
    Revtext revtext;
    Matrix(Viewport port)
    {
        float charDelay=6f;
        float fontsize=0.7f;

        texts.add(new VerticalRevtext(0,500,fontsize,string,charDelay));
        layout = texts.get(0).layout;

        for(int i=1; i<port.getScreenWidth()/(layout.width*1.1);i++)
        {
            mixString();
            texts.add(new VerticalRevtext(i*layout.width*1.1f,500,fontsize,string,charDelay));
            texts.get(texts.size()-1).setNummer(MathUtils.random(0,string.length()));
            //texts.get(texts.size()-1).setNummer(i);
            texts.get(texts.size()-1).counter+=Math.random()*charDelay;



        }
        //revtext=new Revtext(0,350,1.4f,0,"hallo");
    }
    void mixString()
    {
        //int end=MathUtils.random(0,string.length()-2);
        int end = (int) Math.round(Math.random()*(string.length()-2));
        String s1=string.substring(Math.min(end+1,string.length()-1),string.length());
        String s2=string.substring(0,end+1);
        string=  s1+ s2;


    }
    public void actAndDraw( SpriteBatch batch, float delta) {
        //String str="";
        for (VerticalRevtext rev:texts)
        {
            rev.act(delta);
            rev.draw(batch);
            //str+=rev.nummer+" ";
        }

        /*
        revtext.setMaintext(str);
        revtext.skip();
        revtext.act(delta);
        revtext.draw(batch);
        */
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
        float factor= counter%charDelay;
        factor= factor/charDelay; //diese Variable zeigt von 0 bis 1 wie lange bis der nächste Buchstabe auftaucht

        int charsShown=6;
        for(int i=0;i<=charsShown; i++)
        {

            int x=(nummer+i)%maintext.length();
            //font.setColor((i-factor*(charsShown))/3f,(i-factor)/1.f,(i-factor*(charsShown))/3f, 1/*((factor+1f)*i/5)*/);
            font.setColor((i-factor-charsShown+1.5f)*1.2f,(i-factor)/4f,(i-factor-charsShown+1.5f)*1.2f, 1/*((factor+1f)*i/5)*/);
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
                setNummer(nummer);
            }

        }

    }

    public void setNummer(int nummer) {
        this.nummer = nummer%maintext.length();
        counter=(this.nummer-1)*charDelay;
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
