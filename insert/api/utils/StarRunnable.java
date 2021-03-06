package api.utils;

import api.DebugFile;

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
    public long ticksRan = 0;
    private long time = 0;
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
        ticksRan++;
        if(this.delay) {
            if (time > ticks){
                cancel();
                run();
            }
        }
        if(this.timer){
            if(time > frequency){
                time = 0;
                run();
            }
        }
    }
    private int errorCount = 0;
    private synchronized void register(){
        registerQueue.add(this);
    }
    private static ArrayList<StarRunnable> runnables = new ArrayList<StarRunnable>();
    private static ArrayList<StarRunnable> registerQueue = new ArrayList<StarRunnable>();
    public static void tickAll(){
        ArrayList<StarRunnable> list = new ArrayList<StarRunnable>();
        for(StarRunnable runnable : runnables){
            try {
                runnable.tick();
            }catch (Exception e){
                DebugFile.err("A StarRunnable ("+ runnable.getClass().getSimpleName() +") threw an error");
                DebugFile.logError(e, null);
                if(runnable.errorCount++ >= 10) {
                    DebugFile.err("=== !!! THIS RUNNABLE WILL BE TERMINATED (threw 10 errors) !!! ===");
                    runnable.queuedForDelete = true;
                }
            }
            if(runnable.queuedForDelete){
                list.add(runnable);
            }
        }
        for(StarRunnable runnable : list){
            runnables.remove(runnable);
        }
        runnables.addAll(registerQueue);
        registerQueue.clear();
    }
    public static void deleteAll() {
        registerQueue.clear();
        runnables.clear();
    }
}

