package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OwnText extends Label {
    float scale=1;
    GlyphLayout layout;

    // Static skin for efficient resource management
    private static Skin pixthulhuSkin;


    public OwnText(String text, float x, float y, float scale, Color color, Color outlineColor) {
        this(text, x, y, scale, color, outlineColor, true);
    }
    public OwnText(String text, float leftx, float y, float scale, Color color, Color outlineColor,boolean centered) {
        super(text, new Label.LabelStyle(getSkin().getFont("font"), color));

        this.scale=scale;
        layout = new GlyphLayout();
        setX(leftx);
        setY(y);
        this.setFontScale(scale);

        if(centered)
            center();
    }
    public OwnText(String text, float x, float y) {
        this(text,x,y,20,Color.WHITE,null);
    }
    public OwnText(String text, float x, float y, Color color) {
        this(text,x,y,20,color,null);
    }


    public static void initializeSkin() {
        if (pixthulhuSkin == null) {
            pixthulhuSkin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        }
    }
    private static Skin getSkin() {
        initializeSkin();
        return pixthulhuSkin;
    }



    public void setText(String text) {
        super.setText(text);
    }

    void center(String text)
    {
        setText(text);
        layout.setText(getStyle().font, text);
        setX(getX() - (layout.width / 2.0f)*scale);
        setY(getY()-(layout.height )*scale);
    }
    void center()
    {
        center(getText().toString());
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        this.setFontScale(scale);
    }

    @Override
    public void scaleBy(float scale) {
        super.scaleBy(scale);
        this.setFontScale(getFontScaleX() + scale);
    }

    @Override
    protected void scaleChanged() {
        super.scaleChanged();
        this.setFontScale(getScaleX());
    }


    
    @Override
    public void setColor(Color color) {
        super.setColor(color);
        getStyle().fontColor = color;
    }

    public void setColor(Color color, Color outlinecolor) {
        setColor(color);

    }


    
    public void draw(Batch batch) {
        draw(batch,1);
    }
    
    public void draw(Batch batch,  float alpha) {
       // setColor(getColor().r, getColor().g, getColor().b,getColor().a);
        super.draw(batch,alpha );
    }

    public void dispose() {
        // Label doesn't need explicit disposal of font as it's managed by the skin
    }

    // Static cleanup method - call this when shutting down the game
    public static void disposeStatic() {
        if (pixthulhuSkin != null) {
            pixthulhuSkin.dispose();
            pixthulhuSkin = null;
        }
    }
}




class PopUpText extends OwnText {
    public PopUpText(String text, float x, float y, float scale, Color color, Color outlineColor) {
        super(text, x, y, scale, color, outlineColor);
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
        this(text, x, y, 1, Color.WHITE, Color.BLACK);
    }

    @Override
    public void draw(Batch batch, float outlinealpha) {
        super.draw(batch, outlinealpha);
    }
}

class Displaytext extends OwnText
{
    String key;
    Displaytext(String text, float leftx, float y, float scale, Color color, Color outlineColor,boolean centered,String invkey)
    {
        super(text,leftx,y,scale,color,outlineColor,centered);
        key=invkey;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(key+": "+Main.invManager.getValueByKey(key));
    }
}
