package io.github.some_example_name;//package com.Game2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

enum Gamestate { startmenu, paused, playing, dead, dialouge, loading }
public class Main implements ApplicationListener {
    ArrayList<MyTile> loadedwalls = new ArrayList<>();
    ShapeRenderer shape;
    Texture dropTexture;
    Revtext revtext;
    Music music;
    //TiledMap map;
    static Cursor bettercursor;
    String predeterminedDeathmessage= "you died of dumb";
    static Stage uiStage;
   // private OrthogonalTiledMapRenderer renderer;
    OrthographicCamera ocam;
    SpriteBatch spriteBatch;
    Level currentlevel;
    DataCenter dataCenter;
    int levelnummer;
    FitViewport viewport;
    static Skin skin;
    Player Player;
    Vector2 touchPos;
    float deltaFactor=1;
    NPC dialougnpc;///kann auch alle Unterklassen von NPC speichern
    //KARLTOFFEL_BOSS El_Karltoffelboss;
    static boolean debugging=false;
    boolean DevMode=false;
    int deathcount=0;
    Gamestate gamestate;//siehe oben
    @Override
    public void create() {
        ocam=new OrthographicCamera(1024,576);
        viewport = new FitViewport(1024,576, ocam);
        shape= new ShapeRenderer();
        spriteBatch = new SpriteBatch();


        dropTexture = new Texture("drop.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("battle-of-the-dragons-8037.mp3"));
        skin= new Skin(Gdx.files.internal("ui/uiskin.json"));
        uiStage=new Stage(new FitViewport(1024,576));

        Pixmap pixmap = new Pixmap(Gdx.files.internal("Pointers/Pointer.png"));
        // Set hotspot to the middle of it (0,0 would be the top-left corner)
        int xHotspot = 4, yHotspot = 4;
        bettercursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose(); // We don't need the pixmap anymore
       setToDefaultCursor();


        touchPos = new Vector2();
        ocam.position.set(ocam.viewportWidth / 2f, ocam.viewportHeight / 2f, 0);

        //map = new TmxMapLoader().load("Test Karte 2.tmx");
        //renderer = new OrthogonalTiledMapRenderer(map, 1 /4f);
        music.setLooping(true);
        music.setVolume(.1f); // .2f ist das selbe wie 0.2f
        music.play();
        revtext = new Revtext(400,250,1,0.1f,"Hallo das ist ein Revtext");
        dataCenter=new DataCenter(this);
        shape.setAutoShapeType(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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
        float delta = Gdx.graphics.getDeltaTime();



        if(DevMode)
        {


            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            {
                //STRG
                if (Gdx.input.isKeyPressed(Input.Keys.R) ) {
                    deltaFactor*=1+delta;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.F) ) {
                    deltaFactor/=1+delta;

                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.B) ) {
                    debugging = !debugging;

                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                    setState("dead");
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    Player.sethealth(Player.maxhealth, false);
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    Player.collisionOn=!Player.collisionOn;
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    Player.invincible = !Player.invincible;
                }
                //STRG
            }


            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                ocam.zoom += 1*delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                ocam.zoom -= 1*delta;
            }

        }


