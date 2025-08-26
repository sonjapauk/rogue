package org.example.model.game.enemy;

import org.example.model.Direction;
import org.example.model.game.Coordinate;

import java.util.Random;

public class SnakeWizard extends Enemy {
    private Direction[] currentDirections;

    public SnakeWizard() {
        currentDirections = new Direction[2];
    }

    public SnakeWizard(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
        currentDirections = new Direction[2];
    }

    public SnakeWizard(SnakeWizard snakeWizard) {
        super(snakeWizard);
        this.currentDirections = snakeWizard.currentDirections;
    }

    @Override
    public void move() {
        Direction[] newDirections;

        do {
            newDirections = generateDirections();
        } while ((newDirections[0] == currentDirections[0] && newDirections[1] == currentDirections[1]) ||
                (newDirections[0] == currentDirections[1] && newDirections[1] == currentDirections[0]));

        for (Direction direction : newDirections) {
            switch (direction) {
                case UP -> coordinate.setY(coordinate.getY() - 1);
                case DOWN -> coordinate.setY(coordinate.getY() + 1);
                case LEFT -> coordinate.setX(coordinate.getX() - 1);
                case RIGHT -> coordinate.setX(coordinate.getX() + 1);
            }
        }

        currentDirections = newDirections;
    }

    private Direction[] generateDirections() {
        Random random = new Random();

        Direction[] directions = new Direction[2];

        directions[0] = Direction.values()[random.nextInt(Direction.values().length)];

        do {
            directions[1] = Direction.values()[random.nextInt(Direction.values().length)];
        } while (directions[0] == directions[1]);

        return directions;
    }

    @Override
    public Enemy clone() {
        return new SnakeWizard(this);
    }

    @Override
    public boolean hit(Actor target) {
        if (super.hit(target)) {
            Random random = new Random();

            target.isStunned = random.nextInt(10) > 1;

            return true;
        }

        return false;
    }

    @Override
    public void stun() {

    }

    public Direction[] getCurrentDirections() {
        return currentDirections;
    }

    public void setCurrentDirections(Direction[] currentDirections) {
        this.currentDirections = currentDirections;
    }
}
