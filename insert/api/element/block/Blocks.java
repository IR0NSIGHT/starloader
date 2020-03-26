package api.element.block;

import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;

public enum Blocks {
    SHIP_CORE(1, ElementKeyMap.getInfo(1));

    Blocks(int id, ElementInformation elementInfo) {
        block = new Block(id, elementInfo.name); //Not sure if this is the right one
        block.setAnimated(elementInfo.animated);
        block.setArmor(elementInfo.armorValue);
        block.setPrice((int) elementInfo.price);
        block.setHP(elementInfo.maxHitPointsFull); //Not sure if this is the right one
        //block.setEmValue(); No idea how to get this
        //block.setHeatValue(); No idea how to get this
        //block.setKineticValue(); No idea how to get this
        //block.setTransparent(); No idea how to get this
        block.setDescription(elementInfo.description);
        //block.setRecipe(); Will have to construct recipe somehow
        block.setActivatable(elementInfo.canActivate);
        block.setBeacon(elementInfo.beacon);
        //block.setBlockShape(); Will have to convert from LODShape to API BlockShape
        //block.setBlockType(); Sets the block's category, will have to figure this out later
        //block.setControllers(); Will have to figure out how to get controllers
        //block.setControlling(); Will have to figure out how to get controlling blocks
        //block.setCustomModel(); No idea how I'm gonna do this
        block.setDoor(elementInfo.door);
        block.setDrawLogicConnection(elementInfo.drawLogicConnection);
        //block.getInventoryGroup() No idea how I'm gonna do this
        block.setEffectCombination(elementInfo.effectCombinationController);
        block.setEnterable(elementInfo.enterable);
        //block.setLightColors(); Somehow need to convert from Vector4f to a list of RGB values
        block.setMainCombination(elementInfo.mainCombinationController);
        block.setMass(elementInfo.mass);
        block.setOrientable(elementInfo.orientatable);
        block.setPhysical(elementInfo.physical);
        block.setSensorInput(elementInfo.sensorInput);
        block.setSideTexturesPointToOrientation(elementInfo.sideTexturesPointToOrientation);
        block.setSupportCombination(elementInfo.supportCombinationController);
        //block.setTextureIDs(); Will need to convert from short array to ArrayList
        block.setVolume(elementInfo.volume);
        block.setBuildIcon(elementInfo.buildIconNum);
        //block.setCategory(); No idea how to do this
        block.setDeprecated(elementInfo.deprecated);
        block.setFullName(elementInfo.fullName);
        block.setInRecipe(elementInfo.inRecipe);
        block.setInShop(elementInfo.shoppable);
    }

    private Block block;

    public Block getBlock() {
        return this.block;
    }
}
