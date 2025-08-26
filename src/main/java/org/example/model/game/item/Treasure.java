package org.example.model.game.item;

import org.example.model.game.Coordinate;

public class Treasure extends Item {
    public Treasure() {
    }

    public Treasure(Coordinate coordinate, int value) {
        super(coordinate, value);
        setItemType(TreasureType.TREASURE);
    }

    public void addValue(int value) {
        setValue(getValue() + value);
    }
}
