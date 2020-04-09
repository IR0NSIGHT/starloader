package api.systems.weapons;

import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamElementManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;

public class DamageBeam {

    private DamageBeamUnit unit;
    private DamageBeamElementManager elementManager;
    private DamageBeamCollectionManager collectionManager;

    public DamageBeam(DamageBeamUnit unit) {
        this.unit = unit;
        this.collectionManager = unit.elementCollectionManager;
        this.elementManager = unit.elementCollectionManager.getElementManager();
    }

    public DamageBeamUnit getUnit() {
        return unit;
    }

    public DamageBeamElementManager getElementManager() {
        return elementManager;
    }

    public DamageBeamCollectionManager getCollectionManager() {
        return collectionManager;
    }
}
