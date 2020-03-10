//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.player.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.server.data.CatalogState;
import org.schema.game.server.data.FactionState;

public class PlayerCatalogManager extends Observable implements Observer {
    private final PlayerState player;
    private final List<CatalogPermission> availableCatalog = new ArrayList();
    private final List<CatalogPermission> personalCatalog = new ArrayList();
    private final List<CatalogPermission> allCatalog = new ArrayList();
    private boolean flagCatalogUpdated = true;
    private CatalogManager catalogManager;
    private boolean addedFactionObserver;

    public PlayerCatalogManager(PlayerState var1) {
        this.player = var1;
    }

    public void cleanUp() {
        if (this.catalogManager != null) {
            this.catalogManager.deleteObserver(this);
            ((FactionState)this.player.getState()).getFactionManager().deleteObserver(this);
        }

    }

    public List<CatalogPermission> getAllCatalog() {
        return this.allCatalog;
    }

    public List<CatalogPermission> getAvailableCatalog() {
        return this.availableCatalog;
    }

    public List<CatalogPermission> getPersonalCatalog() {
        return this.personalCatalog;
    }

    private void reorganizeCatalog() {
        long var1 = System.currentTimeMillis();
        Collection var3 = this.catalogManager.getCatalog();
        this.availableCatalog.clear();
        this.getPersonalCatalog().clear();
        this.getAllCatalog().clear();

        CatalogPermission var5;
        for(Iterator var4 = var3.iterator(); var4.hasNext(); this.getAllCatalog().add(var5)) {
            var5 = (CatalogPermission)var4.next();

            assert var5 != null;

            boolean var6 = false;
            boolean var7 = false;
            if (var5.ownerUID.equals(this.player.getName())) {
                var6 = true;
                var7 = true;
            } else if (var5.others()) {
                var6 = true;
            } else {
                Faction var8;
                if (var5.faction() && (var8 = ((FactionState)this.player.getState()).getFactionManager().getFaction(this.player.getFactionId())) != null && var8.getMembersUID().keySet().contains(var5.ownerUID)) {
                    var6 = true;
                }
            }
            //if (var6) {
                this.availableCatalog.add(var5);
            //}

            if (var7) {
                this.getPersonalCatalog().add(var5);
            }
        }

        long var9 = System.currentTimeMillis() - var1;
        if (this.player.isClientOwnPlayer()) {
            ((GameClientState)this.player.getState()).notifyOfCatalogChange();
        }

        this.setChanged();
        this.notifyObservers();
        long var10;
        if ((var10 = System.currentTimeMillis() - var1) > 5L) {
            System.err.println(this.player.getState() + " Updating catalog for " + this.player + " took " + var10 + " ms; update: " + var9);
        }

    }

    public void update() {
        if (!this.addedFactionObserver && ((FactionState)this.player.getState()).getFactionManager() != null) {
            ((FactionState)this.player.getState()).getFactionManager().addObserver(this);
            this.addedFactionObserver = true;
        }

        if (this.catalogManager == null && ((CatalogState)this.player.getState()).getCatalogManager() != null) {
            this.catalogManager = ((CatalogState)this.player.getState()).getCatalogManager();
            this.catalogManager.addObserver(this);
        }

        if (this.catalogManager != null) {
            if (this.flagCatalogUpdated) {
                this.reorganizeCatalog();
                this.flagCatalogUpdated = false;
            }

        }
    }

    public void update(Observable var1, Object var2) {
        this.flagCatalogUpdated = true;
    }
}
