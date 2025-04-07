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

import static java.lang.Math.*;


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
    Level currentlevel;
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
    Polygon polygon1;
    Polygon polygon2;
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
        entityStage.addActor(new Schlange(this,0,0));
        shape.setAutoShapeType(true);
        float[] vertices1 = {0, 0, 50, 0, 50, 50,0,50}; // Ein Quadrat
        float[] vertices2 = {25, 25, 75, 25, 75, 75, 60, 75, 50 ,100}; // Ein weiteres Quadrat

         polygon1 = new Polygon(vertices1);
         polygon2 = new Polygon(vertices2);

        // Prüfe auf Überschneidung
        boolean overlaps = Intersector.overlapConvexPolygons(polygon1, polygon2);
        viewport.getCamera().translate(100,10000,-1000);
        if (overlaps) {
            System.out.println("Die Polygone überschneiden sich.");
        } else {
            System.out.println("Die Polygone überschneiden sich nicht.");
        }

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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            //System.out.println("space wird gedrückt");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
             Vector2 vec= new Vector2(1,1);
            vec.setAngleDeg(Player.directionline);
            FireBall ball=new FireBall(Player.getCenterX(),Player.getCenterY(),new Vector2(vec.x,vec.y));
            ball.centerAt(Player);
            entityStage.addActor(ball);
        }

    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = Player.getWidth();
        float bucketHeight = Player.getHeight();

        float delta = Gdx.graphics.getDeltaTime();
        //System.out.println(delta+" frames");
        delta= Math.min(delta,1/30.0f);
        delta=delta/1.0f;

        Player.act(delta);

        //werther.addAction(Actions.rotateBy(0.1f));
        entityStage.act(delta);

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


        viewport.apply();
        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
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
        Gdx.gl.glLineWidth(5);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1,1,1,0.8f);

        //Player.drawHitbox(shape);
        //werther.drawHitbox(shape);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.polygon(polygon2.getVertices());
        shape.polygon(polygon1.getVertices());
        shape.end();



        spriteBatch.begin();
        BitmapFont font;
        CharSequence str = "Hello World!";
        font = new BitmapFont();
        font.getData().setScale(2.0f);
        font.draw(spriteBatch, str, 100, 300);

        werther.draw(spriteBatch,0.4f);
        Player.draw(spriteBatch,shape,1.0f);
        spriteBatch.end();
        entityStage.draw();
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
        System.out.println("Game paused");
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
