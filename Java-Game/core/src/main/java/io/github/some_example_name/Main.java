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
    Revtext revtext;
    Matrix matrix;
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
    NPC currentNPC;
    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        shape= new ShapeRenderer();
        dropTexture = new Texture("drop.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));
        spriteBatch = new SpriteBatch();

        ocam=new OrthographicCamera(800,500);
        viewport = new FitViewport(800, 500, ocam);
        entityStage= new Stage(viewport,spriteBatch);
        //entityStage= new Stage();
        Player = new Player(400,250,1300,100, viewport);
        Player.setWorldbounds(-0,800,0,500);
        touchPos = new Vector2();
        werther= new Testentity(200,200,this);
        entityStage.addActor(werther);
        ocam.position.set(ocam.viewportWidth / 2f, ocam.viewportHeight / 2f, 0);
        bucketRectangle = new Rectangle();
        map = new TmxMapLoader().load("Test Karte 2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        labelStyle = new Label.LabelStyle();
        labelStyle.font= new BitmapFont();
        music.setLooping(true);
        music.setVolume(.2f); // .2f == 0.2f
        music.play();
        revtext = new Revtext(400,250,0.1f,"Hallo das ist ein Revtext");
        matrix= new Matrix(viewport);
        currentNPC= new NPC(500,200,"bucket.png","own Watertile 2.png",0,this);
        entityStage.addActor(new Schlange(this,0,0));
        shape.setAutoShapeType(true);
        // Prüfe auf Überschneidung
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ocam.update();
        System.out.println(width+"w "+ height +"h\n");
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
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ocam.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            ocam.zoom -= 0.02f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            if(currentNPC.inConversation)
            {
                currentNPC.onLeave();
            }
            else{
                currentNPC.onPress();
            }


        }

        float effectiveViewportWidth = ocam.viewportWidth ;
        float effectiveViewportHeight = ocam.viewportHeight;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            ocam.rotate(-0.5f, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            ocam.rotate(0.5f, 0, 0, 1);
            viewport.update((int)effectiveViewportWidth, (int)effectiveViewportHeight);
        }


        //float effectiveViewportWidth = ocam.viewportWidth * ocam.zoom;
        //float effectiveViewportHeight = ocam.viewportHeight * ocam.zoom;


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
        currentNPC.act(delta);
        //werther.addAction(Actions.rotateBy(0.1f));
        entityStage.act(delta);
        revtext.act(delta);
        ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);
        //System.out.println(Gdx.input.getX()+"x "+ Gdx.input.getY()+"y ");
        }






    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        //System.out.println(delta+" frames");
        delta= Math.min(delta,1/30.0f);
        delta=delta/1.0f;

        ScreenUtils.clear(Color.BLACK);

        ocam.update();
        viewport.apply();
        //viewport.update((int)ocam.viewportWidth,(int)ocam.viewportHeight);

        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = Gdx.graphics.getWidth();
        float worldHeight = Gdx.graphics.getHeight();
        //renderer.setView(ocam);
        //renderer.render();
        spriteBatch.setColor(1,1,1,1);
        spriteBatch.draw(backgroundTexture, viewport.getScreenX(), viewport.getScreenY(), worldWidth, worldHeight);
        matrix.actAndDraw(spriteBatch,delta);
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


        shape.end();


        entityStage.draw();
        spriteBatch.begin();


        werther.draw(spriteBatch,0.4f);
        Player.draw(spriteBatch,shape,1.0f);
        revtext.draw(spriteBatch);
        currentNPC.draw(spriteBatch,1);
        currentNPC.drawInConversation(spriteBatch,1f);
         spriteBatch.end();

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
