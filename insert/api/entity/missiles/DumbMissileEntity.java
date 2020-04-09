package api.entity.missiles;

import org.schema.game.common.data.missile.DumbMissile;
import org.schema.game.common.data.missile.Missile;

public class DumbMissileEntity extends MissileEntity{
    public DumbMissileEntity(DumbMissile missile) {
        super(missile);
    }

    @Override
    public DumbMissile getInternalMissile() {
        return (DumbMissile) super.getInternalMissile();
    }
}
