package at.randorf.item;

import at.randorf.Main;
import at.randorf.json.JsonManager;
import at.randorf.lore.LoreFactory;
import at.randorf.registries.ItemRegistry;
import at.randorf.registries.TagRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemFactory {

    public static ItemStack createItem(CustomItemData data) {
        try {
            List<Component> generatedLore = LoreFactory.createLore(data);
            ItemStack itemStack =ItemStack.builder(Objects.requireNonNull(Material.fromKey(data.material())))
                    .customName(Component.text(data.name(),data.rarity().getColor()))
                    .lore(generatedLore)
                    .hideExtraTooltip()
                    .set(TagRegistry.ITEM_DATA,Main.mapper.writeValueAsString(data))
                    .build();
            ItemRegistry.registerItem(data.name(),itemStack);
            return itemStack;
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    public static void  loadItems(){
        JsonManager.scanFolder("basic_items", (key, stream) -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                CustomItemData item = Main.mapper.readValue(reader, CustomItemData.class);
                ItemFactory.createItem(item);

            } catch (Exception e) {
                System.err.println("Fehler beim Interpretieren der Nachricht: " + key);
                e.printStackTrace();
            }
        });
    }
    public static Optional<CustomItemData> getItemData(ItemStack itemStack)  {
        String itemDataJson = itemStack.getTag(TagRegistry.ITEM_DATA);
        if (itemDataJson == null) return Optional.empty();
        try {
            CustomItemData data = Main.mapper.readValue(itemDataJson, CustomItemData.class);
            if (data == null) return Optional.empty();
            return Optional.of(data);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
}