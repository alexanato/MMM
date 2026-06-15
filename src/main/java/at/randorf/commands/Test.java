package at.randorf.commands;


import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class Test extends Command {

    public Test() {
        super("heal");

        // 1. Argument definieren (erwartet einen Spieler/eine Entity)
        var playerArgument = ArgumentType.Entity("ziel");

        // 2. Syntax für /heal <ziel> hinzufügen
        addSyntax((sender, context) -> {
            // Das Argument aus dem Context auslesen
            EntityFinder finder = context.get(playerArgument);
            Player target = finder.findFirstPlayer(sender);
            if (target != null) {
                sender.sendMessage(target.getUsername() + " wurde geheilt!");
            } else {
                sender.sendMessage("Spieler nicht gefunden.");
            }
        }, playerArgument); // <- Wichtig: Das Argument hier registrieren!
    }
}