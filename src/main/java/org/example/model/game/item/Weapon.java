package org.example.model.game.item;

import org.example.model.game.Coordinate;

public class Weapon extends Item {
    public Weapon() {
    }

    public Weapon(Coordinate coordinate, int value, ItemType itemType) {
        super(coordinate, value, itemType);
    }
}
