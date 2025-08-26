package org.example.model.game.enemy;

import org.example.model.game.Coordinate;

public class Zombie extends Enemy {
    public Zombie() {

    }

    public Zombie(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
    }

    public Zombie(Zombie zombie) {
        super(zombie);
    }

    @Override
    public void move() {
    }

    @Override
    public Enemy clone() {
        return new Zombie(this);
    }

    @Override
    public void stun() {

    }
}
