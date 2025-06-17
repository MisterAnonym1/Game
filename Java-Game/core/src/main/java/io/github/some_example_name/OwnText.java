package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.graphics.g2d.FontGenerator;
//import com.badlogic.gdx.graphics.g2d.FontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class OwnText extends Actor {
    private BitmapFont font;
    String text;
    private Color outlineColor;
    private GlyphLayout layout;
    public OwnText(String text, float x, float y, float scale, Color color, Color outlineColor) {
        this.font = new BitmapFont(); // Standard-Schriftart ohne TTF-Import
        this.text = text;
        layout = new GlyphLayout();
        setX(x);
        setY(y);
        setScale(scale);
        setColor(color);
        this.outlineColor = outlineColor;
        //font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(scale); // Skalierung festlegen
    }
    public OwnText(String text, float x, float y) {
        this(text,x,y,1,Color.WHITE,null);
    }
    public OwnText(String text, float x, float y, Color color) {
        this(text,x,y,1,color,null);
    }



    public void setText(String text) {
        this.text = text;
    }

    void center(String text)
    {
        layout.setText(font, text);
        setX(getX()- layout.width / 2.0f);// Zentriere den Text horizontal
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
        font.getData().setScale(scale);
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

    @Override
    public void draw(Batch batch,float outlinealpha) {
        float offset = 1.5f*getScaleX(); // Randdicke

        // Umrandung zeichnen (leicht versetzter Text)

        if(outlineColor==null){return;}
        font.setColor(outlineColor.r,outlineColor.g,outlineColor.b,getColor().a*outlinealpha);
        font.draw(batch, text, getX() - offset, getY());
        font.draw(batch, text, getX() + offset, getY());
        font.draw(batch, text, getX(), getY() - offset);
        font.draw(batch, text, getX(), getY() + offset);

        // Originalfarbe des Textes
        font.setColor(getColor());
        font.draw(batch, text, getX(), getY());
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

