package api.entity.missiles;

import org.schema.game.common.data.missile.Missile;

public class MissileEntity {
    private Missile missile;

    public MissileEntity(Missile missile){

        this.missile = missile;
    }

    public Missile getInternalMissile() {
        return missile;
    }
}
