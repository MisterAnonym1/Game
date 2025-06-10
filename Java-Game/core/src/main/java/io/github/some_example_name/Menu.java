package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.Text;

import java.util.function.Consumer;


public class Menu extends Actor { //Hier werden alle Menüs verwaltet und erschaffen
    float animationstateTime;
    boolean onscreen = false;//ob ein Screen gerade aktiv ist oder nicht
    float delay=1;
    Main main;
    Revtext textbox;//erschafft eine Textbox, <---dein ernst? ich kann selber sehen
    final float ScreenWidth=1024;
    final float ScreenHeight=576;
    @Override
    public void act(float delta) {
        super.act(delta);

    }
}

 class LoadingScreen extends Menu
{
    Animation<TextureRegion> loading;
    boolean finished_loading = false;
    LoadingScreen(Main mainl)
    {

        super();
        int ran = MathUtils.random(0, Script.loadingscreenscript.length - 1);
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*0.7f, 2, 0.01f,Script.loadingscreenscript[ran]);
        textbox.setColor(new Color(0.1f, 0.1f, 0.8f,1));
        delay=1.2f;// Mindest-Zeit die der Ladebildschirm zu sehen ist
        main = mainl;
        loading = Animator.getAnimation("Loadingsheet.png",15,1,1,15,0.05f);
    }
    void setfinished()
    {
        finished_loading = true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        ScreenUtils.clear(Color.WHITE);
        textbox.draw(batch,alpha); textbox.setPosition(textbox.getX()+getX(), textbox.getY()+getY());
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
            destroy();
            //main.Player.healthbar.setVisible(true);
        }
    }
}


 class Deathscreen extends Menu {
    AdvancedTextButton knopf;
    Deathscreen(Main main) { //erschafft den Screen;
        super();
        int ran = MathUtils.random(0, Script.deathscreenscript.length - 1);
        String message=Script.deathscreenscript[ran];
        if(main.predeterminedDeathmessage.length()>0){
            message=main.predeterminedDeathmessage;
            main.predeterminedDeathmessage="";
        }
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.8f, 3, 0.06f,message);
        textbox.setColor(new Color(0.8f, 0.1f, 0.1f,1));
        delay=1;
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
        ShapeRenderer shape = new ShapeRenderer();
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
class NewLevelScreen extends Menu {
    AdvancedTextButton jaknopf;
    AdvancedTextButton neinknopf;
    AdvancedTextButton skillknopf;
    Revtext secondtext;
    NewLevelScreen(Main main) {
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.5f, 3, 0.03f,"Level abgeschlossen Gratulation!\nNeues Level Laden?");
        secondtext = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.5f, 3.2f, 0.06f,"");
        textbox.setColor(Color.WHITE);
        secondtext.setColor(new Color(0, 0f, 0,1));
        this.main=main;

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
        skillknopf.setOnUp(()-> System.out.println("Skills are coming soon"));
        Main.uiStage.addActor(skillknopf);
         delay=0;

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
    SpriteButton exit;//quit game
    Revtext randomtext;
    boolean inmatrix;
    Matrix matrix;
    Startmenu(Main gamel)
    {
        super();
        main = gamel;
        setPosition(0,0);
        hintergrund = new TextureRegion(new Texture("Forest sun backround.png"));
        textbox = new Revtext(ScreenWidth/2f, 400, 3, 0.04f,"Press \"Enter\" to start");
        textbox.setColor(Color.SKY);

        int ran = MathUtils.random(0, Script.startmenuscript.length - 1);
        String message=Script.startmenuscript[ran];
        //message="Now with 10% more bugs!!";
        randomtext = new Revtext(ScreenWidth/2f, 320, 2, 0.03f,message);
        checkmessage(message);
        //randomtext.setBorderColor(Color.black, 1);
        //randomtext.setBorderWidth(6);

        exit = new SpriteButton(675, 120, "Credits Button V1.png",1);
        exit.setOnUp( ()->System.exit(187));
        Main.uiStage.addActor(exit);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        if(inmatrix){matrix.actAndDraw(batch,Gdx.graphics.getDeltaTime());}
        batch.draw(hintergrund, getX(), getY(),0,ScreenHeight, 1024,576,1,1,getRotation());
        textbox.draw(batch,1);
        randomtext.draw(batch,1);
        exit.draw(batch,1);
    }


    public void destroy() {
      hintergrund.getTexture().dispose();
      remove();
      exit.remove();
    }
    void checkmessage(String mes)
    {
        switch (mes)
        {
            case "I was a player once... \nnow I'm just code." :

            case "Das hättest du nicht sehen sollen..." :
                inmatrix=true;
                matrix= new Matrix(main.viewport);
                SequenceAction sequence = new SequenceAction(
                    Actions.rotateBy(-42,0.5F,Interpolation.elastic),
                    Actions.rotateBy(18,0.9f,Interpolation.circleIn),
                    Actions.moveBy(0, -150, 0.5f, Interpolation.linear), // Langsam starten
                    Actions.moveBy(0, -200, 0.4f, Interpolation.pow2),  // Schneller
                    Actions.moveBy(0, -250, 0.3f, Interpolation.exp5)   );
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
        super.act(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            main.setState("DevMode");
            this.destroy();
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            main.setState("beforeGame");
            this.destroy();
            return;
        }
        textbox.act(delta);
        randomtext.act(delta);
        exit.act(delta);
           //1903080120100509014090190200140903080200101201205019




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
            texte[i] = new Revtext( 20,  30 + i * 27, 1.5f,"hallo");
            texte[i].setColor(Color.GOLDENROD);
            //texte[i].setBorderColor(Color.white);
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
          texte[4].setText("Deaths: " + main.deathcount);
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
                if (onCheck != null) onCheck.accept(isChecked);// Umschalten für Checkbox-Funktion
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
         getLabel().setColor(box);
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
                if (onCheck != null) onCheck.accept(isChecked);
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
