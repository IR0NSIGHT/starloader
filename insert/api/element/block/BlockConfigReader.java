package api.element.block;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class BlockConfigReader {
    File blockConfig = new File(System.getProperty("user.dir") + "/data/config/BlockConfig.xml");
    public DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    public DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    public Document document = documentBuilder.parse(blockConfig);
    public Element config;
    public Element element;
    public Element general;

    public BlockConfigReader() throws ParserConfigurationException, IOException, SAXException {
    }

    public void createCategory(String categoryName) {
        Element category = document.createElement("" + categoryName);
        general.appendChild(category);
    }

    public void registerConfigBlocks() {

    }
}
