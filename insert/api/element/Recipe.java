package api.element;

import java.util.Map;

public class Recipe {

    private double bakeTime = 0;
    private int numberProduced = 0;
    private Map<Element, Integer> ingredients = null;

    public Recipe() {
    }

    public double getBakeTime() {
        return this.bakeTime;
    }

    public void setBakeTime(double bakeTime) {
        this.bakeTime = bakeTime;
    }

    public int getNumberProduced() {
        return this.numberProduced;
    }

    public void setNumberProduced(int numberProduced) {
        this.numberProduced = numberProduced;
    }

    public Map<Element, Integer> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Map<Element, Integer> ingredients) {
        this.ingredients = ingredients;
    }
}
