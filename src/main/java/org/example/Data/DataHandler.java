package org.example.Data;

import org.example.GameInfo;

public class DataHandler implements DataLayer {
    private GameData data = new GameData();

    public void saveGame() {
        data.saveGame();
    }

    public void loadGame() {
        data.loadGame();
    }

    @Override
    public GameInfo getCurrentGameInfo() {
        return data.getGameInfo();
    }

    @Override
    public void setCurrentGameInfo(GameInfo currentGameInfo) {

    }

    public GameData getData() {
        return data;
    }

    public void setData(GameData data) {
        this.data = data;
    }
}
