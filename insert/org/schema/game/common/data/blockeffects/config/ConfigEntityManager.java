//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.blockeffects.config;

import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import javax.vecmath.Vector3f;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectParameterType;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;

public class ConfigEntityManager extends Observable {
    private final ShortOpenHashSet transientEffects = new ShortOpenHashSet();
    private final ShortOpenHashSet permanentEffects = new ShortOpenHashSet();
    private final List<ConfigProviderSource> transientEffectSources = new ObjectArrayList();
    private boolean effectsChanged = true;
    private final EffectAccumulator effectAccumulator = new EffectAccumulator();
    private final ConfigEntityManager.EffectEntityType entityType;
    private final long entityId;
    private final ConfigPoolProvider provider;
    private static final byte[] buffer = new byte[131072];
    public String entityName;
    private ShortOpenHashSet rmTmp = new ShortOpenHashSet();
    private ShortList addTmp = new ShortArrayList();

    public ConfigEntityManager(long var1, ConfigEntityManager.EffectEntityType var3, ConfigPoolProvider var4) {
        this.entityId = var1;
        this.entityType = var3;
        this.provider = var4;
    }

    public void saveToDatabase(GameServerState var1) {
        try {
            var1.getDatabaseIndex().getTableManager().getEntityEffectTable().writeEffects(this, this.entityId, this.entityType);
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    public void loadFromDatabase(GameServerState var1) {
        try {
            var1.getDatabaseIndex().getTableManager().getEntityEffectTable().loadEffects(this, this.entityId, this.entityType);
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    public ConfigPool getConfigPool() {
        return this.provider.getConfigPool();
    }

    public void updateToFullNetworkObject(EffectConfigNetworkObjectInterface var1) {
        Iterator var2 = this.transientEffects.iterator();

        short var3;
        while(var2.hasNext()) {
            var3 = (Short)var2.next();
            var1.getEffectAddBuffer().add((short)(-var3));
        }

        var2 = this.permanentEffects.iterator();

        while(var2.hasNext()) {
            var3 = (Short)var2.next();
            var1.getEffectAddBuffer().add(var3);
        }

    }

    public void updateToNetworkObject(EffectConfigNetworkObjectInterface var1) {
    }

    public void updateFromNetworkObject(EffectConfigNetworkObjectInterface var1) {
        boolean var2 = false;
        ShortArrayList var3 = var1.getEffectAddBuffer().getReceiveBuffer();

        int var4;
        short var5;
        for(var4 = 0; var4 < var3.size(); ++var4) {
            var5 = var3.getShort(var4);
            this.addEffect((short)Math.abs(var5), var5 > 0);
            var2 = true;
        }

        var3 = var1.getEffectRemoveBuffer().getReceiveBuffer();

        for(var4 = 0; var4 < var3.size(); ++var4) {
            var5 = var3.getShort(var4);
            this.removeEffect((short)Math.abs(var5), var5 > 0);
            var2 = true;
        }

        if (var2) {
            this.effectsChanged = true;
            this.setChanged();
            this.notifyObservers();
        }

    }

    public void addEffect(short var1, boolean var2) {
        if (var2) {
            this.permanentEffects.add(var1);
        } else {
            this.transientEffects.add(var1);
        }

        this.effectsChanged = true;
    }

    public void initFromNetworkObject(EffectConfigNetworkObjectInterface var1) {
        this.updateFromNetworkObject(var1);
    }

    public void addTransientEffects(ConfigManagerInterface var1) {
        this.transientEffectSources.clear();
        var1.registerTransientEffects(this.transientEffectSources);
        this.rmTmp.clear();
        this.rmTmp.addAll(this.transientEffects);
        int var7 = this.transientEffectSources.size();

        for(int var2 = 0; var2 < var7; ++var2) {
            ConfigProviderSource var3 = (ConfigProviderSource)this.transientEffectSources.get(var2);
            this.addTmp.clear();
            ShortList var9;
            int var4 = (var9 = var3.getAppliedConfigGroups(this.addTmp)).size();

            for(int var5 = 0; var5 < var4; ++var5) {
                short var6 = var9.getShort(var5);
                if (!this.transientEffects.contains(var6)) {
                    this.addEffect(var6, false);
                }

                this.rmTmp.remove(var6);
            }
        }

        Iterator var8 = this.rmTmp.iterator();

        while(var8.hasNext()) {
            short var10 = (Short)var8.next();
            this.removeEffect(var10, false);
        }

    }

    public void updateLocal(Timer var1, ConfigManagerInterface var2) {
        if (this.getConfigPool() != null && !this.getConfigPool().pool.isEmpty()) {
            if (var2 != null) {
                this.addTransientEffects(var2);
            }

            if (this.effectsChanged) {
                this.recalculateEffects();
                this.effectsChanged = false;
                this.setChanged();
                this.notifyObservers();
            }

        }
    }

    private void recalculateEffects() {
        ShortOpenHashSet var1;
        (var1 = new ShortOpenHashSet(this.transientEffects.size() + this.permanentEffects.size())).addAll(this.transientEffects);
        var1.addAll(this.permanentEffects);
        this.effectAccumulator.calculate(var1, this.getConfigPool());
    }

    public void addEffectAndSend(ConfigGroup var1, boolean var2, EffectConfigNetworkObjectInterface var3) {
        this.addEffect(var1.ntId, var2);
        var3.getEffectAddBuffer().add((short)(var2 ? var1.ntId : -var1.ntId));
        this.effectsChanged = true;
    }

    public void removeEffectAndSend(ConfigGroup var1, boolean var2, EffectConfigNetworkObjectInterface var3) {
        this.removeEffect(var1.ntId, var2);
        var3.getEffectRemoveBuffer().add((short)(var2 ? var1.ntId : -var1.ntId));
        this.effectsChanged = true;
    }

    public byte[] serializeByID() throws IOException {
        synchronized(buffer) {
            FastByteArrayOutputStream var2 = new FastByteArrayOutputStream(buffer);
            this.serializeByID(new DataOutputStream(var2));
            byte[] var4 = new byte[(int)var2.position()];
            System.arraycopy(buffer, 0, var4, 0, var4.length);
            return var4;
        }
    }

    public void serializeByID(DataOutput var1) throws IOException {
        ObjectArrayList var2 = new ObjectArrayList(this.effectAccumulator.getActive());
        var1.writeShort(var2.size());
        Iterator var4 = var2.iterator();

        while(var4.hasNext()) {
            ConfigGroup var3 = (ConfigGroup)var4.next();
            var1.writeUTF(var3.id);
        }

    }

    public List<ConfigGroup> loadByID(DataInput var1) throws IOException {
        ObjectArrayList var2 = new ObjectArrayList();
        short var3 = var1.readShort();

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var1.readUTF();
            ConfigGroup var6;
            if ((var6 = (ConfigGroup)this.getConfigPool().poolMapLowerCase.get(var5.toLowerCase(Locale.ENGLISH))) != null) {
                var2.add(var6);
            }
        }

        return var2;
    }

    public boolean isActive(ConfigGroup var1) {
        return this.effectAccumulator.getActive().contains(var1);
    }

    public void removeEffect(short var1, boolean var2) {
        if (var2) {
            this.permanentEffects.remove(var1);
        } else {
            this.transientEffects.remove(var1);
        }

        this.effectsChanged = true;
    }

    public Map<StatusEffectType, EffectModule> getModules() {
        return this.effectAccumulator.getModules();
    }

    public List<EffectModule> getModulesList() {
        return this.effectAccumulator.getModulesList();
    }

    public void addByID(String var1, boolean var2) {
        ConfigGroup var3;
        if ((var3 = (ConfigGroup)this.getConfigPool().poolMapLowerCase.get(var1.toLowerCase(Locale.ENGLISH))) != null) {
            this.addEffect(var3.ntId, var2);
        } else {
            System.err.println("[EFFECT] couldn't add config group '" + var1 + "'. NOT FOUND");
        }
    }

    public List<ConfigGroup> getPermanentEffects() {
        ObjectArrayList var1 = new ObjectArrayList();
        Iterator var2 = this.permanentEffects.iterator();

        while(var2.hasNext()) {
            short var3 = (Short)var2.next();
            ConfigGroup var4;
            if ((var4 = (ConfigGroup)this.getConfigPool().ntMap.get(var3)) != null) {
                var1.add(var4);
            }
        }

        return var1;
    }

    public String toString() {
        return this.entityName != null ? this.entityName : Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_CONFIGENTITYMANAGER_0;
    }

    public Vector3f apply(StatusEffectType var1, Vector3f var2) {
        EffectModule var3;
        if ((var3 = (EffectModule)this.getModules().get(var1)) != null) {
            if (var3.getValueType() != StatusEffectParameterType.VECTOR3f) {
                throw new RuntimeException("Unknown Type: " + var3 + " -> " + var3.getValueType().name());
            } else {
                Vector3f var4 = var3.getVector3fValue();
                var2.x *= var4.x;
                var2.y *= var4.y;
                var2.z *= var4.z;
                return var2;
            }
        } else {
            return var2;
        }
    }

    public int apply(StatusEffectType var1, int var2) {
        EffectModule var3;
        if ((var3 = (EffectModule)this.getModules().get(var1)) != null) {
            if (var3.getValueType() != StatusEffectParameterType.INT) {
                throw new RuntimeException("Unknown Type: " + var3 + " -> " + var3.getValueType().name());
            } else {
                return var2 * var3.getIntValue();
            }
        } else {
            return var2;
        }
    }

    public boolean apply(StatusEffectType var1, boolean var2) {
        EffectModule var3;
        if ((var3 = (EffectModule)this.getModules().get(var1)) != null) {
            if (var3.getValueType() != StatusEffectParameterType.BOOLEAN) {
                throw new RuntimeException("Unknown Type: " + var3 + " -> " + var3.getValueType().name());
            } else {
                return var3.getBooleanValue();
            }
        } else {
            return var2;
        }
    }

    public double apply(StatusEffectType var1, double var2) {
        EffectModule var4;
        if ((var4 = (EffectModule)this.getModules().get(var1)) != null) {
            if (var4.getValueType() != StatusEffectParameterType.FLOAT) {
                throw new RuntimeException("Unknown Type: " + var4 + " -> " + var4.getValueType().name());
            } else {
                return var2 * (double)var4.getFloatValue();
            }
        } else {
            return var2;
        }
    }

    public float apply(StatusEffectType var1, float var2) {
        EffectModule var3;
        if ((var3 = (EffectModule)this.getModules().get(var1)) != null) {
            if (var3.getValueType() != StatusEffectParameterType.FLOAT) {
                throw new RuntimeException("Unknown Type: " + var3 + " -> " + var3.getValueType().name());
            } else {
                return var2 * var3.getFloatValue();
            }
        } else {
            return var2;
        }
    }

    public float apply(StatusEffectType var1, DamageDealerType var2, float var3) {
        EffectModule var4;
        if ((var4 = (EffectModule)this.getModules().get(var1)) != null) {
            EffectModule var5;
            if ((var5 = (EffectModule)var4.getWeaponType().get(var2)) == null) {
                return var3;
            } else if (var5.getValueType() != StatusEffectParameterType.FLOAT) {
                throw new RuntimeException("Unknown Type for weapon: " + var4 + " -> " + var4.getValueType().name());
            } else {
                return var3 * var5.getFloatValue();
            }
        } else {
            return var3;
        }
    }

    public double apply(StatusEffectType var1, DamageDealerType var2, double var3) {
        EffectModule var5;
        if ((var5 = (EffectModule)this.getModules().get(var1)) != null) {
            EffectModule var6;
            if ((var6 = (EffectModule)var5.getWeaponType().get(var2)) == null) {
                return var3;
            } else if (var6.getValueType() != StatusEffectParameterType.FLOAT) {
                throw new RuntimeException("Unknown Type for weapon: " + var5 + " -> " + var5.getValueType().name());
            } else {
                return var3 * (double)var6.getFloatValue();
            }
        } else {
            return var3;
        }
    }

    public ShortList applyMergeTo(boolean var1, boolean var2, ShortList var3) {
        if (var1) {
            var3.addAll(this.permanentEffects);
        }

        if (var2) {
            var3.addAll(this.transientEffects);
        }

        return var3;
    }

    public static enum EffectEntityType {
        OTHER,
        STRUCTURE,
        SECTOR,
        SYSTEM;

        private EffectEntityType() {
        }
    }
}
