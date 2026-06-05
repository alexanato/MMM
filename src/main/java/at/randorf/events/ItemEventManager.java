package at.randorf.events;

import at.randorf.item.ComponentData;
import at.randorf.item.ItemFactory;
import at.randorf.registries.ItemComponentRegistry;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;

public class ItemEventManager {
    public static void register(GlobalEventHandler eventHandler) {

        eventHandler.addListener(EntityAttackEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) return;

            // Beim Angreifen nutzen wir standardmäßig die Haupthand
            ItemStack item = player.getItemInHand(PlayerHand.MAIN);

            triggerComponents(player, item, "attack", event.getTarget());
        });

        // --- 2. EVENT: RECHTS-KLICK (BENUTZEN) ---
        eventHandler.addListener(PlayerUseItemEvent.class, event -> {
            Player player = event.getPlayer();

            // HIER DYNAMISCH: Minestom sagt uns genau, welche Hand geklickt hat
            ItemStack item = event.getItemStack();

            triggerComponents(player, item, "use", null);
        });
    }

    private static void triggerComponents(Player player, ItemStack item, String eventType, net.minestom.server.entity.Entity target) {
        ItemFactory.getItemData(item).ifPresent(data -> {
            for (ComponentData compData : data.components()) {

                ItemComponentRegistry.createComponent(compData.type(), compData.properties())
                        .ifPresent(component -> {

                            // Je nachdem, welches Event gefeuert wurde, rufen wir die passende Methode auf
                            if (eventType.equals("attack") && target != null) {
                                component.onAttack(player, target);
                            } else if (eventType.equals("use")) {
                                component.onUse(player);
                            }

                        });
            }
        });
    }
}
