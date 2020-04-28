package api.utils;

import java.util.ArrayList;

public class Cooldown {
    private static ArrayList<String> cooldowns = new ArrayList<String>();
    public static boolean cooldown(final String uid, int ticks){
        if(cooldowns.contains(uid)){
            return false;
        }
        cooldowns.add(uid);
        new StarRunnable(){
            @Override
            public void run() {
                cooldowns.remove(uid);
            }
        }.runLater(ticks);
        return true;
    }
}
