package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.function.Consumer;


public class Menu extends Actor { //Hier sind alle Menüs die verwendet werden
    float animationstateTime;
    boolean onscreen = false;
    float delay=1;
    Main main;
    Revtext textbox;
    static float ScreenWidth=1024;
    static float ScreenHeight=576;
   static void showRestartConfirmation(Main main) {
        if (main.inPopUpWindow) return; // Verhindert mehrfaches Öffnen
        main.inPopUpWindow=true;
        final Window confirmWindow = new Window("", Main.skin);
        confirmWindow.setModal(true);
        confirmWindow.setMovable(false);
        confirmWindow.setResizable(false);
        confirmWindow.setSize(500, 260); // Größer
        confirmWindow.setPosition(ScreenWidth/2f - 250, ScreenHeight/2f - 130);

        Label label = new Label("Are you sure you want to\n restart the whole game?\nThis will delete your progress.", Main.skin);
        label.setAlignment(Align.center);
        label.setFontScale(2.2f); // Größerer Text
        confirmWindow.add(label).colspan(2).pad(30);
        confirmWindow.row();

        TextButton yesButton = new TextButton("Yes", Main.skin);
        TextButton noButton = new TextButton("No", Main.skin);
        yesButton.getLabel().setFontScale(2.2f); // Größerer Button-Text
        yesButton.getLabel().setColor(Color.BLUE);

        noButton.getLabel().setFontScale(2.2f);
        yesButton.getLabel().setColor(Color.RED);
        yesButton.setSize(100, 100); // Größe nicht hier ändern
        noButton.setSize(100, 100);//sondern unten bei der Hinzufügung

        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.inPopUpWindow=false;
                confirmWindow.remove();
                main.setState("restart");
            }
        });
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmWindow.remove();
                main.inPopUpWindow=false; // Fenster schließen und Pop-up-Status zurücksetzen
            }
        });

        confirmWindow.add(yesButton).width(150).height(70).pad(30);
        confirmWindow.add(noButton).width(150).height(70).pad(30);
        Main.uiStage.addActor(confirmWindow);
    }
     static void showQuitConfirmation(Main main) {
        if (main.inPopUpWindow) return; // Verhindert mehrfaches Öffnen
        main.inPopUpWindow= true; // Verhindert mehrfaches Öffnen
        final Window confirmWindow = new Window("", Main.skin);
        confirmWindow.setModal(true);
        confirmWindow.setMovable(false);
        confirmWindow.setResizable(false);
        confirmWindow.setSize(500, 260); // Größer
        confirmWindow.setPosition(ScreenWidth/2f - 250, ScreenHeight/2f - 130);

        Label label = new Label("Are you sure you want to quit?", Main.skin);
        label.setAlignment(Align.center);
        label.setFontScale(2.2f); // Größerer Text
        confirmWindow.add(label).colspan(2).pad(30);
        confirmWindow.row();

        TextButton yesButton = new TextButton("Yes", Main.skin);
        TextButton noButton = new TextButton("No", Main.skin);
        yesButton.getLabel().setFontScale(2.2f); // Größerer Button-Text
        yesButton.getLabel().setColor(Color.BLUE);

        noButton.getLabel().setFontScale(2.2f);
        yesButton.getLabel().setColor(Color.RED);
        yesButton.setSize(100, 100); // Größe nicht hier ändern
        noButton.setSize(100, 100);//sondern unten bei der Hinzufügung

        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                main.dispose();
                System.exit(0);
            }
        });
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmWindow.remove();
                main.inPopUpWindow=false; // Fenster schließen und Pop-up-Status zurücksetzen
            }
        });

        confirmWindow.add(yesButton).width(200).height(80).pad(30);
        confirmWindow.add(noButton).width(200).height(80).pad(30);
        Main.uiStage.addActor(confirmWindow);
    }

    public void showCreditsWindow() {
        if (main.inPopUpWindow) return; // Verhindert mehrfaches Öffnen
        main.inPopUpWindow = true; // Verhindert mehrfaches Öffnen
        Window creditsWindow = new Window("", Main.skin); // Kein Titel im Fensterkopf
        creditsWindow.setModal(true);
        creditsWindow.setMovable(false);
        creditsWindow.setResizable(false);
        creditsWindow.setSize(800, 500);
        creditsWindow.setPosition(ScreenWidth/2f - 400, ScreenHeight/2f - 250);

        // Titel als Label im Fenster-Inhalt, damit er nicht überlappt
        Label titleLabel = new Label("Credits", Main.skin);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.5f);
        creditsWindow.add(titleLabel).expandX().padTop(10).padBottom(10);
        creditsWindow.row();

        Label creditsLabel = new Label(
                "Game made by: The Boys Deluxe \nGrafik: Our own and from the Internet\nMusic: I stole it (joke)\nMountain king Font by Michael W. Moss: --\n https://www.fontspace.com/mountain-king-font-f119642\nSounds: sheep.wav by Gitanki -- https://freesound.org/s/172712/\nNorwegian sheep by michaelperfect -- https://freesound.org/s/710296/ \nSpecial Thanks: Frau Bauereisen\n" ,
                Main.skin);
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setFontScale(1.6f);
        creditsWindow.add(creditsLabel).expand().fill().pad(20);
        creditsWindow.row();
        TextButton closeButton = new TextButton("Schließen", Main.skin);
        closeButton.getLabel().setFontScale(1.6f);
        closeButton.setSize(180, 60); // Größere Box
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                creditsWindow.remove();
                main.inPopUpWindow=false;
            }
        });
        creditsWindow.add(closeButton).padBottom(20).padTop(10).width(180).height(60);
        Main.uiStage.addActor(creditsWindow);
    }




}




