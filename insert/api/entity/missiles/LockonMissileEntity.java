package api.entity.missiles;

import org.schema.game.common.data.missile.FafoMissile;
import org.schema.game.common.data.missile.Missile;

public class LockonMissileEntity extends MissileEntity{
    public LockonMissileEntity(FafoMissile missile) {
        super(missile);
    }

    @Override
    public FafoMissile getInternalMissile() {
        return (FafoMissile) super.getInternalMissile();
    }
}
