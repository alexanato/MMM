package at.randorf.commands.parser;

import at.randorf.commands.data.CommandActionDefinition;
import at.randorf.commands.data.CommandArgumentDefinition;
import at.randorf.commands.data.CommandDefinition;
import at.randorf.commands.data.CommandNodeDefinition;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CommandParser {

    private CommandParser() {
    }

    public static ParsedCommand parse(CommandDefinition definition) {
        Objects.requireNonNull(definition.name(), "Command name cannot be null");

        List<ParsedCommandSyntax> syntaxes = new ArrayList<>();
        addSubcommands(syntaxes, List.of(), definition.subcommands(), definition.action());
        addArguments(syntaxes, List.of(), definition.arguments(), 0, definition.action());

        return new ParsedCommand(definition.name(), definition.aliases(), definition.action(), syntaxes);
    }

    private static void addSubcommands(
            List<ParsedCommandSyntax> syntaxes,
            List<Argument<?>> path,
            List<CommandNodeDefinition> subcommands,
            CommandActionDefinition parentAction
    ) {
        for (CommandNodeDefinition subcommand : subcommands) {
            List<Argument<?>> nextPath = append(path, ArgumentType.Literal(subcommand.label()));
            CommandActionDefinition action = chooseAction(subcommand.action(), parentAction);

            boolean hasArguments = !subcommand.arguments().isEmpty();
            boolean hasSubcommands = !subcommand.subcommands().isEmpty();

            if (hasArguments) {
                addArguments(syntaxes, nextPath, subcommand.arguments(), 0, action);
            }

            if (hasSubcommands) {
                addSubcommands(syntaxes, nextPath, subcommand.subcommands(), action);
            }

            if (!hasArguments && !hasSubcommands) {
                syntaxes.add(new ParsedCommandSyntax(nextPath, action));
            }
        }
    }

    private static void addArguments(
            List<ParsedCommandSyntax> syntaxes,
            List<Argument<?>> path,
            List<CommandArgumentDefinition> arguments,
            int index,
            CommandActionDefinition parentAction
    ) {
        if (index >= arguments.size()) {
            syntaxes.add(new ParsedCommandSyntax(path, parentAction));
            return;
        }

        CommandArgumentDefinition argumentDefinition = arguments.get(index);
        List<Argument<?>> nextPath = append(path, createArgument(argumentDefinition));
        CommandActionDefinition action = chooseAction(argumentDefinition.action(), parentAction);

        boolean hasNestedArguments = !argumentDefinition.arguments().isEmpty();
        boolean hasSubcommands = !argumentDefinition.subcommands().isEmpty();

        if (hasNestedArguments) {
            addArguments(syntaxes, nextPath, argumentDefinition.arguments(), 0, action);
        }

        if (hasSubcommands) {
            addSubcommands(syntaxes, nextPath, argumentDefinition.subcommands(), action);
        }

        if (!hasNestedArguments && !hasSubcommands) {
            addArguments(syntaxes, nextPath, arguments, index + 1, action);
        }
    }

    private static Argument<?> createArgument(CommandArgumentDefinition definition) {
        String name = Objects.requireNonNull(definition.name(), "Argument name cannot be null");
        String type = Objects.requireNonNull(definition.type(), "Argument type cannot be null").toUpperCase(Locale.ROOT);

        return switch (type) {
            case "WORD" -> ArgumentType.Word(name);
            case "STRING" -> ArgumentType.String(name);
            case "STRING_ARRAY", "TEXT", "GREEDY_STRING" -> ArgumentType.StringArray(name);
            case "INTEGER", "INT" -> ArgumentType.Integer(name);
            case "LONG" -> ArgumentType.Long(name);
            case "DOUBLE" -> ArgumentType.Double(name);
            case "FLOAT" -> ArgumentType.Float(name);
            case "BOOLEAN", "BOOL" -> ArgumentType.Boolean(name);
            case "ENTITY" -> ArgumentType.Entity(name);
            case "ENTITY_TYPE" -> ArgumentType.EntityType(name);
            case "UUID" -> ArgumentType.UUID(name);
            case "ITEM_STACK", "ITEM" -> ArgumentType.ItemStack(name);
            case "BLOCK_STATE", "BLOCK" -> ArgumentType.BlockState(name);
            case "RESOURCE_LOCATION" -> ArgumentType.ResourceLocation(name);
            case "COLOR" -> ArgumentType.Color(name);
            case "TIME" -> ArgumentType.Time(name);
            default -> throw new IllegalArgumentException("Unsupported command argument type: " + definition.type());
        };
    }

    private static CommandActionDefinition chooseAction(CommandActionDefinition action, CommandActionDefinition parentAction) {
        return action != null ? action : parentAction;
    }

    private static List<Argument<?>> append(List<Argument<?>> path, Argument<?> argument) {
        List<Argument<?>> nextPath = new ArrayList<>(path);
        nextPath.add(argument);
        return nextPath;
    }
}
