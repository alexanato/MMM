package at.randorf.registries;

import net.minestom.server.item.ItemStack;

import java.util.HashMap;

public class ItemRegistry {
    public static HashMap<String, ItemStack> items = new HashMap<>();
    public static void registerItem(String name,ItemStack itemStack){
        items.put(name,itemStack);
    }
}
