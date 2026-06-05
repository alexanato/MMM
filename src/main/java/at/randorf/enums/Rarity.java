package at.randorf.enums;

import net.kyori.adventure.text.format.NamedTextColor;

public enum Rarity {
    TRASH("Trash"),
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
    MYTHIC("Mythic"),
    ARTIFACT("Artifact"),
    UNDEFINED("Undefined");

    private final String value;

    Rarity(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public NamedTextColor getColor() {
        return switch (this.value) {
            case "Trash" -> NamedTextColor.GRAY;
            case "Common" -> NamedTextColor.WHITE;
            case "Uncommon" -> NamedTextColor.GREEN;
            case "Rare" -> NamedTextColor.BLUE;
            case "Epic" -> NamedTextColor.LIGHT_PURPLE;
            case "Legendary" -> NamedTextColor.GOLD;
            case "Mythic" -> NamedTextColor.DARK_RED;
            case "Artifact" -> NamedTextColor.AQUA;
            default -> NamedTextColor.GRAY;
        };
    }

    public static Rarity valueOf(String name, boolean ignoreCase) {
        if (ignoreCase) {
            for (Rarity rarity : values()) {
                if (rarity.name().equalsIgnoreCase(name)) {
                    return rarity;
                }
            }
            throw new IllegalArgumentException("Ungültige Rarity: " + name);
        }
        return valueOf(name);
    }
}