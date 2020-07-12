//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.graphicsengine.forms.gui.newgui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.vecmath.Vector4f;
import org.schema.common.util.CompareTools;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.Draggable;
import org.schema.schine.graphicsengine.forms.gui.DropTarget;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIColoredAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIColoredRectangle;
import org.schema.schine.graphicsengine.forms.gui.GUIColoredRectangleLeftRightShadow;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.GUIEnterableList;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.forms.gui.GUIResizableElement;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.ControllerElement.FilterPos;
import org.schema.schine.graphicsengine.forms.gui.newgui.ControllerElement.FilterRowStyle;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea.HButtonType;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.ListColorPalette;
import org.schema.schine.input.InputState;

public abstract class ScrollableTableList<E> extends GUIElement implements Observer {
    protected final ObjectArrayList<ScrollableTableList<E>.Column> columns = new ObjectArrayList();
    protected final Vector4f colorRowHighlight;
    private final Vector4f colorRowA;
    private final Vector4f colorRowB;
    private Vector4f draggingBGColor;
    public int activeSortColumnIndex;
    protected int continousSortColumn;
    protected int columnsHeight;
    protected Set<E> toAddTmp;
    private GUIScrollablePanel scrollPanel;
    private GUIElementList mainList;
    private boolean dirty;
    private GUIElement dependent;
    private int columnWeightedColumnsCount;
    private float columnTotalWeightParts;
    private float columnTotalFixedParts;
    private boolean columnsWidthChanged;
    private int columnFixedColumnsCount;
    private final FilterController<E> filterController;
    private ScrollableTableList<E>.Row selectedRow;
    private boolean sortedYet;
    protected ScrollableTableList<E>.Column defaultSortByColumn;
    private boolean init;
    public SelectedObjectCallback selCallback;
    public ScrollableTableList<E>.Row extendedRow;
    public static FontSize tableFontSize;
    public static FontSize dropdownFontSize;
    public static FontSize innerFontSize;
    private final Object2ObjectOpenHashMap<String, ScrollableTableList<E>.Seperator> seperators;
    private int columnHeight;

    public ScrollableTableList<E>.GUIClippedRow getSimpleRow(Object var1, GUIActiveInterface var2) {
        ScrollableTableList.GUIClippedRow var3;
        (var3 = new ScrollableTableList.GUIClippedRow(this.getState())).activationInterface = var2;
        GUITextOverlayTable var4;
        (var4 = new GUITextOverlayTable(10, 10, this.getState())).setTextSimple(var1);
        var3.attach(var4);
        return var3;
    }

    public ScrollableTableList<E>.Seperator getSeperator(String var1, int var2) {
        String var3 = var1 + var2;
        ScrollableTableList.Seperator var4;
        if ((var4 = (ScrollableTableList.Seperator)this.seperators.get(var3)) == null) {
            (var4 = new ScrollableTableList.Seperator(this.getState(), var2)).name = var1;
            var4.onInit();
            ScrollableTableList.Seperator var5 = (ScrollableTableList.Seperator)this.seperators.put(var3, var4);

            assert var5 == null;
        }

        return var4;
    }

    public ScrollableTableList(InputState var1, float var2, float var3, GUIElement var4) {
        super(var1);
        this.colorRowHighlight = ListColorPalette.selectedColor;
        this.colorRowA = ListColorPalette.mainListBackgroundColor;
        this.colorRowB = ListColorPalette.mainListBackgroundColorAlternate;
        this.draggingBGColor = new Vector4f(0.3F, 0.5F, 0.123F, 1.0F);
        this.activeSortColumnIndex = -1;
        this.continousSortColumn = -1;
        this.columnsHeight = 28;
        this.toAddTmp = new ObjectOpenHashSet();
        this.dirty = true;
        this.columnsWidthChanged = true;
        this.seperators = new Object2ObjectOpenHashMap();
        this.dependent = var4;
        this.filterController = new FilterController(this);
        this.scrollPanel = new GUIScrollablePanel(var2, var3 - (float)this.filterController.filterHeightBottom, var4, var1) {
            public boolean isActive() {
                return ScrollableTableList.this.isActive();
            }
        };
    }

    public void cleanUp() {
        this.mainList.clear();
    }

    public void clear() {
        this.mainList.clear();
        this.dirty = true;
    }

