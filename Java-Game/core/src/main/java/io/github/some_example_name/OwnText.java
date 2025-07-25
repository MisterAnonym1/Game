package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class OwnText extends Actor {
     BitmapFont font;
    String text;
   Color outlineColor;
    GlyphLayout layout;
    int size;
    static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MountainKing-font.ttf"));
    static FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    public OwnText(String text, float x, float y, int size, Color color, Color outlineColor) {
        this(text, x, y, size, color, outlineColor, true);
    }
    public OwnText(String text, float leftx, float y, int size, Color color, Color outlineColor,boolean centered) {;
        parameter.size = size; // Schriftgröße
        this.size=size;
        font = generator.generateFont(parameter);
        //generator.dispose();
        //this.font = new BitmapFont(); // Standard-Schriftart ohne TTF-Import
        this.text = text;
        layout = new GlyphLayout();
        setX(leftx);
        setY(y);
        setColor(color);
        this.outlineColor = outlineColor;

        //font.getData().setScale(scale); // Skalierung festlegen
        //setScale(scale);
        if(centered)
            center();

    }
    public OwnText(String text, float x, float y) {
        this(text,x,y,20,Color.WHITE,null);
    }
    public OwnText(String text, float x, float y, Color color) {
        this(text,x,y,20,color,null);
    }



    public void setText(String text) {
        this.text = text;
    }

    void center(String text)
    {
        this.text=text;
        layout.setText(font, text);
        setX(getX()- layout.width/**getScaleX()*/ / 2.0f);// Zentriere den Text horizontal
        //setY(centerY-layout.height / 2.0f);
        setY(getY());
    }
    void center()
    {
        center(text);
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        //font.getData().setScale(scale);
    }

    @Override
    public void scaleBy(float scale) {
        super.scaleBy(scale);

    }

    @Override
    protected void scaleChanged() {
        super.scaleChanged();
        font.getData().setScale(getScaleX());
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
    public void setColor(Color color) {
        super.setColor(color);
        font.setColor(color);
    }

    public void setColor(Color color,Color outlinecolor) {
        super.setColor(color);
        font.setColor(color);
        setOutlineColor(outlinecolor);
    }

    @Override
    public void draw(Batch batch,float outlinealpha) {
        if(outlineColor!=null) {
            // Umrandung zeichnen (leicht versetzter Text)
            float offset = 1.4f+size/33; // Randdicke
            font.setColor(outlineColor.r, outlineColor.g, outlineColor.b, getColor().a * outlinealpha);

            font.draw(batch, text, getX() - offset, getY());
            font.draw(batch, text, getX() + offset, getY());
            font.draw(batch, text, getX(), getY() - offset);
            font.draw(batch, text, getX(), getY() + offset);

            /*font.draw(batch, text, getX() + offset, getY()- offset);
            font.draw(batch, text, getX() - offset, getY()+ offset);
            font.draw(batch, text, getX()- offset, getY() - offset);
            font.draw(batch, text, getX()+ offset, getY() + offset);*/
        }
        // Originalfarbe des Textes
        font.setColor(getColor());
        font.draw(batch, text, getX(), getY());
    }
    public void draw(Batch batch,float outlinealpha, float alpha) {
        setColor(getColor().r, getColor().g, getColor().b, alpha);
        draw(batch,outlinealpha);
    }

    public void dispose() {
        font.dispose();
    }
}


 /*class CustomText {
    private BitmapFont font;
    private String text;
    private float x, y;
    private float scale;
    private Color color;

     public CustomText(String fontPath, String text, float x, float y, float scale, Color color) {
        // Lade eine TTF-Schriftart für saubere Skalierung
        FontGenerator generator = new FontGenerator(Gdx.files.internal(fontPath));
        FontParameter parameter = new FontParameter();
        parameter.size = (int) (32 * scale); // Skalierbare Größe
        font = generator.generateFont(parameter);
        generator.dispose();

        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(float scale) {
        this.scale = scale;
        font.getData().setScale(scale);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(SpriteBatch batch) {
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}*/

class PopUpText extends OwnText {
    public PopUpText(String text, float x, float y, int size, Color color, Color outlineColor) {
        super(text, x, y, size, color, outlineColor);
        final PopUpText thistext =this;
        addAction(Actions.fadeIn(0.2f));
        addAction(Actions.sequence(
                Actions.moveBy(0, 50, 0.25f,Interpolation.fastSlow),// Bewege den Text nach oben
                Actions.moveBy(0,-80,0.5f, Interpolation.slowFast)

        ));
        addAction(new SequenceAction(Actions.scaleBy(0.3f, 0.3f, 0.4f),
                Actions.fadeOut(0.3f),
                Actions.run(() -> {
                  remove(); // Entferne den Text nach der Animation
                })
        ));
    }

    public PopUpText(String text, float x, float y) {
        this(text, x, y, 20, Color.WHITE, Color.BLACK);
    }

    @Override
    public void draw(Batch batch, float outlinealpha) {
        super.draw(batch, outlinealpha);
    }
}

class Displaytext extends OwnText
{
    String key;
    Displaytext(String text, float leftx, float y, int size, Color color, Color outlineColor,boolean centered,String invkey)
    {
        super(text,leftx,y,size,color,outlineColor,centered);
        key=invkey;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(key+": "+Main.invManager.getValueByKey(key));
    }
}
