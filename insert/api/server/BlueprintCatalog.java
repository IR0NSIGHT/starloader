package api.server;

import api.entity.Entity;
import api.entity.EntityType;
import api.entity.Player;
import org.schema.game.common.data.player.catalog.CatalogManager;
import org.schema.game.common.data.player.catalog.CatalogPermission;
import org.schema.game.server.controller.BluePrintController;;
import org.schema.game.server.data.blueprintnw.BlueprintClassification;
import org.schema.game.server.data.blueprintnw.BlueprintEntry;
import java.io.IOException;

public class BlueprintCatalog {

    private CatalogManager internalCatalog;

    public BlueprintCatalog(CatalogManager internalCatalog) {
        this.internalCatalog = internalCatalog;
    }

    public void addCatologEntry(Entity entity, Player owner, String entryName, CatalogPermission[] permissions) throws IOException {
        /**
         * Adds a blueprint into the catalog. Only works for ships and stations.
         */
        if(entity.getEntityType() == EntityType.SHIP || entity.getEntityType() == EntityType.STATION) {
            BlueprintEntry internalEntry = new BlueprintEntry(entryName, BluePrintController.active);
            internalEntry.setClassification(BlueprintClassification.NONE);
            internalEntry.write(entity.internalEntity, true);
            internalCatalog.addServerEntry(internalEntry, entryName, true);
        }
    }

    public void addCatologEntry(Entity entity, Player owner, String entryName, CatalogPermission[] permissions, BlueprintClassification blueprintClassification) throws IOException {
        /**
         * Adds a blueprint into the catalog including it's classification. Only works for ships and stations.
         */
        if(entity.getEntityType() == EntityType.SHIP || entity.getEntityType() == EntityType.STATION) {
            BlueprintEntry internalEntry = new BlueprintEntry(entryName, BluePrintController.active);
            internalEntry.setClassification(blueprintClassification);
            internalEntry.write(entity.internalEntity, true);
            internalCatalog.addServerEntry(internalEntry, entryName, true);
        }
    }
}