class BootingScreen extends Menu
{
    boolean finished = false;
    Texture backround;
    Texture backround2;
    TextureActor logo;
    Label skipMessage;
    OwnText studioText;
    TriangleBackgroundRenderer background = new TriangleBackgroundRenderer(200);
    BootingScreen(Main mainl) {

        super();
        setZIndex(Integer.MAX_VALUE-1);
        main = mainl;
        Pixmap pixmap = LibgdxHelperClass.generateFuturisticTriangleBackground(800, 600, 200);
        skipMessage = new Label("Press Space to skip", Main.skin);
        skipMessage.setFontScale(4f);
        skipMessage.setPosition(ScreenWidth / 2f - skipMessage.getWidth()*2, ScreenHeight/2);
        skipMessage.setColor(0.5f,0.5f,0.5f,1);
        studioText=new OwnText("The Boyz Deluxe Studios",ScreenWidth / 2f, ScreenHeight / 2f * 0.4f, 50, new Color(0.7f, 0.9f, 0.7f, 1),null);
        backround = new Texture("whitebase.png");
        backround2 = new Texture(pixmap);
        logo=new TextureActor("Boys_logo.jpg");
        logo.centerAt(ScreenWidth / 2f, ScreenHeight / 1.65f);
        logo.setColor(1,1,1,0);
        logo.toFront();
        textbox = new Revtext(ScreenWidth / 2f, ScreenHeight / 2f * 1.3f, 140, 0.0f, "GARDEN SLAYER");
        textbox.setColor(new Color(0.15f, 0.5f, 0.15f, 0), null);
        delay = 0f;
        Action logoAction=Actions.run(()->{logo.addAction(Actions.sequence(
                Actions.delay(1.5f),
                Actions.fadeIn(2, Interpolation.slowFast),
                Actions.delay(2.5f),
                Actions.fadeOut(2),
                Actions.removeActor()

        ));});
        Action textAction=Actions.run(() -> {textbox.addAction(Actions.sequence(
                Actions.fadeIn(3, Interpolation.slowFast),
                Actions.delay(2.5f),
                Actions.parallel(Actions.fadeOut(1),Actions.run(() -> {
                            this.destroy();
                        }
                ))

        ));});
        addAction(Actions.sequence(logoAction,Actions.delay(7.3f), textAction));


    }
    void setfinished()
    {finished = true;}

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(0,0,0,getColor().a);
        batch.draw(backround,0,0,ScreenWidth,ScreenHeight);
        skipMessage.draw(batch,(delay<=1.5f?(delay<=1f?0.6f+delay*2:1.5f-delay):0)*getColor().a);
        textbox.draw(batch,alpha); textbox.setPosition(textbox.getX()+getX(), textbox.getY()+getY());
        logo.setColor(logo.getColor().r,logo.getColor().g,logo.getColor().b,Math.min(logo.getColor().a,getColor().a)*getColor().a);
        logo.draw(batch,1);
        studioText.draw(batch,1,logo.getColor().a*getColor().a);

    }

    void destroy()
    {
        if(finished) return; // Verhindert mehrfaches Zerstören
        setfinished();
        Main.uiStage.addActor(new Startmenu(main));
        toFront();
        addAction(Actions.sequence(
                Actions.delay(0.9f),
                Actions.fadeOut(0.5f),
                Actions.run(() -> {
                    remove();
                })
        ));

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        delay+= delta;
        if(!finished&&(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)||Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            destroy();
        }
        textbox.act(delta);
        logo.act(delta);

    }
}




 class LoadingScreen extends Menu
{
    Animation<TextureRegion> loading;
    boolean finished_loading = false;
    Texture backround;

    LoadingScreen(Main mainl)
    {

        super();
        int ran = MathUtils.random(0, Script.loadingscreenscript.length - 1);
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*0.65f, 40, 0.01f,Script.loadingscreenscript[ran]);
        textbox.setColor(new Color(0.2f, 0.2f, 0.7f,1),null);
        delay=1.2f;// Mindest-Zeit die der Ladebildschirm zu sehen ist
        if(mainl.DevMode){delay=0;}
        main = mainl;
        backround=new Texture("whitebase.png");