    private void handleDirty() {
        if (this.dirty) {
            this.toAddTmp.clear();
            Collection var1 = this.getElementList();

            assert this.noneNull(var1);

            this.toAddTmp.addAll(var1);

            for(int var4 = 0; var4 < this.mainList.size(); ++var4) {
                GUIListElement var2;
                if ((var2 = this.mainList.get(var4)) instanceof ScrollableTableList.Seperator) {
                    this.mainList.removeWithoutUpdate(var4);
                } else {
                    E var5 = ((ScrollableTableList<E>.Row)var2).getSort();
                    boolean var3 = this.toAddTmp.remove(var5);
                    if (!this.isFiltered(var5) && var3) {
                        continue;
                    }

                    if ((var2 = this.mainList.removeWithoutUpdate(var4)) != null) {
                        var2.cleanUp();
                    }
                }

                --var4;
            }

            Iterator<E> var6 = this.toAddTmp.iterator();

            while(var6.hasNext()) {
                if (this.isFiltered(var6.next())) {
                    var6.remove();
                }
            }

            if (!this.sortedYet && this.defaultSortByColumn != null) {
                this.defaultSortByColumn.sortBy(true);
                this.sortedYet = true;
            }

            this.seperators.clear();
            this.updateListEntries(this.mainList, this.toAddTmp);
            this.mainList.updateDim();
            if (this.activeSortColumnIndex >= 0) {
                ((ScrollableTableList.Column)this.columns.get(this.activeSortColumnIndex)).sort();
                this.insertSeperators();
            }

            this.dirty = false;
        }

    }

    public void draw() {
        assert this.init;

        this.handleDirty();
        if (this.continousSortColumn >= 0 && this.continousSortColumn == this.activeSortColumnIndex) {
            ((ScrollableTableList.Column)this.columns.get(this.activeSortColumnIndex)).sort();
        }

        GlUtil.glPushMatrix();
        this.transform();
        this.columnHeight = 24;
        this.filterController.drawTop(0);
        int var1 = 0;

        for(int var2 = 0; var2 < this.columns.size(); ++var2) {
            int var3 = ((ScrollableTableList.Column)this.columns.get(var2)).draw(var1);
            ((ScrollableTableList.Column)this.columns.get(var2)).seperator.setHeight((int)this.mainList.getHeight());
            var1 += var3;
        }

        this.filterController.drawContent(this.scrollPanel, this.columnHeight);
        this.filterController.drawBottom(this.scrollPanel, this.columnHeight);
        Iterator var4 = this.getChilds().iterator();

        while(var4.hasNext()) {
            ((AbstractSceneNode)var4.next()).draw();
        }

        if (this.isMouseUpdateEnabled()) {
            this.checkMouseInside();
        }

        GlUtil.glPopMatrix();
        this.columnsWidthChanged = false;
    }

    private boolean noneNull(Collection<E> var1) {
        Iterator var2 = var1.iterator();

        do {
            if (!var2.hasNext()) {
                return true;
            }
        } while(var2.next() != null);

        System.err.println("ERROR: " + var1);
        return false;
    }

    public void onInit() {
        this.initColumns();
        this.columns.trim();
        this.columnTotalFixedParts = 0.0F;
        this.columnTotalWeightParts = 0.0F;
        this.columnFixedColumnsCount = 0;
        this.columnWeightedColumnsCount = 0;

        int var1;
        for(var1 = 0; var1 < this.columns.size(); ++var1) {
            if (((ScrollableTableList.Column)this.columns.get(var1)).fixedSize >= 0) {
                this.columnTotalFixedParts += (float)((ScrollableTableList.Column)this.columns.get(var1)).fixedSize;
                ++this.columnFixedColumnsCount;
            } else {
                this.columnTotalWeightParts += 1.0F + ((ScrollableTableList.Column)this.columns.get(var1)).weigth;
                ++this.columnWeightedColumnsCount;
            }
        }

        for(var1 = 0; var1 < this.columns.size(); ++var1) {
            ((ScrollableTableList.Column)this.columns.get(var1)).init(this.getState());
        }

        this.mainList = new GUIElementList(this.getState());
        this.scrollPanel.setContent(this.mainList);
        this.mainList.setScrollPane(this.scrollPanel);

        for(var1 = 0; var1 < this.columns.size(); ++var1) {
            Sprite var2 = Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI 8px Vertical-32x1-gui-");
            float var3 = 1.0F / (float)var2.getMultiSpriteMaxX();
            float var4 = 1.0F / (float)var2.getMaterial().getTexture().getWidth();
            GUITexDrawableArea var5;
            (var5 = new GUITexDrawableArea(this.getState(), var2.getMaterial().getTexture(), 10.0F * var3 + var4 * 2.0F, 0.0F)).setColor(new Vector4f(1.0F, 1.0F, 1.0F, 1.0F));
            var5.setWidth(2);
            var5.renderMode = 2;
            ((ScrollableTableList.Column)this.columns.get(var1)).seperator = var5;
            if (var1 > 0) {
                this.mainList.attach(var5);
            }
        }

        this.filterController.calcInit();
        this.init = true;
    }

