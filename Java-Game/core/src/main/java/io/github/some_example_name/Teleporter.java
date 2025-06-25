package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Teleporter extends TextureActor
{
    static TextureRegion spriteSheet = new TextureRegion( new Texture("TPall2.png"));
    enum TelState {activ, inactiv,onstand};
    TextureRegion[]textures;
    TelState state= TelState.inactiv;
    TextureRegion currenttexture;
    float countdown=2;
    Main main;
    Teleporter(float x, float y, Main main)
    {
        super("teleporter V3.png",0,0,1f/3f,0.5f);
        setPosition(x,y);
        this.main= main;
        textures= /*TextureRegion.split(spriteSheet,206,145)[0]*/ Animator.getAnimation("teleporter V3.png",3,2,1,3,1).getKeyFrames();
        deactivate();
        scale(0.4f);

    }

    @Override
    void initializeHitbox() {
        super.initializeHitbox();
        //hitbox = new Rectangle(getX() - hitboxOffsetX, getY() - hitboxOffsetY, 206, 183);
    }

    public void deactivate() {

        state=TelState.inactiv;
        currenttexture=textures[0];
    }
    public void activate() {
        main.Player.normalise();
        state=TelState.activ;
        countdown=2;
        currenttexture=textures[1];
    }
    public void onStand() {
        main.Player.normalise();
        state=TelState.onstand;
        currenttexture=textures[2];
        main.Player.addAction(Actions.sequence(

            Actions.parallel(
                //Actions.color(new Color(0.3f, 0.6f, 1f, 0.3f), 1.9f),  // Tint Blau & halb transparent
                Actions.repeat(30, Actions.sequence( // **Flacker-Effekt**
                    Actions.color(new Color(40/255f, 245/255f, 1f, 0.4f), 0.08f), // Sehr durchsichtiges Blau
                        Actions.color(new Color(0.8f, 0.9f, 1f, 0.8f), 0.1f)
                    // Stärkeres Blau
                )),
                Actions.sequence(
                    Actions.delay(main.DevMode?0.1f:0.7f),
                Actions.parallel(
                Actions.repeat(30, Actions.sequence( // **Wackeleffekt**
                    Actions.moveBy(3,0, 0.02f),
                    Actions.moveBy(-3,-0, 0.02f)
                )),
                    Actions.sequence(
                    Actions.delay(main.DevMode? 0.1f:1f),
                    Actions.fadeOut(0.2f),// **Langsames Verschwinden**
                    new Action() {
                        @Override
                        public boolean act(float v) {
                            main.setState("paused");
                            if(main.levelnummer+1  >=2/* LevelList.levels.length*/)
                            {
                                main.setState("winscreen");
                                System.out.println("Time played: "+DataCenter.getTimeplayed());
                                System.out.println("Time: "+DataCenter.getformatedTimeplayed());
                            }
                            else{
                                Main.uiStage.addActor(new NewLevelScreen(main));}
                            activate();
                            return true;
                        }
                    })

                ))
            )

        ));
    }

    @Override
    public void draw(Batch batch, float delta) {
        batch.setColor(getColor().r,getColor().g,getColor().b,1);
        //super.draw(batch,1);
        batch.draw(currenttexture,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void destroy() {
        super.destroy();
        for(TextureRegion tex: textures)
        {
            tex.getTexture().dispose();
        }
    }
}
