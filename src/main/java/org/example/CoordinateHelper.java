package org.example;

import org.example.model.game.Coordinate;

public class CoordinateHelper {
    public static String toString(Coordinate c) {
        return c.getX() + "," + c.getY();
    }

    public static Coordinate fromString(String s) {
        String[] parts = s.split(",");
        return new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
