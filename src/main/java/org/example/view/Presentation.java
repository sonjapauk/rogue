package org.example.view;

import org.example.GameInfo;
import org.example.model.UserAction;

import java.io.IOException;

public interface Presentation {
    void render(GameInfo currentGameInfo) throws IOException;

    UserAction getUserAction() throws IOException;

    void close() throws IOException;
}
