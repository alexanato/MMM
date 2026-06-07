package at.randorf.item.item_component;

import at.randorf.item.ItemComponent;
import at.randorf.item.ItemComponentContext;

import java.util.Map;

public class GetStat implements ItemComponent {
    private String stat = "";
    public GetStat(Map<String, String> properties) {
        this.stat = properties.get("stat");
    }
    public String returnData(ItemComponentContext ctx){
        return ctx.itemData().stats().getOrDefault(stat, "");
    }
}
