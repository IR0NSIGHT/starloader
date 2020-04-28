package api.systems.weapons;

import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamElementManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;

public class Cannon {

    private WeaponUnit unit;
    private WeaponElementManager elementManager;
    private WeaponCollectionManager collectionManager;
    //TODO add combination add-on
    public Cannon(WeaponUnit unit) {
        this.unit = unit;
        this.collectionManager = unit.elementCollectionManager;
        this.elementManager = unit.elementCollectionManager.getElementManager();
    }

    public WeaponUnit getUnit() {
        return unit;
    }

    public WeaponElementManager getElementManager() {
        return elementManager;
    }

    public WeaponCollectionManager getCollectionManager() {
        return collectionManager;
    }
}
