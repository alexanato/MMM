package at.randorf.commands.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommandDefinition(
        String name,
        List<String> aliases,
        List<CommandArgumentDefinition> arguments,
        List<CommandNodeDefinition> subcommands,
        CommandActionDefinition action
) {
    public CommandDefinition {
        aliases = aliases == null ? List.of() : List.copyOf(aliases);
        arguments = arguments == null ? List.of() : List.copyOf(arguments);
        subcommands = subcommands == null ? List.of() : List.copyOf(subcommands);
    }
}
