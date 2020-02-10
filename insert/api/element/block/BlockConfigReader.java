package api.element.block;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import api.element.InventoryGroup;
import api.element.Recipe;
import api.main.Global;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BlockConfigReader {
    File blockConfig = new File(System.getProperty("user.dir") + "/data/config/BlockConfig.xml");
    //Todo:Make sure this will actually access the xml correctly
    public DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    public DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    public Document document = documentBuilder.parse(blockConfig);

    public BlockConfigReader() throws ParserConfigurationException, IOException, SAXException {
        //Todo:Handle any errors
    }

    /*
    public void registerConfigBlocks() {
        document.getDocumentElement().normalize();

        //General>Hulls
        Element hulls = document.getElementById("Hulls");
        //General>Hulls>Basic
        NodeList basic = hulls.getElementsByTagName("Basic");
        //General>Hulls>Colors
        NodeList colors = hulls.getChildNodes();
        for(int c = 0; c < colors.getLength(); c ++) {
            Node color = colors.item(c);
            NodeList blockNodes = color.getChildNodes();
            for(int n = 0; n < blockNodes.getLength(); n ++) {
                Node blockNode = blockNodes.item(n);

                Block block = new Block(Integer.parseInt(blockNode.getAttributes().getNamedItem("icon").getNodeValue()), blockNode.getAttributes().getNamedItem("type").getNodeValue());
                NodeList blockItems = blockNode.getChildNodes();

                //Texture IDs
                List<Integer> textureIDs = null;
                String intList = (blockNode.getAttributes().getNamedItem("textureId").getNodeValue());
                Scanner scan = new Scanner(intList);
                while(scan.hasNextInt()) {
                    textureIDs.add(scan.nextInt());
                }
                block.setTextureIDs(textureIDs);

                //Recipe
                Recipe recipe = new Recipe();
                Node recipeNode = blockItems.item(0);
                NodeList recipeNodes = recipeNode.getChildNodes();
                Map<api.element.Element, Integer> ingredients = null;

                for(int r = 0; r < recipeNodes.getLength(); r ++) {
                    Node component = recipeNodes.item(r);
                    int count = Integer.parseInt(component.getAttributes().getNamedItem("count").getNodeValue());
                    String itemString = component.getNodeValue();
                    api.element.Element item = new api.element.Element(1, null);
                    item.setName(itemString);
                    ingredients.put(item, count);
                }
                recipe.setIngredients(ingredients);
                recipe.setNumberProduced(Integer.parseInt(blockItems.item(7).getNodeValue()));
                recipe.setBakeTime(Double.parseDouble(blockItems.item(9).getNodeValue()));
                block.setRecipe(recipe);

                //Armor Value
                Node armorValueNode = blockItems.item(2);
                double armorValue = Double.parseDouble(armorValueNode.getNodeValue());
                block.setArmor(armorValue);

                //EffectArmor
                Node effectValueNode = blockItems.item(3);
                NodeList effectNodes = effectValueNode.getChildNodes();
                block.setHeatValue(Double.parseDouble(effectNodes.item(0).getNodeValue()));
                block.setKineticValue(Double.parseDouble(effectNodes.item(1).getNodeValue()));
                block.setEmValue(Double.parseDouble(effectNodes.item(2).getNodeValue()));

                //Price
                Node priceNode = blockItems.item(4);
                int price = Integer.parseInt(priceNode.getNodeValue());
                block.setPrice(price);

                //Description
                Node descriptionNode = blockItems.item(5);
                String description = descriptionNode.getNodeValue();
                block.setDescription(description);

                //Inventory Group
                Node inventoryGroupNode = blockItems.item(10);
                InventoryGroup inventoryGroup = new InventoryGroup(inventoryGroupNode.getNodeValue());
                inventoryGroup.addBlock(block);
                Global.inventoryGroups.add(inventoryGroup);

                //Animated
                Node animatedNode = blockItems.item(11);
                Boolean animated = Boolean.getBoolean(animatedNode.getNodeValue());
                block.setAnimated(animated);

                //Transparency
                Node transparencyNode = blockItems.item(13);
                Boolean transparent = Boolean.getBoolean(transparencyNode.getNodeValue());
                block.setTransparent(transparent);

                //In Shop


                //Register Block to list
                Global.blockList.add(block);
            }
        }
    }
    */
}
