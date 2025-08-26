package org.example.model.menu;

import org.example.model.UserAction;

import java.util.List;

public class Menu {
    MenuState currentState = MenuState.NEW_GAME;
    private boolean isChosen = false;
    final List<MenuState> service = List.of(MenuState.values());

    public boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public MenuState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MenuState current) {
        this.currentState = current;
    }

    public void updateMenuState(UserAction action) {
        int currentIndex = service.indexOf(currentState);
        int statesCount = service.size();

        switch (action) {
            case UP -> currentIndex = (currentIndex - 1) % statesCount;
            case DOWN -> currentIndex = (currentIndex + 1) % statesCount;
            case ACTION -> setIsChosen(true);
        }

        currentIndex = currentIndex < 0 ? statesCount + currentIndex : currentIndex;

        setCurrentState(service.get(currentIndex));
    }
}
