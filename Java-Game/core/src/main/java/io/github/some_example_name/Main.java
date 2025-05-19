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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

enum Gamestate { startmenu, pausemenu, playing, dead, dialouge, loading }
public class Main implements ApplicationListener {
    ArrayList<MyTile> loadedwalls = new ArrayList<>();
    ShapeRenderer shape;
    Texture dropTexture;
    Revtext revtext;
    Matrix matrix;
    Music music;
    //TiledMap map;
    static Stage uiStage;
   // private OrthogonalTiledMapRenderer renderer;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    Level currentlevel;
    int levelnummer;
    FitViewport viewport;
    static Skin skin;
    Player Player;
    Vector2 touchPos;
    SpriteButton testbutton;
    AdvancedTextButton textbutton;
    float deltaFactor=1;
    NPC dialougnpc;///kann auch alle Unterklassen von NPC speichern
    //KARLTOFFEL_BOSS El_Karltoffelboss;
    static boolean debugging=false;
    boolean DevMode=false;
    Gamestate gamestate;//siehe oben
    @Override
    public void create() {
        ocam=new OrthographicCamera(800,500);
        viewport = new FitViewport(800, 500, ocam);
        shape= new ShapeRenderer();
        spriteBatch = new SpriteBatch();


        dropTexture = new Texture("drop.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));
        skin= new Skin(Gdx.files.internal("ui/uiskin.json"));


        uiStage=new Stage(new FitViewport(800,500));


        touchPos = new Vector2();
        ocam.position.set(ocam.viewportWidth / 2f, ocam.viewportHeight / 2f, 0);
        //map = new TmxMapLoader().load("Test Karte 2.tmx");
        //renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        music.setLooping(true);
        music.setVolume(.1f); // .2f ist das selbe wie 0.2f
        music.play();
        revtext = new Revtext(400,250,1,0.1f,"Hallo das ist ein Revtext");
        matrix= new Matrix(viewport);
        //dialougnpc= new NPC(500,200,"bucket.png","own Watertile 2.png",0,this);
        shape.setAutoShapeType(true);
        //El_Karltoffelboss = new KARLTOFFEL_BOSS(0,0,this, "El_Karlotoffel" );
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        testbutton=new SpriteButton(300,300,"drop.png",1);
        testbutton.setOnUp( ()-> this.spriteBatch.setColor(1,1,1,0.6f));
        textbutton=new AdvancedTextButton("Hallo guten Tag",300,300,1, new Color(0.5f,1f,0.4f,1),new Color(1f,0.4f,0.4f,1) );
        testbutton.setOnUp( ()-> this.Player.setColor(1,1,1,1f));
        uiStage.addActor(testbutton);
        uiStage.addActor(textbutton);

        setState("startmenu");
        Gdx.input.setInputProcessor(uiStage);
        new InventoryManager().setValueByKey("Coins", 187);
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
        if(gamestate!=Gamestate.playing){return;}
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
            Level.projectiles.add(new FireBall(Player.getCenterX(),Player.getCenterY(),new Vector2(vec.x,vec.y)));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ocam.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            ocam.zoom -= 0.02f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            if(dialougnpc.inConversation)
            {
                dialougnpc.onLeave();
            }
            else{
                dialougnpc.onPress();
            }


        }

        //float effectiveViewportWidth = ocam.viewportWidth * ocam.zoom;
        //float effectiveViewportHeight = ocam.viewportHeight * ocam.zoom;


    }

    private void logic()
    {


        float delta = Gdx.graphics.getDeltaTime();
        //System.out.println(delta+" frames");
        delta= Math.min(delta,1/30.0f);
        delta=delta/deltaFactor;
        uiStage.act(delta);
        if(gamestate==Gamestate.playing)
        {
            Player.act(delta);
            //dialougnpc.act(delta);
            //El_Karltoffelboss.act(delta);
            revtext.act(delta);
            currentlevel.act(delta);

            ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);

            for (MyTile tile : currentlevel.teleporters)
            {
                if(Player.hitbox.overlaps(tile.hitbox)) //Hitbox im Player erstellen
                {
                    setState("newlevel");
                break;
                }
            }
        }
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
        //matrix.actAndDraw(spriteBatch,delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if(gamestate==Gamestate.playing)
        {
            currentlevel.draw(spriteBatch,shape,delta);
            //dialougnpc.draw(spriteBatch,delta);
            //dialougnpc.drawInConversation(spriteBatch);
            // El_Karltoffelboss.draw(spriteBatch,delta);
            Player.draw(spriteBatch,shape, delta,1.0f);
        }
        else{uiStage.draw();}

        spriteBatch.end();

    }

    void setState(String newState) {
        // jenachdem was man beim "newState" angibt
        // wird unterschiedliches ausgeführt

        switch(newState) {
            case "startmenu" :
                gamestate = Gamestate.startmenu;
                uiStage.addActor(new Startmenu(this));

                break;
            case "DevMode" :
                DevMode = true;
                uiStage.addActor(new DevMenu(this));
            case "beforeGame" :
                gamestate = Gamestate.loading;
                Player = new Player(400,250,DevMode ? 400 : 250,100, viewport);
                Player.setWorldbounds(-0,800,0,500);
                levelnummer = -1;
            case "newlevel" : //triggert den Modus um eine neues Level zu laden
                gamestate = Gamestate.loading;
                uiStage.addActor(new LoadingScreen(this));
                //Player.healthbar.setVisible(false);
                if(levelnummer + 1 < LevelList.levels.length)
                {
                    levelnummer++;
                }
                showLevel(this.levelnummer);
                gamestate=Gamestate.playing;
                break;
            case "pause" :

                gamestate = Gamestate.pausemenu;
                break;
            case "dead" :
                uiStage.addActor(new Deathscreen(this));
                //Sound.playSound(Sound.pong_d);
                gamestate = Gamestate.dead;
                break;
            case "respawn" :
                Player.sethealth(Player.maxhealth, false);
                Player.setPosition(currentlevel.xcoplayer, currentlevel.ycoplayer);
                //-->entitys zurück an ihren spawn
                //-->leben der Entitys zurück setzten und inactive machen
                gamestate = Gamestate.playing;
                break;
            case "returntogame" :
                dialougnpc.onLeave();
                gamestate = Gamestate.playing;
                break;
            case "dialouge" :
                gamestate = Gamestate.dialouge;
                dialougnpc.onPress();
                break;

        }
    }



    void showLevel(int level) {
        // aktuell sichtbares Level zerstören
        if(currentlevel != null) {
            currentlevel.destroy();
        }

        currentlevel = new Level(LevelList.levels[level],this);
        currentlevel.load();


      /*if(devmenu.onscreen) {
         devmenu.setOffscreen();
         player.hitbox.setAlpha(0);
       }
      */
        if(Player != null) {

            //player.bringToFront();
            Player.setWorldbounds(0, 0, 3189, currentlevel.getHeight());
            Player.setPosition(currentlevel.xcoplayer, currentlevel.ycoplayer);
        }

        // Nummer des Levels anzeigen:
        if(levelnummer != 0) {

        }
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
