package at.randorf.registries;

import at.randorf.item.ItemComponent;
import at.randorf.item.ItemComponentType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.function.Function;

public class ItemComponentRegistry {
    private static final String COMPONENT_PACKAGE = "at.randorf.item.item_component";
    private static final Map<String, Function<Map<String, String>, ItemComponent>> REGISTRY = new HashMap<>();

    static {
        registerAnnotatedComponents(COMPONENT_PACKAGE);
    }

    public static Optional<ItemComponent> createComponent(String type, Map<String, String> properties) {
        Function<Map<String, String>, ItemComponent> factory = REGISTRY.get(type.toUpperCase(Locale.ROOT));
        if (factory == null) return Optional.empty();

        return Optional.of(factory.apply(properties));
    }

    private static void registerAnnotatedComponents(String packageName) {
        for (Class<?> clazz : findClasses(packageName).values()) {
            if (!ItemComponent.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            ItemComponentType annotation = clazz.getAnnotation(ItemComponentType.class);
            if (annotation == null) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends ItemComponent> componentClass = (Class<? extends ItemComponent>) clazz;
            register(annotation.value(), componentClass);
        }
    }

    private static void register(String type, Class<? extends ItemComponent> componentClass) {
        try {
            Constructor<? extends ItemComponent> constructor = componentClass.getConstructor(Map.class);
            REGISTRY.put(type.toUpperCase(Locale.ROOT), properties -> {
                try {
                    return constructor.newInstance(properties);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not create item component " + componentClass.getName(), e);
                }
            });
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Item component " + componentClass.getName() + " needs a public constructor with Map<String, String>",
                    e
            );
        }
    }

    private static Map<String, Class<?>> findClasses(String packageName) {
        Map<String, Class<?>> classes = new HashMap<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    findClassesInDirectory(packageName, new File(resource.toURI()), classes);
                } else if ("jar".equals(resource.getProtocol())) {
                    findClassesInJar(path, resource, classes);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Could not scan item component package " + packageName, e);
        }

        return classes;
    }

    private static void findClassesInDirectory(String packageName, File directory, Map<String, Class<?>> classes) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(packageName + "." + file.getName(), file, classes);
                continue;
            }

            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                loadClass(className).ifPresent(clazz -> classes.put(className, clazz));
            }
        }
    }

    private static void findClassesInJar(String path, URL resource, Map<String, Class<?>> classes) throws IOException {
        JarURLConnection connection = (JarURLConnection) resource.openConnection();
        try (JarFile jarFile = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = URLDecoder.decode(entry.getName(), StandardCharsets.UTF_8);
                if (entry.isDirectory() || !name.startsWith(path) || !name.endsWith(".class")) {
                    continue;
                }

                String className = name.substring(0, name.length() - 6).replace('/', '.');
                loadClass(className).ifPresent(clazz -> classes.put(className, clazz));
            }
        }
    }

    private static Optional<Class<?>> loadClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            return Optional.empty();
        }
    }
}
