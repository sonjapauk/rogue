package org.example.model.game;

public class DifficultyController {
    private int difficultyIndex;
    private int startHealth;
    private int endHealth;

    public DifficultyController() {
        difficultyIndex = 0;
        startHealth = 0;
        endHealth = 0;
    }

    public void startDifficultyControl(int currentHealth) {
        difficultyIndex = 0;
        startHealth = currentHealth;
    }

    public void calculateDifficultyIndex(int currentHealth) {
        endHealth = currentHealth;

        int healthLost = startHealth - endHealth;

        difficultyIndex += healthLost % 10;
    }

    public int getDifficultyIndex() {
        return difficultyIndex;
    }

    public void setDifficultyIndex(int difficultyIndex) {
        this.difficultyIndex = difficultyIndex;
    }

    public int getStartHealth() {
        return startHealth;
    }

    public void setStartHealth(int startHealth) {
        this.startHealth = startHealth;
    }

    public int getEndHealth() {
        return endHealth;
    }

    public void setEndHealth(int endHealth) {
        this.endHealth = endHealth;
    }

}
