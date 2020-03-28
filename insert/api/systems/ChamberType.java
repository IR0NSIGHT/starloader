package api.systems;

import org.schema.game.common.data.element.ElementKeyMap;

public enum ChamberType {
    FTL(ElementKeyMap.REACTOR_CHAMBER_JUMP),
    MOBILITY(ElementKeyMap.REACTOR_CHAMBER_MOBILITY),
    DEFENCE(1046),
    MASS(991),
    LOGISTICS(ElementKeyMap.REACTOR_CHAMBER_LOGISTICS),
    RECON(ElementKeyMap.REACTOR_CHAMBER_SCANNER),
    STEALTH(ElementKeyMap.REACTOR_CHAMBER_STEALTH),
    POWER(1048),
    ;
    private short id;
    ChamberType(int id){
        this.id = (short) id;
    }

    public short getId() {
        return id;
    }
}