    public abstract void initColumns();

    protected ScrollableTableList<E>.Column addColumn(String var1, float var2, Comparator<E> var3) {
        return this.addColumn(var1, var2, var3, this.columns.isEmpty());
    }

    protected ScrollableTableList<E>.Column addColumn(String var1, float var2, Comparator<E> var3, boolean var4) {
        ScrollableTableList.Column var5 = new ScrollableTableList.Column(var1, var2, this.columns.size(), var3);
        this.columns.add(var5);
        if (var4) {
            this.defaultSortByColumn = var5;
        }

        return var5;
    }

    protected ScrollableTableList<E>.Column addFixedWidthColumn(String var1, int var2, Comparator<E> var3) {
        return this.addFixedWidthColumn(var1, var2, var3, this.columns.isEmpty());
    }

    protected ScrollableTableList<E>.Column addFixedWidthColumn(String var1, int var2, Comparator<E> var3, boolean var4) {
        ScrollableTableList.Column var5;
        (var5 = new ScrollableTableList.Column(var1, 0.0F, this.columns.size(), var3)).fixedSize = var2;
        this.columns.add(var5);
        if (var4) {
            this.defaultSortByColumn = var5;
        }

        return var5;
    }

    protected abstract Collection<E> getElementList();

    public void flagDirty() {
        this.dirty = true;
    }

    public abstract void updateListEntries(GUIElementList var1, Set<E> var2);

    public float getHeight() {
        return (float)this.filterController.filterHeightBottom + this.scrollPanel.getHeight();
    }

    public float getContentHeight() {
        return this.scrollPanel.getHeight();
    }
    //INSERTED CODE @223
    public GUIScrollablePanel getScrollPanel() {
        return scrollPanel;
    }
    ///

    public FilterController<E> getFilterController() {
        return filterController;
    }

    public float getWidth() {
        return this.scrollPanel.getWidth();
    }

    public boolean isActive() {
        return this.dependent.isActive();
    }

    public void update(Observable var1, Object var2) {
        if (var1 != null && var1 instanceof GUIEnterableList) {
            this.mainList.updateDim();
        } else {
            this.dirty = true;
        }
    }

    public void addButton(GUICallback var1, String var2, FilterRowStyle var3, FilterPos var4) {
        this.filterController.addButton(var1, var2, var3, var4);
    }

    public void addButton(GUICallback var1, String var2, HButtonType var3, FilterRowStyle var4, FilterPos var5) {
        this.filterController.addButton(var1, var2, var3, var4, var5);
    }

    public <O> void addDropdownFilter(GUIListFilterDropdown<E, O> var1, CreateGUIElementInterface<O> var2, FilterRowStyle var3) {
        this.filterController.addDropdownFilter(var1, var2, var3);
    }

    public <O> void addDropdownFilter(GUIListFilterDropdown<E, O> var1, CreateGUIElementInterface<O> var2, FilterRowStyle var3, FilterPos var4) {
        this.filterController.addDropdownFilter(var1, var2, var3, var4);
    }

    public void addTextFilter(GUIListFilterText<E> var1, String var2, FilterRowStyle var3) {
        this.filterController.addTextFilter(var1, var2, var3);
    }

    public void addTextFilter(GUIListFilterText<E> var1, String var2, FilterRowStyle var3, FilterPos var4) {
        this.filterController.addTextFilter(var1, var2, var3, var4);
    }

    public void addTextFilter(GUIListFilterText<E> var1, FilterRowStyle var2) {
        this.filterController.addTextFilter(var1, var2);
    }

