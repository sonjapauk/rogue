package org.example;

public final class MapCellValues {
    private MapCellValues() {}

    public static final int EMPTY = 0;
    public static final int VERTICAL_WALL = 1;
    public static final int HORIZONTAL_WALL = 2;
    public static final int TOP_LEFT_CORNER = 3;
    public static final int BOTTOM_LEFT_CORNER = 4;
    public static final int BOTTOM_RIGHT_CORNER = 5;
    public static final int TOP_RIGHT_CORNER = 6;
    public static final int FLOOR = 9;
    public static final int GRASS = 10;
    public static final int DOOR_HORIZONTAL = 11;
    public static final int DOOR_VERTICAL = 12;

    public static final int PLAYER = 20;
    public static final int NPC = 21;

    public static final int AGILITY_ELIXIR = 22;
    public static final int MAX_HP_ELIXIR = 23;
    public static final int STRENGTH_ELIXIR = 24;

    public static final int APPLE = 25;
    public static final int BREAD = 26;
    public static final int MEAT = 27;

    public static final int AGILITY_SCROLL = 28;
    public static final int STRENGTH_SCROLL = 29;
    public static final int MAX_HP_SCROLL = 30;

    public static final int AXE = 31;
    public static final int SWORD = 32;
    public static final int KNIFE = 33;
    public static final int HAMMER = 34;
    public static final int STICK = 35;
    public static final int HANDS = 36;

    public static final int GOLD = 37;

    public static final int ZOMBIE = 50;
    public static final int ORC = 51;
    public static final int VAMPIRE = 52;
    public static final int GHOST = 53;
    public static final int SKELETON = 54;
    public static final int MIMIC = 55;
}
