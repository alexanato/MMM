package at.randorf.commands.parser;

import at.randorf.Main;
import at.randorf.commands.data.CommandArgumentDefinition;
import at.randorf.commands.data.CommandDefinition;
import at.randorf.commands.data.CommandNodeDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CommandDefinitionResolver {
    private static final String COMMAND_FOLDER = "commands";

    private CommandDefinitionResolver() {
    }

    public static CommandDefinition resolve(CommandDefinition definition) {
        return new CommandDefinition(
                definition.name(),
                definition.aliases(),
                resolveArguments(definition.arguments(), new HashSet<>()),
                resolveSubcommands(definition.subcommands(), new HashSet<>()),
                definition.action()
        );
    }

    private static List<CommandArgumentDefinition> resolveArguments(
            List<CommandArgumentDefinition> arguments,
            Set<String> resolvingRefs
    ) {
        return arguments.stream()
                .map(argument -> resolveArgument(argument, resolvingRefs))
                .toList();
    }

    private static CommandArgumentDefinition resolveArgument(
            CommandArgumentDefinition argument,
            Set<String> resolvingRefs
    ) {
        if (argument.ref() != null && !argument.ref().isBlank()) {
            String ref = argument.ref();
            if (!resolvingRefs.add(ref)) {
                throw new IllegalStateException("Cyclic command argument reference: " + ref);
            }
            CommandArgumentDefinition resolved = resolveArgument(loadReference(ref, CommandArgumentDefinition.class), resolvingRefs);
            resolvingRefs.remove(ref);
            return resolved;
        }

        return new CommandArgumentDefinition(
                argument.ref(),
                argument.name(),
                argument.type(),
                resolveArguments(argument.arguments(), resolvingRefs),
                resolveSubcommands(argument.subcommands(), resolvingRefs),
                argument.action()
        );
    }

    private static List<CommandNodeDefinition> resolveSubcommands(
            List<CommandNodeDefinition> subcommands,
            Set<String> resolvingRefs
    ) {
        return subcommands.stream()
                .map(subcommand -> resolveSubcommand(subcommand, resolvingRefs))
                .toList();
    }

    private static CommandNodeDefinition resolveSubcommand(
            CommandNodeDefinition subcommand,
            Set<String> resolvingRefs
    ) {
        if (subcommand.ref() != null && !subcommand.ref().isBlank()) {
            String ref = subcommand.ref();
            if (!resolvingRefs.add(ref)) {
                throw new IllegalStateException("Cyclic command subcommand reference: " + ref);
            }
            CommandNodeDefinition resolved = resolveSubcommand(loadReference(ref, CommandNodeDefinition.class), resolvingRefs);
            resolvingRefs.remove(ref);
            return resolved;
        }

        return new CommandNodeDefinition(
                subcommand.ref(),
                subcommand.label(),
                resolveArguments(subcommand.arguments(), resolvingRefs),
                resolveSubcommands(subcommand.subcommands(), resolvingRefs),
                subcommand.action()
        );
    }

    private static <T> T loadReference(String ref, Class<T> type) {
        String normalizedRef = ref.endsWith(".json") ? ref : ref + ".json";
        String resourcePath = COMMAND_FOLDER + "/" + normalizedRef;

        try (InputStream inputStream = CommandDefinitionResolver.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Command reference not found: " + resourcePath);
            }

            return Main.mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read command reference: " + resourcePath, e);
        }
    }
}