        //float effectiveViewportWidth = ocam.viewportWidth * ocam.zoom;
        //float effectiveViewportHeight = ocam.viewportHeight * ocam.zoom;


    }

    private void logic()
    {

        float delta = Gdx.graphics.getDeltaTime();
        delta= Math.min(delta,1/30.0f);
        delta=delta/deltaFactor;
        uiStage.act(delta);

        //System.out.println(Gdx.input.getX()+"x "+ Gdx.input.getY()+"y ");

      if(gamestate == Gamestate.playing)
      {
         updatewalls();
         Player.act(delta);
         //dialougnpc.act(delta);
         //El_Karltoffelboss.act(delta);
         revtext.act(delta);
         currentlevel.act(delta);




         //Player.stayinWorldbounds();
         updateEntitys();
         checkplayercollision();
          //ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);
          /*if( new Vector2( ocam.position.x - Player.getCenterX(),ocam.position.y - Player.getCenterY()).len()>=99999 )
          {
              ocam.position.lerp(new Vector3(Player.getCenterX(),Player.getCenterY(),1),0.1f);
          }
          else
          {*/
          ocam.position.x=MathUtils.clamp(Player.getCenterX(), 0+517, currentlevel.getLength()*64-517);
          ocam.position.y=MathUtils.clamp(Player.getCenterY(), (-currentlevel.getHeight()+1)*64+288,64-288 );

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
        delta=delta/deltaFactor;

        ScreenUtils.clear(Color.BLACK);

        ocam.update();
        viewport.apply();


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

        if(gamestate == Gamestate.playing||gamestate ==Gamestate.dead)
        {
        currentlevel.draw(spriteBatch,shape,delta);
            //dialougnpc.draw(spriteBatch,delta);
            //dialougnpc.drawInConversation(spriteBatch);
            // El_Karltoffelboss.draw(spriteBatch,delta);
        Player.draw(spriteBatch,shape, delta,1.0f);
        }
        spriteBatch.end();

        uiStage.draw();
        if(gamestate==Gamestate.dialouge)
        {
            dialougnpc.act(delta);
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
                Player.normalise();
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
            case "paused" :
                gamestate = Gamestate.paused;
                break;
            case "resume" :
                Player.normalise();
                gamestate = Gamestate.playing;
                break;
            case "dead" :
                if(gamestate==Gamestate.dead){
                    return;
                }
                Player.normalise();
                gamestate = Gamestate.dead;
                Player.status= Entity.EntityStatus.dead;
                uiStage.addActor(new Deathscreen(this));
                //Sound.playSound(Sound.pong_d);
                deathcount++;
                break;
            case "respawn" :
                Player.sethealth(Player.maxhealth, false);
                Player.setPosition(currentlevel.xcoplayer, currentlevel.ycoplayer);
                Player.normalise();
                Player.status= Entity.EntityStatus.idle;
                Level.projectiles.clear();
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
            enti.moveatAngle( resolveCollision(enti.hitbox,walllist.get(i))  );
            enti.collides = true;
            enti.movement.setLength(0);
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

    void updateEntitys()
    {
        for (Testentity enti : currentlevel.testentitys)
        {
            if(Player.isattacking)
            {
                    if(Player.handleAttack(enti)) {
                        continue;
                    }

            }

            if(enti.hitbox.overlaps(Player.hitbox))
            {
                Vector2 vec= resolveCollision(enti.hitbox, Player.hitbox);
                vec.setLength(vec.len()/1.7f);
                enti.moveatAngle(vec);
                enti.collides = true;
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

                if(Player.isattacking)
                {
                    if(Player.handleAttack(gegner)) {
                        continue;
                    }

                }

            /*if(gegner.update())
            {
                currentlevel.gegnerliste.remove(gegner);
                continue
            }*/
            resolveWallCollision(gegner);
        }

        updateNpcs();
        updateProjectiles();
    }

    void updateNpcs()
    {
        for (NPC npc : currentlevel.npcs)
        {
            if(Player.inradiusof(npc, 182)) {
                npc.inradius=true;
                if(Gdx.input.isKeyJustPressed(Input.Keys.E))
                {
                dialougnpc = npc;
                setState("dialouge");
                break;}
            }else{npc.inradius=false;}
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
                 if(Player.getdistance(tile)<30)
                    {
                     tile.onStand();
                    }
                }
                if(tile.state== Teleporter.TelState.onstand)
                {
                    if(Player.getdistance(tile)>30)
                    {
                        tile.activate();
                        break;
                    }
                }
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
        // aktuell sichtbares Level zerstören
        if(currentlevel != null) {
            currentlevel.destroy();
        }

        currentlevel = new Level(LevelList.levels[level],this);
        currentlevel.load();
        dataCenter.level=currentlevel;

      /*if(devmenu.onscreen) {
         devmenu.setOffscreen();
         player.hitbox.setAlpha(0);
       }
      */
        if(Player != null) {

            //player.bringToFront();
            ///+xPlayer.setWorldbounds(0, currentlevel.getLength(), 0, currentlevel.getHeight());
            Player.setPosition(currentlevel.xcoplayer, currentlevel.ycoplayer);
        }

        // Nummer des Levels anzeigen:
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

    }

    @Override
    public void pause() {
        System.out.println("Game paused");
        setState("paused");
    }

    @Override
    public void resume() {
        setState("resume");
    }

    @Override
    public void dispose() {

    }
    public static void setToDefaultCursor()
    {
        Gdx.graphics.setCursor(bettercursor);
    }
}
