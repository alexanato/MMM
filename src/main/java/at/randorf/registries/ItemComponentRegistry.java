package at.randorf.registries;

import at.randorf.item.ItemComponent;
import at.randorf.item.item_component.HelloWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ItemComponentRegistry {
    private static final Map<String, Function<Map<String, String>, ItemComponent>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("HELLO_WORLD", HelloWorld::new);
    }
    public static Optional<ItemComponent> createComponent(String type, Map<String, String> properties) {
        Function<Map<String, String>, ItemComponent> factory = REGISTRY.get(type.toUpperCase());
        if (factory == null) return Optional.empty();

        return Optional.of(factory.apply(properties));
    }
}
