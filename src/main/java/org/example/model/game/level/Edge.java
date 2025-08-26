package org.example.model.game.level;

class Edge {
    private final double distance;

    private final int srcRoom;

    private final int dstRoom;

    Edge(int srcRoom, int dstRoom, double distance) {
        this.srcRoom = srcRoom;
        this.dstRoom = dstRoom;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public int getDstRoom() {
        return dstRoom;
    }

    public int getSrcRoom() {
        return srcRoom;
    }
}
