package api.element.block;

public enum FactoryType {
    //stateDescs = {"none", "capsule refinery", "micro assembler", "basic factory", "standard factory", "advanced factory"},
    NONE,
    CAPSULE_REFINERY,
    MICRO_ASSEMBLER,
    BASIC,
    STANDARD,
    ADVANCED;

    public short getId(){
        return (short) ordinal();
    }
}
