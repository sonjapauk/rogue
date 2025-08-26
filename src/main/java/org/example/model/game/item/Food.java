package org.example.model.game.item;

import org.example.model.game.Coordinate;

public class Food extends Item {

    public Food() {
    }

    public Food(Coordinate coordinate, int value, FoodType foodType) {
        super(coordinate, value, foodType);
    }
}
