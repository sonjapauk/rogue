package org.example.model.game.level;

public class Room {
    private int x;
    private int y;

    private int length;

    private int width;

    private int[][] room;

    private int roomNumber;

    public Room() {
    }

    public Room(int x, int y, int length, int width, int roomNumber) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.roomNumber = roomNumber;

        room = new int[length + 1][width + 1];
        fill();
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void fill() {
        int j = 0;
        int k = 0;

        while (k < width) {
            room[j][++k] = 1;
        }

        room[j][k] = 4;

        while (j < length) {
            room[++j][k] = 2;
        }

        room[j][k] = 5;

        while (k > 0) {
            room[j][--k] = 1;
        }

        room[j][k] = 6;

        while (j > 0) {
            room[--j][k] = 2;
        }

        room[j][k] = 3;

        for (j = 1; j <= length - 1; j++) {
            for (k = 1; k <= width - 1; k++) {
                room[j][k] = 9;
            }
        }
    }

    public void setRoom(int[][] room) {
        this.room = room;
    }

    public int[][] getRoom() {
        return room;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
