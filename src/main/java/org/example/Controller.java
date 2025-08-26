package org.example;

import org.example.Data.DataLayer;
import org.example.model.Domen;
import org.example.model.UserAction;
import org.example.view.Presentation;

import java.io.IOException;

public class Controller {
    Presentation gameView;
    Domen gameModel;
    DataLayer gameData;

    Controller(Presentation gameView, Domen gameModel, DataLayer data) {
        this.gameView = gameView;
        this.gameModel = gameModel;
        gameData = data;
        data.loadGame();
        gameModel.setGameInfo(data.getCurrentGameInfo());
    }

    void game() throws IOException {
        SoundPlayer.playLoop("/music/rogueSong.wav", 0.2f);

        try {
            gameView.render(gameData.getCurrentGameInfo());

            while (gameModel.isActive()) {
                UserAction currentAction = gameView.getUserAction();

                if (currentAction != UserAction.NONE) {
                    gameModel.update(currentAction);
                    gameData.setCurrentGameInfo(gameModel.getGameInfo());
                }

                SoundPlayer.handleSoundEvents(gameData.getCurrentGameInfo().getCurrentEventsSound());
                gameView.render(gameData.getCurrentGameInfo());

                Thread.sleep(75);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            gameData.saveGame();
            gameView.close();
            SoundPlayer.soundClose();
            gameData.getCurrentGameInfo().getEventHandler().shutdownScheduler();
        }
    }
}
