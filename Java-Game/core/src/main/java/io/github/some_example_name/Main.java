package io.github.some_example_name;//package com.Game2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;

enum Gamestate { startmenu, paused, playing, dead, dialouge, loading }
public class Main implements ApplicationListener {
    ArrayList<MyTile> loadedwalls = new ArrayList<>();
    ShapeRenderer shape;
    Music music;
    InputMultiplexer multiplexer; //für mehrere InputVerarbeiter
    boolean inPopUpWindow=false;
    //TiledMap map;
    static Cursor bettercursor;
    String predeterminedDeathmessage= "you died of dumb";
    static Stage uiStage;
    float cameraoffsetx=0, cameraoffsety=0;
   // private OrthogonalTiledMapRenderer renderer;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    Level currentlevel;
    DataCenter dataCenter;
    int levelnummer;
    static InventoryManager invManager;
    FitViewport viewport;
    static Skin skin;
    Player Player;
    float deltaFactor=1;
    NPC dialougnpc;///kann auch alle Unterklassen von NPC speichern
    static boolean debugging=false;
   static boolean DevMode=false;
    Gamestate gamestate;//siehe oben
    @Override
    public void create() {
        ocam=new OrthographicCamera(1024,576);
        viewport = new FitViewport(1024,576, ocam);
        shape= new ShapeRenderer();
        spriteBatch = new SpriteBatch();


        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));
        skin= new Skin(Gdx.files.internal("ui/uiskin.json"));
        uiStage=new Stage(new FitViewport(1024,576));

        Pixmap pixmap = new Pixmap(Gdx.files.internal("Pointers/Pointer.png"));
        // Set hotspot to the middle of it (0,0 would be the top-left corner)
        int xHotspot = 4, yHotspot = 4;
        bettercursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose(); // We don't need the pixmap anymore
        invManager = new InventoryManager();
       setToDefaultCursor();

        Player = new Player(400,-250, viewport);

        ocam.position.set(ocam.viewportWidth / 2f, ocam.viewportHeight / 2f, 0);
        //map = new TmxMapLoader().load("Test Karte 2.tmx");
        //renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        music.setLooping(true);
        music.setVolume(.17f); // .2f ist das selbe wie 0.2f
        music.play();
        loadSounds();
        loadTilesets();
        dataCenter=new DataCenter(this);
        shape.setAutoShapeType(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        setState("startmenu");
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); // deine UI
        Gdx.input.setInputProcessor(multiplexer);

        //Gdx.input.setInputProcessor(uiStage);
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
        float delta = Gdx.graphics.getDeltaTime();
        if(gamestate!=Gamestate.dialouge&&gamestate!=Gamestate.loading&& Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Menu.showQuitConfirmation(this);
        }


        if(DevMode)
        {

            //Wurde ins DevMenu verlegt
        }


    }

    private void logic()
    {

        float delta = Gdx.graphics.getDeltaTime();
        uiStage.act(delta);
        delta= Math.min(delta,1/30.0f);
        delta=delta*deltaFactor;
        if(gamestate!=Gamestate.loading&&gamestate!=Gamestate.paused&&gamestate!=Gamestate.startmenu){DataCenter.updateTimeplayed();}
      if(gamestate == Gamestate.playing)
      {
         updatewalls();
         Player.act(delta==0?Gdx.graphics.getDeltaTime():delta);
          //Player.stayinWorldbounds();

         currentlevel.act(delta);
         updateEntitys(delta);

         checkplayercollision();
         Player.stayinWorldbounds();
          //ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);
          /*if( new Vector2( ocam.position.x - Player.getCenterX(),ocam.position.y - Player.getCenterY()).len()>=99999 )
          {
              ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);
          }
          else
          {*/
          ocam.position.x=MathUtils.clamp(Player.getCenterX(), 0+517, currentlevel.getLength()*64-517)+cameraoffsetx;
          ocam.position.y=MathUtils.clamp(Player.getCenterY(), (-currentlevel.getHeight()+1)*64+288,64-288 )+ cameraoffsety;

          if(Player.curhealth <= 0) {
            setState("dead");
         }

      }

      if(gamestate == Gamestate.dialouge) {
         if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
         {
            setState("returntogame");
         }
      }

      //----
    }






    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        delta= Math.min(delta,1/30.0f);
        delta=delta*deltaFactor;

        if(gamestate==Gamestate.loading){ScreenUtils.clear(Color.WHITE);}
        else{
        ScreenUtils.clear(Color.BLACK);}

        ocam.update();
        viewport.apply();


        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = Gdx.graphics.getWidth();
        float worldHeight = Gdx.graphics.getHeight();
        spriteBatch.setColor(1,1,1,1);


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if(gamestate == Gamestate.playing||gamestate ==Gamestate.dead||gamestate== Gamestate.paused)
        {
        currentlevel.draw(spriteBatch,shape,delta);
        //Player wird im Level gedrawt
        }
        spriteBatch.end();

        uiStage.draw();
        if(gamestate==Gamestate.dialouge)
        {
            dialougnpc.act(Gdx.graphics.getDeltaTime());
            dialougnpc.drawInConversation(uiStage.getBatch());
        }


        //------
    }

    void setState(String newState) {
        // jenachdem was man beim "newState" angibt
        // wird unterschiedliches ausgeführt

        switch(newState) {
            case "startmenu" :
                gamestate = Gamestate.startmenu;
                BootingScreen boscreen=new BootingScreen(this);
                uiStage.addActor(boscreen);
                boscreen.toFront();
                levelnummer=invManager.getValueByKey("Level")-1;

                break;
            case "DevMode" :
                DevMode = true;
                multiplexer.addProcessor(new ZoomInputProcessor(ocam)); // dein Zoom
                if(levelnummer==0) {
                    levelnummer = -1; }

                uiStage.addActor(new DevMenu(this));
            case "beforeGame" :
                gamestate = Gamestate.loading;
                if(!DevMode) {
                    levelnummer = Math.max(0,levelnummer);
                }

            case "newlevel" : //triggert den Modus um eine neues Level zu laden
                Player.normalise();
                gamestate = Gamestate.loading;
                System.out.println("delta: "+Gdx.graphics.getDeltaTime());

                levelnummer++;
                showLevel(this.levelnummer);
                System.out.println("delta: "+Gdx.graphics.getDeltaTime()+"\n");
                LoadingScreen screen =new LoadingScreen(this);
                uiStage.addActor(screen);
                screen.toFront();
                Player.setZIndex(Integer.MAX_VALUE-1);
                gamestate=Gamestate.playing;
                break;
            case "paused" :
                gamestate = Gamestate.paused;
                break;
            case "winscreen" :
                gamestate = Gamestate.paused;
                uiStage.addActor(new WinScreen(this));
                break;
            case "resume" :
                if(Player!=null){
                //Player.normalise();
                }
                gamestate = Gamestate.playing;
                break;
            case "dead" :
                if(gamestate==Gamestate.dead){
                    return;
                }
                SoundManager.play("player-death", 0.9f, 0.6f);
                Player.normalise();
                gamestate = Gamestate.dead;
                Player.status= Entity.EntityStatus.dead;
                uiStage.addActor(new Deathscreen(this));

                DataCenter.increaseDeathcount();
                break;
            case "respawn" :
                if(gamestate!=Gamestate.dead){
                    System.out.println("respawn called but not dead");
                    return;
                }
                currentlevel.resetObjects();
                //_/entitys zurück an ihren spawn
                //_/leben der Entitys zurück setzten und inactive machen
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
            case "restart" :
                levelnummer=0;
                Player.upgradeManager.reset();
                Player.reset();
                invManager.resetInventory();
                gamestate = Gamestate.startmenu;
                uiStage.clear();
                Player.reAddUiElements();
                uiStage.addActor(new Startmenu(this));
                DataCenter.resetTimeplayed();
                break;

        }
        //--------
    }

    public Vector2 resolveCollision(Rectangle rectA, Rectangle rectB) {
        if (!rectA.overlaps(rectB)){return new Vector2(0,0);}

        float overlapX = Math.min(rectA.x + rectA.width, rectB.x + rectB.width) - Math.max(rectA.x, rectB.x);
        float overlapY = Math.min(rectA.y + rectA.height, rectB.y + rectB.height) - Math.max(rectA.y, rectB.y);

        // Prüfe, ob horizontale oder vertikale Bewegung kleiner ist
        if (overlapX < overlapY) {
            return new Vector2(rectA.x < rectB.x ? -overlapX : overlapX, 0); // Nach links oder rechts schieben
        } else {
            return new Vector2(0, rectA.y < rectB.y ? -overlapY : overlapY); // Nach oben oder unten schieben
        }
    }
    public Vector2 resolveCollision(Rectangle rectA, Polygon pol) {
        return MathHelper.getCollisionResolutionVector(rectA, pol);
    }
    void resolveWallCollision(Entity enti)
    {
        if(!enti.collisionOn) {
            return;
        }
        ArrayList <Polygon> walllist = getcollidingWalls(enti);
        for (int i = 0; i <walllist.size() ; i++)
        {
            Vector2 resolve=resolveCollision(enti.hitbox,walllist.get(i));
            enti.moveatAngle(resolve);
            //enti.additionalForce.clamp(0,enti.additionalForce.len()-30/**weight*/);
            enti.additionalForce.setLength(0);
            enti.collides = true;
            //enti.damageby(40*Gdx.graphics.getDeltaTime());
            ///break; // <--maybe wäre so besser
        }
    }
    ArrayList <Polygon> getcollidingWalls(TextureActor actor)
    {
        ArrayList <Polygon> list = new ArrayList<>();
        if(!actor.collisionOn) {
            return list;
        }
        for (MyTile tile : loadedwalls) {
            if(MathHelper.overlaps(actor.hitbox,tile.hitbox)) {
                list.add(tile.hitbox);
            }
        }
        return list;
    }

    void updateEntitys(float delta)
    {

        if(currentlevel.testentitys.size()!=0)
        {
            float herdcenterx=0,herdcentery=0;
          for(Testentity enti : currentlevel.testentitys)
          {
              herdcenterx+=enti.getCenterX();
              herdcentery+=enti.getCenterY();
          }
          herdcenterx/=currentlevel.testentitys.size();
          herdcentery/=currentlevel.testentitys.size();

                for(Testentity enti : currentlevel.testentitys)
                {
                    enti.setStrivingTarget(herdcenterx,herdcentery);
                }

        }

        for (Testentity enti : currentlevel.testentitys)
        {

            if(Player.isattacking)
            {
                    if(Player.handleAttack(enti)) {
                        continue;
                    }

            }
            resolveWallCollision(enti);
            if(enti.hitbox.overlaps(Player.hitbox))
            {
                Vector2 vec= resolveCollision(enti.hitbox, Player.hitbox);
                vec.setLength(vec.len()/1.7f);
                enti.moveatAngle(vec);
                enti.movement.setLength(0);
            }

            resolveWallCollision(enti);

            if(enti.hitbox.overlaps(Player.hitbox))
            {
                Vector2 vec= resolveCollision(Player.hitbox,enti.hitbox);
                Player.moveatAngle(vec);
            }


        }


        for (Gegner gegner : currentlevel.gegnerliste)
        {
            if(gegner.status== Entity.EntityStatus.dead)
            {
                continue;
            }
                if(Player.isattacking)
                {
                    if(Player.handleAttack(gegner)) {
                        continue;
                    }

                }
            if(!gegner.collisionOn) {continue;}

            resolveWallCollision(gegner);
            if(Player.collisionOn&&gegner.attackStatus!= Gegner.AttackStatus.inair) {
            if(gegner.hitbox.overlaps(Player.hitbox))
            {
                Vector2 vec= resolveCollision(gegner.hitbox, Player.hitbox);
                vec.setLength(vec.len()/2f/gegner.weight*Player.weight);
                gegner.moveatAngle(vec);
            }

            resolveWallCollision(gegner);

            if(gegner.hitbox.overlaps(Player.hitbox))
            {
                Player.moveatAngle(resolveCollision(Player.hitbox,gegner.hitbox));
                gegner.onPlayertouch();
            }
            }


        }

        updateNpcs();
        updateProjectiles();
    }

    void updateNpcs()
    {
        boolean setText=false;
        for (NPC npc : currentlevel.npcs)
        {
            if(Player.inradiusof(npc, 182)) {
                npc.inradius=true;
                setText=true;
                if(Gdx.input.isKeyJustPressed(Input.Keys.E))
                {
                dialougnpc = npc;
                setState("dialouge");
                break;}
            }else{npc.inradius=false;}
        }
        if(setText)
        {
            //Player.speechbox.
        }
    }


    void updateProjectiles()
    {

        for (Projectile prc : currentlevel.projectiles) {
            if(prc.collisionOn)
            {
                if(Player.collisionOn) {
                        if(Player.hitbox.overlaps(prc.hitbox))
                        {
                            prc.onHit(Player);

                        }
                }

                if(getcollidingWalls(prc).size()!=0)
                {
                    prc.onHit();
                    continue;
                }

                if(Player.isattacking)
                {
                    if(Player.handleAttack(prc,false)) {
                        prc.onHit();
                    }

                }
            }
        }


    }



    void checkplayercollision()
    {
        if(!dataCenter.areEnemysRemaining()||DevMode==true)
        {

            for (Teleporter tile : currentlevel.teleporters)
            {
                if(tile.state== Teleporter.TelState.inactiv)
                {
                   tile.activate();
                }
                if(tile.state== Teleporter.TelState.activ)
                {
                 if(Player.getfootDistance(tile)<=30)
                    {
                     tile.onStand();
                    }
                }
                if(tile.state== Teleporter.TelState.onstand)
                {
                    if(Player.getfootDistance(tile)>30)
                    {
                        tile.activate();
                        break;
                    }
                }
            }
        }
        for(TextureActor object: Level.objects)
        {
            if( Player.hitbox.overlaps(object.hitbox))
            {
                if(object instanceof Coin)
                {
                    ((Coin) object).onTouch(Player);
                }
                /*else if(object instanceof Door)
                {
                    ((Door) object).onStand();
                }
                else if(object instanceof Chest)
                {
                    ((Chest) object).onStand();
                }*/
            }
        }


        if(Player.collisionOn)
        {
            resolveWallCollision(Player);

            for (NPC npc : currentlevel.npcs)
            {
                if(npc.collisionOn && Player.hitbox.overlaps(npc.hitbox)) {
                    Player.moveatAngle( resolveCollision(Player.hitbox,npc.hitbox));
                }
            }
        }
        //--------
    }


    void showLevel(int level) {

        if(level==0){deltaFactor=0;}

        // aktuell sichtbares Level zerstören
        if(currentlevel != null) {
            currentlevel.destroy();
        }
        invManager.setValueByKey("Playtime", (int)DataCenter.getTimeplayed());
        invManager.setValueByKey("Level", level);
        DataCenter.setLevelnumber(level);
        currentlevel = new Level(LevelList.levels[level],this);
        currentlevel.load();
        dataCenter.level=currentlevel;

        if(Player != null) {
            Player.setWorldbounds(-64, MyTile.columnToX(currentlevel.getLength())-64, MyTile.rowToY(currentlevel.getHeight()),0 );
            Player.setPosition(currentlevel.xcoplayer, currentlevel.ycoplayer);
        }

        // Nummer des Levels anzeigen (altes feature):
        if(levelnummer != 0) {

        }
        //------
    }

    void updatewalls()
    {
        loadedwalls.clear();
        for (MyTile wall : currentlevel.walls)
        {
            if(/*inloadedworld(wall, 1.4)*/ true) {

                loadedwalls.add(wall);
            }
        }
        //-------
    }

    void loadSounds() {
        SoundManager.load("medium-explosion", "medium-explosion-40472.mp3");
        //SoundManager.load("sword-swing", "sounds/sword-swing-40473.mp3");
        SoundManager.load("player-death", "player-hurt_death.mp3");
        SoundManager.load("sheep", "baeh-sheep.wav");
        //SoundManager.load("sheep-hurt", "sheep-hurt.wav");
        SoundManager.load("coin_pickup", "dading.mp3");
    }

    void loadTilesets() {
        //TileManager.load("walls", "Tileset_Wall.png", 64, 64);
        TileManager.load("ground", "Tileset_Ground.png", 12, 11);
        TileManager.load("grass", "Tileset_Grass.png", 8, 8);
        TileManager.load("wall", "Tileset_Wall.png", 16, 16);
        TileManager.load("water--","Tileset_watersides.png",3,3);
        TileManager.load("water","Tileset_watersides2.png",3,3);
    }


    @Override
    public void pause() {
        //System.out.println("Game paused");
        if(gamestate==Gamestate.playing){
        //setState("paused");
            }
    }

    @Override
    public void resume() {
        if(gamestate==Gamestate.paused){
            //setState("resume");
            }

    }

    @Override
    public void dispose() {
        //currentlevel.destroy();
        TextureCache.disposeAll();
        SoundManager.dispose();
        for (TextureRegion reg : FireBall.explosion.getKeyFrames()) {
            reg.getTexture().dispose();
        }

    }
    public static void setToDefaultCursor()
    {
        Gdx.graphics.setCursor(bettercursor);
    }
    public void camerashake(float x, float y)
    {
        cameraoffsetx+=x;
        cameraoffsety+=y;
        ocam.rotate((x+y)/8);
    }
    public void randomcamerashake(float rangex, float rangey)
    {
        //cameraoffsetx+=Math.random()*rangex-rangex/2;
        //cameraoffsety+=Math.random()*rangey-rangey/2;
        camerashake((float) (Math.random()*rangex-rangex/2), (float) (Math.random()*rangey-rangey/2));
    }
    public void ryhtmiccamerashakerotation(float plusminusx, float plusminusy)
    {

    }
    public void resetCameraOffset()
    {
        cameraoffsetx=0;
        cameraoffsety=0;
        ocam.up.set(0, 1, 0);
        ocam.direction.set(0, 0, -1);
    }
}
