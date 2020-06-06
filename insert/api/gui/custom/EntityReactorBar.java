package api.gui.custom;

import api.entity.Entity;
import api.systems.Reactor;
import api.systems.Shield;
import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;
import org.schema.game.common.controller.elements.power.reactor.PowerInterface;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;

public abstract class EntityReactorBar extends CustomHudBar {

    public Entity entity;
    public void setEntity(Entity e){
        entity = e;
    }

    @Override
    public boolean drawBar() {
        return entity != null;
    }



    @Override
    public float getFilled() {
        if(entity == null){
            return 0;
        }
        Reactor reactor = entity.getCurrentReactor();
        if(reactor == null){
            return 0;
        }
        return (float) reactor.getHpPercent();
    }

    @Override
    public String getText() {
        Entity entity = this.entity;
        if(entity != null){
            Reactor reactor = entity.getCurrentReactor();
            if(reactor != null) {
                return "Reactor: [" + StringTools.massFormat(reactor.getHp()) + " / " + StringTools.massFormat(reactor.getMaxHp()) + "]";
            }
        }
        return "Reactor: N/A";
    }
}
