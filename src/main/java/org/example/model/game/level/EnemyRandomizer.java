package org.example.model.game.level;

import org.example.model.game.Coordinate;
import org.example.model.game.enemy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

class EnemyDrop {
    Supplier<Enemy> supplier;
    int weight;

    EnemyDrop(Supplier<Enemy> supplier, int weight) {
        this.supplier = supplier;
        this.weight = weight;
    }

    Enemy getEnemy() {
        return supplier.get();
    }
}

public class EnemyRandomizer {
    static final int MAX_LEVEL = 21;

    public static List<Enemy> getEnemyListForMapping(int level, int difficultIndex) {
        List<EnemyDrop> enemyDrops = List.of(
                new EnemyDrop(() -> new Zombie(new Coordinate(0, 0), 6 + level, 3 + level, 1 + level, 3),
                        calcWeight(level, 10, 1)),  // min=1, max=10

                new EnemyDrop(() -> new Ogre(new Coordinate(0, 0), 7 + level, 7 + level, 1 + level, 3),
                        calcWeight(level, 5, 8)),

                new EnemyDrop(() -> new Vampire(new Coordinate(0, 0), 6 + level, 3 + level, 6 + level, 6),
                        calcWeight(level, 1, 10)),

                new EnemyDrop(() -> new Ghost(new Coordinate(0, 0), 2 + level, 1 + level, 6 + level, 2),
                        calcWeight(level, 5, 8)),

                new EnemyDrop(() -> new SnakeWizard(new Coordinate(0, 0), 6 + level, 1, 7 + level, 6),
                        calcWeight(level, 1, 9)),

                new EnemyDrop(() -> new Mimic(new Coordinate(0, 0), 10 + level, 1, 6 + level, 1),
                        calcWeight(level, 5, 8))
        );

        List<Enemy> enemyForMapping = new ArrayList<>();
        int enemiesCount = 10 - difficultIndex;

        for (int i = 0; i < enemiesCount; ++i) {
            enemyForMapping.add(getRandomEnemy(enemyDrops));
        }

        return enemyForMapping;
    }

    static int calcWeight(int level, int minWeight, int maxWeight) {
        if (level < 1) level = 1;
        if (level > MAX_LEVEL) level = MAX_LEVEL;

        return minWeight + (maxWeight - minWeight) * (level - 1) / (MAX_LEVEL - 1);
    }

    static Enemy getRandomEnemy(List<EnemyDrop> enemyDrops) {
        Random random = new Random();

        int totalWeight = enemyDrops.stream()
                .filter(drop -> drop.weight > 0)
                .mapToInt(drop -> drop.weight)
                .sum();

        int current = 0;

        int rand = totalWeight == 0 ? 0 : random.nextInt(totalWeight);

        for (EnemyDrop drop : enemyDrops) {
            if (rand - current < drop.weight) {
                return drop.getEnemy();
            }

            current += drop.weight;
        }

        return null;
    }
}
