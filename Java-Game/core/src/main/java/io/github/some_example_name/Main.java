package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.black;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    Texture backgroundTexture;
    FitViewport viewport;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Sprite bucketSprite;
    Music music;
int i=0;
    @Override
    public void create() {
        viewport = new FitViewport(8, 5);
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        image = new Texture("libgdx.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        bucketSprite = new Sprite(bucketTexture); // Initialize the sprite based on the texture
        bucketSprite.setSize(1, 1);

    }

    @Override

    public void render() {
        // organize code into three methods
        input();
        logic();
        draw();
    }


        private void input()
        {
            float speed = 1.0f;
            float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                bucketSprite.translateX(speed * delta); // Move the bucket right
            }

        }

    private void logic() {

    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }


    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        i++;
        //batch.draw(bucketTexture, (float) (i*0.02), 0, 1, 1); // draw the bucket with width/height of 1 meter
        bucketSprite.draw(batch);
        batch.end();
    }




    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
