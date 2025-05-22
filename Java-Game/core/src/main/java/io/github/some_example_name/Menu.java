package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
        delay=1;// Mindest-Zeit die der Ladebildschirm zu sehen ist
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
        animationstateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = loading.getKeyFrame(animationstateTime, true);
        batch.draw(currentFrame,ScreenWidth/2f-200,ScreenHeight/2f-150,getOriginX(),getOriginY(),400,300,getScaleX(),getScaleY(),getRotation());

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
        textbox = new Revtext(ScreenWidth/2f, ScreenHeight/2f*1.8f, 3, 0.06f,Script.deathscreenscript[ran]);
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


class Startmenu extends Menu
{
    Texture hintergrund;
    SpriteButton exit;//quit game
    Revtext randomtext;
    Startmenu(Main gamel)
    {
        super();
        main = gamel;
        hintergrund = new Texture("Forest sun backround.png");
        textbox = new Revtext(ScreenWidth/2f, 280, 3, 0.04f,"Press \"Enter\" to start");
        textbox.setColor(new Color(80/255.0f, 190/255.0f, 61/255.0f,1));
        int ran = MathUtils.random(0, Script.startmenuscript.length - 1);
        randomtext = new Revtext(ScreenWidth/2f, 200, 2, 0.03f,Script.startmenuscript[ran]);
        //randomtext.setBorderColor(Color.black, 1);
        //randomtext.setBorderWidth(6);
        exit = new SpriteButton(675, 120, "Credits Button V1.png",1);
        exit.setOnUp( ()->System.exit(187));
        Main.uiStage.addActor(exit);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        batch.draw(hintergrund, 0, 0, 1024,576);
        textbox.draw(batch,1);
        randomtext.draw(batch,1);
        exit.draw(batch,1);
    }


    public void destroy() {
      hintergrund.dispose();
      remove();
      exit.remove();
    }

    @Override
    public void act(float delta)
    {
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
        texte = new Revtext[8];
        setOffscreen();
        delay=1;
        this.main = main;
        for (int i = 0; i < texte.length - 1; i++)
        {
            texte[i] = new Revtext( 20,  80 + i * 60, 2,"hallo");
            texte[i].setColor(Color.GOLDENROD);
            //texte[i].setBorderColor(Color.white);
        }

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
      for (int i = 0; i < texte.length - 1; i++)
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
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
