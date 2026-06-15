package at.randorf.commands.action;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;

import java.util.Map;

@CommandActionType("GET_ARGUMENT")
public class GetArgumentCommandAction extends StringCommandAction {
    private final String name;
    private final String fallback;

    public GetArgumentCommandAction(Map<String, String> properties) {
        super(properties);
        this.name = property("name", "");
        this.fallback = property("fallback", "");
    }

    @Override
    public void execute(CommandSender sender, CommandContext context) {
        sender.sendMessage(returnData(context));
    }

    @Override
    public String returnData(CommandContext context) {
        Object value = context.getMap().get(name);
        return value == null ? fallback : String.valueOf(value);
    }
}
