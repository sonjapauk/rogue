package org.example.model.game.enemy;

import org.example.model.game.Coordinate;

import java.util.Random;

public abstract class Actor {
    protected Coordinate coordinate = new Coordinate(4, 30);
    protected int currentHealth = 50;

    protected int maxHealth = 50;
    protected int strength = 5;
    protected int agility = 5;

    protected boolean foundTarget;

    protected boolean isStunned = false;

    protected int hitsCount = 0;

    public Actor() {
    }

    public Actor(Coordinate coordinate, int health, int strength, int agility) {
        this.coordinate = coordinate;
        this.currentHealth = health;
        this.strength = strength;
        this.agility = agility;
        this.foundTarget = false;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getAgility() {
        return agility;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getStrength() {
        return strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean hit(Actor target) {
        hitsCount++;

        if (target instanceof Vampire && hitsCount == 1) {
            return false;
        }

        Random random = new Random();

        if (random.nextInt(getAgility()) > random.nextInt(target.getAgility())) {
            target.setCurrentHealth(target.getCurrentHealth() - getStrength());
            return true;
        }

        return false;
    }

    public boolean foundTarget() {
        return foundTarget;
    }

    public void setFoundTarget(boolean foundTarget) {
        this.foundTarget = foundTarget;

        if (!foundTarget) {
            hitsCount = 0;
        }
    }

    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }

    public boolean isStunned() {
        return isStunned;
    }

    public abstract void stun();

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHitsCount() {
        return hitsCount;
    }

    public void setHitsCount(int hitsCount) {
        this.hitsCount = hitsCount;
    }
}
