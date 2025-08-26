package org.example.model.game.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.model.game.Coordinate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon"),
        @JsonSubTypes.Type(value = Food.class, name = "food"),
        @JsonSubTypes.Type(value = Scroll.class, name = "scroll"),
        @JsonSubTypes.Type(value = Elixir.class, name = "elixir"),
        @JsonSubTypes.Type(value = Treasure.class, name = "treasure"),
})

public class Item {
    private Coordinate coordinate;
    private int value;
    private ItemType itemType;
    private Object JsonTypeInfo;

    public Item(Coordinate coordinate, int value, ItemType itemType) {
        this.coordinate = coordinate;
        this.value = value;
        this.itemType = itemType;
    }

    public Item(Coordinate coordinate, int value) {
        this.coordinate = coordinate;
        this.value = value;
    }

    public Item() {
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
