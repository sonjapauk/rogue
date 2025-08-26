package org.example.model.game.level;

import org.example.model.Direction;

import org.example.model.game.Coordinate;

import java.util.*;

public class LevelMap {
    private static final int ROOMS_COUNT_X = 3;
    private static final int ROOMS_COUNT_Y = 3;
    private static final int SEGMENT_SIZE_X = 13;
    private static final int SEGMENT_SIZE_Y = 13;
    private int[][] map = new int[SEGMENT_SIZE_Y * ROOMS_COUNT_Y][SEGMENT_SIZE_X * ROOMS_COUNT_X];
    private List<Room> rooms;

    private UnionFind paths;

    public LevelMap() {
        do {
            generateRooms();
            generateMap();
        } while (!isCorrectMap());
    }

    public static int getRoomsCountX() {
        return ROOMS_COUNT_X;
    }

    public static int getRoomsCountY() {
        return ROOMS_COUNT_Y;
    }

    public static int getSegmentSizeX() {
        return SEGMENT_SIZE_X;
    }

    public static int getSegmentSizeY() {
        return SEGMENT_SIZE_Y;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public UnionFind getPaths() {
        return paths;
    }

    public void setPaths(UnionFind paths) {
        this.paths = paths;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }


    public void generateRooms() {
        rooms = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < ROOMS_COUNT_Y * ROOMS_COUNT_X; i++) {
            int width = 3 + random.nextInt(SEGMENT_SIZE_X - 1 - 3); // 5 - 12
            int length = 3 + random.nextInt(SEGMENT_SIZE_Y - 1 - 3); // 1 - 19

            int x = SEGMENT_SIZE_X * (i % ROOMS_COUNT_X) +
                    random.nextInt(SEGMENT_SIZE_X * (i % ROOMS_COUNT_X + 1) - SEGMENT_SIZE_X * (i % ROOMS_COUNT_X) - width); // 0 - l-
            int y = SEGMENT_SIZE_Y * (i / ROOMS_COUNT_X) + random.nextInt(SEGMENT_SIZE_Y * (i / ROOMS_COUNT_X + 1) - SEGMENT_SIZE_Y * (i / ROOMS_COUNT_X) - length);

            rooms.add(new Room(x, y, length, width, i));
        }
    }

    private boolean isCorrectRoom(Room room) {
        int x = room.getX();
        int y = room.getY();
        int length = room.getLength();
        int width = room.getWidth();

        boolean foundExit = false;

        int j = y;
        int k = x;

        while (k < x + width) {
            if (map[j][++k] != 1) {
                if (foundExit) {
                    return false;
                }

                foundExit = true;
            } else {
                foundExit = false;
            }
        }

        if (map[j][k] != 4) {
            return false;
        }

        while (j < y + length) {
            if (map[++j][k] != 2) {
                if (foundExit) {
                    return false;
                }

                foundExit = true;
            } else {
                foundExit = false;
            }
        }


        if (map[j][k] != 5) {
            return false;
        }

        while (k > x) {
            if (map[j][--k] != 1) {
                if (foundExit) {
                    return false;
                }

                foundExit = true;
            } else {
                foundExit = false;
            }
        }


        if (map[j][k] != 6) {
            return false;
        }

        while (j > y) {
            if (map[--j][k] != 2) {
                if (foundExit) {
                    return false;
                }

                foundExit = true;
            } else {
                foundExit = false;
            }
        }

        return map[j][k] == 3;
    }

    private void setRoom(Room room) {
        for (int i = 0; i < room.getLength() + 1; i++) {
            System.arraycopy(room.getRoom()[i], 0, map[i + room.getY()], room.getX(), room.getWidth() + 1);
        }
    }

    private List<Edge> generateRoomEdges(Room room) {
        List<Edge> roomEdges = new ArrayList<>();

        int i = room.getRoomNumber();

        for (int j = i + 1; j < rooms.size(); j++) {
            double dx = (rooms.get(i).getX() + rooms.get(i).getWidth() / 2.) - (rooms.get(j).getX() + rooms.get(j).getX() / 2.);
            double dy = (rooms.get(i).getY() + rooms.get(i).getLength() / 2.) - (rooms.get(j).getY() + rooms.get(j).getLength() / 2.);

            double distance = Math.sqrt(dx * dx + dy * dy);

            roomEdges.add(new Edge(i, j, distance));
        }

        return roomEdges;
    }

    private UnionFind getPaths(List<Edge> edges) {
        UnionFind paths = new UnionFind(rooms.size());

        int pathsCount = 0;
        int i = 0;

        while (pathsCount < rooms.size() - 1 && i < edges.size()) {
            if (paths.union(edges.get(i).getSrcRoom(), edges.get(i).getDstRoom())) {
                pathsCount++;
            }

            i++;
        }

        return paths;
    }


    public void generateMap() {
        map = new int[SEGMENT_SIZE_Y * ROOMS_COUNT_Y][SEGMENT_SIZE_X * ROOMS_COUNT_X];
        List<Edge> edges = new ArrayList<>();

        for (Room currentRoom : rooms) {
            setRoom(currentRoom);
            edges.addAll(generateRoomEdges(currentRoom));
        }

        edges.sort(Comparator.comparingDouble(Edge::getDistance));

        paths = getPaths(edges);

        for (int j = 0; j < rooms.size(); j++) {
            makeTunnel(rooms.get(paths.getParent()[j]), rooms.get(j));
        }
    }


