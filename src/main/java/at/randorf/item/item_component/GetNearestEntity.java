package at.randorf.item.item_component;

import at.randorf.item.ItemComponent;
import at.randorf.item.ItemComponentContext;
import at.randorf.item.ItemComponentType;
import at.randorf.item.item_data_type.EntityType;
import com.google.auto.service.AutoService;

import java.util.Map;

@ItemComponentType("GET_NEAREST_ENTITY")
public class GetNearestEntity extends ObjectComponent{
    public GetNearestEntity(Map properties) {
        super(properties);
    }

    @Override
    protected EntityType getObject(ItemComponentContext ctx) {
        return new EntityType(30,50);
    }
}
