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
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

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
        campoDataHora.textProperty().addListener((obs, oldValue, newValue) -> {

            String numeros = newValue.replaceAll("[^0-9]", "");

            if (numeros.length() > 12) {
                numeros = numeros.substring(0, 12);
            }

            StringBuilder texto = new StringBuilder();

            for (int i = 0; i < numeros.length(); i++) {

                if (i == 2 || i == 4) {
                    texto.append("/");
                }

                if (i == 8) {
                    texto.append(" ");
                }

                if (i == 10) {
                    texto.append(":");
                }

                texto.append(numeros.charAt(i));
            }

            if (!texto.toString().equals(newValue)) {
                campoDataHora.setText(texto.toString());
                campoDataHora.positionCaret(texto.length());
            }
        });

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

        carregarClientes();
        carregarServicos();
        carregarAgendamentos();

        comboCliente.setItems(clientes);
        comboServico.setItems(servicos);

        btnSalvar.setOnAction(e -> {

            if (!validarCampos()) return;

            LocalDateTime data =
                    LocalDateTime.parse(
                            campoDataHora.getText().trim(),
                            formatter
                    );

            Cliente cliente = comboCliente.getValue();
            Servico servico = comboServico.getValue();

            if (agendamentoEmEdicao == null) {

                Agendamento novo = new Agendamento(data, cliente, servico);

                agendamentos.add(novo);

            } else {

                agendamentoEmEdicao.setCliente(cliente);
                agendamentoEmEdicao.setServico(servico);
                agendamentoEmEdicao.setDataAgendamento(data);

                tabela.refresh();

                agendamentoEmEdicao = null;
                btnCancelar.setDisable(true);
            }

            limparFormulario();
            salvarDados();
        });

        btnCancelar.setOnAction(e -> {

            agendamentoEmEdicao = null;

            limparFormulario();

            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {

            Agendamento selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta("Selecione um agendamento para editar.");

                return;
            }

            agendamentoEmEdicao = selecionado;

            comboCliente.setValue(
                    selecionado.getCliente()
            );

            comboServico.setValue(
                    selecionado.getServico()
            );

            campoDataHora.setText(
                    selecionado.getDataAgendamento()
                            .format(formatter)
            );

            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {

            Agendamento selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta("Selecione um agendamento para deletar.");

                return;
            }

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Deletar agendamento?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            confirm.showAndWait()
                    .ifPresent(resp -> {

                        if (resp == ButtonType.YES) {

                            agendamentos.remove(
                                    selecionado
                            );

                            salvarDados();
                        }
                    });
        });



    }
    private boolean validarCampos() {

        if (comboCliente.getValue() == null) {

            alerta("Selecione um cliente.");

            return false;
        }

        if (comboServico.getValue() == null) {

            alerta("Selecione um serviço.");

            return false;
        }

        try {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm"
                    );

            LocalDateTime.parse(
                    campoDataHora.getText().trim(),
                    formatter
            );

        } catch (DateTimeParseException e) {

            alerta(
                    "Data inválida."
            );

            return false;
        }

        return true;
    }
    private void limparFormulario() {

        comboCliente.setValue(null);

        comboServico.setValue(null);

        campoDataHora.clear();
    }
    private void salvarDados() {

        ArrayList<Object> dados =
                new ArrayList<>(agendamentos);

        RepositoryManager.salvar(
                ARQUIVO,
                dados
        );
    }
    @SuppressWarnings("unchecked")
    private void carregarClientes() {

        ArrayList<Object> dados =
                RepositoryManager.carregar(
                        "data/clientes.dat"
                );

        for (Object obj : dados) {

            if (obj instanceof Cliente) {

                clientes.add(
                        (Cliente) obj
                );
            }
        }
    }
    @SuppressWarnings("unchecked")
    private void carregarServicos() {

        ArrayList<Object> dados =
                RepositoryManager.carregar(
                        "data/servicos.dat"
                );

        for (Object obj : dados) {

            if (obj instanceof Servico servico) {

                if (servico.isStatus()) {

                    servicos.add(servico);
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    private void carregarAgendamentos() {

        ArrayList<Object> dados =
                RepositoryManager.carregar(ARQUIVO);

        for (Object obj : dados) {

            if (obj instanceof Agendamento) {

                agendamentos.add((Agendamento) obj);
            }
        }
    }
    private void alerta(String mensagem) {

        new Alert(
                Alert.AlertType.WARNING,
                mensagem,
                ButtonType.OK
        ).showAndWait();
    }


}
