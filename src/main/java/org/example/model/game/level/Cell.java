package org.example.model.game.level;

import org.example.model.game.Coordinate;

class Cell {
    Coordinate coordinate;
    int g;
    int h;
    Cell parent;

    Cell(Coordinate coordinate, int g, int h, Cell parent) {
        this.coordinate = coordinate;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    int getF() {
        return h + g;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        return coordinate.equals(((Cell) obj).coordinate);
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode();
    }
}
