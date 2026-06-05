package at.randorf.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum LoreStyle {
    PRIMARY(NamedTextColor.GRAY),
    VALUE(NamedTextColor.GREEN),
    ABILITY_TITLE(NamedTextColor.DARK_GREEN),
    ABILITY_TEXT(NamedTextColor.GREEN),
    FLAVOUR(NamedTextColor.DARK_GRAY);

    private final TextColor color;

    LoreStyle(TextColor color) {
        this.color = color;
    }

    public TextColor getColor() {
        return this.color;
    }
}