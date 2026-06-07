package at.randorf.item.item_component;

import at.randorf.item.ItemComponentContext;
import at.randorf.item.item_data_type.EntityType;

import java.util.Map;

public class GetNearestEntity extends ObjectComponent{
    public GetNearestEntity(Map properties) {
        super(properties);
    }

    @Override
    protected EntityType getObject(ItemComponentContext ctx) {
        return new EntityType(30,50);
    }
}
