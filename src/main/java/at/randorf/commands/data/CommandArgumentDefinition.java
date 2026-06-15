package at.randorf.commands.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommandArgumentDefinition(
        String ref,
        String name,
        String type,
        List<CommandArgumentDefinition> arguments,
        List<CommandNodeDefinition> subcommands,
        CommandActionDefinition action
) {
    public CommandArgumentDefinition {
        arguments = arguments == null ? List.of() : List.copyOf(arguments);
        subcommands = subcommands == null ? List.of() : List.copyOf(subcommands);
    }
}
