package api.systems;

import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import java.util.List;

public class Chamber {

    private ReactorElement internalChamber;

    public Chamber(ReactorElement internalChamber) {
        this.internalChamber = internalChamber;
    }

    public List<Chamber> getChildren() {
        List<Chamber> children = null;
        for(ReactorElement internalChildChamber : internalChamber.children) {
            children.add(new Chamber(internalChildChamber));
        }
        return children;
    }

    public int getSize() {
        return internalChamber.getSize();
    }

    public boolean isDamaged() {
        return internalChamber.isDamaged();
    }

    public boolean hasValidConduit() {
        return internalChamber.validConduit;
    }

    public boolean isRoot() {
        return internalChamber.isRoot();
    }
}
