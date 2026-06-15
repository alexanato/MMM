package at.randorf.commands.action;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

public class NoopCommandAction implements CommandAction {

    @Override
    public void execute(CommandSender sender, CommandContext context) {
        sender.sendMessage("Command ausgefuhrt.");
    }
}
