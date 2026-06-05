package at.randorf.item;

import at.randorf.enums.Rarity;

import java.util.List;
import java.util.Map;

public record CustomItemData(
        String name,
        String typeName,
        Rarity rarity,
        Map<String, String> stats,
        Map<String, String> hiddenStats,
        String abilityTitle,
        String abilityDescription,
        String material,
        List<ComponentData> components,
        int id
) {}