//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.blockeffects.config;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectFloatValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EffectAccumulator {
    private final ShortArrayList notFound = new ShortArrayList();
    private final List<ConfigGroup> active = new ObjectArrayList();
    private final Map<StatusEffectType, EffectModule> modules = new Object2ObjectOpenHashMap();
    private final List<EffectModule> modulesList = new ObjectArrayList();
    private final List<EffectConfigElement> activeElements = new ObjectArrayList();
    private final Map<StatusEffectType, List<EffectConfigElement>> effectMap = new Object2ObjectOpenHashMap();

    public EffectAccumulator() {
    }

    public void calculate(ShortSet var1, ConfigPool var2) {
        this.notFound.clear();
        this.active.clear();
        this.activeElements.clear();
        this.modulesList.clear();
        this.effectMap.clear();
        this.modules.clear();

        Iterator var6 = var1.iterator();

        while (var6.hasNext()) {
            short var3 = (Short) var6.next();
            ConfigGroup var4;
            if ((var4 = (ConfigGroup) var2.ntMap.get(var3)) != null) {
                System.err.println("!!! Registering config group: " + var4.id);
                this.active.add(var4);

                List<EffectConfigElement> var5;
                EffectConfigElement var9;
                for (Iterator var7 = var4.elements.iterator(); var7.hasNext(); ((List) var5).add(var9)) {
                    var9 = (EffectConfigElement) var7.next();
                    System.err.println(" > Interating through: " + var9.value);
                    this.activeElements.add(var9);
                    var5 = this.effectMap.get(var9.getType());
                    if (var5 == null) {
                        var5 = new ObjectArrayList<>();
                        this.effectMap.put(var9.getType(), var5);
                    }
                }
            } else {
                System.err.println("No info" + var3);
                this.notFound.add(var3);
            }
        }

        var6 = this.effectMap.entrySet().iterator();

        while (var6.hasNext()) {
            Entry var8;
            Collections.sort((List) (var8 = (Entry) var6.next()).getValue());
            EffectModule var10;
            (var10 = new EffectModule()).create((StatusEffectType) var8.getKey(), (List) var8.getValue());

            assert var10.getType() != null;

            this.modulesList.add(var10);
            this.modules.put((StatusEffectType) var8.getKey(), var10);
        }
        //INSERTED CODE
        /*ConfigGroup config = new ConfigGroup(StatusEffectType.CUSTOM_EFFECT_01.name());
        this.active.add(config);
        EffectConfigElement element = new EffectConfigElement();
        element.init(StatusEffectType.CUSTOM_EFFECT_01);
        config.elements.add(element);
        this.activeElements.add(element);
        ObjectArrayList<EffectConfigElement> v = new ObjectArrayList<>();
        this.effectMap.put(element.getType(), v);
        EffectModule module = new EffectModule();
        module.create(StatusEffectType.CUSTOM_EFFECT_01, v);
        module.setFloatValue(100);
        this.modulesList.add(module);
        this.modules.put(StatusEffectType.CUSTOM_EFFECT_01, module);*/

    }

    public List<ConfigGroup> getActive() {
        return this.active;
    }

    public Map<StatusEffectType, EffectModule> getModules() {
        return this.modules;
    }

    public List<EffectModule> getModulesList() {
        return this.modulesList;
    }

    public ShortArrayList getNotFound() {
        return this.notFound;
    }
}
