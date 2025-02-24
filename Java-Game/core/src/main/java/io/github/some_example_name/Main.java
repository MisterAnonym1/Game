package io.github.some_example_name;//package com.Game2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;



public class Main implements ApplicationListener {
    Texture backgroundTexture;
    ShapeRenderer shape;
    Texture dropTexture;

    Music music;
    TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public static Label.LabelStyle labelStyle;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Batch batch;
    Player Player;
    Stage entityStage;
    Vector2 touchPos;
    Testentity werther;
    Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");

        shape= new ShapeRenderer();
        dropTexture = new Texture("drop.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));
        spriteBatch = new SpriteBatch();

        viewport = new FitViewport(800, 500);
        entityStage= new Stage(viewport,spriteBatch);
        //entityStage= new Stage();
        Player = new Player(3,4,300,100, viewport);
        //Player.setSize(1, 2);
        Player.setWorldbounds(0,800,0,500);
        touchPos = new Vector2();
        werther= new Testentity(200,200,this);
        entityStage.addActor(werther);
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
        music.setVolume(.2f);
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            entityStage.addActor(new FireBall(Player.getX(),Player.getY(),new Vector2(Player.movement.x,Player.movement.y)));
        }
        /*if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Player.translateX(speed * delta);
            ocam.translate(speed*delta,0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Player.translateX(-speed * delta);
            ocam.translate(-speed*delta,0);
        }*/

        /*if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            //Player.setCenterX(touchPos.x);
            Vector2 vec= new Vector2(touchPos.x-Player.getX()-Player.getWidth()/2,0);
            vec.setLength(Math.min(speed*delta,vec.len()));

            Player.moveBy(vec.x,vec.y);
            System.out.println(touchPos.x+"x "+ touchPos.y+"y ");

            ocam.translate(vec.x,0);
            ocam.update();

            //viewport.getCamera().rotate(new Vector3(0,0,1), (float) Math.random()*4-2);
        }*/
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = Player.getWidth();
        float bucketHeight = Player.getHeight();

        float delta = Gdx.graphics.getDeltaTime();
        System.out.println(delta+" frames");
        delta=Math.min(delta,1/30.0f);
        delta=(float)(delta/1.0);

        Player.act(delta);

        //werther.addAction(Actions.rotateBy(0.1f));
        entityStage.act(delta);
        Player.stayinWorldbounds();
        //System.out.println(Gdx.input.getX()+"x "+ Gdx.input.getY()+"y ");

        bucketRectangle.set(Player.getX(), Player.getY(), bucketWidth, bucketHeight);

        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-200f * delta);
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);

            }
        }

        dropTimer += delta;
        if (dropTimer > 10f) {
            dropTimer = 0;
            //createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        System.out.println(Color.BLACK.a);
        viewport.apply();
        shape.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        //shape.setProjectionMatrix(viewport.getCamera().combined);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        //renderer.setView(ocam);
        //renderer.render();
        spriteBatch.setColor(1,1,1,1);
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);


        for (Sprite dropSprite : dropSprites) {
            spriteBatch.end();
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            spriteBatch.begin();
            dropSprite.draw(spriteBatch);


        }
        spriteBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        //shape.rect(Player.worldbounds.getX(),Player.worldbounds.getY(),Player.worldbounds.getWidth(),Player.worldbounds.getHeight());
        Player.drawHitbox(shape);
        werther.drawHitbox(shape);
        shape.end();

        entityStage.draw();

        spriteBatch.begin();
        //werther.draw(spriteBatch,0.4f);
        Player.draw(spriteBatch,shape,1f);



        spriteBatch.end();
        //ar.draw(shape);

    }

    private void createDroplet() {
        float dropWidth = 100f;
        float dropHeight = 100;
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
