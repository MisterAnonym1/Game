package io.github.some_example_name;

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
        //this.text.setSize(0.5f);
        //this.text.setPosition(x + 50, y + 50);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(box, centerx - getWidth() / 2, centery - getHeight() / 2,
                getWidth(), getHeight());
    }
}
