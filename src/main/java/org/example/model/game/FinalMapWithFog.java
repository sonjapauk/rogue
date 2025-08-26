package org.example.model.game;

import org.example.model.game.enemy.*;
import org.example.model.game.item.*;
import org.example.model.game.level.Level;
import org.example.model.game.level.MapCell;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.example.model.game.level.LevelMap.*;

public class FinalMapWithFog {
    private MapCell[][] finalMap;

    public MapCell[][] getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(MapCell[][] finalMap) {
        this.finalMap = finalMap;
    }

    void prepareFinalMap(Level level, Coordinate heroCoordinate, Coordinate exitCoordinate) {
        finalMap = new MapCell[getSegmentSizeY() * getRoomsCountY()][getSegmentSizeX() * getRoomsCountX()];

        for (int i = 0; i < level.getMap().length; i++) {
            for (int j = 0; j < level.getMap()[i].length; j++) {
                finalMap[i][j] = new MapCell(level.getMap()[i][j], false);
            }
        }

        finalMap[heroCoordinate.getY()][heroCoordinate.getX()] = new MapCell(20, true);
        finalMap[exitCoordinate.getY()][exitCoordinate.getX()] = new MapCell(21, false); // false
    }

    void updateFinalMap(Level level, Coordinate heroCoordinate) {
        for (int i = 0; i < level.getMap().length; i++) {
            for (int j = 0; j < level.getMap()[i].length; j++) {
                finalMap[i][j].setValue(level.getMap()[i][j]);
            }
        }

        finalMap[heroCoordinate.getY()][heroCoordinate.getX()].setValue(20);
    }

    void updateVisible(Level level, Coordinate heroCoordinate) {
        int[][] service = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 2}, {0, -2}, {2, 0}, {-2, 0}};
        List<Coordinate> nearArea = Stream.of(service).map(serv -> new Coordinate(serv[0] + heroCoordinate.getX(), serv[1] + heroCoordinate.getY())).toList();

        int roomNumber = level.getCurrentMap().getRoomNumber(heroCoordinate);

        updateItemsVisible(level, level.getItemsOnLevel().values(), roomNumber, nearArea);
        updateEnemiesVisible(level, level.getEnemiesOnLevel().values(), roomNumber, nearArea);

        if (roomNumber != -1) {
            int roomBeginX = level.getCurrentMap().getRooms().get(roomNumber).getX();
            int roomBeginY = level.getCurrentMap().getRooms().get(roomNumber).getY();

            int roomEndX = roomBeginX + level.getCurrentMap().getRooms().get(roomNumber).getWidth();
            int roomEndY = roomBeginY + level.getCurrentMap().getRooms().get(roomNumber).getLength();

            for (int i = roomBeginY; i <= roomEndY; i++) {
                for (int j = roomBeginX; j <= roomEndX; j++) {
                    finalMap[i][j].setVisible(true);
                }
            }
        }

        for (Coordinate dir : nearArea) {
            int newY = dir.getY();
            int newX = dir.getX();

            if (newY >= 0 && newY < finalMap.length && newX >= 0 && newX < finalMap[0].length) {
                finalMap[newY][newX].setVisible(true);
            }
        }

        finalMap[heroCoordinate.getY()][heroCoordinate.getX()].setVisible(true);
        finalMap[heroCoordinate.getY()][heroCoordinate.getX()].setValue(20);
    }

    int convertTypeToInt(ItemType type) {
        return switch (type) {
            case FoodType.APPLE -> 25;
            case FoodType.BREAD -> 26;
            case FoodType.MEAT -> 27;
            case ElixirType.AGILITY_ELIXIR -> 22;
            case ElixirType.MAX_HEALTH_ELIXIR -> 23;
            case ElixirType.STRENGTH_ELIXIR -> 24;
            case ScrollsType.AGILITY_SCROLL -> 28;
            case ScrollsType.STRENGTH_SCROLL -> 29;
            case ScrollsType.MAXIMUM_HEALTH_SCROLL -> 30;
            case WeaponsType.AXE -> 31;
            case WeaponsType.SWORD -> 32;
            case WeaponsType.KNIFE -> 33;
            case WeaponsType.HAMMER -> 34;
            case WeaponsType.STICK, WeaponsType.HANDS -> 35;
            case TreasureType.TREASURE -> 37;
            case null, default -> {
                throw new IllegalStateException("Неизвестный тип предмета: " + type);
            }
        };
    }

    void updateItemsVisible(Level level, Collection<Item> items, int currentRoomNumber, List<Coordinate> nearArea) {
        for (Item item : items) {
            if (currentRoomNumber == level.getCurrentMap().getRoomNumber(item.getCoordinate()) || nearArea.stream().anyMatch(c -> c.equals(item.getCoordinate()))) {
                int itemValue = convertTypeToInt(item.getItemType());

                Coordinate coordinate = item.getCoordinate();
                finalMap[coordinate.getY()][coordinate.getX()].setValue(itemValue);
            }
        }
    }

    void updateEnemiesVisible(Level level, Collection<Enemy> enemies, int currentRoomNumber, List<Coordinate> nearArea) {
        for (Enemy enemy : enemies) {
            if ((currentRoomNumber != -1 && currentRoomNumber == level.getCurrentMap().getRoomNumber(enemy.getCoordinate())) || nearArea.stream().anyMatch(c -> c.equals(enemy.getCoordinate()))) {
                int enemyValue = switch (enemy) {
                    case Zombie ignored3 -> 50;
                    case Ogre ignored2 -> 51;
                    case Vampire ignored1 -> 52;
                    case Ghost ghost -> ghost.isVisible() ? 53 : 9;
                    case SnakeWizard ignored -> 54;
                    case Mimic mimic -> mimic.isVisible() ? 55 : convertTypeToInt(mimic.getItemSkin());
                    case null, default -> {
                        throw new IllegalStateException("Unknown enemies type: " + enemy.getClass());
                    }
                };

                Coordinate coordinate = enemy.getCoordinate();
                finalMap[coordinate.getY()][coordinate.getX()].setValue(enemyValue);
            }
        }
    }
}

