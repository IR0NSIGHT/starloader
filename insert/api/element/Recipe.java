package api.element;

import java.util.Map;

public class Recipe {

    private double bakeTime = 0;
    private int numberProduced = 0;
    private Map<ElementList, Integer> ingredients = null;

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

    public Map<ElementList, Integer> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Map<ElementList, Integer> ingredients) {
        this.ingredients = ingredients;
    }
}
