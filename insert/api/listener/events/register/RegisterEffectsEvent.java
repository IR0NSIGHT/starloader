package api.listener.events.register;

import api.listener.events.Event;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;

import java.util.ArrayList;

public class RegisterEffectsEvent extends Event {
    public static int id = idLog++;
    public ArrayList<Pair<StatusEffectType, Float>> registeredEffects = new ArrayList<Pair<StatusEffectType, Float>>();
    public void addEffectModifier(StatusEffectType type, float value){
        registeredEffects.add(new ImmutablePair<StatusEffectType, Float>(type, value));
    }

}