    public boolean isFiltered(E var1) {
        return this.filterController.isFiltered(var1);
    }

    public ScrollableTableList<E>.Row getSelectedRow() {
        return this.selectedRow;
    }

    public void setSelectedRow(ScrollableTableList<E>.Row var1) {
        this.selectedRow = var1;
        if (var1 != null) {
            var1.lastClick = System.currentTimeMillis();
            if (this.selCallback != null) {
                this.selCallback.onSelected(var1.f);
            }
        }

    }

    private void insertSeperators() {
        if (this.seperators.size() > 0) {
            for(int var1 = 0; var1 < this.mainList.size(); ++var1) {
                if (this.mainList.get(var1) instanceof ScrollableTableList.Seperator) {
                    this.mainList.removeWithoutUpdate(var1);
                    --var1;
                }
            }

            Iterator var4 = this.seperators.values().iterator();

            while(true) {
                while(var4.hasNext()) {
                    ScrollableTableList.Seperator var2 = (ScrollableTableList.Seperator)var4.next();

                    for(int var3 = 0; var3 < this.mainList.size(); ++var3) {
                        if (this.mainList.get(var3) instanceof ScrollableTableList.Row && ((ScrollableTableList.Row)this.mainList.get(var3)).seperator.sortIndex == var2.sortIndex) {
                            assert !this.mainList.contains(var2);

                            this.mainList.addWithoutUpdate(var3, var2);
                            break;
                        }
                    }
                }

                this.mainList.updateDim();
                break;
            }
        }

    }

    static {
        tableFontSize = FontSize.MEDIUM;
        dropdownFontSize = FontSize.MEDIUM;
        innerFontSize = FontSize.SMALL;
    }

    public class Column implements Comparator<E>, GUICallback {
        public GUIHorizontalArea bg;
        public GUITexDrawableArea seperator;
        String columnName;
        float weigth;
        int fixedSize = -1;
        private int textWidth;
        private int index;
        private GUITextOverlay text;
        private Comparator<GUIListElement> comparator;
        private Comparator<E> comp;
        ScrollableTableList<E>.Column.CComp ccomp = new ScrollableTableList.Column.CComp();

        public Column(String var2, float var3, int var4, Comparator<E> var5) {
            this.columnName = var2;
            this.weigth = var3;
            this.index = var4;
            this.comp = var5;
        }

        private int comp(E var1, E var2) {
            return this.compare(var1, var2);
        }

        public int compare(E var1, E var2) {
            return this.comp.compare(var1, var2);
        }

        public void init(InputState var1) {
            this.bg = new GUIHorizontalArea(var1, HButtonType.BUTTON_GREY_DARK, 10);
            this.bg.onInit();
            this.bg.setMouseUpdateEnabled(true);
            this.bg.setCallback(this);
            GUIScrollablePanel var2;
            (var2 = new GUIScrollablePanel(this.bg.getWidth(), this.bg.getHeight(), this.bg, var1)).setScrollable(0);
            var2.setLeftRightClipOnly = true;
            this.text = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium16(), var1);
            this.text.setTextSimple(this.columnName);
            this.text.onInit();
            var2.setContent(this.text);
            this.textWidth = this.text.getFont().getWidth(this.columnName);
            this.bg.attach(var2);
            this.comparator = this.getComparator();
            this.comparator = Collections.reverseOrder(this.comparator);
        }

        public Comparator<GUIListElement> getComparator() {
            return new Comparator<GUIListElement>() {
                public int compare(GUIListElement var1, GUIListElement var2) {
                    return !(var1 instanceof ScrollableTableList.Seperator) && !(var2 instanceof ScrollableTableList.Seperator) ? ((ScrollableTableList.Row)var1).compareTo(Column.this.index, (ScrollableTableList.Row)var2) : 0;
                }
            };
        }

        private float getPercentWidth() {
            return (1.0F + this.weigth) / ScrollableTableList.this.columnTotalWeightParts;
        }

