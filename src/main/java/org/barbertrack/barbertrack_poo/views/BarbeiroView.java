package org.barbertrack.barbertrack_poo.views;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.barbertrack.barbertrack_poo.model.Barbeiro;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;

public class BarbeiroView extends Application {

    private static final String ARQUIVO = "data/barbeiros.dat";

    private final ObservableList<Barbeiro> barbeiros =
            FXCollections.observableArrayList();

    private TableView<Barbeiro> tabela;

    private TextField campoNome;
    private TextField campoTelefone;
    private TextField campoCpf;
    private TextField campoDataAdmissao;

    private Barbeiro barbeiroEmEdicao = null;

    @Override
    public void start(Stage stage) {

        stage.setTitle("Gerenciar Barbeiros");

        Label labelNome = new Label("Nome:");
        campoNome = new TextField();

        Label labelTelefone = new Label("Telefone:");
        campoTelefone = new TextField();

        Label labelCpf = new Label("CPF:");
        campoCpf = new TextField();

        Label labelData = new Label("Data Admissão:");
        campoDataAdmissao = new TextField();
        campoDataAdmissao.setPromptText("DD/MM/AAAA");

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.setDisable(true);

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));

        form.add(labelNome, 0, 0);
        form.add(campoNome, 1, 0);

        form.add(labelTelefone, 0, 1);
        form.add(campoTelefone, 1, 1);

        form.add(labelCpf, 0, 2);
        form.add(campoCpf, 1, 2);

        form.add(labelData, 0, 3);
        form.add(campoDataAdmissao, 1, 3);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 4);

        tabela = new TableView<>();

        tabela.setItems(barbeiros);
        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Barbeiro, String> colNome =
                new TableColumn<>("Nome");

        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getNome()));

        TableColumn<Barbeiro, String> colTelefone =
                new TableColumn<>("Telefone");

        colTelefone.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getTelefone()));

        TableColumn<Barbeiro, String> colCpf =
                new TableColumn<>("CPF");

        colCpf.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getCpf()));

        TableColumn<Barbeiro, String> colData =
                new TableColumn<>("Data Admissão");

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDataAdmissao()));

        tabela.getColumns().addAll(
                colNome,
                colTelefone,
                colCpf,
                colData
        );

        Button btnEditar =
                new Button("Editar selecionado");

        Button btnDeletar =
                new Button("Deletar selecionado");

        HBox acoesTabela =
                new HBox(8, btnEditar, btnDeletar);

        acoesTabela.setPadding(
                new Insets(4, 0, 0, 0));

        VBox root = new VBox(
                12,
                form,
                new Separator(),
                tabela,
                acoesTabela
        );

        root.setPadding(new Insets(12));

        Scene scene =
                new Scene(root, 700, 500);

        stage.setScene(scene);
        stage.show();

        carregarDados();

        btnSalvar.setOnAction(e -> {

            if (!validarCampos()) {
                return;
            }

            String nome =
                    campoNome.getText().trim();

            String telefone =
                    campoTelefone.getText().trim();

            String cpf =
                    campoCpf.getText().trim();

            String data =
                    campoDataAdmissao.getText().trim();

            try {
                if (barbeiroEmEdicao == null) {
                    Barbeiro novo = new Barbeiro(nome, telefone, cpf, data);
                    barbeiros.add(novo);
                } else {
                    barbeiroEmEdicao.setNome(nome);
                    barbeiroEmEdicao.setTelefone(telefone);
                    barbeiroEmEdicao.setCpf(cpf);
                    barbeiroEmEdicao.setDataAdmissao(data);
                    tabela.refresh();
                    barbeiroEmEdicao = null;
                    btnCancelar.setDisable(true);
                }
                limparFormulario();
                salvarDados();
            } catch (Exception ex) {
                alerta(ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> {

            barbeiroEmEdicao = null;

            limparFormulario();

            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {

            Barbeiro selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta(
                        "Selecione um barbeiro para editar."
                );

                return;
            }

            barbeiroEmEdicao = selecionado;

            campoNome.setText(
                    selecionado.getNome());

            campoTelefone.setText(
                    selecionado.getTelefone());

            campoCpf.setText(
                    selecionado.getCpf());

            campoDataAdmissao.setText(
                    selecionado.getDataAdmissao());

            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {

            Barbeiro selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta(
                        "Selecione um barbeiro para deletar."
                );

                return;
            }

            Alert confirm =
                    new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Deletar \"" +
                                    selecionado.getNome()
                                    + "\"?",
                            ButtonType.YES,
                            ButtonType.NO
                    );

            confirm.showAndWait()
                    .ifPresent(resp -> {

                        if (resp == ButtonType.YES) {

                            barbeiros.remove(
                                    selecionado
                            );

                            salvarDados();
                        }
                    });
        });
    }

    private boolean validarCampos() {

        if (campoNome.getText().isBlank()) {

            alerta("Informe o nome.");

            return false;
        }

        if (campoTelefone.getText().isBlank()) {

            alerta("Informe o telefone.");

            return false;
        }

        if (campoCpf.getText().length() != 11) {

            alerta(
                    "CPF deve possuir 11 dígitos."
            );

            return false;
        }

        if (!campoDataAdmissao.getText()
                .matches("\\d{2}/\\d{2}/\\d{4}")) {

            alerta(
                    "Data deve estar no formato DD/MM/AAAA."
            );

            return false;
        }

        return true;
    }

    private void limparFormulario() {

        campoNome.clear();
        campoTelefone.clear();
        campoCpf.clear();
        campoDataAdmissao.clear();
    }

    private void salvarDados() {

        ArrayList<Object> dados =
                new ArrayList<>(barbeiros);

        RepositoryManager.salvar(
                ARQUIVO,
                dados
        );
    }

    private void carregarDados() {

        ArrayList<Object> dados =
                RepositoryManager.carregar(
                        ARQUIVO
                );

        for (Object obj : dados) {

            if (obj instanceof Barbeiro) {

                barbeiros.add(
                        (Barbeiro) obj
                );
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