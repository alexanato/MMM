package at.randorf.commands.action;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

public interface CommandAction {
    void execute(CommandSender sender, CommandContext context);

    default String returnData(CommandContext context) {
        return "";
    }
}
