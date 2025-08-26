package org.example.model.game.AllAboutHero;

import org.example.model.UserAction;

public class InventorySelector {
    private int currentIndex = 0;
    private int maxIndex;
    private ItemSelectorType currentType;

    public void StartInventoryState(int itemsLength, ItemSelectorType type) {
        currentType = type;
        maxIndex = itemsLength;
        currentIndex = 0;
    }

    public void processInventory(UserAction action) {
        switch (action) {
            case UP -> {
                if (currentIndex > 0) {
                    currentIndex--;
                }
            }

            case DOWN -> {
                if (currentIndex < maxIndex - 1) {
                    currentIndex++;
                }
            }
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public ItemSelectorType getCurrentType() {
        return currentType;
    }

    public void setCurrentType(ItemSelectorType currentType) {
        this.currentType = currentType;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }
}
