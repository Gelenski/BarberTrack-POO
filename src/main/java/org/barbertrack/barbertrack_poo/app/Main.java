package org.barbertrack.barbertrack_poo.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.views.BarbeariaView;
import org.barbertrack.barbertrack_poo.views.ClienteView;
import org.barbertrack.barbertrack_poo.views.ServicoView;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BarberTrack");

        Button btnServicos = new Button("Gerenciar Serviços");
        btnServicos.setOnAction(e -> {
            new ServicoView().start(new Stage());
        });

        Button btnClientes = new Button("Gerenciar clientes");
        btnClientes.setOnAction(e -> {
            new ClienteView().start(new Stage());
        });

        Button btnBarbearias = new Button("Gerenciar barbearias");
        btnBarbearias.setOnAction(e -> {
            new BarbeariaView().start(new Stage());
        });

        VBox root = new VBox(10, btnServicos, btnClientes, btnBarbearias);
        root.setPadding(new Insets(12));

        stage.setScene(new Scene(root, 400, 300));
        stage.show(); // só aqui
    }

    public static void main(String[] args) {
        launch(args);
    }
}
