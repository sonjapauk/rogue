package org.example.model;


import org.example.GameInfo;
import org.example.GlobalState;
import org.example.model.game.Game;
import org.example.model.menu.Menu;
import org.example.rating.Rating;

public class Model implements Domen {
    Menu menu;
    Game game;
    GameInfo currentGameInfo;
    Rating rating;

    public Model() {
        menu = new Menu();
        rating = new Rating();
    }

    public void setGameInfo(GameInfo currentGameInfo) {
        this.currentGameInfo = currentGameInfo;
    }

    @Override
    public void update(UserAction currentAction) {
        if (currentGameInfo.getGlobalState() == GlobalState.MENU) {
            if (currentAction == UserAction.EXIT) {
                currentGameInfo.setGlobalState(GlobalState.EXIT);
            }

            menu.updateMenuState(currentAction);
            currentGameInfo.setCurrentMenuState(menu.getCurrentState());

            if (menu.getIsChosen()) {
                menu.setIsChosen(false);

                switch (currentGameInfo.getCurrentMenuState()) {
                    case NEW_GAME -> {
                        game = new Game(currentGameInfo);
                        currentGameInfo.setGlobalState(GlobalState.GAME);
                    }

                    case CONTINUE -> {
                        if (currentGameInfo.getHasSave()) {
                            currentGameInfo.setGlobalState(GlobalState.GAME);
                            game = new Game(currentGameInfo);
                            game.loadGame(currentGameInfo);
                        }
                    }

                    case CONTROL_INFO -> currentGameInfo.setGlobalState(GlobalState.MENU);
                    case RATING -> currentGameInfo.setGlobalState(GlobalState.RATING);
                    case EXIT -> currentGameInfo.setGlobalState(GlobalState.EXIT);
                }
            }

        }

        if (currentGameInfo.getGlobalState() == GlobalState.GAME) {
            game.updateState(currentAction, currentGameInfo);
        }

        if (currentGameInfo.getGlobalState() == GlobalState.RATING) {
            if (rating.isExitRating(currentAction)) {
                currentGameInfo.setGlobalState(GlobalState.MENU);
            }
        }
    }

    @Override
    public boolean isActive() {
        return getGameInfo().getGlobalState() != GlobalState.EXIT;
    }

    @Override
    public GameInfo getGameInfo() {
        return currentGameInfo;
    }
}
