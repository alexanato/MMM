package at.randorf.item;

import net.minestom.server.entity.Entity;

public interface ItemComponent {

    default void onAttack(Entity attacker, Entity target) {}

    default void onUse(Entity user) {}
}