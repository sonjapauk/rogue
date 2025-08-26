package org.example.model.game.item;

import org.example.model.game.Coordinate;

public class Elixir extends Item {
    private int duration;

    Elixir() {
    }

    public Elixir(Coordinate coordinate, int value, ItemType itemType, int duration) {
        super(coordinate, value, itemType);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
