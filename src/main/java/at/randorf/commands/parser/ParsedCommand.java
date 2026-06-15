package at.randorf.commands.parser;

import at.randorf.commands.data.CommandActionDefinition;

import java.util.List;

public record ParsedCommand(
        String name,
        List<String> aliases,
        CommandActionDefinition defaultAction,
        List<ParsedCommandSyntax> syntaxes
) {
    public ParsedCommand {
        aliases = aliases == null ? List.of() : List.copyOf(aliases);
        syntaxes = syntaxes == null ? List.of() : List.copyOf(syntaxes);
    }
}
