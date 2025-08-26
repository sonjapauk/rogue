package org.example.model.game.level;

import java.util.stream.IntStream;

public class UnionFind {
    private int[] parent;

    UnionFind() {
    }

    UnionFind(int roomsCount) {
        parent = IntStream.range(0, roomsCount).toArray();
    }

    public int find(int roomNumber) {
        if (parent[roomNumber] == roomNumber) {
            return roomNumber;
        }

        return find(parent[roomNumber]);
    }

    public boolean union(int room1, int room2) {
        int root1 = find(room1);
        int root2 = find(room2);

        if (root1 != root2) {
            parent[root1] = root2;
            return true;
        }

        return false;
    }

    public int[] getParent() {
        return parent;
    }
}