        loading = Animator.getAnimation("Loadingsheet.png",15,1,1,15,0.05f);
        main.Player.coindisplay.setZIndex(Integer.MAX_VALUE-1);
    }
    void setfinished()
    {
        finished_loading = true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(backround,0,0,ScreenWidth,ScreenHeight);        textbox.draw(batch,alpha); textbox.setPosition(textbox.getX()+getX(), textbox.getY()+getY());
        animationstateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = loading.getKeyFrame(animationstateTime, true);
        batch.draw(currentFrame,getX()+ ScreenWidth/2f-200, getY()+ ScreenHeight/2f-80,getOriginX(),getOriginY(),400,300,getScaleX(),getScaleY(),getRotation());

    }

    void destroy()
    {
        for(TextureRegion frame:loading.getKeyFrames())
        {
            frame.getTexture().dispose();
            break;
        }
        main.Player.coindisplay.toFront();
        remove();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        textbox.act(delta);
        if(delay > 0) {
            delay-=delta;
        }
        if(main.gamestate==Gamestate.playing && delay <= 0) {
            setfinished();
            destroy();
            //main.Player.healthbar.setVisible(true);
        }
    }
}


 class Deathscreen extends Menu {
    AdvancedTextButton knopf;
    ShapeRenderer shape;
    Deathscreen(Main main) { //erschafft den Screen;
        super();
        int ran = MathUtils.random(0, Script.deathscreenscript.length - 1);
        String message=Script.deathscreenscript[ran];
        if(main.predeterminedDeathmessage.length()>0){
            message=main.predeterminedDeathmessage;
            main.predeterminedDeathmessage="";
        }
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.8f, 45, 0.06f,message);
        textbox.setColor(new Color(0.8f, 0.1f, 0.1f,1));
        delay=1;
        shape = new ShapeRenderer();
        this.main=main;
        knopf = new AdvancedTextButton("Respawn",ScreenWidth/2f, 130, 3,Color.SCARLET,Color.BLACK );
        knopf.getLabel().setFontScale(2f); // 1.5x größer
        knopf.setOnUp(()->this.destroy());
        Main.uiStage.addActor(knopf);


        setColor(1,1,1,0);
        addAction(Actions.fadeIn(1.1f));

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //if(knopf.getColor().a>=0.85f){
        //ScreenUtils.clear(new Color(40/255.0f, 30/255.0f, 30/255.0f,textbox.getColor().a));}
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(14);
        shape.setProjectionMatrix(batch.getProjectionMatrix());

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(40/255.0f, 30/255.0f, 30/255.0f,getColor().a));
        shape.rect(0,0,ScreenWidth,ScreenHeight);
        //shape.rect(0,0,ScreenWidth,ScreenHeight,Color.SCARLET,Color.CHARTREUSE,Color.WHITE, Color.BLUE);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.3f,0.2f,0.2f,getColor().a);
        float centerX = ScreenWidth/2.0f;
        float centerY = ScreenHeight/2.0f;
        float outerRadius = 45;
        float innerRadius = 100;

        float[] vertices = {
            // Spitze oben
            centerX, centerY + outerRadius,
            // Oben links (innere Ecke)
            centerX - innerRadius, centerY + innerRadius,
            // Linke Spitze
            centerX - outerRadius, centerY,
            // Unten links (innere Ecke)
            centerX - innerRadius, centerY - innerRadius,
            // Spitze unten
            centerX, centerY - outerRadius,
            // Unten rechts (innere Ecke)
            centerX + innerRadius, centerY - innerRadius,
            // Rechte Spitze
            centerX + outerRadius, centerY,
            // Oben rechts (innere Ecke)
            centerX + innerRadius, centerY + innerRadius
        };

        shape.polygon(vertices);
        shape.end();

        batch.begin();
        batch.setColor(1,1,1,getColor().a);
        batch.draw(main.Player.deadAnimation.getKeyFrame(0.81f),centerX-153,centerY-118,300,300);
        textbox.draw(batch,getColor().a);
        knopf.draw(batch,getColor().a);

    }
    void destroy(){
        main.setState("respawn");
        knopf.remove();
        remove();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        delay-=delta;
        if(Gdx.input.isKeyPressed(Input.Keys.R)||Gdx.input.isKeyPressed(Input.Keys.ENTER)||Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {destroy();}
        textbox.act(delta);
        knopf.act(delta);
    }

}
class WinScreen extends Menu
{
    AdvancedTextButton knopf;
    AdvancedTextButton exitknopf;
    AdvancedTextButton creditsknopf;
    TextureRegion hintergrund;
    ShapeRenderer shape;
    private final String playtime= DataCenter.getformatedTimeplayed();
    OwnText wintext;
    ConfettiManager confettiManager;
    WinScreen(Main main) { //erschafft den Screen;
        super();
        this.main=main;
        int ran = MathUtils.random(0, Script.winscreenscript.length - 1);
        String message=Script.winscreenscript[ran];
        /*if(main.predeterminedDeathmessage.length()>0){
            message=main.predeterminedDeathmessage;
            main.predeterminedDeathmessage="";
        }*/

         shape = new ShapeRenderer();
        hintergrund = new TextureRegion(new Texture("Forest sun backround.png"));

        confettiManager=new ConfettiManager();
        confettiManager.add(new ConfettiRain(0.00f));
        confettiManager.add(new ConfettiRain(0.06f));
        wintext = new OwnText(""+playtime, ScreenWidth/2f, ScreenHeight/2f*1.2f,80, Color.GOLD,Color.BLACK);

        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*0.8f, 40, 0.06f,message);
        textbox.setColor(Color.WHITE);

        knopf = new AdvancedTextButton("Restart Game",ScreenWidth/2f, 100, 3,Color.CORAL,Color.RED );
        knopf.getLabel().setFontScale(2f); // nur Schrift 2x größer
        knopf.setOnUp(()->showRestartConfirmation(main));
        Main.uiStage.addActor(knopf);

        exitknopf=new AdvancedTextButton("Quit Game",ScreenWidth/2f-215, 100, 3,Color.ROYAL,Color.BLACK );
        exitknopf.getLabel().setFontScale(2f); //  nur Schrift 2x größer
        exitknopf.setOnUp(()->{
            showQuitConfirmation(main);
        });
        Main.uiStage.addActor(exitknopf);

        creditsknopf=new AdvancedTextButton("  Credits  ",ScreenWidth/2f+210, 100, 3,Color.CYAN,Color.BLACK );
        creditsknopf.getLabel().setFontScale(2f); //  nur Schrift 2x größer
        creditsknopf.setOnUp(()->{
            showCreditsWindow();
        });
        Main.uiStage.addActor(creditsknopf);

