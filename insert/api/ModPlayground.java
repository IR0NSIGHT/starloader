package api;

import api.config.BlockConfig;
import api.element.block.Block;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import api.entity.Entity;
import api.entity.Player;
import api.entity.Station;
import api.gui.custom.examples.*;
import api.listener.Listener;
import api.listener.events.CannonShootEvent;
import api.listener.events.DamageBeamShootEvent;
import api.listener.events.Event;
import api.listener.events.StructureStatsCreateEvent;
import api.listener.events.block.BlockActivateEvent;
import api.listener.events.block.BlockKillEvent;
import api.listener.events.block.BlockModifyEvent;
import api.listener.events.block.BlockSalvageEvent;
import api.listener.events.calculate.CurrentPowerCalculateEvent;
import api.listener.events.calculate.MaxPowerCalculateEvent;
import api.listener.events.calculate.ShieldCapacityCalculateEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.player.PlayerChatEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.listener.events.register.ElementRegisterEvent;
import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.listener.events.systems.InterdictionCheckEvent;
import api.listener.events.systems.ShieldHitEvent;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.server.Server;
import api.systems.ChamberType;
import api.systems.addons.JumpInterdictor;
import api.systems.addons.custom.*;
import api.systems.modules.custom.CustomShipBeamElement;
import api.systems.modules.custom.example.disruptor.CustomBeamUnit;
import api.utils.StarRunnable;
import api.utils.VecUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.elements.activation.AbstractUnit;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.controller.elements.beam.BeamCommand;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamHandler;
import org.schema.game.common.controller.elements.jumpprohibiter.InterdictionAddOn;
import org.schema.game.common.controller.elements.pulse.push.PushPulseElementManager;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityCollectionManager;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityUnit;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.Universe;
import org.schema.game.mod.listeners.SegmentControllerListener;
import org.schema.game.server.data.GameServerState;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
        this.forceEnable = true;
    }

    public static short xorId = 0;

    @Override
    public void onBlockConfigLoad(BlockConfig config) {

    }

    public static void initBlockData() {
        final BlockConfig config = new BlockConfig();
        for (StarMod mod : StarLoader.starMods) {
            if(mod.isEnabled()) {
                mod.onBlockConfigLoad(config);
            }
        }
        /*for (ElementInformation element : config.getElements()) {
            try {
                ElementKeyMap.addInformationToExisting(element);
            } catch (ParserConfigurationException e) {
                DebugFile.logError(e, null);
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");

        //HELP COMMAND
        StarLoader.registerListener(PlayerCommandEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                PlayerCommandEvent e = (PlayerCommandEvent) event;
                if(e.command.equalsIgnoreCase("help")){
                    Player player = e.player;
                    player.sendServerMessage("### COMMANDS: ###");
                    for (ImmutablePair<String, String> command : StarLoader.getCommands()) {
                        player.sendServerMessage(command.left + ": " + command.right);
                    }
                }
            }
        });


        /*StarLoader.registerListener(ShieldCapacityCalculateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                ShieldCapacityCalculateEvent e = (ShieldCapacityCalculateEvent) event;

                long bonusShields = 0;
                ShieldCapacityUnit capacityUnit = ((ShieldCapacityCalculateEvent) event).getUnit();
                Vector3i max = capacityUnit.getMax(new Vector3i());
                Vector3i min = capacityUnit.getMin(new Vector3i());
                int deltaX = Math.abs(max.x-min.x);
                int deltaY = Math.abs(max.y-min.y);
                int deltaZ = Math.abs(max.z-min.z);
                int smallAxes = 0;
                if(deltaX <= 5)
                    smallAxes++;
                if(deltaY <= 5)
                    smallAxes++;
                if(deltaZ <= 5)
                    smallAxes++;
                if(smallAxes >= 2) {
                    e.setShields((long) (e.getCapacity() * 1.2));
                }
            }
        });*/
        getConfig().saveDefault("this is a: test");

        StarLoader.registerListener(PlayerCommandEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                PlayerCommandEvent e = (PlayerCommandEvent) event;
                Player p = e.player;
                if(e.command.equalsIgnoreCase("test")){
                    Entity currentEntity = p.getCurrentEntity();
                    if(currentEntity == null){
                        p.sendServerMessage("You are in: nothing, thanks for playing");
                    }else{
                        p.sendServerMessage("You are in: " + currentEntity.getUID());
                    }
                }
            }
        });

        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                HudCreateEvent ev = (HudCreateEvent) event;
                BasicInfoGroup bar = new BasicInfoGroup(ev);
            }
        });


//        final int[] t = {0};
//        new StarRunnable() {
//            @Override
//            public void run() {
//                t[0] += 4;
//            }
//        }.runTimer(1);
//        StarLoader.registerListener(CannonShootEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                CannonShootEvent e = (CannonShootEvent) event;
//                Color hsb = Color.getHSBColor(((float) t[0] % 360) / 360F, 1F, 1F);
//                Vector4f tuple4f = new Vector4f(hsb.getRed() / 255F, hsb.getGreen() / 255F, hsb.getBlue() / 255F, 1F);
//                e.setColor(tuple4f);
//            }
//        });

        StarLoader.registerListener(RegisterEffectsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterEffectsEvent ev = (RegisterEffectsEvent) event;
                for (StatusEffectType types : StatusEffectType.values()) {
                    if (types.name().contains("CUSTOM")) {
                        ev.addEffectModifier(types, 10F);
                    }
                }
            }
        });
    }
}
