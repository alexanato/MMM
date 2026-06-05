package at.randorf.item;

import java.util.Map;

public record ComponentData(
        String type,
        Map<String, String> properties
) {}