        setColor(1,1,1,0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setColor(1,1,1,1);
        batch.draw(hintergrund, 0, 0,0,ScreenHeight, 1026,576,1,1,getRotation());
        knopf.draw(batch,1);
        exitknopf.draw(batch,1);
        creditsknopf.draw(batch,1);
        batch.end();
        shape.setProjectionMatrix(batch.getProjectionMatrix());
        shape.begin(ShapeRenderer.ShapeType.Filled);
        confettiManager.draw(shape);
        shape.end();
        batch.begin();
        textbox.draw(batch,1);
        wintext.draw(batch,1);
    }
    void destroy(String state){
        main.setState(state); ///restart game
        knopf.remove();
        exitknopf.remove();
        creditsknopf.remove();
        remove();
    }


    @Override
    public void act(float delta)
    {
        super.act(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {destroy("resume");}
        textbox.act(delta);
        wintext.act(delta);
        confettiManager.act(delta);
    }



}



class NewLevelScreen extends Menu {
    AdvancedTextButton jaknopf;
    AdvancedTextButton neinknopf;
    AdvancedTextButton skillknopf;
    Revtext secondtext;
    UpgradeManager upmanager;
    // --- Skill Menü Variablen ---
    private final int maxLevel = 5;
    private Window skillWindow;
    private java.util.List<Label> skillCostLabels = new java.util.ArrayList<>();
    private java.util.List<Upgrade> skillUpgrades = new java.util.ArrayList<>();

    NewLevelScreen(Main main) {
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.5f, 50, 0.02f,"Level abgeschlossen Gratulation!\nNeues Level Laden?");
        secondtext = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.5f, 52, 0.06f,"");
        textbox.setColor(Color.WHITE);
        secondtext.setColor(new Color(0, 0f, 0,1));
        this.main=main;
        upmanager=main.Player.upgradeManager;
        jaknopf = new AdvancedTextButton(" JA ",ScreenWidth/2f-150, 130, 3,Color.SKY,Color.WHITE );
        jaknopf.getLabel().setFontScale(2f); // 1.5x größer
        jaknopf.setOnUp(()->this.destroy("newlevel"));
        Main.uiStage.addActor(jaknopf);

        neinknopf = new AdvancedTextButton("Nein",ScreenWidth/2f, 130, 3,Color.SKY,Color.WHITE );
        neinknopf.getLabel().setFontScale(2f); // 1.5x größer
        neinknopf.setOnUp(()->this.destroy("resume"));
        Main.uiStage.addActor(neinknopf);

