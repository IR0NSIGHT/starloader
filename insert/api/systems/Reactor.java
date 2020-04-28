package api.systems;

import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;
import java.util.List;

public class Reactor {

    public ReactorTree internalReactor;

    public Reactor(ReactorTree internalReactor) {
        this.internalReactor = internalReactor;
    }

    public float getHp() {
        return internalReactor.getHp();
    }

    public float getMaxHp() {
        return internalReactor.getMaxHp();
    }

    public List<Chamber> getChildren() {
        List<Chamber> children = null;
        for(ReactorElement internalChildChamber : internalReactor.children) {
            children.add(new Chamber(internalChildChamber));
        }
        return children;
    }

    public int getSize() {
        return internalReactor.getSize();
    }

    public int getLevel() {
        return internalReactor.getLevel();
    }

    public double getRegen() {
        return internalReactor.pw.getCurrentPowerGain();
    }

    public double getCurrentUsage() {
        return internalReactor.pw.getCurrentConsumptionPerSec();
    }

    public double getHpPercent() {
        return internalReactor.getHpPercent();
    }
    public double getUsagePercent() {
        return internalReactor.pw.getPowerConsumptionAsPercent();
    }

    public void setBoost(float boost) {
        internalReactor.pw.setReactorBoost(boost);
    }
}
