package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
class Boss extends Gegner {
    String bossName="";
    HealthBar bossbar;
    OwnText bossTitel;
    Boss (float x, float y, Main logic, String filepath) {
        super(x, y, logic, filepath);
        bossbar = new HealthBar(512, 500, maxhealth, 1.8f, 1f, Main.uiStage.getViewport());
        bossbar.centeratX();
        bossbar.setColor(Color.SCARLET);
        Main.uiStage.addActor(bossbar);
        bossbar.toBack();
        bossTitel=new OwnText("",512, 490,28, Color.WHITE,Color.BLACK);
        Main.uiStage.addActor(bossTitel);
        bossTitel.toBack();
    }

    @Override
    public void destroy() {
        super.destroy();
        bossbar.remove();
        bossTitel.remove();
    }
    @Override
    boolean damageby(float damage){
        bossbar.takeDamage(damage);
        curhealth-=damage;
        if(curhealth <= 0) {
            return true;
        } else{
            return false;
        }
    }
    @Override
    void sethealth(float health, boolean ignoremax) {
        super.sethealth(health, ignoremax);
        bossbar.maxHealth=maxhealth;
        bossbar.healTo(health);
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
        bossTitel.setText(bossName);
        bossTitel.center();
    }
}


