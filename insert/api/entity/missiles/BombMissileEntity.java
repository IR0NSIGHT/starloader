package api.entity.missiles;

import org.schema.game.common.data.missile.BombMissile;
import org.schema.game.common.data.missile.Missile;

public class BombMissileEntity extends MissileEntity{

    public BombMissileEntity(BombMissile missile) {
        super(missile);
    }

    @Override
    public BombMissile getInternalMissile() {
        return (BombMissile) super.getInternalMissile();
    }
}
