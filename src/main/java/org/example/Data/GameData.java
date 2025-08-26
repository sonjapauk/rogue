package org.example.Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.GameInfo;
import org.example.GlobalState;
import org.example.model.menu.MenuState;

import java.io.File;
import java.io.IOException;

public class GameData {
    private GameInfo gameInfo = new GameInfo();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String SAVE_FILE = "save.json";

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void loadGame() {
        try {
            gameInfo = objectMapper.readValue(new File(SAVE_FILE), GameInfo.class);
        } catch (IOException ignored) {
        }
    }

    public void saveGame() {
        try {
                gameInfo.setGlobalState(GlobalState.MENU);
                gameInfo.setCurrentMenuState(MenuState.NEW_GAME);
                objectMapper.writeValue(new File(SAVE_FILE), gameInfo);
        } catch (IOException ignored) {
        }
    }
}
