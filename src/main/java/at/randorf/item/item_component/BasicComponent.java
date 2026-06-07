package at.randorf.item.item_component;

import at.randorf.item.ItemComponent;

import java.util.Map;

public abstract class BasicComponent implements ItemComponent {
    public Map<String, String> properties;
    public BasicComponent(Map<String, String> properties){
        this.properties = properties;
    }
}
