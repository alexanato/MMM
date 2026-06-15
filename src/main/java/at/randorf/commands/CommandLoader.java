package at.randorf.commands;

import at.randorf.Main;
import at.randorf.commands.data.CommandDefinition;
import at.randorf.commands.parser.CommandDefinitionResolver;
import at.randorf.commands.parser.CommandParser;
import at.randorf.commands.parser.ParsedCommand;
import at.randorf.json.JsonManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public final class CommandLoader {
    private static final String COMMAND_FOLDER = "commands";

    public static void loadCommands() {
        JsonManager.scanFolder(COMMAND_FOLDER, (key, inputStream) -> {
            try {
                CommandDefinition definition = Main.mapper.readValue(inputStream, CommandDefinition.class);
                definition = CommandDefinitionResolver.resolve(definition);
                ParsedCommand parsedCommand = CommandParser.parse(definition);
                Command command = CommandFactory.create(parsedCommand);
                MinecraftServer.getCommandManager().register(command);
                System.out.println("Command geladen: /" + command.getName());
            } catch (Exception e) {
                throw new IllegalStateException("Could not load command json: " + key, e);
            }
        });
    }
}
