package at.randorf.lore;

import at.randorf.enums.Rarity;
import at.randorf.item.CustomItemData;
import net.kyori.adventure.text.Component;
import java.util.List;

public class LoreFactory {

    public static List<Component> createLore(CustomItemData data) {
        LoreBuilder builder = new LoreBuilder();

        builder.property("Typ", data.typeName());
        builder.space();

        if (data.stats() != null && !data.stats().isEmpty()) {
            data.stats().forEach((statName, value) -> builder.attribute("", statName, value));
            builder.space();
        }
        if(data.abilityTitle() != null){
            builder.abilityTitle(data.abilityTitle(),data.rarity().getColor());
            builder.space();
        }
        if (data.abilityDescription() != null) {
            builder.ability(data.abilityDescription(),data.rarity().getColor());
            builder.space();
        }
        builder.rarity(data.rarity().getValue(), data.rarity().getColor());
        return builder.build();
    }
}