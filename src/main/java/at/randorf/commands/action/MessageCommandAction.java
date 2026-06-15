package at.randorf.commands.action;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

import java.util.Map;

@CommandActionType("MESSAGE")
public class MessageCommandAction extends StringCommandAction {
    private final String message;

    public MessageCommandAction(Map<String, String> properties) {
        super(properties);
        this.message = property("message", "Command ausgefuhrt.");
    }

    @Override
    public void execute(CommandSender sender, CommandContext context) {
        sender.sendMessage(returnData(context));
    }

    @Override
    public String returnData(CommandContext context) {
        return format(message, context);
    }
}