        skillknopf = new AdvancedTextButton("Skills",ScreenWidth/2f+150, 130, 3,Color.SKY,Color.WHITE );
        skillknopf.getLabel().setFontScale(2f); // 1.5x größer
        skillknopf.setOnUp(this::showSkillMenu);
        Main.uiStage.addActor(skillknopf);
         delay=0;

    }

    private void showSkillMenu() {
        skillCostLabels.clear();
        skillUpgrades.clear();
        if (skillWindow != null && skillWindow.hasParent()) return;
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        skillWindow = new Window("", skin);
        skillWindow.setSize(ScreenWidth, ScreenHeight);
        skillWindow.setPosition(0, 0);
        skillWindow.setMovable(false);
        skillWindow.setModal(true);
        skillWindow.setResizable(false);
        skillWindow.setColor(new Color(0.13f, 0.18f, 0.32f, 0.98f)); // Blau-grau

        Table table = new Table(skin);
        table.setFillParent(true);
        table.defaults().pad(20);

        Label title = new Label("Skill Menü", skin);
        title.setFontScale(2.2f);
        title.setColor(new Color(0.5f,0.7f,1f,1f)); // Hellblau
        table.add(title).colspan(4).center().padBottom(40);
        table.row();

        addSkillRow(table, upmanager.getUpgrade("Health"));
        addSkillRow(table, upmanager.getUpgrade("Damage"));
        addSkillRow(table, upmanager.getUpgrade("Speed"));

        table.row();
        TextButton closeBtn = new TextButton("Schließen", skin);
        closeBtn.getLabel().setFontScale(1.7f);
        closeBtn.setColor(new Color(0.5f,0.7f,1f,1f));
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                skillWindow.remove();
            }
        });
        table.add(closeBtn).colspan(4).center().padTop(30).width(220).height(60);
        skillWindow.add(table).expand().fill();
        Main.uiStage.addActor(skillWindow);
        main.Player.coindisplay.toFront();
    }

    private void addSkillRow(Table table, Upgrade upgrade) {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Label skillLabel = new Label(upgrade.getName(), skin);
        skillLabel.setFontScale(1.7f); // Größer
        ProgressBar bar = new ProgressBar(0, maxLevel, 1, false, skin);
        bar.setValue(upgrade.getLevel());
        bar.setAnimateDuration(0.2f);
        bar.getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("blue-loading.png"))));
        bar.getStyle().knobBefore.setMinHeight(24); // dicker
        bar.getStyle().background.setMinHeight(18); // dicker
        bar.setHeight(32f);
        bar.setWidth(260f);
        TextButton plusBtn = new TextButton("+", skin);
        plusBtn.getLabel().setFontScale(2.1f); // Größer
        plusBtn.setColor(new Color(0.5f,0.7f,1f,1f));
        int cost = upgrade.getCost(upgrade.getLevel()+1);
        int coins = Main.invManager.getValueByKey("Coins");
        Label costLabel = new Label(cost + " Münzen", skin);
        costLabel.setFontScale(1.3f);
        if(bar.getValue()==bar.getMaxValue()) {
            costLabel.setText("Max");
            costLabel.setColor(Color.GRAY);
        }else if (coins < cost) {costLabel.setColor(Color.RED);} else {costLabel.setColor(Color.GOLD);}
        skillCostLabels.add(costLabel);
        skillUpgrades.add(upgrade);
        plusBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int val = upgrade.getLevel();
                int coins = Main.invManager.getValueByKey("Coins");
                int cost = upgrade.getCost(val + 1);
                if (val < maxLevel && coins >= cost) {
                    Main.invManager.setValueByKey("Coins", coins - cost);
                    upgrade.upgrade();
                    bar.setValue(val + 1);
                    if(bar.getValue()==bar.getMaxValue()) {
                        costLabel.setText("Max");
                        costLabel.setColor(Color.GRAY);
                    } else {
                        costLabel.setText(upgrade.getCost(val+2) +" Münzen");
                    }
                    updateAllSkillCostLabels();
                    main.Player.coindisplay.toFront();

                }
            }
        });
        table.row();
        table.add(skillLabel).width(160).height(60);
        table.add(bar).width(260).height(32).padLeft(10).padRight(10);
        table.add(costLabel).width(110).height(60);
        table.add(plusBtn).width(70).height(60);
    }

    private void updateAllSkillCostLabels() {
        int coins = Main.invManager.getValueByKey("Coins");
        for (int i = 0; i < skillCostLabels.size(); i++) {
            Label label = skillCostLabels.get(i);
            Upgrade upgrade = skillUpgrades.get(i);
            int nextLevel = upgrade.getLevel() + 1;
            int cost = upgrade.getCost(nextLevel);
            if (upgrade.getLevel() >= maxLevel) {
                label.setText("Max");
                label.setColor(Color.GRAY);
            } else {
                label.setText(cost + " Münzen");
                if (coins < cost) label.setColor(Color.RED); else label.setColor(Color.GOLD);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //if(knopf.getColor().a>=0.85f){
        //ScreenUtils.clear(new Color(40/255.0f, 30/255.0f, 30/255.0f,textbox.getColor().a));}
        delay+=Gdx.graphics.getDeltaTime();
        batch.end();
        ShapeRenderer shape = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(14);
        shape.setProjectionMatrix(batch.getProjectionMatrix());


        // Sinus-Funktion für weichen Farbverlauf
        //float n = Math.abs((float) Math.sin(delay * Math.PI / 10) );
        float n = Math.abs((float) Math.sin(delay * Math.PI / 5));

        float  speed = 0.3f; // Geschwindigkeit der Farbrotation
        float angle = Math.abs(delay) * speed; // Drehwinkel für sanfte Farbverschiebung

        shape.begin(ShapeRenderer.ShapeType.Filled);
        // Farben rotieren durch zyklische Sinus- und Kosinusfunktionen
        Color topLeft = new Color(0.3f + Math.abs((float) Math.sin(angle)) * 0.4f, 0f, 0.6f + Math.abs((float) Math.cos(angle + Math.PI / 2)) * 0.4f, 1);
        Color topRight = new Color(0.2f + Math.abs((float) Math.sin(angle + Math.PI / 2)) * 0.5f, 0f, 0.8f + Math.abs((float) Math.cos(angle + Math.PI)) * 0.2f, 1);
        Color bottomLeft = new Color(0.4f + Math.abs((float) Math.sin(angle + Math.PI)) * 0.3f, 0f, 0.7f + Math.abs((float) Math.cos(angle + 3 * Math.PI / 2)) * 0.3f, 1);
        Color bottomRight = new Color(0.1f + Math.abs((float) Math.sin(angle + 3 * Math.PI / 2)) * 0.6f, 0f, 0.9f + Math.abs((float) Math.cos(angle + 2 * Math.PI)) * 0.1f, 1);

        shape.rect(0, 0, ScreenWidth, ScreenHeight, topLeft, topRight, bottomLeft, bottomRight);


        Color topLeft2 = new Color((51 + (n * 204f)) / 255f, 0f, (102 + (n * 153 / 10)) / 255f, 0.5f);
        Color topRight2 = new Color((153 - (n * 153f)) / 255f, 0f, (204 - (n * 102 / 10)) / 255f, 0.5f);
        Color bottomLeft2 = new Color((102 - (n * 102f)) / 255f, 0f, (255 - (n * 51 / 10)) / 255f, 0.5f);
        Color bottomRight2 = new Color((204 - (n * 204f)) / 255f, 0f, (51 + (n * 204 / 10)) / 255f, 0.5f);

        shape.rect(0, 0, ScreenWidth, ScreenHeight, topLeft2, topRight2, bottomLeft2, bottomRight2);



        // Zeichne das Rechteck mit Farbinterpolation



        //shape.rect(0,0,ScreenWidth,ScreenHeight,new Color((102 - ((n * 102) / 10)) / 255f, 0f, 204f/255f,1),new Color(n * 102 / 10/255f, 0f, 204f/255f,1),new Color((102-(n*102/10))/255f, 0f, 204f/255f,1),new Color(n * 102 / 10/255f, 0f, 204f/255f,1));
        //shape.rect(0, 0, ScreenWidth, ScreenHeight, color1, color2, color3, color4);
        /*float wave = Math.abs((float) Math.sin(delay * Math.PI / 4));
        Color dynamicColor = new Color(0.2f + wave * 0.6f, 0.0f, 0.4f + wave * 0.5f, 1);
        shape.setColor(dynamicColor);
        shape.rect(0, 0, ScreenWidth, ScreenHeight);*/
        for (int x = 0; x < ScreenWidth; x += 50) {
            for (int y = 0; y < ScreenHeight; y += 50) {
                float pulse = Math.abs((float) Math.cos((x + y + Math.abs(delay)) * 0.05));
                Color gridColor = new Color(0.3f + pulse * 0.5f, 0.0f, 0.5f + pulse * 5f, 1f);
                shape.setColor(gridColor);
                shape.circle(x, y, 20);
            }
        }


        shape.end();



        batch.begin();
        textbox.draw(batch,1);
        secondtext.draw(batch,1);
        jaknopf.draw(batch,1);
        neinknopf.draw(batch,1);
        skillknopf.draw(batch,1);

    }
    void destroy(String state){
        main.setState(state);
        jaknopf.remove();
        neinknopf.remove();
        skillknopf.remove();
        remove();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)||Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {destroy("newlevel");}
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {destroy("resume");}
        textbox.act(delta);
        secondtext.act(delta);
        skillknopf.act(delta);
        jaknopf.act(delta);
        neinknopf.act(delta);
    }

}


