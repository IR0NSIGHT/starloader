//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui.fleet;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Observer;
import java.util.Set;

import api.listener.events.systems.GetAvailableFleetsEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.hsqldb.lib.StringComparator;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.common.data.fleet.FleetManager;
import org.schema.game.common.data.fleet.FleetStateInterface;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIListFilterText;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITextOverlayTable;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList;
import org.schema.schine.graphicsengine.forms.gui.newgui.ControllerElement.FilterRowStyle;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList.GUIClippedRow;
import org.schema.schine.input.InputState;

public class FleetScrollableListNew extends ScrollableTableList<Fleet> implements Observer {
    public FleetScrollableListNew(InputState var1, GUIElement var2) {
        super(var1, 100.0F, 100.0F, var2);
        ((GameClientState)this.getState()).getFleetManager().addObserver(this);
    }

    public void cleanUp() {
        ((GameClientState)this.getState()).getFleetManager().deleteObserver(this);
        super.cleanUp();
    }

    public void initColumns() {
        new StringComparator();
        this.addColumn(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FLEET_FLEETSCROLLABLELISTNEW_4, 3.0F, new Comparator<Fleet>() {
            public int compare(Fleet var1, Fleet var2) {
                return var1.getName().compareToIgnoreCase(var2.getName());
            }
        }, true);
        this.addFixedWidthColumn(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FLEET_FLEETSCROLLABLELISTNEW_5, 39, new Comparator<Fleet>() {
            public int compare(Fleet var1, Fleet var2) {
                return var1.getMembers().size() - var2.getMembers().size();
            }
        });
        this.addColumn(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FLEET_FLEETSCROLLABLELISTNEW_6, 3.0F, new Comparator<Fleet>() {
            public int compare(Fleet var1, Fleet var2) {
                return var1.getFlagShipName().compareToIgnoreCase(var2.getFlagShipName());
            }
        });
        this.addColumn(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FLEET_FLEETSCROLLABLELISTNEW_7, 1.0F, new Comparator<Fleet>() {
            public int compare(Fleet var1, Fleet var2) {
                return var1.getFlagShipSector().compareToIgnoreCase(var2.getFlagShipSector());
            }
        });
        this.addColumn(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FLEET_FLEETSCROLLABLELISTNEW_8, 1.5F, new Comparator<Fleet>() {
            public int compare(Fleet var1, Fleet var2) {
                return var1.getMissionName().compareToIgnoreCase(var2.getMissionName());
            }
        });
        this.addTextFilter(new GUIListFilterText<Fleet>() {
            public boolean isOk(String var1, Fleet var2) {
                return var2.getName().toLowerCase(Locale.ENGLISH).contains(var1.toLowerCase(Locale.ENGLISH));
            }
        }, FilterRowStyle.FULL);

    }

    protected Collection<Fleet> getElementList() {
        FleetManager fleetMan = ((GameClientState) this.getState()).getFleetManager();
        Collection<Fleet> availableFleetsClient = fleetMan.getAvailableFleetsClient();

        //INSERTED CODE
        GetAvailableFleetsEvent ev = new GetAvailableFleetsEvent(this, fleetMan, availableFleetsClient);
        StarLoader.fireEvent(ev, false);
        ///

        return availableFleetsClient;
    }

    public void updateListEntries(GUIElementList var1, Set<Fleet> var2) {
        var1.deleteObservers();
        var1.addObserver(this);
        ((GameClientState)this.getState()).getFleetManager();
        ((GameClientState)this.getState()).getPlayer();
        Iterator var10 = var2.iterator();

        while(var10.hasNext()) {
            final Fleet var3 = (Fleet)var10.next();
            GUITextOverlayTable var4 = new GUITextOverlayTable(10, 10, this.getState());
            GUITextOverlayTable var5 = new GUITextOverlayTable(10, 10, this.getState());
            GUITextOverlayTable var6 = new GUITextOverlayTable(10, 10, this.getState()) {
                public void draw() {
                    super.draw();
                }
            };
            GUITextOverlayTable var7 = new GUITextOverlayTable(10, 10, this.getState());
            GUITextOverlayTable var8 = new GUITextOverlayTable(10, 10, this.getState());
            GUIClippedRow var9;
            (var9 = new GUIClippedRow(this.getState())).attach(var4);
            var4.setTextSimple(new Object() {
                public String toString() {
                    return var3.getName();
                }
            });
            var5.setTextSimple(new Object() {
                public String toString() {
                    return String.valueOf(var3.getMembers().size());
                }
            });
            var6.setTextSimple(new Object() {
                public String toString() {
                    return var3.getFlagShipName();
                }
            });
            var6.setTextSimple(new Object() {
                public String toString() {
                    return var3.getFlagShipName();
                }
            });
            var7.setTextSimple(new Object() {
                public String toString() {
                    return var3.getFlagShipSector();
                }
            });
            var8.setTextSimple(new Object() {
                public String toString() {
                    return var3.getMissionName();
                }
            });
            var4.getPos().y = 4.0F;
            var5.getPos().y = 4.0F;
            var6.getPos().y = 4.0F;
            var7.getPos().y = 4.0F;
            var8.getPos().y = 4.0F;
            FleetScrollableListNew.FleetRow var11;
            (var11 = new FleetScrollableListNew.FleetRow(this.getState(), var3, new GUIElement[]{var9, var5, var6, var7, var8})).onInit();
            var1.addWithoutUpdate(var11);
        }

        var1.updateDim();
    }

    class FleetRow extends ScrollableTableList<Fleet>.Row {
        public FleetRow(InputState var2, Fleet var3, GUIElement... var4) {
            super(var2, var3, var4);
            this.highlightSelect = true;
        }

        protected boolean isSimpleSelected() {
            return ((FleetStateInterface)this.getState()).getFleetManager().getSelected() == this.f;
        }

        public void clickedOnRow() {
            if (((FleetStateInterface)this.getState()).getFleetManager().getSelected() != this.f) {
                ((FleetStateInterface)this.getState()).getFleetManager().setSelected((Fleet)this.f);
            } else {
                ((FleetStateInterface)this.getState()).getFleetManager().setSelected((Fleet)null);
            }

            super.clickedOnRow();
        }
    }
}
