package at.randorf.commands.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommandNodeDefinition(
        String ref,
        String label,
        List<CommandArgumentDefinition> arguments,
        List<CommandNodeDefinition> subcommands,
        CommandActionDefinition action
) {
    public CommandNodeDefinition {
        arguments = arguments == null ? List.of() : List.copyOf(arguments);
        subcommands = subcommands == null ? List.of() : List.copyOf(subcommands);
    }
}
