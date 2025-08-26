package org.example.Data;

import org.example.GameInfo;

public interface DataLayer {
    void saveGame();

    void loadGame();

    GameInfo getCurrentGameInfo();

    void setCurrentGameInfo(GameInfo currentGameInfo);
}
