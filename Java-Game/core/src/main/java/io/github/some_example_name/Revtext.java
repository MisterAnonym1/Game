package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.Color;


public class Revtext extends OwnText {
    String maintext;
    boolean linefinished;
    int nummer=0;
    float counter;
    float charDelay;
    float lineDelay=0.1f;
    boolean waitingforLine;
    boolean centered;
    float centerx, centery;

    public Revtext(float centerx, float centery, float scale, float chardelay, String mainText) {
        super("", centerx, centery, scale, Color.WHITE);
        centered = true;
        this.centerx = centerx;
        this.centery = centery;
        charDelay = chardelay;
        maintext = mainText;
        setColor(1,1,1,1);
        lineDelay = (0.1f-chardelay)*mainText.length()+0.2f;
        toFront();
        center(mainText);

    }
    public Revtext(float centerx, float centery, float scale, float chardelay, String mainText, String style) {
        super("", centerx, centery, scale, Color.WHITE,style);
        centered = true;
        this.centerx = centerx;
        this.centery = centery;
        charDelay = chardelay;
        maintext = mainText;
        setColor(1,1,1,1);
        lineDelay = (0.1f-chardelay)*mainText.length()+0.2f;
        toFront();
        center(mainText);

    }

    public Revtext(float leftx, float bottomy, float scale, String mainText) {
        super("", leftx, bottomy, scale, Color.WHITE, false);
        centered = false;
        charDelay = 0;
        maintext = mainText;
        newText(mainText);
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
        String currentText = getText().toString();
        setcurrentText(currentText + maintext.charAt(nummer));
        nummer++;
        return getText().toString();
    }
    void skip() {
        setcurrentText(maintext);
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

    public void setcurrentText(String text) {
        super.setText(text);
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
        layout.setText(getStyle().font, text);
        setX(centerx- (layout.width / 2.0f)*scale);// Zentriere den Text horizontal
        layout.setText(getStyle().font,"X");
        setY(centery+ layout.height / 2.0f); // Zentriere den Text vertikal
        //setY(getX()-layout.height / 2.0f);
        //setY(getY());

    }

    public void reset() {
        maintext = "";
        setcurrentText("");
        counter = 0;
        nummer = 0;
        linefinished = false;
        waitingforLine = false;
    }
}
