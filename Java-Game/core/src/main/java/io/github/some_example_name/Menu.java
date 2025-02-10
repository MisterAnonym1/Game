package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Menu extends Actor { //Hier werden alle Menüs verwaltet und erschaffen
    boolean onscreen = false; //ob ein Screen gerade aktiv ist oder nicht
    Revtext textbox; //erschafft eine Textbox, <---dein ernst? ich kann selber sehen


}

public class Deathscreen extends Menu {
    Button knopf;
    Rectangle screen;
    Star star;
    int delay;
    Deathscreen(Main game) { //erschafft den Screen;
        onscreen = false;
        stopActing();
        screen = new Rectangle(0, 0, 800, 800);
        screen.setFillColor(new Color(197, 187, 187), 1);
        int ran = Random.randint(0, Script.deathscreenscript.length - 1);
        textbox = new Revtext(400, 400, 70, Script.deathscreenscript[ran]);
        textbox.setFillColor(new Color(144, 21, 20));
        screen.setStatic(true);
        knopf = new Button(300, 520, 50, "Respawn");
        knopf.setFillColor(new Color(144, 21, 20), 1);
        knopf.setTextColor(0);
        knopf.addChangeListener(new GameChangeListener(game, "respawn"));
        knopf.setStatic(true);
        star = new Star(400, 400);
        setVisible(false);

    }
    void setVisible(boolean visible)
    {
        onscreen = false;
        if(visible) {
            int ran = MathUtils.random().randint(0, Script.deathscreenscript.length - 1);
            textbox.newText(Script.deathscreenscript[ran]);
            onscreen = true;
            restartActing();
            delay = 14;
        }
        else {
            screen.bringToFront();
            star.setVisible(visible);
            star.bringToFront();
            screen.setVisible(visible);
            textbox.bringToFront();
            textbox.setVisible(visible);
            knopf.bringToFront();
            knopf.setVisible(visible);

        }
    }
    void act()
    {
        if(delay > 0) {
            delay--;
            if(delay <= 0) {
                screen.bringToFront();
                star.setVisible(true);
                star.bringToFront();
                screen.setVisible(true);
                textbox.bringToFront();
                textbox.setVisible(true);
                knopf.bringToFront();
                knopf.setVisible(true);
            }
        }
    }

}

public class Startscreen extends Menu {
    Rectangle screen;
    void exist() {
        onscreen = true;
        Rectangle screen = new Rectangle(0, 0, 800, 800);
        textbox = new Revtext(00, 520, 40, "Lorem Ipsum, Sapere Aude \nLorem Ipsum, Sapere Aude");
    }

}
class Startmenu extends Menu
{
    Sprite hintergrund;
    Knopf exit;//quit game
    Revtext randomtext;
    Main game;
    boolean AdminCheck;
    Startmenu(Main gamel)
    {
        game = gamel;
        hintergrund = new Sprite(400, 400, SpriteLibrary.backround2, 0);
        hintergrund.scale(0.75);
        textbox = new Revtext(400, 330, 70, "Press \"Enter\" to start");
        textbox.setFillColor(new Color(80, 190, 61));
        int ran = Random.randint(0, Script.startmenuscript.length - 1);
        randomtext = new Revtext(0, 420, 45, Script.startmenuscript[ran]);
        randomtext.setBorderColor(Color.black, 1);
        randomtext.setBorderWidth(6);
        randomtext.move(400 - randomtext.getWidth() / 2, 0);
        exit = new Knopf(675, 120, SpriteLibrary.TowerDefense, 115, true);
        exit.rotate(45);
        exit.scale(4.2);
    }

    void act()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            AdminCheck = true;
            textbox.destroy();
            randomtext.destroy();
            hintergrund.destroy();
            exit.destroy();
            game.setState("DevMode");
            destroy();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            textbox.destroy();
            randomtext.destroy();
            hintergrund.destroy();
            exit.destroy();
            game.setState("beforeGame");
            destroy();

        }
        else if(exit.gedrückt)
        {
            System.exit(137);
        }


    }
}

class DevMenu extends Menu
{
    Text[] texte;
    Main logic;
    DevMenu(Main log)
    {
        texte = new Text[8];
        logic = log;
        for (int i = 0; i < texte.length - 1; i++)
        {
            texte[i] = new Text(getWorld().getLeft() + 20, getWorld().getTop() + 80 + i * 100, 45, "");
            texte[i].setBorderWidth(8);
            texte[i].setFillColor(Color.blue, 1);
            texte[i].setBorderColor(Color.white);
            texte[i].setStatic(true);

        }
        Timer.repeat(new Updatetxt(texte, this), 300);

    }
    void setOnscreen()
    {
        onscreen = true;
        logic.player.ar.setVisible1(true);
        for (int i = 0; i < texte.length - 1; i++)
        {
            texte[i].setVisible(true);
            texte[i].bringToFront();
        }
    }
    void setOffscreen()
    {
        onscreen = false;
        logic.player.ar.setVisible1(false);
        for (int i = 0; i < texte.length - 1; i++)
        {
            texte[i].setVisible(false);
        }
    }

   /*public void act()
   {

      if(onscreen) {
         texte[0].setText("loadedwalls amount: " + logic.loadedwalls.size());
      }
   }*/
}


class Updatetxt implements Runnable {
    Text[]texte;
    DevMenu menu;
    Updatetxt(Text[] txt, DevMenu men)
    {
        texte = txt;
        menu = men;
    }
    public void run() {
        if(menu.onscreen) {
            texte[0].setText("loadedwalls amount: " + menu.logic.loadedwalls.size());
            texte[1].setText("player speed: " + menu.logic.player.curspeed);
            texte[2].setText("fps: " + menu.logic.fps);
            texte[3].setText("Testentitys: " + menu.logic.currentlevel.testentitys.size());
        }
    }

}

public class Knopf extends Sprite
{
    boolean aktiv;
    boolean gedrückt;
    boolean einmal;

    Knopf(double x, double y, SpriteLibrary spriteL, int nummer, boolean nureinmal)
    { super(x, y, spriteL, nummer, ScaleMode.nearest_neighbour);
        aktiv = true;
        einmal = nureinmal;
        gedrückt = false;
        bringToFront();
    }

    public void onMouseEnter(double x, double y)
    {

        tint("#b9b0b0");
        getWorld().setCursor("pointer");

    }


    public void onMouseLeave(double x, double y)
    {
        tint("#ffffff");
        getWorld().setCursor("default");
    }


    public void onMouseDown(double x, double y, int key) {
        tint("#716868");

    }


    public void onMouseUp(double x, double y, int key) {
        if(aktiv)
        {
            gedrückt = true;
            tint("#b9b0b0");
            if(einmal == true)
            {
                setVisible(false);
                aktiv = false;
                getWorld().setCursor("default");
            }
        }
    }

}


class GameChangeListener implements ChangeListener {
    Main game;
    String zustand;
    GameChangeListener(Main game, String zustand)
    {
        this.game = game;
        this.zustand = zustand;
    }
    public void onChange(Object changedObject, String newValue) {
        game.setState(zustand);
        game.getWorld().setCursor("default");
    }

}
