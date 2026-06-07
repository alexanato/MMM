package at.randorf.registries;

import at.randorf.item.item_data_type.EntityResolver;
import at.randorf.item.item_data_type.EntityType;
import at.randorf.item.item_data_type.ObjectResolver;

import java.util.HashMap;
import java.util.Map;

public final class ObjectResolverRegistry {

    public static final Map<Class<?>, ObjectResolver<?>> RESOLVERS
            = new HashMap<>();
    static {
        ObjectResolverRegistry.register(
                EntityType.class,
                new EntityResolver()
        );
    }
    public static <T> void register(
            Class<T> type,
            ObjectResolver<T> resolver) {

        RESOLVERS.put(type, resolver);
    }
    public static void  register(ObjectResolver objectResolver){
        RESOLVERS.put(objectResolver.getClass(),objectResolver);
    }

    public static <T> ObjectResolver<T> get(Class<T> type) {
        return (ObjectResolver<T>) RESOLVERS.get(type);
    }
}