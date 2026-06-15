package at.randorf.commands.action;

import at.randorf.commands.data.CommandActionDefinition;
import at.randorf.Main;
import net.minestom.server.command.builder.CommandContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.function.Function;

public final class CommandActionRegistry {
    private static final String ACTION_PACKAGE = "at.randorf.commands.action";
    private static final CommandAction DEFAULT_ACTION = new NoopCommandAction();
    private static final Map<String, Function<Map<String, String>, CommandAction>> ACTIONS = new HashMap<>();

    static {
        registerAnnotatedActions(ACTION_PACKAGE);
    }

    private CommandActionRegistry() {
    }

    public static void register(String type, Function<Map<String, String>, CommandAction> factory) {
        ACTIONS.put(type.toUpperCase(Locale.ROOT), factory);
    }

    public static CommandAction createAction(CommandActionDefinition definition, CommandContext context) {
        if (definition == null || definition.type() == null || definition.type().isBlank()) {
            return DEFAULT_ACTION;
        }

        Function<Map<String, String>, CommandAction> factory = ACTIONS.get(definition.type().toUpperCase(Locale.ROOT));
        if (factory == null) {
            return DEFAULT_ACTION;
        }

        return factory.apply(getStringProperties(definition, context));
    }

    private static void registerAnnotatedActions(String packageName) {
        for (Class<?> clazz : findClasses(packageName).values()) {
            if (!CommandAction.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            CommandActionType annotation = clazz.getAnnotation(CommandActionType.class);
            if (annotation == null) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends CommandAction> actionClass = (Class<? extends CommandAction>) clazz;
            register(annotation.value(), createFactory(actionClass));
        }
    }

    private static Function<Map<String, String>, CommandAction> createFactory(Class<? extends CommandAction> actionClass) {
        try {
            Constructor<? extends CommandAction> constructor = actionClass.getConstructor(Map.class);
            return properties -> {
                try {
                    return constructor.newInstance(properties);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not create command action " + actionClass.getName(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Command action " + actionClass.getName() + " needs a public constructor with Map<String, String>",
                    e
            );
        }
    }

    private static Map<String, String> getStringProperties(CommandActionDefinition definition, CommandContext context) {
        Map<String, String> properties = new HashMap<>();
        definition.properties().forEach((key, value) -> {
            if (value instanceof Map<?, ?> map) {
                CommandActionDefinition nestedDefinition = Main.mapper.convertValue(map, CommandActionDefinition.class);
                CommandAction nestedAction = createAction(nestedDefinition, context);
                properties.put(key, nestedAction.returnData(context));
            } else {
                properties.put(key, String.valueOf(value));
            }
        });
        return properties;
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
            throw new IllegalStateException("Could not scan command action package " + packageName, e);
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
