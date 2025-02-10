package io.github.some_example_name;

import org.w3c.dom.Text;

import java.awt.*;

class Revtext  {
    /*String maintext;
    String showtext;
    boolean isfinished;
    int toline;
    int nummer;
    int counter;
    double centeratx;
    double centeraty;
    Revtext(double centerx, double centery, double fontsize, String mtext)
    {
        super(centerx, centery, fontsize, "");
        maintext = mtext;
        showtext = "";
        nummer = 0;
        centeratx = centerx;
        centeraty = centery;
        isfinished = false;
        center(fontsize, mtext);
        setFillColor(Color.white);

        //move((getWorld().getWidth() - getWidth()) / 8, 0);
        setBorderColor(256 * 256 * 256 + 256 * 256 + 256);
        setBorderWidth(6);
        setAlpha(1);
        setStatic(true);
    }
    String reveal()
    {
        showtext = showtext + maintext.charAt(nummer);
        nummer++;
        return showtext;
    }
    void newText(String newtext)
    {

        this.showtext = "";
        center(getFontSize(), newtext);
        nummer = 0;
        maintext = newtext;
        setText("");
        isfinished = false;
    }
    public void act()
    {
        counter++;
        if(counter >= 1 && nummer < maintext.length() && !isfinished)
        {
            setText(reveal());
            counter = 0;
        }
        if(counter > 40 && nummer >= maintext.length())
        {
            isfinished = true;
        }
    }
    void center(double font, String text)
    {
        Text ctext = new Text(getCenterX(), getCenterY(), font, text);
        moveTo(getWorld().getLeft()+ centeratx - ctext.getWidth() / 2,getWorld().getTop()+ centeraty);
        ctext.destroy();
    }
}




class Star extends Rectangle
{
    Rectangle rec;
    Star(double x, double y)
    {
        super(x - 250, y - 250, 500, 500);
        rec = new Rectangle(x - 250, y - 250, 500, 500);
        setStatic(true);
        rec.setStatic(true);
        rec.rotate(45);
        setBorderColor(256 * 256 * 88 + 256 * 84 + 84);
        setBorderWidth(40);
        rec.setBorderColor(256 * 256 * 88 + 256 * 84 + 84);
        rec.setBorderWidth(40);
        setFillColor(new Color(88, 84, 84), 0);
        rec.setFillColor(new Color(88, 84, 84), 0);
    }
    void destroy()
    {
        super.destroy();
        rec.destroy();
    }
    void setVisible(boolean visible)
    {
        super.setVisible(visible);
        rec.setVisible(visible);
    }
    void bringToFront()
    {
        super.bringToFront();
        rec.bringToFront();
    }*/
}