    boolean isCorrectMap() {
        for (Room room : rooms) {
            if (!isCorrectRoom(room)) {
                return false;
            }
        }

        return true;
    }


    void makeTunnel(Room src, Room dst) {
        int currentX = src.getX() + src.getWidth() / 2;
        int currentY = src.getY() + src.getLength() / 2;

        int dstX = dst.getX() + dst.getWidth() / 2;
        int dstY = dst.getY() + dst.getLength() / 2;

        while (currentX != dstX || currentY != dstY) {
            if (currentX < dstX) {
                currentX++;
            } else if (currentX > dstX) {
                currentX--;
            } else if (currentY < dstY) {
                currentY++;
            } else {
                currentY--;
            }

            if (map[currentY][currentX] == 2) {
                map[currentY][currentX] = 11;
            } else if (map[currentY][currentX] == 1) {
                map[currentY][currentX] = 12;
            } else if (map[currentY][currentX] != 9 && map[currentY][currentX] != 11 && map[currentY][currentX] != 12) {
                map[currentY][currentX] = 10;
            }
        }
    }

    public boolean inSameRoom(Coordinate point1, Coordinate point2) {
        return !(notInRoom(point1)) && getRoomNumber(point2) == getRoomNumber(point1);
    }

    public boolean notInRoom(Coordinate point) {
        return getRoomNumber(point) == -1;
    }

    public int getRoomNumber(Coordinate point) {
        int x = point.getX();
        int y = point.getY();

        for (Room room : rooms) {
            if (x > room.getX() && x < room.getX() + room.getWidth() &&
                    y > room.getY() && y < room.getY() + room.getLength()) {
                return room.getRoomNumber();
            }
        }

        return -1;
    }

    public boolean hasPath(Coordinate point1, Coordinate point2) {
        int y1 = point1.getY();
        int y2 = point2.getY();

        int x1 = point1.getX();
        int x2 = point2.getX();

        int yStepsCount = Math.abs(y1 - y2);
        int xStepsCount = Math.abs(x1 - x2);

        int yStep = y1 < y2 ? 1 : -1;
        int xStep = x1 < x2 ? 1 : -1;

        while (yStepsCount > 0) {
            if (map[y1 + yStep][x1] == 0) {
                return false;
            }

            yStepsCount--;
        }

        while (xStepsCount > 0) {
            if (map[y2][x1 + xStep] == 0) {
                return false;
            }

            xStepsCount--;
        }

        return true;
    }

    public boolean outOfMap(Coordinate point) {
        return (point.getX() < 0 || point.getX() >= ROOMS_COUNT_X * SEGMENT_SIZE_X ||
                point.getY() < 0 || point.getY() >= ROOMS_COUNT_Y * SEGMENT_SIZE_Y);
    }


    public boolean hasWall(Coordinate point1, Coordinate point2) {
        int y1 = point1.getY();
        int y2 = point2.getY();

        int x1 = point1.getX();
        int x2 = point2.getX();

        int yStepsCount = Math.abs(y1 - y2);
        int xStepsCount = Math.abs(x1 - x2);

        int yStep = y1 < y2 ? 1 : -1;
        int xStep = x1 < x2 ? 1 : -1;

        while (yStepsCount > 0) {
            if (map[y1 + yStep][x1] == 2 || map[y1 + yStep][x1] == 1) {
                return true;
            }

            yStepsCount--;
        }

        while (xStepsCount > 0) {
            if (map[y2][x1 + xStep] == 1 || map[y1 + yStep][x1] == 2) {
                return true;
            }

            xStepsCount--;
        }

        return false;
    }

    public boolean inSameCorridor(Coordinate point1, Coordinate point2) {
        return hasPath(point1, point2) || hasPath(point2, point1);
    }

    public boolean isCollide(Coordinate point) {
        int y = point.getY();
        int x = point.getX();

        return map[y][x] != 9 && map[y][x] != 10 && map[y][x] != 11 && map[y][x] != 12;
    }


    public int[][] getMap() {
        return map;
    }


    public int getManhattanDistance(Coordinate point1, Coordinate point2) {
        return (Math.abs(point1.getX() - point2.getX()) + Math.abs(point1.getY() - point2.getY()));
    }

    public List<Coordinate> connectPoints(Coordinate start, Coordinate target) {
        PriorityQueue<Cell> open = new PriorityQueue<>(Comparator.comparingInt(Cell::getF));
        Set<Cell> closed = new HashSet<>();

        List<Coordinate> path = new ArrayList<>();

        open.add(new Cell(start, 0, getManhattanDistance(start, target), null));

        while (!open.isEmpty()) {
            Cell current = open.poll();

            if (current.coordinate.equals(target)) {
                while (!current.coordinate.equals(start)) {
                    path.addFirst(current.coordinate);
                    current = current.parent;
                }

                break;
            }

            closed.add(current);

            for (Direction direction : Direction.values()) {
                int neighborX = current.coordinate.getX();
                int neighborY = current.coordinate.getY();

                switch (direction) {
                    case UP -> neighborY++;
                    case DOWN -> neighborY--;
                    case LEFT -> neighborX--;
                    case RIGHT -> neighborX++;
                }

                Coordinate neighborCoordinate = new Coordinate(neighborX, neighborY);

                Cell neighbor = new Cell(neighborCoordinate,
                        current.g + 1,
                        getManhattanDistance(neighborCoordinate, target),
                        current);

                if (isCollide(neighborCoordinate) || closed.contains(neighbor)) {
                    continue;
                }

                open.add(neighbor);
            }
        }

        return path;
    }
}
