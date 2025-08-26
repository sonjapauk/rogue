package org.example.model.game.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeaponsType.class, name = "weapon"),
        @JsonSubTypes.Type(value = ScrollsType.class, name = "scroll"),
        @JsonSubTypes.Type(value = FoodType.class, name = "food"),
        @JsonSubTypes.Type(value = ElixirType.class, name = "elixir"),
        @JsonSubTypes.Type(value = TreasureType.class, name = "treasure")
})
public interface ItemType {
}
