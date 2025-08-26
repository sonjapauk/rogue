package org.example.model.game.enemy;

import org.example.model.Direction;
import org.example.model.game.Coordinate;

import java.util.Random;

public class Vampire extends Enemy {
    public Vampire() {

    }

    public Vampire(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
    }

    public Vampire(Vampire vampire) {
        super(vampire);
    }

    @Override
    public void move() {
        Random random = new Random();

        Direction direction = Direction.values()[random.nextInt(Direction.values().length)];

        switch (direction) {
            case UP -> coordinate.setY(coordinate.getY() - 1);
            case DOWN -> coordinate.setY(coordinate.getY() + 1);
            case LEFT -> coordinate.setX(coordinate.getX() - 1);
            case RIGHT -> coordinate.setX(coordinate.getX() + 1);
        }
    }

    @Override
    public Enemy clone() {
        return new Vampire(this);
    }

    @Override
    public void stun() {

    }

    @Override
    public boolean hit(Actor target) {
        if (super.hit(target)) {
            target.maxHealth--;
            return true;
        }

        return false;
    }
}
