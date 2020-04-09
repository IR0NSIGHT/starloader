package api.entity.missiles;

import org.schema.game.common.data.missile.FafoMissile;
import org.schema.game.common.data.missile.HeatMissile;

public class SwarmerMissileEntity extends MissileEntity{
    public SwarmerMissileEntity(HeatMissile missile) {
        super(missile);
    }

    @Override
    public HeatMissile getInternalMissile() {
        return (HeatMissile) super.getInternalMissile();
    }
}
