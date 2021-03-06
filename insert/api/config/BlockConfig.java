package api.config;

import api.DebugFile;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.FactoryResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class BlockConfig {
    public static void addRecipe(ElementInformation info, int type, int bakeTime, FactoryResource... resources){
        for (FactoryResource resource : resources){
            info.consistence.add(resource);
            info.cubatomConsistence.add(resource);
        }
        info.factoryBakeTime = bakeTime;
        info.blockResourceType = 2;
        info.producedInFactory = type;
    }
    public static void registerComputerModulePair(short computer, short module){
        ElementInformation comp = ElementKeyMap.infoArray[computer];
        comp.mainCombinationController = true;
        comp.systemBlock = true;
        comp.controlledBy.add((short) 1);
        comp.controlling.add(module);

        ElementKeyMap.infoArray[module].controlledBy.add(computer);
    }
    public static void registerElementBlock(ElementInformation info){
        info.systemBlock = true;
        info.controlledBy.add((short) 1);
    }
    public static void setBlocksConnectable(ElementInformation cBlock, ElementInformation vBlock){
        cBlock.setCanActivate(true);
        vBlock.setCanActivate(true);
        cBlock.controlling.add(vBlock.id);
        vBlock.controlledBy.add(cBlock.id);
    }
    public static void clearRecipes(ElementInformation element){
        element.cubatomConsistence.clear();
        element.consistence.clear();
    }
    public static void setBasicInfo(ElementInformation info, String description, int price, float mass, boolean placeable, boolean activatable, int buildIcoNum){
        info.description = description;
        info.price = price;
        info.mass = mass;
        info.placable = placeable;
        info.setMaxHitPointsE(1);
        info.setCanActivate(activatable);
        info.setBuildIconNum(buildIcoNum);

    }

    public static ElementInformation newElement(String name, short... ids){
        short id = (short) ElementKeyMap.insertIntoProperties(name);
        ElementInformation elementInformation = new ElementInformation(id, name, ElementKeyMap.getCategoryHirarchy(), ids);
        int idLength = ids.length;
        if(idLength == 3 || idLength == 6 || idLength == 1) {
            elementInformation.individualSides = idLength;
            if(idLength == 1){
                short t = ids[0];
                ids = new short[]{t,t,t,t,t,t};
                elementInformation.setTextureId(ids);
            }else if(idLength == 3){
                ids = new short[]{ids[0],ids[1],ids[0],ids[1],ids[0],ids[1]};
                elementInformation.setTextureId(ids);
            }else{
                elementInformation.setTextureId(ids);
            }

            try {
                Method m = ElementInformation.class.getDeclaredMethod("recreateTextureMapping");
                m.setAccessible(true);
                m.invoke(elementInformation);
                //imagine using java 6
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            DebugFile.warn("You just passed a " + idLength + " array to newElement... Use sizes of 1, 3, or 6");
            DebugFile.warn("If its a grey basic armor texture, that is why");
        }
        //todo resort at end
        ElementKeyMap.sortedByName.add(elementInformation);
        return elementInformation;
    }
    public static ElementInformation newChamber(String name, short rootChamber, StatusEffectType appliedEffect){
        //TODO find out why 640 is a chamber texture
        ElementInformation info = newElement(name, new short[]{640});
        info.blockResourceType = 2;
        info.sourceReference = 1085;
        info.chamberRoot = rootChamber;
        info.chamberParent = 1085;
        info.chamberPermission = 1;
        info.chamberPrerequisites.add((short) 1085);
        info.placable = false;
        info.canActivate = true;
        info.systemBlock = true;

        info.price = 100;
        info.description = "A Custom chamber";
        info.shoppable = false;
        info.mass = 0.15F;

        info.chamberConfigGroupsLowerCase.add(appliedEffect.name().toLowerCase(Locale.ENGLISH));
        ElementKeyMap.chamberAnyTypes.add(info.getId());

        ElementInformation parentInfo = ElementKeyMap.getInfo(rootChamber);
        parentInfo.chamberChildren.add(info.getId());
        return info;
    }

    private ArrayList<ElementInformation> elements = new ArrayList<ElementInformation>();
    public ArrayList<ElementInformation> getElements(){
        return elements;
    }
    public void add(ElementInformation entry) {
        // ElementKeyMap.getCategoryHirarchy();
        elements.add(entry);
        try {
            ElementKeyMap.addInformationToExisting(entry);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
