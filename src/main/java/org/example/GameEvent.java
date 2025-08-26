package org.example;

public class GameEvent {
    private EventType currentEvent;
    private int value;

    private boolean isSound = false;

    GameEvent() {
    }

    public GameEvent(EventType event, int value) {
        currentEvent = event;
        this.value = value;
    }

    public void setCurrentEvent(EventType currentEvent) {
        this.currentEvent = currentEvent;
    }

    public EventType getCurrentEvent() {
        return currentEvent;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean getSound() {
        return isSound;
    }
}
