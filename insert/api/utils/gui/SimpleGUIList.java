package api.utils.gui;

import api.ModPlayground;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;

import java.util.*;

public abstract class SimpleGUIList<T> extends ScrollableTableList<T> {
    @Override
    public void draw() {
        super.draw();
    }

    public SimpleGUIList(InputState inputState, float v, float v1, GUIElement guiElement) {
        super(inputState, v, v1, guiElement);
    }

    private final ArrayList<RowStringCreator<T>> orderedEntryCreators = new ArrayList<RowStringCreator<T>>();

    public void createColumn(String name, float weight, Comparator<T> comparator, RowStringCreator<T> creator) {
        this.addColumn(name, weight, comparator);
        orderedEntryCreators.add(creator);
    }

    public void createColumn(String name, float weight, RowStringCreator<T> creator) {
        createColumn(name, weight, new Comparator<T>() {
            //Assume they are strings and sort them in alphabetical order, this should work for 99% of all cases
            @Override
            public int compare(T t, T t1) {
                return t.toString().compareToIgnoreCase(t1.toString());
            }
        }, creator);

    }

    public void addDefaultFilter() {
        this.addTextFilter(new GUIListFilterText<T>() {
            public boolean isOk(String filter, T listItem) {
                //Should work atleast 50% of the time
                return listItem.toString().toLowerCase(Locale.ENGLISH).contains(filter.toLowerCase(Locale.ENGLISH));
            }
        }, ControllerElement.FilterRowStyle.FULL);
    }

    private ArrayList<ButtonStructure<T>> buttonCreators = new ArrayList<ButtonStructure<T>>();

    public void createEntryButton(GUITextButton.ColorPalette palette, String text, EntryCallback<T> callback) {
        buttonCreators.add(new ButtonStructure<T>(palette, text, callback)) ;
    }


    //2 Types of things to register for list entries:
    // GUITextOverlayTable -> these are put into the Row, they must be in order and of the same length as the columns
    //      Row.expanded (type GUIElementList) this is when the player clicks on it, and it expands
    // Expanded Row Items -> Attached to a GUIAnchor, then the GUIAnchor is put into an GUIElementList and that GUIElementList is added to the Row.expanded
    @Override
    public void updateListEntries(GUIElementList guiListElements, Set<T> entries) {
        int columnSize = columns.size();
        for (final T entry : entries) {
            //GUI Overlay Tables
            ArrayList<GUIElement> columnData = new ArrayList<GUIElement>(columnSize);
            for (final RowStringCreator<T> creator : orderedEntryCreators) {
                GUITextOverlayTable table = new GUITextOverlayTable(10, 10, this.getState());
                table.getPos().y = 5.0F;
                table.setTextSimple(new Object() {
                    @Override
                    public String toString() {
                        return creator.update(entry);
                    }
                });
                columnData.add(table);
            }
            SimpleGUIRow row = new SimpleGUIRow(this.getState(), entry, columnData.toArray(new GUIElement[0]));
            row.expanded = new GUIElementList(this.getState());

            //Main row item (anchor)
            GUIAncor anchor = new GUIAncor(this.getState(), 100.0F, 30.0F);

            //Expanded Row Items
            int buttonsAdded = 0;
            int buttonWidth = 60;
            for (final ButtonStructure<T> buttonCreator : buttonCreators) {
                GUITextButton button = new GUITextButton(this.getState(), buttonWidth, 24, buttonCreator.palette, buttonCreator.text, new GUICallback() {
                    @Override
                    public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                        buttonCreator.callback.on((SimpleGUIList<T>) guiElement, entry, mouseEvent);
                    }

                    @Override
                    public boolean isOccluded() {
                        return !SimpleGUIList.this.isActive();
                    }
                });
                anchor.attach(button);
                button.setPos((buttonWidth+20) * buttonsAdded, 0, 0);
                buttonsAdded++;
            }


            //Create expanded row
            row.expanded.add(new GUIListElement(anchor, anchor, this.getState()));
            row.onInit();
            guiListElements.addWithoutUpdate(row);

        }
        guiListElements.updateDim();
    }


    private class SimpleGUIRow extends ScrollableTableList<T>.Row {
        public SimpleGUIRow(InputState inputState, T t, GUIElement... guiElements) {
            super(inputState, t, guiElements);
            this.highlightSelect = true;
        }
    }

    private static class ButtonStructure<T> {
        final GUITextButton.ColorPalette palette;
        final String text;
        final EntryCallback<T> callback;

        ButtonStructure(GUITextButton.ColorPalette palette, String text, EntryCallback<T> callback) {

            this.palette = palette;
            this.text = text;
            this.callback = callback;
        }
    }



}