        public int draw(int var1) {
            int var2;
            if (this.fixedSize >= 0) {
                var2 = this.fixedSize;
            } else {
                var2 = (int)(this.getPercentWidth() * (ScrollableTableList.this.getWidth() - ScrollableTableList.this.columnTotalFixedParts));
            }

            if (this.index == ScrollableTableList.this.columns.size() - 1) {
                int var3 = (int)(ScrollableTableList.this.getWidth() - (float)(var2 + var1));
                var2 += var3;
            }

            if (this.bg.getWidth() != (float)var2) {
                ScrollableTableList.this.columnsWidthChanged = true;
            }

            this.bg.setWidth(var2);
            this.bg.setPos((float)var1, 0.0F, 0.0F);
            this.text.setPos((float)(var2 / 2 - this.textWidth / 2), 6.0F, 0.0F);
            if (this.index > 0) {
                this.seperator.setPos((float)(var1 - 2), 0.0F, 0.0F);
            }

            this.bg.draw();
            return var2;
        }

        private void sort() {
            Collections.sort(ScrollableTableList.this.mainList, this.ccomp);
            ScrollableTableList.this.insertSeperators();
        }

        public int getPosX() {
            return (int)this.bg.getPos().x;
        }

        public void callback(GUIElement var1, MouseEvent var2) {
            if (var2.pressedLeftMouse()) {
                this.sortBy(true);
            }

        }

        public void reverseOrders() {
            this.comparator = Collections.reverseOrder(this.comparator);
        }

        public void sortBy(boolean var1) {
            if (var1) {
                this.reverseOrders();
            }

            ScrollableTableList.this.activeSortColumnIndex = this.index;
            this.sort();
        }

        public boolean isOccluded() {
            return !ScrollableTableList.this.dependent.isActive();
        }

        class CComp implements Comparator<GUIListElement> {
            private CComp() {
            }

