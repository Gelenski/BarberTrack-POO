package org.barbertrack.barbertrack_poo.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BarberTrack");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
