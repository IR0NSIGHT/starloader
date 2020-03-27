package api.systems;

import api.element.Element;

public class Reactor {

    //private final Element reactorBlock = Element.getElementFromName("REACTOR_POWER");
    //private final Element stabilizerBlock = Element.getElementFromName("REACTOR_STABILIZER");

    private int size;
    private double stabilization;
    //private ChamberTree chambers;
    //ToDo: Implement chamber stuff
    private double regen;
    private double currentUsage;
    private int level;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getStabilization() {
        return stabilization;
    }

    public void setStabilization(double stabilization) {
        this.stabilization = stabilization;
    }

    public double getRegen() {
        return regen;
    }

    public void setRegen(double regen) {
        this.regen = regen;
    }

    public double getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(double currentUsage) {
        this.currentUsage = currentUsage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
