package api;

import api.config.BlockConfig;
import api.inventory.ItemStack;
import api.listener.Listener;
import api.listener.events.EntityScanEvent;
import api.listener.events.Event;
import api.listener.events.KeyPressEvent;
import api.listener.events.StructureStatsCreateEvent;
import api.main.GameClient;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.server.Server;
import api.utils.StarRunnable;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.ElementCountMap;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.element.ElementCategory;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        ElementInformation dabxd = BlockConfig.newElement("Impervium Armor", new short[]{5,2,3,4,5,6});
        dabxd.setBuildIconNum(234);
        dabxd.setMaxHitPointsE(100000);
        dabxd.setArmorValue(1000);
        config.add(dabxd);
    }
    public static void initBlockData(){
        final BlockConfig config = new BlockConfig();
        for(StarMod mod : StarLoader.starMods){
            mod.onBlockConfigLoad(config);
        }
        for (ElementInformation element : config.getElements()) {
            try {
                //ElementKeyMap.addInformationToExisting(new ElementInformation(element.getId(), element.getName(), ElementKeyMap.getCategoryHirarchy(), element.getTextureIds()));
                ElementKeyMap.addInformationToExisting(element);
            } catch (ParserConfigurationException e) {
                DebugFile.logError(e, null);
                e.printStackTrace();
            }
        }
        /*try {
            int aaaaa = ElementKeyMap.insertIntoProperties("aaaaa");
            ElementKeyMap.addInformationToExisting(new ElementInformation((short) aaaaa, "aaaaa", ElementKeyMap.getCategoryHirarchy(), new short[]{1,2,3,4,5,6}));
            Server.broadcastMessage("Key: " + aaaaa);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");
        //File file = config.writeXML();
       // config.loadXML();
        //ElementKeyMap.initializeData(file);
                //config.loadXML();
              //  ElementKeyMap.getInfo(ElementKeyMap.ACTIVAION_BLOCK_ID)



        //This is to register a new listener to listen for StructureStatsCreateEvent
        StarLoader.registerListener(StructureStatsCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                final StructureStatsCreateEvent e = ((StructureStatsCreateEvent) event);
                e.addStatLabel(20, new StatLabelResult() {
                    @Override
                    public String getValue() {
                        //SegmentControllers are ships or stations.
                        //It's going to take a while to get used to naming, dont worry about it.
                        SegmentController ship = e.getCurrentShip();
                        if(ship != null){
                            return String.valueOf(ship.railController.getDockedCount());
                        }
                        return "No ship";
                    }

                    @Override
                    public int getStatDistance() {
                        //Controls how far to the right it is.
                        return 30;
                    }

                    @Override
                    public String getName() {
                        return "Docked Entities: ";
                    }
                });
            }
        });
    }
}
