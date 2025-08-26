package org.example.model;

import org.example.GameInfo;
import org.example.model.UserAction;

public interface Domen {
    void update(UserAction currentAction);

    GameInfo getGameInfo();

    void setGameInfo(GameInfo gameInfo);

    boolean isActive();
}
