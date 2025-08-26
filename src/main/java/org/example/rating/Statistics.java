package org.example.rating;

public class Statistics {
    private int treasureCount = 0;
    private int elixirsCount = 0;
    private int scrollsCount = 0;
    private int foodCount = 0;
    private int moveCount = 0;
    private int defeatEnemiesCount = 0;
    private int hitsCount = 0;
    private int enemyHistsCount = 0;

    private int level = 1;

    public int getEnemyHistsCount() {
        return enemyHistsCount;
    }

    public void setEnemyHistsCount(int enemyHistsCount) {
        this.enemyHistsCount = enemyHistsCount;
    }

    public int getHitsCount() {
        return hitsCount;
    }

    public void setHitsCount(int hitsCount) {
        this.hitsCount = hitsCount;
    }

    public int getDefeatEnemiesCount() {
        return defeatEnemiesCount;
    }

    public void setDefeatEnemiesCount(int defeatEnemiesCount) {
        this.defeatEnemiesCount = defeatEnemiesCount;
    }

    public int getTreasureCount() {
        return treasureCount;
    }

    public void setTreasureCount(int treasureCount) {
        this.treasureCount = treasureCount;
    }

    public int getElixirsCount() {
        return elixirsCount;
    }

    public void setElixirsCount(int elixirsCount) {
        this.elixirsCount = elixirsCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public int getScrollsCount() {
        return scrollsCount;
    }

    public void setScrollsCount(int scrollsCount) {
        this.scrollsCount = scrollsCount;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void elixirCountIncrement() {
        elixirsCount++;
    }

    public void scrollsCountIncrement() {
        scrollsCount++;
    }

    public void foodCountIncrement() {
        foodCount++;
    }

    public void moveCountIncrement() {
        moveCount++;
    }

    public void defeatEnemiesCountIncrement() {
        defeatEnemiesCount++;
    }

    public void hitsCountIncrement() {
        hitsCount++;
    }

    public void enemiesHitsCountIncrement() {
        enemyHistsCount++;
    }


}