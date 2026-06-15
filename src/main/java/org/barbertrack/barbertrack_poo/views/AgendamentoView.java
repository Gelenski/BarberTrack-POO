package org.barbertrack.barbertrack_poo.views;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.model.Agendamento;
import org.barbertrack.barbertrack_poo.model.Cliente;
import org.barbertrack.barbertrack_poo.model.Servico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AgendamentoView extends Application {
    private static final String ARQUIVO = "data/agendamentos.dat";

    private final ObservableList<Agendamento> agendamentos = FXCollections.observableArrayList();

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    private final ObservableList<Servico> servicos = FXCollections.observableArrayList();

    private TableView<Agendamento> tabela;

    private TextField campoDataHora;

    private ComboBox<Cliente> comboCliente;
    private ComboBox<Servico> comboServico;

    private Agendamento agendamentoEmEdicao = null;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gerenciar agendamento");

        Label labelCliente = new Label("Cliente");
        comboCliente = new ComboBox<>();

        Label labelServico = new Label("Servico");
        comboServico = new ComboBox<>();

        Label labelDataHora = new Label("Data agendamento");
        campoDataHora = new TextField();
        campoDataHora.setPromptText("dd/MM/yyyy HH:mm");

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setDisable(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(labelCliente, 0, 0);
        form.add(comboCliente, 1, 0);
        form.add(labelServico, 2, 0);
        form.add(comboServico, 3, 0);
        form.add(labelDataHora, 0, 1);
        form.add(campoDataHora, 1, 1);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(agendamentos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Agendamento, String> colCliente =
                new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getCliente().getNome()
                ));

        TableColumn<Agendamento, String> colServico =
                new TableColumn<>("Serviço");
        colServico.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getServico().getNome()
                ));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        TableColumn<Agendamento, String> colData =
                new TableColumn<>("Data");

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .getDataAgendamento()
                                .format(formatter)
                ));

        tabela.getColumns().addAll(colCliente, colServico, colData);

        Button btnEditar = new Button("Editar selecionado");
        Button btnDeletar = new Button("Deletar selecionado");

        HBox acoesTabela = new HBox(8,btnEditar, btnDeletar);
        acoesTabela.setPadding(new Insets(4, 0, 0, 0));

        VBox root = new VBox(12, form, new Separator(), tabela, acoesTabela);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 520, 460);
        stage.setScene(scene);
        stage.show();



    }

}
