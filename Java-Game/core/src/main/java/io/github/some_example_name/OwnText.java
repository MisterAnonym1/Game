package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OwnText extends Label {
    float scale;
    GlyphLayout layout;



    public OwnText(String text, float x, float y, float scale, Color color) {
        this(text, x, y, scale, color,  true);
    }

    public OwnText(String text, float x, float y, float scale, Color color, String style,boolean centered) {
        super(text, new Label.LabelStyle(getSkin().getFont(style), color));

        this.scale=scale;
        layout = new GlyphLayout();
        setX(x);
        setY(y);
        this.setFontScale(scale);

        if(centered) center();
    }
    public OwnText(String text, float x, float y, float scale, Color color, String style) {
       this(text,x,y,scale,color,style,true);
    }
    public OwnText(String text, float x, float y, float scale, Color color,boolean centered) {
        this(text,x,y,scale,color,"font",centered);
    }

    public OwnText(String text, float x, float y, Color color) {
        this(text,x,y,1,color);
    }



    private static Skin getSkin() {
        return Main.skin;
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
    public void setScale(float scale)
    {
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




    
    public void draw(Batch batch) {
        draw(batch,1);
    }

}




class PopUpText extends OwnText {
    public PopUpText(String text, float x, float y, float scale, Color color) {
        super(text, x, y, scale, color);
        addAction(Actions.fadeIn(0.2f));
        addAction(Actions.sequence(
                Actions.moveBy(0, 50, 0.25f,Interpolation.fastSlow),// Bewege den Text nach oben
                Actions.moveBy(0,-80,0.5f, Interpolation.slowFast)

        ));
        // Entferne den Text nach der Animation
        addAction(new SequenceAction(Actions.scaleBy(0.3f, 0.3f, 0.4f),
                Actions.fadeOut(0.3f),
                Actions.run(this::remove)
        ));
    }

    public PopUpText(String text, float x, float y) {
        this(text, x, y, 1, Color.WHITE);
    }

    @Override
    public void draw(Batch batch, float outlinealpha) {
        super.draw(batch, outlinealpha);
    }
}

class Displaytext extends OwnText
{
    String key;
    Displaytext(String text, float leftx, float y, float scale, Color color,boolean centered,String invkey)
    {
        super(text,leftx,y,scale,color,centered);
        key=invkey;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(key+": "+Main.invManager.getValueByKey(key));
    }
}
