package org.example.model.game.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.model.game.Coordinate;

import java.util.Random;

class GhostArea {
    private Coordinate point;

    private int length;

    private int width;

    public GhostArea() {
        point = new Coordinate(0, 0);
    }

    public GhostArea(Coordinate point, int length, int width) {
        this.point = new Coordinate(point.getX(), point.getY());
        this.length = length;
        this.width = width;
    }

    public Coordinate getPoint() {
        return point;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void setPoint(Coordinate point) {
        this.point = point;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

public class Ghost extends Enemy {
    private GhostArea area;

    private boolean isVisible = true;

    public Ghost() {
        area = new GhostArea();
    }

    public Ghost(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
        area = new GhostArea(coordinate, 0, 0);
    }

    public Ghost(Ghost ghost) {
        super(ghost);
        this.area = ghost.area;
        this.isVisible = ghost.isVisible;
    }

    @Override
    public void move() {
        Random random = new Random();

        coordinate.setX(area.getPoint().getX() + random.nextInt(area.getWidth()));
        coordinate.setY(area.getPoint().getY() + random.nextInt(area.getLength()));

        isVisible = random.nextInt(10) > 7;
    }

    @Override
    public Enemy clone() {
        return new Ghost(this);
    }

    @Override
    public void stun() {

    }

    @JsonIgnore
    public void setArea(Coordinate point, int length, int width) {
        this.area = new GhostArea(point, length, width);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public GhostArea getArea() {
        return area;
    }

    public void setArea(GhostArea area) {
        this.area = area;
    }

    @Override
    public boolean hit(Actor target) {
        isVisible = true;
        return super.hit(target);
    }
}
