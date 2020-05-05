package api.config;

import api.DebugFile;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import org.schema.game.client.view.gui.weapon.WeaponRowElement;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.game.common.data.element.annotation.ElemType;
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
import java.util.ArrayList;
import java.util.Locale;

public class BlockConfig {
    public static void addRecipe(ElementInformation info, FactoryType type, int bakeTime, FactoryResource... resources){
        for (FactoryResource resource : resources){
            info.consistence.add(resource);
            info.cubatomConsistence.add(resource);
        }
        info.factoryBakeTime = bakeTime;
        info.blockResourceType = 2;
        info.producedInFactory = type.getId();
    }
    public static void clearRecipes(ElementInformation element){
        element.cubatomConsistence.clear();
        element.consistence.clear();
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
            }
            elementInformation.normalizeTextureIds();
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
    public void loadXML(){
        //ElementKeyMap.reinitializeData(new FileExt("loader"+File.separator+"CustomBlockConfig.xml"), false, null, null);
    }
    @Deprecated
    public File writeXML() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("Config");
            Element elem = document.createElement("Element");
            Element gen = document.createElement("General");
            document.appendChild(root);
            root.appendChild(elem);
            elem.appendChild(gen);


            for (ElementInformation element : elements) {
                element.appendXML(document, gen);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource domSource = new DOMSource(document);
            new File("loader").mkdir();
            File f = new File("loader/CustomBlockConfig.xml");
            StreamResult streamResult = new StreamResult(f);

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
