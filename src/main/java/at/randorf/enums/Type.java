package at.randorf.enums;
public enum Type {
    SWORD("Sword"),
    AXE("Axe"),
    MACHINE("Machine"),
    ARTIFACT("Artifact");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Type valueOf(String name, boolean ignoreCase) {
        if (ignoreCase) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }
        return valueOf(name);
    }
}