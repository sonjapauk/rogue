package org.example.model.game.enemy;

import org.example.model.Direction;
import org.example.model.game.Coordinate;

import java.util.Random;

public class Ogre extends Enemy {

    public Ogre() {

    }

    public Ogre(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
    }

    public Ogre(Ogre ogre) {
        super(ogre);
    }

    @Override
    public void move() {
        Random random = new Random();

        Direction direction = Direction.values()[random.nextInt(Direction.values().length)];

        switch (direction) {
            case UP -> coordinate.setY(coordinate.getY() - 2);
            case DOWN -> coordinate.setY(coordinate.getY() + 2);
            case LEFT -> coordinate.setX(coordinate.getX() - 2);
            case RIGHT -> coordinate.setX(coordinate.getX() + 2);
        }
    }

    @Override
    public Enemy clone() {
        return new Ogre(this);
    }

    @Override
    public boolean hit(Actor target) {
        isStunned = true;
        return super.hit(target);
    }

    @Override
    public void stun() {
        isStunned = false;
    }
}
