package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SmartNPC extends Gegner {
    SmartNPC(float x, float y, Main main)
    {
        super(x,y,main, new TextureRegion(new Texture("HappySheep_All.png"),0,0,50,40));

        defaultAnimation= new Animation<TextureRegion>(0.3f,texture.split(texture.getRegionWidth() / 8, texture.getRegionWidth() / 2)[0]);
    }

    @Override
    boolean update(float delta) {
        return false;
    }



    @Override
    public void engagePlayer(float delta) {

    }

    @Override
    void sterben() {
    destroy();
    }

}
