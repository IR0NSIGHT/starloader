package api.listener.events;

/**
 * STARMADE MOD
 * CREATOR: IR0NSIGHT
 * DATE: 25.08.2020
 * TIME: 15:48
 */
public class UniverseGenerationEvent extends Event {
    /**
     * fired when a new universe is generated. holds all values needed to tweak the random generation
     * starCount, armCount, spead, range and rotation are vanilla fields
     * starChance is a modded field. 1/starChance stars will be created. starcount will not be met if starchance != 1
     * Current state: modifiying values through event doesnt seem to work. dont know why
     */
    private double starCount;
    private double armCount;
    private double spread;
    private double range;
    private double rotation;
    private int starChance;

    public UniverseGenerationEvent(double starCount, double armCount, double spread , double range, double rotation, int starChance) {
        this.starCount = starCount;
        this.armCount = armCount;
        this.spread = spread ;
        this.range =range;
        this.rotation = rotation;
        this.starChance = starChance;
    }

    public void setStarCount(double c) {
        starCount = c;
    }

    public void  setArmCount(double c) {
        armCount = c;
    }

    public void setSpread (double s) {
        spread = s;
    }

    public void setRange (double r) {
        range = r;
    }

    public void setRotation(double r) {
        rotation = r;
    }

    public void setStarChance (int c) {
        starChance = c;
    }

    public int getStarChance () {
        return starChance;
    }

    public double getArmCount() {
        return armCount;
    }

    public double getRange() {
        return range;
    }

    public double getRotation() {
        return rotation;
    }

    public double getSpread() {
        return spread;
    }

    public double getStarCount() {
        return starCount;
    }
}
