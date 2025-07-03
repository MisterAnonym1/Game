package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public class DataCenter
{
    Main main;
    Level level;
    Player player;
    private static int deathcount=0;
    private static int enemysKilled;
    float damageInLastSecond;
    private static int levelnumber=0;
    private static double timeplayed;
    DataCenter(Main main)
    {
        this.main=main;
        level=main.currentlevel;
        player=main.Player;
        setTimeplayed(Main.invManager.getValueByKey("Playtime"));
    }
    public boolean areEnemysRemaining()
    {
        if(level==null)return false;
        if(Level.gegnerliste.isEmpty())return false;
        for(Gegner geg: Level.gegnerliste)
        {
            if(geg.getSignature()!=99)
            {
                return true;
            }
        }
        return false;
    }

    public static void setLevelnumber(int levelnumber) {
        DataCenter.levelnumber = levelnumber;
    }

    public static int getLevelnumber() {
        return levelnumber;
    }

    public static void increaseDeathcount(/*by 1*/) {
        DataCenter.deathcount +=1;
    }

    public static int getDeathcount() {
        return deathcount;
    }
    public static void newlevel() {
        levelnumber++;
    }
    public static void updateTimeplayed()
    {
        timeplayed+= Gdx.graphics.getDeltaTime();
    }


    public static double getTimeplayed() {
        return timeplayed;
    }
    public static String getformatedTimeplayed() {
        int hours= (int)Math.floor(timeplayed/3600f);
        int minutes=(int)Math.floor(timeplayed%3600f/60);
        int seconds=(int)Math.round(timeplayed%60);
        return "0"+hours+":"+(minutes<10?"0":"")+minutes+":"+(seconds<10?"0":"")+seconds;
    }
    public static void resetTimeplayed() {
        timeplayed=0;
    }
    public static void setTimeplayed(double timeplayed) {
        DataCenter.timeplayed = timeplayed;
    }

    void act(float delta)
    {

    }

}
