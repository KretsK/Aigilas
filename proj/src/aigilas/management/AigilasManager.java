package aigilas.management;

import aigilas.creatures.CreatureFactory;
import aigilas.dungeons.Dungeon;
import aigilas.dungeons.DungeonFactory;
import sps.entities.EntityManager;
import sps.net.Client;

public class AigilasManager {
    private AigilasManager(){}
    private static AigilasManager __instance;
    public static AigilasManager get(){
        if(__instance == null){
            __instance = new AigilasManager();
        }
        return __instance;
    }

    public void reset(){
        __instance = new AigilasManager();
        System.out.println("Generating the dungeon...");
        EntityManager.reset();
        CreatureFactory.reset();
        DungeonFactory.start();
        Dungeon.reset();
        Client.get().dungeonHasLoaded();
    }
}
