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
        Reactor reactor = entity.getCurrentReactor();
        if(reactor == null){
            return 0;
        }
        return (float) entity.getCurrentReactor().getHpPercent();
    }

    @Override
    public String getText() {
        Reactor reactor = entity.getCurrentReactor();
        if(reactor == null){
            return "Reactor: N/A";
        }
        return "Reactor: [" + StringTools.massFormat(reactor.getHp()) + " / " + StringTools.massFormat(reactor.getMaxHp()) + "]";
    }
}