            public int compare(GUIListElement var1, GUIListElement var2) {
                if (var1 instanceof ScrollableTableList.Row && var2 instanceof ScrollableTableList.Row) {
                    ScrollableTableList.Row var3 = (ScrollableTableList.Row)var1;
                    ScrollableTableList.Row var4 = (ScrollableTableList.Row)var2;
                    if (var3.seperator != null && var4.seperator == null) {
                        return -1;
                    }

                    if (var3.seperator == null && var4.seperator != null) {
                        return 1;
                    }

                    int var5;
                    if (var3.seperator != null && var4.seperator != null && (var5 = var3.seperator.compareTo(var4.seperator)) != 0) {
                        return var5;
                    }
                }

                return Column.this.comparator.compare(var1, var2);
            }
        }
    }

    public abstract class Row extends GUIListElement implements Draggable, DropTarget<ScrollableTableList<E>.Row>, GUIEnterableListOnExtendedCallback {
        public ScrollableTableList<E>.Seperator seperator;
        protected boolean rightClickSelectsToo;
        public long lastClick;
        public GUIElementList expanded;
        protected boolean draggable;
        public GUIColoredAncor bg;
        public GUIColoredAncor bgHightlight;
        public GUIEnterableList l;
        public GUIEnterableListBlockedInterface extendableBlockedInterface;
        public GUIEnterableListOnExtendedCallback onExpanded;
        protected boolean highlightSelect = false;
        protected boolean highlightSelectSimple = false;
        protected int customColumnHeightExpanded = -1;
        private final GUIElement[] elements;
        private boolean init = false;
        private boolean onInitCalled;
        public E f;
        private boolean allwaysOneSelected;
        public final GUIResizableElement[] useColumnWidthElements;
        private int dPosX;
        private int dPosY;
        private long tStartDrag;

        protected boolean isDragStartOk() {
            return true;
        }

        public Row(InputState var2, E var3, GUIElement... var4) {
            super(var2);
            this.f = var3;

            assert var4.length == ScrollableTableList.this.columns.size();

            for(int var5 = 0; var5 < var4.length; ++var5) {
                if (var4[var5] instanceof ScrollableTableList.GUIClippedRow) {
                    var4[var5] = ((ScrollableTableList.GUIClippedRow)var4[var5]).getClippedPane(var5);
                }
            }

            this.elements = var4;
            this.useColumnWidthElements = new GUIResizableElement[var4.length];

            assert this.checkTexts();

        }

        public void extended() {
            if (this.isAllwaysOneSelected()) {
                if (ScrollableTableList.this.extendedRow != null && ScrollableTableList.this.extendedRow != this) {
                    ScrollableTableList.this.extendedRow.unexpend();
                }

                ScrollableTableList.this.extendedRow = this;
            }

        }

        public void collapsed() {
            if (this.isAllwaysOneSelected() && ScrollableTableList.this.extendedRow == this) {
                ScrollableTableList.this.extendedRow = null;
            }

        }

        private boolean checkTexts() {
            for(int var1 = 0; var1 < this.elements.length; ++var1) {
                if (this.elements[var1] instanceof GUITextOverlay) {
                    assert !((GUITextOverlay)this.elements[var1]).getText().isEmpty() : "at column " + var1;

                    Iterator var2 = ((GUITextOverlay)this.elements[var1]).getText().iterator();

                    while(var2.hasNext()) {
                        Object var3 = var2.next();

                        assert var3 != null : "at column " + var1;

                        assert var3.toString() != null : "at column " + var1;
                    }
                }
            }

            return true;
        }

        public float getExtendedHighlightBottomDist() {
            return 32.0F;
        }

        protected boolean isSimpleSelected() {
            return ScrollableTableList.this.getSelectedRow() == this;
        }

        protected GUIContextPane createContext() {
            return null;
        }

        public void clickedOnRow() {
            if (this.highlightSelectSimple) {
                if (this.isSimpleSelected()) {
                    if (!this.isAllwaysOneSelected()) {
                        ScrollableTableList.this.setSelectedRow((ScrollableTableList.Row)null);
                        return;
                    }

                    if (System.currentTimeMillis() - this.lastClick < 300L) {
                        if (ScrollableTableList.this.selCallback != null) {
                            ScrollableTableList.this.selCallback.onSelected(this.f);
                        }

                        this.onDoubleClick();
                        if (ScrollableTableList.this.selCallback != null) {
                            ScrollableTableList.this.selCallback.onDoubleClick(this.f);
                        }
                    }

                    this.lastClick = System.currentTimeMillis();
                    return;
                }

                ScrollableTableList.this.setSelectedRow(this);
            }

        }

        public void unexpend() {
            if (this.l != null) {
                this.l.setExpanded(false);
            }

        }

        private void adaptWidths() {
            this.bg.setWidth(ScrollableTableList.this.getWidth() - 4.0F);
            this.bg.setPos(2.0F, 0.0F, 0.0F);
            if (this.bgHightlight != null) {
                this.bgHightlight.setWidth(ScrollableTableList.this.getWidth());
            }

            int var1;
            for(var1 = 0; var1 < ScrollableTableList.this.columns.size(); ++var1) {
                this.elements[var1].getPos().x = (float)(4 + ((ScrollableTableList.Column)ScrollableTableList.this.columns.get(var1)).getPosX());
            }

            for(var1 = 0; var1 < ScrollableTableList.this.columns.size(); ++var1) {
                if (this.useColumnWidthElements[var1] != null) {
                    if (var1 < ScrollableTableList.this.columns.size() - 1) {
                        this.useColumnWidthElements[var1].setWidth(this.elements[var1 + 1].getPos().x - this.elements[var1].getPos().x);
                    } else {
                        this.useColumnWidthElements[var1].setWidth(this.getWidth() - this.elements[var1].getPos().x);
                    }
                }
            }

        }

        public final E getSort() {
            return this.f;
        }

        public Vector4f[] getCustomRowColors() {
            return null;
        }

        public void draw() {
            assert this.onInitCalled;

            Vector4f[] var1;
            label87: {
                var1 = this.getCustomRowColors();
                Vector4f var10000;
                Vector4f var10001;
                if (this.draggable && this.getState().getController().getInputController().getDragging() == this) {
                    var10000 = this.bg.getColor();
                    var10001 = ScrollableTableList.this.draggingBGColor;
                } else if (this.draggable && this.getState().getController().getInputController().getDragging() != null && this.bg.isInside()) {
                    var10000 = this.bg.getColor();
                    var10001 = ScrollableTableList.this.colorRowHighlight;
                } else if (this.isSimpleSelected()) {
                    var10000 = this.bg.getColor();
                    var10001 = ScrollableTableList.this.colorRowHighlight;
                } else {
                    if (var1 != null) {
                        this.bg.getColor().set(this.currentIndex % 2 == 0 ? var1[0] : var1[1]);
                        break label87;
                    }

                    var10000 = this.bg.getColor();
                    var10001 = this.currentIndex % 2 == 0 ? ScrollableTableList.this.colorRowA : ScrollableTableList.this.colorRowB;
                }

                var10000.set(var10001);
            }

            if (ScrollableTableList.this.columnsWidthChanged || !this.init || this.bg.getWidth() != ScrollableTableList.this.getWidth()) {
                this.adaptWidths();
                this.init = true;
            }

            if (var1 != null && this.l != null) {
                if (this.bgHightlight != null) {
                    this.bgHightlight.setColor(var1[2]);
                }

                this.l.expandedBackgroundColor = var1[2];
            } else {
                if (this.bgHightlight != null) {
                    this.bgHightlight.setColor(ScrollableTableList.this.colorRowHighlight);
                }

                if (this.l != null) {
                    this.l.expandedBackgroundColor = ScrollableTableList.this.colorRowHighlight;
                }
            }

            super.draw();
        }

        public void onInit() {
            int var1;
            if (this.expanded != null) {
                this.bg = new GUIColoredRectangle(this.getState(), 0.0F, (float)ScrollableTableList.this.columnsHeight, new Vector4f());
                this.bg.renderMode = 2;
                if (this.highlightSelect) {
                    this.bgHightlight = new GUIColoredRectangleLeftRightShadow(this.getState(), 0.0F, this.customColumnHeightExpanded > 0 ? (float)this.customColumnHeightExpanded : (float)ScrollableTableList.this.columnsHeight, ScrollableTableList.this.colorRowHighlight);

                    for(var1 = 0; var1 < ScrollableTableList.this.columns.size(); ++var1) {
                        this.bgHightlight.attach(this.elements[var1]);
                    }
                } else {
                    this.bgHightlight = this.bg;
                }

                for(var1 = 0; var1 < ScrollableTableList.this.columns.size(); ++var1) {
                    this.bg.attach(this.elements[var1]);
                }

                this.l = new GUIEnterableList(this.getState(), this.expanded, this.bg, this.bgHightlight);
                this.l.extendedHighlightBottomDist = this.getExtendedHighlightBottomDist();
                this.l.scrollPanel = ScrollableTableList.this.scrollPanel;
                this.l.expandedBackgroundColor = ScrollableTableList.this.colorRowHighlight;
                this.l.extendedCallback = this.onExpanded;
                this.l.extendedCallbackSelector = this;
                this.l.extendableBlockedInterface = this.extendableBlockedInterface;
                this.l.addObserver(ScrollableTableList.this);
                this.content = this.l;
                this.selectContent = this.l;
                this.l.onInit();
                this.l.switchCollapsed(false);
            } else {
                this.bg = new GUIColoredRectangle(this.getState(), 0.0F, (float)ScrollableTableList.this.columnsHeight, new Vector4f());
                this.bg.renderMode = 2;
                this.bg.setMouseUpdateEnabled(true);
                this.bg.setCallback(new GUICallback() {
                    public void callback(GUIElement var1, MouseEvent var2) {
                        if (Row.this.draggable && var2.pressedLeftMouse() && Row.this.isDragStartOk()) {
                            Row.this.getState().getController().getInputController().setDragging(Row.this);
                        }

                        if (var2.pressedLeftMouse()) {
                            Row.this.clickedOnRow();
                        } else if (var2.pressedRightMouse()) {
                            if (Row.this.rightClickSelectsToo) {
                                Row.this.clickedOnRow();
                            }

                            System.err.println("[CLIENT] OPENING CONTEXT MENU");
                            Row.this.getState().getController().getInputController().setCurrentContextPane(Row.this.createContext());
                        }

                        Row.this.checkTarget(var2);
                    }

                    public boolean isOccluded() {
                        return !ScrollableTableList.this.isActive();
                    }
                });

                for(var1 = 0; var1 < ScrollableTableList.this.columns.size(); ++var1) {
                    this.bg.attach(this.elements[var1]);
                }

                this.content = this.bg;
                this.selectContent = this.bg;
            }

            super.onInit();
            this.onInitCalled = true;
        }

        public boolean checkDragReleasedMouseEvent(MouseEvent var1) {
            return var1.button == 0 && !var1.state || var1.button == 0 && var1.state && this.getState().getController().getInputController().getDragging().isStickyDrag();
        }

        public int getDragPosX() {
            return this.dPosX;
        }

        public void setDragPosX(int var1) {
            this.dPosX = var1;
        }

        public int getDragPosY() {
            return this.dPosY;
        }

        public void setDragPosY(int var1) {
            this.dPosY = var1;
        }

        public Object getPlayload() {
            return null;
        }

        public long getTimeDragStarted() {
            return this.tStartDrag;
        }

        public boolean isStickyDrag() {
            return false;
        }

        public void setStickyDrag(boolean var1) {
        }

        public void setTimeDraggingStart(long var1) {
            this.tStartDrag = var1;
        }

        public void reset() {
        }

        public short getType() {
            return -1;
        }

        public void callback(GUIElement var1, MouseEvent var2) {
            this.checkTarget(var2);
        }

        public void checkTarget(MouseEvent var1) {
            if (this.getState().getController().getInputController().getDragging() != null && this.getState().getController().getInputController().getDragging().checkDragReleasedMouseEvent(var1) && this.getState().getController().getInputController().getDragging() instanceof ScrollableTableList.Row) {
                this.onDrop((ScrollableTableList.Row)this.getState().getController().getInputController().getDragging());
                this.getState().getController().getInputController().getDragging().reset();
                this.getState().getController().getInputController().setDragging((Draggable)null);
            }

        }

        public boolean isTarget(Draggable var1) {
            return var1 != null;
        }

        public void onDrop(ScrollableTableList<E>.Row var1) {
        }

        public boolean isOccluded() {
            return false;
        }

        public int compareTo(int var1, ScrollableTableList<E>.Row var2) {
            return ((ScrollableTableList.Column)ScrollableTableList.this.columns.get(var1)).comp(this.getSort(), var2.getSort());
        }

        public void onDoubleClick() {
            assert this.isAllwaysOneSelected();

        }

        public boolean isAllwaysOneSelected() {
            return this.allwaysOneSelected;
        }

        public void setAllwaysOneSelected(boolean var1) {
            this.allwaysOneSelected = var1;
        }
    }

    public class GUIClippedRow extends GUIAncor {
        private int column;

        public GUIClippedRow(InputState var2) {
            super(var2, 10.0F, 10.0F);
        }

        public void draw() {
            this.setWidth(((ScrollableTableList.Column)ScrollableTableList.this.columns.get(this.column)).bg.getWidth() - 8.0F);
            this.setHeight(ScrollableTableList.this.columnsHeight);
            super.draw();
        }

        public GUIScrollablePanel getClippedPane(int var1) {
            this.column = var1;
            GUIScrollablePanel var2;
            (var2 = new GUIScrollablePanel(24.0F, 24.0F, this, this.getState())).setContent(this);
            var2.setLeftRightClipOnly = true;
            return var2;
        }
    }

    public class Seperator extends GUIListElement implements Comparable<ScrollableTableList<E>.Seperator> {
        public String name = "";
        public Vector4f color = new Vector4f();
        public Vector4f colorText = new Vector4f(0.1F, 0.8F, 0.8F, 1.0F);
        public int sepheight;
        private GUIColoredRectangle bg;
        public final int sortIndex;
        public boolean sortByName;
        private GUITextOverlay septext;

        public Seperator(InputState var2, int var3) {
            super(var2);
            this.sepheight = ScrollableTableList.this.columnsHeight + 15;
            this.sortByName = false;
            this.sortIndex = var3;
        }

        public void onInit() {
            this.bg = new GUIColoredRectangle(this.getState(), 0.0F, (float)this.sepheight, this.color);
            this.bg.renderMode = 2;
            this.bg.setWidth(ScrollableTableList.this.getWidth() - 4.0F);
            this.bg.setPos(2.0F, 0.0F, 0.0F);
            this.bg.setColor(this.color);
            this.content = this.bg;
            this.selectContent = this.bg;
            this.septext = new GUITextOverlay(10, 10, FontLibrary.getBlenderProHeavy30(), this.getState());
            this.septext.setTextSimple(this.name);
            this.septext.getPos().set(12.0F, 6.0F, 0.0F);
            this.septext.setColor(this.colorText);
            this.bg.attach(this.septext);
        }

        public int compareTo(ScrollableTableList<E>.Seperator var1) {
            return this.sortByName ? this.name.compareTo(var1.name) : CompareTools.compare(this.sortIndex, var1.sortIndex);
        }

        public void draw() {
            super.draw();
        }
    }
}
