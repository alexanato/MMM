package at.randorf.commands;

import at.randorf.commands.action.CommandAction;
import at.randorf.commands.action.CommandActionRegistry;
import at.randorf.commands.data.CommandActionDefinition;
import at.randorf.commands.parser.ParsedCommand;
import at.randorf.commands.parser.ParsedCommandSyntax;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.Argument;

import java.util.Objects;

public final class CommandFactory {

    private CommandFactory() {
    }

    public static Command create(ParsedCommand parsedCommand) {
        Objects.requireNonNull(parsedCommand.name(), "Command name cannot be null");

        Command command = new Command(parsedCommand.name(), parsedCommand.aliases().toArray(String[]::new));
        command.setDefaultExecutor(createExecutor(parsedCommand.defaultAction()));

        for (ParsedCommandSyntax syntax : parsedCommand.syntaxes()) {
            command.addSyntax(createExecutor(syntax.action()), syntax.arguments().toArray(Argument[]::new));
        }

        return command;
    }

    private static CommandExecutor createExecutor(CommandActionDefinition action) {
        return (sender, context) -> {
            CommandAction commandAction = CommandActionRegistry.createAction(action, context);
            commandAction.execute(sender, context);
        };
    }

}
