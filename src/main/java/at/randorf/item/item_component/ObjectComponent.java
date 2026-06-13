package at.randorf.item.item_component;

import at.randorf.Main;
import at.randorf.item.ItemComponent;
import at.randorf.item.ItemComponentContext;
import at.randorf.item.item_data_type.ObjectResolver;
import at.randorf.registries.ObjectResolverRegistry;

import java.util.Map;

public abstract class ObjectComponent<T> extends BasicComponent implements ItemComponent {

    public ObjectComponent(Map<String, String> properties) {
        super(properties);
    }

    protected abstract T getObject(ItemComponentContext ctx);
    public ObjectComponent(){}
    @Override
    public String returnData(ItemComponentContext ctx) {

        T object = getObject(ctx);

        if (object == null) {
            return "";
        }

        String field = properties.get("field");

        if (field == null || field.isBlank()) {
            try {
                return Main.mapper.writeValueAsString(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return ObjectResolver.resolve( object,field);
        //return ((ObjectResolver<Object>) ObjectResolverRegistry.get(object.getClass())).resolve(object, field);
    }
}