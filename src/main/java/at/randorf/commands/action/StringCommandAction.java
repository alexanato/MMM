package at.randorf.commands.action;

import net.minestom.server.command.builder.CommandContext;

import java.util.Map;

public abstract class StringCommandAction implements CommandAction {
    private final Map<String, String> properties;

    protected StringCommandAction(Map<String, String> properties) {
        this.properties = Map.copyOf(properties);
    }

    protected String property(String key, String fallback) {
        String value = properties.get(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    protected String format(String template, CommandContext context) {
        String result = template;
        for (Map.Entry<String, Object> entry : context.getMap().entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }
}
