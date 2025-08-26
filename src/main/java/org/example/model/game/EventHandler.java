package org.example.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.EventType;
import org.example.GameEvent;
import org.example.model.UserAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EventHandler {
    private List<GameEvent> currentEvents = new ArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentTimeoutTask = null;

    public List<GameEvent> getCurrentEvents() {
        return currentEvents;
    }

    public void setCurrentEvents(List<GameEvent> currentEvents) {
        this.currentEvents = currentEvents;
    }

    public void addEvent(GameEvent newEvent) {
        boolean wasEmpty = currentEvents.isEmpty();
        currentEvents.add(newEvent);

        if (wasEmpty) {
            startTimer();
        }
    }

    @JsonIgnore
    public GameEvent getCurrentEvent() {
        if (currentEvents.isEmpty()) {
            return new GameEvent(EventType.NONE, 0);
        }

        return currentEvents.getFirst();
    }

    public void removeEvent(UserAction action) {
        if (!currentEvents.isEmpty() && action == UserAction.SKIP) {
            currentEvents.remove(0);

            if (currentTimeoutTask != null && !currentTimeoutTask.isDone()) {
                currentTimeoutTask.cancel(false);
                currentTimeoutTask = null;
            }

            if (!currentEvents.isEmpty()) {
                startTimer();
            }
        } else if(!currentEvents.isEmpty() && action == UserAction.SKIP_ALL){
            currentEvents.clear();

            if (currentTimeoutTask != null && !currentTimeoutTask.isDone()) {
                currentTimeoutTask.cancel(false);
                currentTimeoutTask = null;
            }
        }
    }

    private void startTimer() {
        currentTimeoutTask = scheduler.schedule(() -> {
            if (!currentEvents.isEmpty()) {
                currentEvents.remove(0);
            }

            currentTimeoutTask = null;

            if (!currentEvents.isEmpty()) {
                startTimer();
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void shutdownScheduler() {
        scheduler.shutdownNow();
    }
}
