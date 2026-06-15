package at.randorf.commands.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommandActionDefinition(
        String type,
        Map<String, Object> properties
) {
    public CommandActionDefinition {
        properties = properties == null ? Map.of() : Map.copyOf(properties);
    }
}
