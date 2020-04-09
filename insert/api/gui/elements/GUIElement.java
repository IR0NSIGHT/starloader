package api.gui.elements;

import api.gui.GUIPanel;
import org.schema.schine.input.InputState;

public class GUIElement extends org.schema.schine.graphicsengine.forms.gui.GUIElement {

    private int width;
    private int height;
    private int posX = 0;
    private int posY = 0;
    private GUIPanel containerPanel;

    public GUIElement(InputState inputState, int width, int height) {
        super(inputState);
        this.width = width;
        this.height = height;
    }

    public GUIElement(InputState inputState, int width, int height, int posX, int posY) {
        super(inputState);
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void draw() {

    }

    public void drawAt(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        draw();
    }

    @Override
    public void onInit() {

    }
}