class Startmenu extends Menu
{
    TextureRegion hintergrund;
    SpriteButton credits;
    Revtext randomtext;
    Matrix matrix;
    String[] easterEggMessages;
    Startmenu(Main mainl)
    {
        super();
        main = mainl;
        setPosition(0,0);
        hintergrund = new TextureRegion(new Texture("misty-forest-background.png"));
        textbox = new Revtext(ScreenWidth/2f, 400, 65, 0.04f,"Press \"Enter\" to start");
        textbox.setColor(Color.SKY);
        delay=0;
        int ran = MathUtils.random(0, Script.startmenuscript.length - 1);
        String message=Script.startmenuscript[ran];
        message= increaseEasterEggProbability(message);
        //message="Now with 10% more bugs!!";
        randomtext = new Revtext(ScreenWidth/2f, 320, 40, 0.03f,message);
        checkmessage(message);


        credits = new SpriteButton(675, 60, "Credits Button V1.png",2);
        credits.setOnUp( ()->showCreditsWindow());
        Main.uiStage.addActor(credits);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        //if(inmatrix){matrix.actAndDraw(batch,Gdx.graphics.getDeltaTime());}
        batch.draw(hintergrund, getX(), getY(),0,ScreenHeight, 1024,576,1,1,getRotation());
        textbox.draw(batch,1);
        randomtext.draw(batch,1);
        credits.draw(batch,1);
    }


    public void destroy() {
      hintergrund.getTexture().dispose();
      if(matrix!=null){matrix.remove();}
      remove();
      credits.remove();
    }


    String increaseEasterEggProbability(String mess)
    {
        easterEggMessages= new String[]{"I was a player once... \nnow I'm just code.", "Das haettest du nicht sehen sollen...",
                "Now with 10% more bugs!!"};
        boolean alreadyEgg=false;
        for (int i = 0; i < easterEggMessages.length; i++) {
            if(mess.equals(easterEggMessages[i]))
            {
                alreadyEgg=true;
            }
        }
        if(!alreadyEgg&&Math.random()<0.2f)
        {
        mess=easterEggMessages[MathUtils.random(0,easterEggMessages.length-1)];
        }
        return mess;
    }


    void checkmessage(String mes)
    {
        switch (mes)
        {
            case "I was a player once... \nnow I'm just code." :

            case "Das haettest du nicht sehen sollen..." :
                matrix= new Matrix(main.viewport);
                main.uiStage.addActor(matrix);
                matrix.toBack();
                SequenceAction sequence = new SequenceAction(
                    Actions.rotateBy(-42,0.5F,Interpolation.elastic),
                    Actions.rotateBy(18,0.9f,Interpolation.circleIn),
                    //Actions.moveBy(0, -150, 0.5f, Interpolation.linear), // Langsam starten
                    Actions.moveBy(0, -600, 1.2f, Interpolation.pow2)
                      );
                addAction(sequence);
                break;
            case "This is not a bug, it is a feature.":
                randomtext.rotateBy(-30);
                break;
            case "Now with 10% more bugs!!":
                SequenceAction rotatesequence = new SequenceAction(
                    Actions.rotateBy(50,  1.3f, Interpolation.bounceIn),
                    Actions.rotateBy(-20,  0.6f, Interpolation.pow2),
                    Actions.rotateBy(-70, 0.4f, Interpolation.exp5),
                    Actions.rotateBy(+33, 1.1f, Interpolation.elastic),
                    Actions.rotateBy(-23, 0.7f, Interpolation.circleOut)
                );
                // Wiederhole die Sequenz unendlich
                randomtext.addAction( Actions.forever(new SequenceAction(rotatesequence,Actions.rotateBy(+30, 2f, Interpolation.bounceOut),Actions.delay(0.5f))   ));
                //randomtext.addAction( Actions.forever(new SequenceAction(rotatesequence)));
                break;

        }
    }
    @Override
    public void act(float delta)
    {

        delay+=delta;
        if(delay<1.4f){return;} // Warte 1.4 Sekunden bevor die Tasten abgefragt werden
        super.act(delta);
        if(!pinDialogVisible && Gdx.input.isKeyPressed(Input.Keys.X)) {
            if(!Main.DevMode){
            showPinDialog();
            return;}
            main.setState("DevMode");
            this.destroy();
            return;

        }
        if(!pinDialogVisible && Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            main.setState("beforeGame");
            Main.DevMode=false;
            this.destroy();
            return;
        }

        textbox.act(delta);
        randomtext.act(delta);
        credits.act(delta);
           //1903080120100509014090190200140903080200101201205019
    }

