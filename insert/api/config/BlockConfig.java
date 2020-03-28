package api.config;

import api.DebugFile;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.ChamberType;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementCategory;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.resource.FileExt;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class BlockConfig {
    public static ElementInformation newElement(String name, short... ids){
        short id = (short) ElementKeyMap.insertIntoProperties(name);
        return new ElementInformation(id, name, ElementKeyMap.getCategoryHirarchy(), ids);
    }
    public static ElementInformation newChamber(String name, short rootChamber, short[] ids, StatusEffectType appliedEffect){
        ElementInformation info = newElement(name, ids);
        info.blockResourceType = 2;
        //info.sourceReference = 1085;
        info.chamberRoot = rootChamber;
        //info.chamberParent = 1085;
        info.chamberPermission = 1;
        info.chamberPrerequisites.add(rootChamber);
        info.placable = true;
        info.canActivate = true;
        info.systemBlock = true;
        //info.chamberConfigGroupsLowerCase.add("mobility - top speed 1");
        info.chamberConfigGroupsLowerCase.add(appliedEffect.name().toLowerCase());
        ElementKeyMap.chamberAnyTypes.add(info.getId());

        ElementInformation parentInfo = ElementKeyMap.getInfo(rootChamber);
        info.chamberChildren.add(parentInfo.getId());

        return info;
    }

    private ArrayList<ElementInformation> elements = new ArrayList<>();
    public ArrayList<ElementInformation> getElements(){
        return elements;
    }
    public void add(ElementInformation entry) {
        // ElementKeyMap.getCategoryHirarchy();
        elements.add(entry);
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
