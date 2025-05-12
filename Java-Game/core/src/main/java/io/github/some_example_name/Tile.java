package io.github.some_example_name;

public class Tile extends Main {

    void teleport() //sollte gehen, nur collidesWith muss definiert werden.
   {
      for (MyTile tile : currentlevel.teleporters)
      {
         if(Player.hitbox.collidesWith(tile.hitbox)) //Hitbox im Player erstellen
         {
             level1 = new Level(LevelList.levels[levelzahl+1], this);
             levelzahl++;
             level1.load();
            break;
         }
      }

/*
      if(player.collisionOn) {
         for (Projectile prc : currentlevel.projectiles)
         {
            if(player.hitbox.collidesWith(prc.hitbox))
            {
               currentlevel.projectiles.remove(prc);
               player.damageby(prc.damage);
               prc.destroy();

            }
         }
         //print("\n" + currentlevel.projectiles.size());
      }
     */
    /*
    boolean collidesWithWalls(Entity enti)
   {
      if(!enti.collisionOn) {
         return false;
      }
      for (MyTile tile : loadedwalls) {
         if(enti.collisionOn && enti.hitbox.collidesWith(tile.hitbox)) {

            return true;
         }
      }
      return false;
   }
     */
}
