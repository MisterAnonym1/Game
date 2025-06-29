package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Revtext extends OwnText {
    String maintext;
    boolean linefinished;
    int toline;
    int nummer=0;
    float counter;
    float charDelay=0.3f;
    float lineDelay=0.1f;
    boolean waitingforLine;
    boolean centered;
    float centerx, centery;

    public Revtext(float centerx, float centery, int fontsize, float chardelay, String mainText) {
        super("", centerx, centery, fontsize, Color.WHITE, null);
        centered = true;
        this.centerx = centerx;
        this.centery = centery;
        charDelay = chardelay;
        maintext = mainText;
        setColor(1,1,1,1);
        outlineColor= Color.BLACK;
        lineDelay = (0.1f-chardelay)*mainText.length()+0.2f;
        toFront();
        center(mainText);

    }
    public Revtext(float leftx, float bottomy, int fontsize, String mainText) {
        super("", leftx, bottomy, fontsize, Color.WHITE, null);
        centered = false;
        charDelay = 0;
        maintext = mainText;
        text = mainText;
        setColor(1,1,1,1);
        linefinished = true;
        lineDelay = 2;
        toFront();
    }
    @Override
    public void draw(Batch sbatch, float parentalpha) {
        float x = getX();
        float y = getY();
        sbatch.setTransformMatrix(new Matrix4().translate(x, y, 0).rotate(0, 0, 1, getRotation()).translate(-x, -y, 0));
        super.draw(sbatch, parentalpha);
        sbatch.setTransformMatrix(new Matrix4());
    }
    String reveal() {
        text = text + maintext.charAt(nummer);
        nummer++;
        return text;
    }
    void skip() {
        text = maintext;
        nummer = maintext.length();
        counter = 0;
        waitingforLine = true;
    }
    void newText(String newtext) {
        reset();
        if(centered){
            center(newtext);
        }
        maintext = newtext;
        linefinished = false;
    }
    public void setText(String text) {
        this.text = text;
        maintext = text;
        linefinished = true;
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        counter += delta;
        if(!linefinished && !waitingforLine && counter >= nummer*charDelay && nummer < maintext.length()) {
            reveal();
        }
        if (nummer >= maintext.length()) {
            if (!waitingforLine) {
                waitingforLine = true;
                counter = 0;
            }
        }
        if(waitingforLine && counter >= lineDelay ) {
            waitingforLine = false;
            linefinished = true;
        }
    }
    void center(String text) {
        layout.setText(font, text);
        setX(centerx- layout.width / 2.0f);// Zentriere den Text horizontal
        layout.setText(font,"X");
        setY(centery+ layout.height / 2.0f); // Zentriere den Text vertikal
        //setY(getX()-layout.height / 2.0f);
        //setY(getY());

    }
    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }
    public void reset() {
        maintext = "";
        text = "";
        counter = 0;
        nummer = 0;
        linefinished = false;
        waitingforLine = false;
    }
}
