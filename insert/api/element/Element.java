package api.element;

import api.main.Global;

public class Element {

    private String fullName;
    private String description;
    private boolean inShop = false;
    private boolean inRecipe = false;
    private boolean deprecated = false;
    private int buildIcon;
    private Recipe recipe = null;
    private Category category;
    private String name;
    private int id;
    public Element(int elementId, String elementName) {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe blockRecipe) {
        this.recipe = blockRecipe;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String string) {
        this.fullName = string;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public boolean isInShop() {
        return inShop;
    }

    public void setInShop(boolean bool) {
        this.inShop = bool;
    }

    public boolean isInRecipe() {
        return inRecipe;
    }

    public void setInRecipe(boolean bool) {
        this.inRecipe = bool;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean bool) {
        this.deprecated = bool;
    }

    public int getBuildIcon() {
        return buildIcon;
    }

    public void setBuildIcon(int value) {
        this.buildIcon = value;
    }

    public static Element getElementFromID(int id) {
        return Global.elementList.get(id);
    }
}