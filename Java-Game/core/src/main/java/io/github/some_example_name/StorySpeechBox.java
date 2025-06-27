package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StorySpeechBox extends Actor {
    Revtext text;
    Texture box;
    float centerx, centery;
    StorySpeechBox( float cx, float cy,float width, float height) {
        centerx = cx;
        centery = cy;
        box = new Texture("story-boxV2.png");
        setSize(width,height);

        text= new Revtext(cx,cy,32,0.05f,"They know too much. X");

        text.setColor( Color.WHITE,Color.BLACK);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(box, centerx - getWidth() / 2, centery - getHeight() / 2,
                getWidth(), getHeight());
        text.draw(batch, parentAlpha);
    }
    @Override
    public void act(float delta) {
        text.act(delta);
        //text.setPosition(centerx - getWidth() / 2 + 20, centery - getHeight() / 2 + 20);
    }
}
