package org.barbertrack.barbertrack_poo.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.views.*;

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

        Button btnAgendamentos = new Button("Gerenciar agendamentos");
        btnAgendamentos.setOnAction(e -> {
            new AgendamentoView().start(new Stage());
        });

        Button btnBarbearias = new Button("Gerenciar barbearias");
        btnBarbearias.setOnAction(e -> {
            new BarbeariaView().start(new Stage());
        });

        Button btnBarbeiros = new Button("Gerenciar barbeiros");
        btnBarbeiros.setOnAction( event ->  {
            new BarbeiroView().start(new Stage());
                });

        Button btnEspecialidades = new Button("Gerenciar especialidades");
        btnEspecialidades.setOnAction( event ->  {
            new EspecialidadeView().start(new Stage());
        });


        VBox root = new VBox(10, btnServicos, btnClientes, btnBarbearias, btnAgendamentos, btnBarbeiros, btnEspecialidades);
        root.setPadding(new Insets(12));

        stage.setScene(new Scene(root, 400, 300));
        stage.show(); // só aqui
    }

    public static void main(String[] args) {
        launch(args);
    }
}
