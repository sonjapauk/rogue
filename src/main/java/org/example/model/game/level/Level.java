package org.example.model.game.level;

import org.example.model.game.Coordinate;
import org.example.model.game.enemy.Enemy;
import org.example.model.game.item.*;

import java.util.*;

public class Level {
    private int level = 1;
    private Map<Coordinate, Item> itemsOnLevel = new HashMap<>();

    private Map<Coordinate, Enemy> enemiesOnLevel = new HashMap<>();
    private LevelMap currentMap;

    private int startRoom = 0;

    public Level() {
        currentMap = new LevelMap();
    }

    public Level(int level) {
        this.level = level;

        currentMap = new LevelMap();
    }

    public int getStartRoom() {
        return startRoom;
    }

    public void setStartRoom(int startRoom) {
        this.startRoom = startRoom;
    }

    public LevelMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(LevelMap currentMap) {
        this.currentMap = currentMap;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<Coordinate, Enemy> getEnemiesOnLevel() {
        return enemiesOnLevel;
    }

    public void setEnemiesOnLevel(Map<Coordinate, Enemy> enemiesOnLevel) {
        this.enemiesOnLevel = enemiesOnLevel;
    }

    public boolean isItem(Coordinate coordinate) {
        return itemsOnLevel.containsKey(coordinate);
    }

    public void setMap(int[][] map) {
        currentMap.setMap(map);
    }

    public int[][] getMap() {
        return currentMap.getMap();
    }

    public Map<Coordinate, Item> getItemsOnLevel() {
        return itemsOnLevel;
    }

    public void setItemsOnLevel(Map<Coordinate, Item> itemsOnLevel) {
        this.itemsOnLevel = itemsOnLevel;
    }

    public Coordinate getHeroCoordinateOnMap() {
        Random random = new Random();
        int roomNumber;

        do {
            roomNumber = random.nextInt(LevelMap.getRoomsCountX() * LevelMap.getRoomsCountX() - 1);
        } while (roomNumber == currentMap.getPaths().find(0));

        Room neededRoom = currentMap.getRooms().get(roomNumber);

        int y = neededRoom.getY();
        int x = neededRoom.getX();

        startRoom = roomNumber;

        return new Coordinate(1 + random.nextInt(neededRoom.getWidth() - 1) + x,
                1 + random.nextInt(neededRoom.getLength() - 1) + y);
    }

    public Coordinate getItemCoordinate() {
        Random random = new Random();
        Coordinate result;

        do {
            int roomNumber;

            do {
                roomNumber = random.nextInt(LevelMap.getRoomsCountX() * LevelMap.getRoomsCountY() - 1);
            } while (roomNumber == startRoom);

            Room neededRoom = currentMap.getRooms().get(roomNumber);

            int y = neededRoom.getY();
            int x = neededRoom.getX();

            result = new Coordinate(1 + random.nextInt(neededRoom.getWidth() - 1) + x,
                    1 + random.nextInt(neededRoom.getLength() - 1) + y);
        } while (itemsOnLevel.containsKey(result));

        return result;
    }

    public Coordinate getEnemiesCoordinate() {
        Random random = new Random();
        Coordinate result;

        do {
            int roomNumber;

            do {
                roomNumber = random.nextInt(LevelMap.getRoomsCountX() * LevelMap.getRoomsCountY() - 1);
            } while (roomNumber == startRoom);

            Room neededRoom = currentMap.getRooms().get(roomNumber);

            int y = neededRoom.getY();
            int x = neededRoom.getX();

            result = new Coordinate(1 + random.nextInt(neededRoom.getWidth() - 1) + x,
                    1 + random.nextInt(neededRoom.getLength() - 1) + y);
        } while (enemiesOnLevel.containsKey(result));

        return result;
    }

    public Coordinate getExitCoordinateOnMap() {
        Random random = new Random();
        int exitRoomNumber;

        do {
            exitRoomNumber = random.nextInt(9);
        } while (exitRoomNumber == startRoom);

        Room neededRoom = currentMap.getRooms().get(exitRoomNumber);

        int y = neededRoom.getY();
        int x = neededRoom.getX();

        return new Coordinate(1 + random.nextInt(neededRoom.getWidth() - 1) + x,
                1 + random.nextInt(neededRoom.getLength() - 1) + y);
    }

    public boolean isCollide(Coordinate point) {
        return currentMap.isCollide(point);
    }

    public void killEnemy(Enemy enemy) {
        enemiesOnLevel.remove(enemy.getCoordinate(), enemy);
    }

    public boolean isEnemy(Coordinate coordinate) {
        return enemiesOnLevel.containsKey(coordinate);
    }

    public boolean isNear(Coordinate point1, Coordinate point2) {
        return currentMap.inSameRoom(point1, point2) ||
                (currentMap.notInRoom(point1) && currentMap.notInRoom(point2) && currentMap.inSameCorridor(point1, point2));
    }

    public boolean outOfMap(Coordinate point) {
        return currentMap.outOfMap(point);
    }

    public List<Coordinate> connectPoints(Coordinate start, Coordinate target) {
        return currentMap.connectPoints(start, target);
    }
}
