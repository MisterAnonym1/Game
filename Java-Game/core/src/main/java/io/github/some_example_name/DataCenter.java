package io.github.some_example_name;

public class DataCenter
{
    Main main;
    Level level;
    Player player;
    static int deathcount;
    static int enemysKilled;
    float damageInLastSecond;
    DataCenter(Main main)
    {
        this.main=main;
        level=main.currentlevel;
        player=main.Player;
    }
    public boolean areEnemysRemaining()
    {
        if(level==null)return false;
        if(Level.gegnerliste.size() == 0)return false;
        for(Gegner geg: Level.gegnerliste)
        {
            if(geg.getSignature()!=99)
            {
                return true;
            }
        }
        return false;
    }

    void act(float delta)
    {

    }
}
