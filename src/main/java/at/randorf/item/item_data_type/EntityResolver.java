package at.randorf.item.item_data_type;

import at.randorf.Main;
import at.randorf.registries.ObjectResolverRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EntityResolver implements ObjectResolver {
    @Override
    public String resolve(Object object, String field) {
        EntityType entityType = (EntityType) object;
        switch (field) {
            case "x" -> {
                return String.valueOf(entityType.X());
            }
            case "y" -> {
                return String.valueOf(entityType.Y());
            }
            default -> {
                try {
                    Main.mapper.writeValueAsString(entityType);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