    Window pinWindow;
    TextField pinField;
    boolean pinDialogVisible = false;
    final String DEV_PIN = "TheBoyz";

    private void showPinDialog() {
        if (pinDialogVisible) return;
        pinDialogVisible = true;
        pinWindow = new Window("", Main.skin);
        pinWindow.setModal(true);
        pinWindow.setMovable(false);
        pinWindow.setResizable(false);
        pinWindow.setSize(400, 200);
        pinWindow.setPosition(ScreenWidth/2f - 200, ScreenHeight/2f - 100);

        Label label = new Label("Bitte PIN eingeben:", Main.skin);
        label.setAlignment(Align.center);
        label.setFontScale(1.5f);
        pinWindow.add(label).colspan(2).pad(20);
        pinWindow.row();

        pinField = new TextField("", Main.skin);
        pinField.setPasswordMode(true);
        pinField.setPasswordCharacter('*');
        pinField.setMaxLength(10);

        pinField.setSize(260, 40); // Größer
       pinField.getStyle().font.getData().setScale(2.5f); // Text größer

        pinField.getStyle().messageFont = pinField.getStyle().font; // Gleiche Schriftgröße für Message
        pinField.getStyle().messageFontColor = Color.RED; // Fehlermeldung rot
        pinWindow.add(pinField).colspan(2).width(260).height(40).pad(16);
        pinWindow.row();
        final Startmenu menu=this;
        TextButton okButton = new TextButton("OK", Main.skin);
        TextButton cancelButton = new TextButton("Abbrechen", Main.skin);
        okButton.getLabel().setFontScale(1.4f);
        cancelButton.getLabel().setFontScale(1.4f);
        okButton.setSize(100, 50);
        cancelButton.setSize(100, 50);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (DEV_PIN.toLowerCase().equals(pinField.getText().toLowerCase())) {
                    pinWindow.remove();
                    pinDialogVisible = false;
                    pinField.getStyle().font.getData().setScale(1f); // Text normal groß
                    main.setState("DevMode");
                    menu.destroy();

                } else {
                    pinField.setText("");
                    pinField.setMessageText("Falscher PIN!");
                }
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pinField.getStyle().font.getData().setScale(1f); // Text normal groß
                pinWindow.remove();
                pinDialogVisible = false;
            }
        });
        pinWindow.add(okButton).width(120).height(60).pad(10);
        pinWindow.add(cancelButton).width(120).height(60).pad(10);
        Main.uiStage.addActor(pinWindow);
        pinField.setText("");
        pinField.setMessageText("");
        pinField.setFocusTraversal(false);
        pinField.setCursorPosition(pinField.getText().length());
    }
}

class DevMenu extends Menu
{
    Revtext[] texte;
    int fpscounter=0;
    DevMenu(Main main)
    {
        super();
        texte = new Revtext[12];
        setOffscreen();
        delay=1;
        this.main = main;
        for (int i = 0; i < texte.length; i++)
        {
            texte[i] = new Revtext( 20,  30 + i * 27, 20,"hallo");
            texte[i].setColor(Color.GOLDENROD);
            texte[i].setOutlineColor(Color.BLACK);
        }
        texte[0].setColor(Color.YELLOW);
        texte[1].setColor(Color.RED);
        texte[5].setColor(Color.RED);
        texte[7].setColor(Color.RED);
        texte[6].setColor(Color.WHITE);
        texte[8].setColor(Color.WHITE);
    }
    void setOnscreen()
    {
        onscreen = true;
        //logic.player.ar.setVisible1(true);
    }
    void setOffscreen()
    {
        onscreen = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!onscreen){return;};
      for (int i = 0; i < texte.length; i++)
        {
            if(texte[i]!= null){ texte[i].draw(batch,parentAlpha);}

        }
    }

    public void act(float delta)
   {

       delta = Gdx.graphics.getDeltaTime();
       if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
           main.deltaFactor=(main.deltaFactor==0?1:0);
       }
       if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
       {
           //STRG
           if (Gdx.input.isKeyPressed(Input.Keys.O) ) {
               main.deltaFactor*=1+delta;
           }
           if (Gdx.input.isKeyPressed(Input.Keys.L) ) {
               main.deltaFactor/=1f+delta;

           }
           if (Gdx.input.isKeyJustPressed(Input.Keys.M) ) {
               Level.objects.add(new Coin(main.Player.getHitboxCenterX(), main.Player.getHitboxCenterY(), 50));

           }
           if (Gdx.input.isKeyJustPressed(Input.Keys.B) ) {
               main.debugging = !main.debugging;

           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
               main.setState("dead");
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
               showQuitConfirmation(main);
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
               showRestartConfirmation(main);
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.H)) {
               main.Player.sethealth(main.Player.maxhealth);
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
               main.Player.collisionOn=!main.Player.collisionOn;
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
               main.Player.invincible = !main.Player.invincible;
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
               main.currentlevel.reload();
           }
           if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
               if(main.Player.speed!=400){
                   main.Player.speed=400;}
               else{main.Player.speed=250;}
           }

           //---STRG
       }




       if(Main.debugging != onscreen)
       {
           onscreen = !onscreen;
       }
      if(onscreen) {

          delay-=delta;
          fpscounter+=1;
          if(delay<=0)
          {
              delay=1;
              texte[2].setText("Fps: " + (int)fpscounter/delay +" or "+ 1.0/delta);
              fpscounter=0;
          }

          texte[0].setText("loadedwalls amount: " + main.loadedwalls.size());
          texte[1].setText("Player speed: " + main.Player.movement.len());
          texte[3].setText("Testentitys: " + main.currentlevel.testentitys.size());
          texte[4].setText("Deaths: " + DataCenter.getDeathcount());
          texte[5].setText("Player direction-angle: " + main.Player.directionline);
          Vector2 cursorposition= main.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
          texte[6].setText("Cursor Coordinates: " + Math.round(cursorposition.x)+"X " + Math.round(cursorposition.y)+"Y "   );
          texte[7].setText("line to cursor length: " + new Vector2(cursorposition.x-main.Player.getCenterX(),cursorposition.y-main.Player.getCenterY()).len());
          texte[8].setText("Cursor Screen Coordinates: (" + Math.round(Gdx.input.getX())+"|" + Math.round( Gdx.input.getY())+")"   );
          texte[9].setText("Gamestate: " +main.gamestate);

      }
   }


}

