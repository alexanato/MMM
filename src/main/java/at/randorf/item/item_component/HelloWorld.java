package at.randorf.item.item_component;

import at.randorf.item.ItemComponent;
import at.randorf.item.ItemComponentType;
import com.google.auto.service.AutoService;
import net.minestom.server.entity.Entity;

import java.util.Map;
@ItemComponentType("HELLO_WORLD")
public class HelloWorld implements ItemComponent {
    private String message = "Hello World!";
    public HelloWorld(Map<String, String> properties) {
        this.message = properties.get("message");
    }
    @Override
    public void onUse(Entity user) {
        user.getInstance().getPlayers().forEach(player -> player.sendMessage(message));
    }
}
