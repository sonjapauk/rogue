package org.example;

import org.example.Data.DataHandler;
import org.example.Data.DataLayer;
import org.example.model.Domen;
import org.example.model.Model;
import org.example.view.Presentation;
import org.example.view.View;

import java.io.IOException;
import java.util.Set;

public class RogueGame {
    public static void main(String[] args) {
        try {
            Domen model = new Model();
            Presentation view = new View();
            DataLayer data = new DataHandler();
            Controller controller = new Controller(view, model, data);

            controller.game();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}