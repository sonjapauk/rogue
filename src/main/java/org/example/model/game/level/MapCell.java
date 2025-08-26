package org.example.model.game.level;

public class MapCell {
    int value;
    boolean isVisible;

    public MapCell() {
    }

    public MapCell(int value, boolean isVisible) {
        this.value = value;
        this.isVisible = isVisible;
    }

    public int getValue() {
        return value;
    }

    public boolean getVisible() {
        return isVisible;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
