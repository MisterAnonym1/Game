package io.github.some_example_name;//package com.Game2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Main implements ApplicationListener {
    Texture backgroundTexture;
    ShapeRenderer shape;
    Texture dropTexture;
    Revtext revtext;
    Matrix matrix;
    Music music;
    TiledMap map;
    Stage uiStage;
    private OrthogonalTiledMapRenderer renderer;
    public static Label.LabelStyle labelStyle;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    Level currentlevel;
    int levelzahl;
    FitViewport viewport;
    Batch batch;
    Player Player;
    Vector2 touchPos;
    Testentity werther;
    Array<Sprite> dropSprites;
    SpriteButton testbutton;
    float dropTimer;
    float deltaFactor=1;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    NPC currentNPC;
    KARLTOFFEL_BOSS El_Karltoffelboss;
    static boolean debugging=false;
    @Override
    public void create() {
        ocam=new OrthographicCamera(800,500);
        viewport = new FitViewport(800, 500, ocam);
        shape= new ShapeRenderer();
        spriteBatch = new SpriteBatch();


        backgroundTexture = new Texture("background.png");
        dropTexture = new Texture("drop.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));



        currentlevel = new Level(LevelList.levels[0], this);
        uiStage=new Stage(new FitViewport(800,500));
        Player = new Player(400,250,300,100, viewport, uiStage.getViewport());
        Player.setWorldbounds(-0,800,0,500);

        touchPos = new Vector2();
        werther= new Testentity(200,200,this);
        ocam.position.set(ocam.viewportWidth / 2f, ocam.viewportHeight / 2f, 0);
        bucketRectangle = new Rectangle();
        map = new TmxMapLoader().load("Test Karte 2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        labelStyle = new Label.LabelStyle();
        labelStyle.font= new BitmapFont();
        music.setLooping(true);
        music.setVolume(.2f); // .2f ist das selbe wie 0.2f
        music.play();
        revtext = new Revtext(400,250,1,0.1f,"Hallo das ist ein Revtext");
        matrix= new Matrix(viewport);
        currentNPC= new NPC(500,200,"bucket.png","own Watertile 2.png",0,this);
        shape.setAutoShapeType(true);
        El_Karltoffelboss = new KARLTOFFEL_BOSS(0,0,this, "El_Karlotoffel" );
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        testbutton=new SpriteButton(300,300,"drop.png",1);
        uiStage.addActor(testbutton);
        currentlevel.load();
        Gdx.input.setInputProcessor(uiStage);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiStage.getViewport().update(width, height, true);
        ocam.update();
        //System.out.println(width+"w "+ height +"h\n");
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
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) ) {
                deltaFactor*=1+delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) ) {
                deltaFactor/=1+delta;

            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.B) ) {
               debugging = !debugging;

            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
             Vector2 vec= new Vector2(1,1);
            vec.setAngleDeg(Player.directionline);
            currentlevel.projectiles.add(new FireBall(Player.getCenterX(),Player.getCenterY(),new Vector2(vec.x,vec.y)));
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
        delta=delta/deltaFactor;

        Player.act(delta);
        currentNPC.act(delta);
        El_Karltoffelboss.act(delta);
        revtext.act(delta);
        currentlevel.act(delta);
        uiStage.act(delta);
        ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);

        //System.out.println(Gdx.input.getX()+"x "+ Gdx.input.getY()+"y ");
        }






    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        delta= Math.min(delta,1/30.0f);
        delta=delta/deltaFactor;

        ScreenUtils.clear(Color.BLACK);

        ocam.update();
        viewport.apply();
        //viewport.update((int)ocam.viewportWidth,(int)ocam.viewportHeight);

        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = Gdx.graphics.getWidth();
        float worldHeight = Gdx.graphics.getHeight();
        spriteBatch.setColor(1,1,1,1);



        //+spriteBatch.draw(backgroundTexture, viewport.getScreenX(), viewport.getScreenY(), worldWidth, worldHeight);
        matrix.actAndDraw(spriteBatch,delta);
        currentlevel.draw(spriteBatch,shape,delta);
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

        spriteBatch.begin();


        //werther.draw(spriteBatch,delta);
        Player.draw(spriteBatch,shape, delta,1.0f);
        revtext.draw(spriteBatch);
       currentNPC.draw(spriteBatch,delta);
        currentNPC.drawInConversation(spriteBatch);
        El_Karltoffelboss.draw(spriteBatch,delta);
        uiStage.draw();
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