class SpriteButton extends Button
{

    private Runnable onClick;  // Wird einmal beim Klick ausgeführt
    private Runnable onHold;   // Wird durchgehend ausgeführt, solange gedrückt
    private Runnable onUp; // Wird einmal nach dem Klick ausgeführt
    private Consumer<Boolean> onCheck;
    // Funktioniert wie eine Checkbox
    private boolean isChecked = false;
    private boolean isPressed = false;
    //private boolean isTouching = false; // Speichert, ob der Button gedrückt wird
    SpriteButton(float x, float y,String filepath, float scale)
    {
        this(x,y,new TextureRegion(new Texture(filepath)),scale);
    }
    SpriteButton(float x, float y,TextureRegion region, float scale)
    {
        super(new TextureRegionDrawable(region));
        setPosition(x,y);
        setDisabled(false);
        //scaleBy(scale);
        setSize(getWidth()*scale,getHeight()*scale);
        toFront();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(isDisabled()){return false;};
                onMouseDown();
                if (onClick != null) onClick.run();
                return true;
                // WICHTIG: Muss `true` zurückgeben, damit `touchUp` ausgelöst wird
            }



            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //onMouseLeave();
                isChecked = !isChecked;
                if (onUp != null) onUp.run();
                //if (onCheck != null) onCheck.accept(isChecked);// Umschalten für Checkbox-Funktion
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                onMouseEnter();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                onMouseLeave();
            }
        });

    }


    public void onMouseEnter()
    {
        //if(!activ){return;};
        setColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand);
    }



    public void onMouseLeave()
    {
        setColor(1f,1f,1f,1);
        //Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Main.setToDefaultCursor();
        if(isPressed){onMouseEnter();}
        isPressed = false;

    }


    public void onMouseDown() {
        isPressed = true;
        setColor(0.5f,0.5f,0.5f,1);

    }
    @Override
    public void act(float delta) {
        super.act(delta);
        if(isDisabled()){return;}
        if(isPressed){
            onMouseDown();
            if (onHold != null) {
                onHold.run(); // Wird durchgehend ausgeführt, solange gedrückt
            }}
    }
    public void setOnClick(Runnable action) { this.onClick = action; } //Code für beim Klicken setzen
    public void setOnHold(Runnable action) { this.onHold = action; }//Code für beim Hold setzen
    public void setOnCheck(Consumer<Boolean> action) { this.onCheck = action; } //Code für Checkbox setzen
    public void setOnUp(Runnable action) { this.onUp = action; }//Code für beim Klicken setzen



}
 class AdvancedTextButton extends TextButton {
    private Runnable onClick;  // Wird einmal beim Klick ausgeführt
    private Runnable onHold;   // Wird durchgehend ausgeführt, solange gedrückt
    private Runnable onUp;     // Wird ausgeführt, wenn losgelassen
    private Consumer<Boolean> onCheck; // Checkbox-Funktion
    private boolean isChecked = false;
    private boolean isPressed = false;

     public AdvancedTextButton(String text, float centerx, float centery, float scale, Color textcolor,Color box )
     {
         this(text,centerx,centery,scale);
         setColor(box);
         getLabel().setColor(textcolor);
     }
    public AdvancedTextButton(String text, float centerx, float centery, float scale) {
        super(text,  Main.skin);
        setSize(getWidth() * scale, getHeight() * scale);
        setPosition(centerx-getWidth()/2.0f,centery-getHeight()/2.0f);
        toFront();

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isDisabled()) return false;
                onMouseDown();
                if (onClick != null) onClick.run();
                return true; // WICHTIG: Muss `true` zurückgeben, damit `touchUp()` ausgelöst wird
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isChecked = !isChecked;
                if (onUp != null) onUp.run();
                //if (onCheck != null) onCheck.accept(isChecked);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                onMouseEnter();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                onMouseLeave();
            }
        });
    }

    public void onMouseEnter() {
        setColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
    }

    public void onMouseLeave() {
        setColor(1f, 1f, 1f, 1);
        //Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Main.setToDefaultCursor();
        if (isPressed) onMouseEnter();
        isPressed = false;
    }

    public void onMouseDown() {
        isPressed = true;
        setColor(0.5f, 0.5f, 0.5f, 1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isDisabled()) return;
        if (isPressed) {
            onMouseDown();
            if (onHold != null) onHold.run();
        }
    }

    public void setOnClick(Runnable action) { this.onClick = action; }
    public void setOnHold(Runnable action) { this.onHold = action; }
    public void setOnCheck(Consumer<Boolean> action) { this.onCheck = action; }
    public void setOnUp(Runnable action) { this.onUp = action; }
}
