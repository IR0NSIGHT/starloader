package api.utils;

import java.util.ArrayList;

public abstract class StarRunnable {
    private long ticks;
    private long frequency;

    public abstract void run();
    public void runLater(long ticks){
        this.ticks = ticks;
        this.delay = true;
    }
    public void runTimer(long frequency){
        this.frequency = frequency;
        this.timer = true;
    }
    long time = 0;
    private boolean delay = false;
    private boolean timer = false;
    public StarRunnable(){
        register();
    }
    private boolean queuedForDelete = false;
    public void cancel(){
        queuedForDelete = true;
    }
    private void tick(){
        time++;
        if(this.delay) {
            if (time > ticks){
                run();
                cancel();
            }
        }
        if(this.timer){
            if(time > frequency){
                run();
                time = 0;
            }
        }
    }
    private void register(){
        runnables.add(this);
    }
    private static ArrayList<StarRunnable> runnables = new ArrayList<>();
    public static void tickAll(){
        ArrayList<StarRunnable> list = new ArrayList<>();
        for(StarRunnable runnable : runnables){
            runnable.tick();
            if(runnable.queuedForDelete){
                list.add(runnable);
            }
        }
        for(StarRunnable runnable : list){
            runnables.remove(runnable);
        }
    }
}

