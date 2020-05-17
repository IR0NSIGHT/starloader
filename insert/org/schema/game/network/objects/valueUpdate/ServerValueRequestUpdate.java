//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.network.objects.valueUpdate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.power.PowerManagerInterface;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;

public class ServerValueRequestUpdate extends BooleanValueUpdate {
    public ServerValueRequestUpdate.Type requestType;

    public ServerValueRequestUpdate(ServerValueRequestUpdate.Type var1) {
        this.requestType = var1;
    }

    public ServerValueRequestUpdate() {
    }

    public void serialize(DataOutput var1, boolean var2) throws IOException {
        super.serialize(var1, var2);
        var1.writeByte((byte)this.requestType.ordinal());
    }

    public void deserialize(DataInput var1, int var2, boolean var3) throws IOException {
        super.deserialize(var1, var2, var3);
        this.requestType = ServerValueRequestUpdate.Type.values()[var1.readByte()];
    }

    public void setServer(ManagerContainer<?> var1, long var2) {
        assert !var1.getSegmentController().isOnServer();

    }

    public ValTypes getType() {
        return ValTypes.SERVER_UPDATE_REQUEST;
    }

    public boolean checkOnAdd() {
        return this.requestType != null;
    }

    public boolean applyClient(ManagerContainer<?> var1) {
        assert var1.getSegmentController().isOnServer();

        this.requestType.e.execute(this, var1);
        return true;
    }

    public static enum Type {
        ALL(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                ServerValueRequestUpdate.Type[] var3;
                int var4 = (var3 = ServerValueRequestUpdate.Type.values()).length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    ServerValueRequestUpdate.Type var6;
                    if ((var6 = var3[var5]) != ServerValueRequestUpdate.Type.ALL) {
                        var6.e.execute(var1, var2);
                    }
                }

            }
        }),
        SHIELD(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                ((ShieldContainerInterface)var2).getShieldAddOn().sendShieldUpdate();
            }
        }),
        POWER(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                ((PowerManagerInterface)var2).getPowerAddOn().sendPowerUpdate();
                ((PowerManagerInterface)var2).getPowerAddOn().sendPowerExpectedUpdate();
                if (!var2.isUsingPowerReactors()) {
                    ((PowerManagerInterface)var2).getPowerAddOn().sendBatteryPowerUpdate();
                    ((PowerManagerInterface)var2).getPowerAddOn().sendBatteryPowerExpectedUpdate();
                }

            }
        }),
        JUMP(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                if (var2 instanceof ShipManagerContainer) {
                    ((ShipManagerContainer)var2).getJumpAddOn().sendChargeUpdate();
                }

            }
        }),
        STEALTH(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.getStealthAddOn().sendChargeUpdate();
            }
        }),
        REACTOR_BOOST(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.getReactorBoostAddOn().sendChargeUpdate();
            }
        }),
        JUMP_INTERDICTION(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.getInterdictionAddOn().sendChargeUpdate();
            }
        }),
        SCAN(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.getScanAddOn().sendChargeUpdate();
            }
        }),
        EFFECT(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.getEffectAddOnManager().sendChargeUpdate();
            }
        }),
        MISSILE_CAP(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                if (var2.getMissileCapacityMax() > 1.0F) {
                    var2.setMissileCapacity(var2.getMissileCapacity(), var2.getMissileCapacityTimer(), true);
                }

            }
        }),
        FIRE_MODES(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.sendAllFireModes();
            }
        }),
        CUSTOM(new ServerValueRequestUpdate.Exec() {
            public final void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2) {
                var2.sendAllFireModes();
            }
        }),

        ;

        private final ServerValueRequestUpdate.Exec e;

        private Type(ServerValueRequestUpdate.Exec var3) {
            this.e = var3;
        }
    }

    interface Exec {
        void execute(ServerValueRequestUpdate var1, ManagerContainer<?> var2);
    }
}
