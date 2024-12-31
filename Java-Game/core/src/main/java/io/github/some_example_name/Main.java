package io.github.some_example_name;//package com.Game2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;



public class Main implements ApplicationListener {
    Texture backgroundTexture;
    ShapeRenderer shape;
    Texture dropTexture;
    Sound dropSound;
    Music music;
    TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public static Label.LabelStyle labelStyle;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Batch batch;
    Sprite Player;
    Vector2 touchPos;
    Arrow ar;
    Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        ar = new Arrow(1,1,1,0);
        shape= new ShapeRenderer();
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        spriteBatch = new SpriteBatch();

        viewport = new FitViewport(8, 5);
        Player = new Sprite(new Texture("Al Assad.png"));
        Player.setSize(1, 2);
        touchPos = new Vector2();
        ocam=new OrthographicCamera(50,50);
        //ocam.translate(-10,0);
        dropSprites = new Array<>();
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
        map = new TmxMapLoader().load("Test Karte 2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        labelStyle = new Label.LabelStyle();
        labelStyle.font= new BitmapFont();
        music.setLooping(true);
        music.setVolume(.1f);
        music.play();



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Player.translateX(speed * delta);
            ocam.translate(speed*delta,0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Player.translateX(-speed * delta);
            ocam.translate(-speed*delta,0);
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            //Player.setCenterX(touchPos.x);
            Vector2 vec= new Vector2(touchPos.x-Player.getX()-Player.getWidth()/2,0);
            vec.setLength(Math.min(speed*delta,vec.len()));
            Player.translateX(vec.x);
            System.out.println(touchPos.x+"x "+ touchPos.y+"y ");
            ar.translateX(vec.x);
            ocam.translate(vec.x,0);
            ocam.update();
            ar.rotateTo(vec.angleDeg());
            //viewport.getCamera().rotate(new Vector3(0,0,1), (float) Math.random()*4-2);
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = Player.getWidth();
        float bucketHeight = Player.getHeight();

        Player.setX(MathUtils.clamp(Player.getX(), 0, 99999));

        float delta = Gdx.graphics.getDeltaTime();
        bucketRectangle.set(Player.getX(), Player.getY(), bucketWidth, bucketHeight);

        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);
                dropSound.play();
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) {
            dropTimer = 0;
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        shape.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        //shape.setProjectionMatrix(viewport.getCamera().combined);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        //renderer.setView(ocam);
        //renderer.render();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        spriteBatch.end();
        ar.draw(shape);
        spriteBatch.begin();
        Player.draw(spriteBatch);


        for (Sprite dropSprite : dropSprites) {
            spriteBatch.end();
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.4F, 0.4F, 0.4F, 0.30F);
            shape.rect(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);
            shape.end();
            spriteBatch.begin();
            dropSprite.draw(spriteBatch);


        }
        spriteBatch.end();
        //ar.draw(shape);

    }

    private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
