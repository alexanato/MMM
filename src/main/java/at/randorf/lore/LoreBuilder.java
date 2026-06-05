package at.randorf.lore;

import at.randorf.enums.LoreStyle;
import at.randorf.enums.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {
    private final List<Component> lines = new ArrayList<>();

    public LoreBuilder space() {
        lines.add(Component.empty());
        return this;
    }

    public LoreBuilder property(String key, String value) {
        lines.add(Component.text()
                .append(Component.text(key + ": ", LoreStyle.PRIMARY.getColor()))
                .append(Component.text(value, LoreStyle.VALUE.getColor()))
                .build());
        return this;
    }

    public LoreBuilder attribute(String icon, String name, String value) {
        String prefix = icon.isEmpty() ? "" : icon + " ";
        lines.add(Component.text()
                .append(Component.text(prefix + name + ": ", LoreStyle.PRIMARY.getColor()))
                .append(Component.text(value, LoreStyle.VALUE.getColor()))
                .build());
        return this;
    }

    public LoreBuilder abilityTitle(String title,NamedTextColor color) {
        lines.add(Component.text(title, color).decoration(TextDecoration.BOLD, true));
        return this;
    }
    public LoreBuilder ability(String description,NamedTextColor color) {
        if (description == null || description.isEmpty()) {
            return this;
        }

        String[] words = description.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > 35 && currentLine.length() > 0) {
                lines.add(Component.text(currentLine.toString(), color).decoration(TextDecoration.ITALIC,true));
                currentLine = new StringBuilder();
            }

            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            lines.add(Component.text(currentLine.toString(), color));
        }

        return this;
    }


    public LoreBuilder rarity(String name, NamedTextColor color) {
        lines.add(Component.text(name, color).decoration(TextDecoration.BOLD, true));
        return this;
    }

    public List<Component> build() {
        return lines.stream()
                .map(comp -> comp.hasDecoration(TextDecoration.ITALIC) ? comp : comp.decoration(TextDecoration.ITALIC, false))
                .toList();
    }
}