package org.example.model.game.level;

import org.example.model.game.Coordinate;
import org.example.model.game.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

class ItemDrop {
    Supplier<Item> supplier;
    int weight;

    ItemDrop(Supplier<Item> supplier, int weight) {
        this.supplier = supplier;
        this.weight = weight;
    }

    Item createItem() {
        return supplier.get();
    }
}

public class ItemsRandomizer {
    public static List<Item> getItemsListForMapping(int level, int difficultyIndex) {
        List<ItemDrop> itemDrops = List.of(
                new ItemDrop(() -> new Food(new Coordinate(0, 0), 1 + level, FoodType.APPLE), 15),
                new ItemDrop(() -> new Food(new Coordinate(0, 0), 3 + level, FoodType.BREAD), 10),
                new ItemDrop(() -> new Food(new Coordinate(0, 0), 5 + level, FoodType.MEAT), 5),
                new ItemDrop(() -> new Elixir(new Coordinate(0, 0), 5 + level, ElixirType.MAX_HEALTH_ELIXIR, 30), 7),
                new ItemDrop(() -> new Elixir(new Coordinate(0, 0), 5 + level, ElixirType.AGILITY_ELIXIR, 30), 7),
                new ItemDrop(() -> new Elixir(new Coordinate(0, 0), 5 + level, ElixirType.STRENGTH_ELIXIR, 30), 7),
                new ItemDrop(() -> new Scroll(new Coordinate(0, 0), 3 + level / 3, ScrollsType.MAXIMUM_HEALTH_SCROLL), 3),
                new ItemDrop(() -> new Scroll(new Coordinate(0, 0), 3 + level / 3, ScrollsType.AGILITY_SCROLL), 3),
                new ItemDrop(() -> new Scroll(new Coordinate(0, 0), 3 + level / 3, ScrollsType.STRENGTH_SCROLL), 3),
                new ItemDrop(() -> new Weapon(new Coordinate(0, 0), 1 + level, WeaponsType.STICK), 10),
                new ItemDrop(() -> new Weapon(new Coordinate(0, 0), 3 + level, WeaponsType.KNIFE), 4),
                new ItemDrop(() -> new Weapon(new Coordinate(0, 0), 5 + level, WeaponsType.HAMMER), 2),
                new ItemDrop(() -> new Weapon(new Coordinate(0, 0), 10 + level, WeaponsType.AXE), 1),
                new ItemDrop(() -> new Treasure(new Coordinate(0, 0), 10 * level), 30)
        );

        List<Item> itemsForMapping = new ArrayList<>();

        int itemsCount = 20 - level / 3 + difficultyIndex;

        for (int i = 0; i < itemsCount; i++) {
            itemsForMapping.add(getRandomItem(itemDrops));
        }

        return itemsForMapping;
    }

    static Item getRandomItem(List<ItemDrop> itemDrops) {
        int totalWeight = itemDrops.stream()
                .mapToInt(drop -> drop.weight)
                .sum();

        int rand = new Random().nextInt(totalWeight) + 1;
        int current = 0;

        for (ItemDrop drop : itemDrops) {
            current += drop.weight;

            if (rand <= current) {
                return drop.createItem();
            }
        }

        return null;
    }
}

