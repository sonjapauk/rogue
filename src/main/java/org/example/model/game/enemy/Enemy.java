package org.example.model.game.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.model.game.Coordinate;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Zombie.class, name = "zombie"),
        @JsonSubTypes.Type(value = Ogre.class, name = "ogre"),
        @JsonSubTypes.Type(value = Vampire.class, name = "vampire"),
        @JsonSubTypes.Type(value = Ghost.class, name = "ghost"),
        @JsonSubTypes.Type(value = SnakeWizard.class, name = "snakeWizard"),
        @JsonSubTypes.Type(value = Mimic.class, name = "mimic"),
})

public abstract class Enemy extends Actor {
    private int hostility = 2;

    private boolean seeHero = false;

    public Enemy() {
    }

    private List<Coordinate> chasePath;

    private int value;


    public Enemy(Coordinate coordinate, int health, int strength, int agility, int hostility, int value) {
        super(coordinate, health, strength, agility);
        this.hostility = hostility;
        this.chasePath = new ArrayList<>();
        this.value = value;
        this.maxHealth = health;
    }

    public Enemy(Enemy enemy) {
        super(new Coordinate(enemy.coordinate.getX(), enemy.coordinate.getY()), enemy.currentHealth, enemy.strength, enemy.agility);
        this.hostility = enemy.hostility;
        chasePath = new ArrayList<>(enemy.chasePath);
        this.value = enemy.value;
        this.seeHero = enemy.seeHero;
        this.foundTarget = enemy.foundTarget;
        this.hitsCount = enemy.hitsCount;
        this.maxHealth = enemy.maxHealth;
    }

    public abstract void move();

    public boolean needToChase(Coordinate target) {
        int dx = target.getX() - coordinate.getX();
        int dy = target.getY() - coordinate.getY();

        double distanceToTarget = dx * dx + dy * dy;

        return distanceToTarget <= hostility * hostility;
    }

    public void chaseSmart() {
        if (!chasePath.isEmpty()) {
            if (chasePath.size() == 1) {
                foundTarget = true;
            } else {
                foundTarget = false;
                hitsCount = 0;
                coordinate = chasePath.getFirst();
            }
        }
    }

    public int getHostility() {
        return hostility;
    }

    public void setHostility(int hostility) {
        this.hostility = hostility;
    }

    public void setSeeHero(boolean seeHero) {
        this.seeHero = seeHero;
    }

    public boolean seeHero() {
        return seeHero;
    }

    public List<Coordinate> getChasePath() {
        return chasePath;
    }

    public void setChasePath(List<Coordinate> chasePath) {
        this.chasePath = chasePath;
    }

    @JsonIgnore
    public int getPathSize() {
        if (!chasePath.isEmpty()) {
            return chasePath.size();
        }

        return 0;
    }

    public int getValue() {
        return value;
    }

    public abstract Enemy clone();

    public abstract void stun();
}

