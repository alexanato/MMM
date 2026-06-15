package at.randorf.commands.parser;

import at.randorf.commands.data.CommandActionDefinition;
import net.minestom.server.command.builder.arguments.Argument;

import java.util.List;

public record ParsedCommandSyntax(
        List<Argument<?>> arguments,
        CommandActionDefinition action
) {
    public ParsedCommandSyntax {
        arguments = arguments == null ? List.of() : List.copyOf(arguments);
    }
}
