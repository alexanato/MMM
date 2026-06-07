package at.randorf.item.item_data_type;

import at.randorf.Main;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectResolver {
    private static final Map<Class<?>, Map<String, Method>> ObjectCache = new HashMap<>();

    private static Map<String, Method> scanMethods(Class<?> clazz) {

        Map<String, Method> methods =
                new HashMap<>();
        Method[] objectMethods = Object.class.getDeclaredMethods();
        for (Method method : clazz.getDeclaredMethods()) {
            methods.put(
                    method.getName(),
                    method);
        }
        System.out.println(methods);
        return methods;
    }

    public static String resolve(Object object, String field) {
        ObjectCache.computeIfAbsent(
                object.getClass(),
                ObjectResolver::scanMethods
        );
        return ObjectCache.get(object.getClass()). entrySet().stream()
                .filter(entry -> entry.getKey().equals(field))
                .map(Map.Entry::getValue)
                .map(m-> {
                    try {
                        return m.invoke(object);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return (Object) Main.mapper.writeValueAsString(object);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Fehler beim Erzeugen des Fallback-JSONs", e);
                    }
                }).toString();
    }
}
