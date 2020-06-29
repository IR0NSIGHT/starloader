package api.utils.gui;

import api.common.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleGUIBuilder extends GUIElement implements GUIActiveInterface {
    public GUIMainWindow mainPanel;
//    public GUITextButton buttonTest;
    private boolean init;
    private boolean tabsCreated = false;
    private GUIContentPane lastTab;

    public GUIContentPane getLastTab() {
        return lastTab;
    }

    //Visibility settings
    private boolean visible = true;
    public void setVisible(boolean b){
        visible = b;
    }

    public boolean isVisible() {
        return visible;
    }

    //Tab -> List of Lines -> List of elements
    private HashMap<GUIContentPane, ArrayList<LineContents>> lineElements = new HashMap<GUIContentPane, ArrayList<LineContents>>();
    private int currentLine = 0;

    //Line options
    private LineContents getCurrentLine(){
        ArrayList<LineContents> lines = lineElements.get(lastTab);

        //If the tab has no lines in it yet, add a new blank arraylist
        if(lines == null){
            ArrayList<LineContents> newLines = new ArrayList<LineContents>();
            lineElements.put(lastTab, newLines);
            lines = newLines;
        }

        //If the line doesnt exist yet, populate it,
        //  also generate all lines before it incase they called newLine() a bunch of times and didnt populate the previous lines
        while(currentLine >= lines.size()){
            lines.add(new LineContents());
        }

        return lines.get(currentLine);
    }
    public SimpleGUIBuilder newLine(){
        currentLine++;

        return this;
    }
    public SimpleGUIBuilder setCurrentLineHeight(int height){
        getCurrentLine().setLineHeight(height);
        return this;
    }
    public SimpleGUIBuilder scaleCurrentLine(){
        getCurrentLine().setLineHeight(-1);
        return this;
    }

    private SimpleGUIBuilder(InputState var1) {
        super(var1);
    }
    public void addSimpleGUIList(SimpleGUIList<?> list){
        newLine();
        newLine();
//        GUIAncor anchor = anchor(300);
        list.onInit();
        getLastTab().getContent(0).attach(list);
        getCurrentLine().getElements().add(list);
        setCurrentLineHeight(250);
    }
    public SimpleGUIBuilder dynamicText(Color slickColor, UnicodeFont font, Object stringFunction){
        GUITextOverlay t = new GUITextOverlay(100, 100, this.getState());
        t.setFont(font);
        t.setColor(slickColor);
        t.setTextSimple(stringFunction);
        getCurrentLine().getElements().add(t);
        return this;
    }
    public SimpleGUIBuilder fixedText(final String name, Color slickColor, UnicodeFont font){
        GUITextOverlay t = new GUITextOverlay(100, 100, this.getState());
        t.setTextSimple(new Object(){
            @Override
            public String toString() {
                return name;
            }
        });
        t.setColor(slickColor);
        t.setFont(font);
        getCurrentLine().getElements().add(t);
        return this;
    }
    private GUIAncor anchor(int height){
        GUIAncor anchor = new GUIAncor(this.getState(), 100,100);
        anchor.getPos().set(0,0,0);
        getCurrentLine().getElements().add(anchor);
        return anchor;
    }
    public SimpleGUIBuilder button(final String name, GUICallback fct){
        return button(name, GUIHorizontalArea.HButtonType.BUTTON_BLUE_MEDIUM, fct);
    }
    public SimpleGUIBuilder button(final String name, GUIHorizontalArea.HButtonType type, GUICallback fct){
        //Get list of lines from the current tab
        LineContents currentLine = getCurrentLine();
        GUIHorizontalButton e = new GUIHorizontalButton(this.getState(), type, new Object() {
            final String nameCopy = name;

            @Override
            public String toString() {
                return nameCopy;
            }
        }, fct, this.mainPanel.activeInterface, new GUIActivationCallback(){

            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        currentLine.elements.add(e);
        return this;
    }
    public SimpleGUIBuilder padding(){
        new GUITile(this.getState(), 10, 10, new Object(){
            @Override
            public String toString() {
                return "";
            }
        });
        return this;
    }
    public static SimpleGUIBuilder newBuilder(String tabName){
        SimpleGUIBuilder builder = new SimpleGUIBuilder(GameClient.getClientState());
        builder.onInit();
        builder.newTab(tabName);
//        builder.lastTab.setTextBoxHeightLast(123);
//        builder.lastTab.addNewTextBox(1);
        builder.mainPanel.setSelectedTab(0);
        builder.tabsCreated = true;
        return builder;
    }

    public void cleanUp() {
    }

    public void draw() {
        if (this.init && visible) {
            this.mainPanel.draw();
            update(null);
            ArrayList<LineContents> lines = lineElements.get(getSelectedTabElement());
            for (LineContents line : lines) {
                for (GUIElement guiElement : line.elements) {
                    if(!(guiElement instanceof SimpleGUIList)){
                        guiElement.draw();
                    }
                }
            }
        }
    }

    public void onInit() {
        if (this.mainPanel != null) {
            this.mainPanel.cleanUp();
        }

        this.mainPanel = new GUIMainWindow(this.getState(), 750, 550, "SimpleGUIBuilder");
        this.mainPanel.onInit();

        this.mainPanel.setCloseCallback(new GUICallback() {
            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
//                    SimpleGUIBuilder.this.getState().getWorldDrawer().getGuiDrawer().getPlayerPanel().deactivateAll();
                    setVisible(false);
                }
            }
            public boolean isOccluded() {
                return !SimpleGUIBuilder.this.getState().getController().getPlayerInputs().isEmpty();
            }
        });
        this.mainPanel.orientate(48);
        //Put tabs
//        this.recreateTabs();
        this.mainPanel.clearTabs();

        this.mainPanel.activeInterface = this;
        this.init = true;


    }
    public SimpleGUIBuilder newTab(String name){
        lastTab = this.mainPanel.addTab(name);
        currentLine = 0;
        return this;
    }
    private GUIContentPane getSelectedTabElement(){
        return this.mainPanel.getTabs().get(this.mainPanel.getSelectedTab());
    }


    public void update(Timer var1) {
        if (this.init && visible) {
            GUIContentPane tab = this.getSelectedTabElement();
            float frameWidth = mainPanel.getInnerWidth() - 26;
            float frameHeight = mainPanel.getInnerHeigth() - 70;
            ArrayList<LineContents> lines = lineElements.get(tab);
            int lineIndex = 0;
            int lineHeightProgress = 0;
            for (LineContents line : lines) {
                int xIndex = 0;
                int elementsOnLine = line.getElements().size();
                float elementLength = ((frameWidth) / elementsOnLine);

                Vector3f originPos = new Vector3f(mainPanel.getPos().x+41, mainPanel.getPos().y+70,0);
                for (GUIElement guiElement : line.getElements()) {
                    //Update position (and size if needed)
                    //Handle lists differently because they are wack
                    int elementHeight = line.getLineHeight();
                    if(elementHeight == -1){
                        //Scale to bottom
                        elementHeight = (int) (frameHeight-lineHeightProgress);
                    }
                    if(guiElement instanceof SimpleGUIList){
                        guiElement.getPos().set(
                                (elementLength * xIndex),
                                 lineHeightProgress,
                                0);
                        SimpleGUIList<?> list = ((SimpleGUIList<?>) guiElement);
                        list.getScrollPanel().dependent = null;//Breaks custom line elements
                        list.getScrollPanel().setWidth(elementLength);
                        list.getScrollPanel().setHeight(elementHeight);
                    }else {
                        guiElement.getPos().set(
                                originPos.x + (elementLength * xIndex),
                                originPos.y + lineHeightProgress,
                                0);
                        if (guiElement instanceof GUIResizableElement) {
                            ((GUIResizableElement) guiElement).setWidth(elementLength);
                            ((GUIResizableElement) guiElement).setHeight(elementHeight);
                        }
                    }
                    xIndex++;
                }
                //Update y position for the next line
                lineHeightProgress += line.getLineHeight();
                lineIndex++;
            }
        }
    }

    public PlayerState getPlayer() {
        return this.getState().getPlayer();
    }

    public float getHeight() {
        return this.mainPanel.getHeight();
    }

    public GameClientState getState() {
        return (GameClientState)super.getState();
    }

    public float getWidth() {
        return this.mainPanel.getWidth();
    }

    public boolean isActive() {
//        return this.getState().getController().getPlayerInputs().isEmpty();
        return isVisible();
    }


    public void reset() {
        this.mainPanel.reset();
    }
}
class LineContents {
    ArrayList<GUIElement> elements;
    int lineHeight;

    public LineContents() {
        this.elements = new ArrayList<GUIElement>();
        this.lineHeight = 30;
    }

    public ArrayList<GUIElement> getElements() {
        return elements;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }
}
