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

import org.barbertrack.barbertrack_poo.model.Especialidade;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;

public class EspecialidadeView extends Application {

    private static final String ARQUIVO = "data/especialidades.dat";

    private final ObservableList<Especialidade> especialidades =
            FXCollections.observableArrayList();

    private TableView<Especialidade> tabela;

    private TextField campoNome;
    private TextField campoDescricao;
    private TextField campoNivelHabilidade;

    private Especialidade especialidadeEmEdicao = null;

    @Override
    public void start(Stage stage) {

        stage.setTitle("Gerenciar Especialidades");

        Label labelNome = new Label("Nome:");
        campoNome = new TextField();

        Label labelDescricao = new Label("Descrição:");
        campoDescricao = new TextField();

        Label labelNivel = new Label("Nível de Habilidade:");
        campoNivelHabilidade = new TextField();
        campoNivelHabilidade.setPromptText("Ex: 1 (iniciante), 2, 3...");

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.setDisable(true);

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));

        form.add(labelNome, 0, 0);
        form.add(campoNome, 1, 0);

        form.add(labelDescricao, 0, 1);
        form.add(campoDescricao, 1, 1);

        form.add(labelNivel, 0, 2);
        form.add(campoNivelHabilidade, 1, 2);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(especialidades);
        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Especialidade, String> colNome =
                new TableColumn<>("Nome");
        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Especialidade, String> colDescricao =
                new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescricao()));

        TableColumn<Especialidade, String> colNivel =
                new TableColumn<>("Nível de Habilidade");
        colNivel.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.valueOf(c.getValue().getNivelHabilidade())));

        tabela.getColumns().addAll(colNome, colDescricao, colNivel);

        Button btnEditar = new Button("Editar selecionado");
        Button btnDeletar = new Button("Deletar selecionado");

        HBox acoesTabela = new HBox(8, btnEditar, btnDeletar);
        acoesTabela.setPadding(new Insets(4, 0, 0, 0));

        VBox root = new VBox(
                12, form, new Separator(), tabela, acoesTabela);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 600, 450);
        stage.setScene(scene);
        stage.show();

        carregarDados();

        btnSalvar.setOnAction(e -> {
            if (!validarCampos()) return;

            String nome = campoNome.getText().trim();
            String descricao = campoDescricao.getText().trim();
            int nivel;

            try {
                nivel = Integer.parseInt(campoNivelHabilidade.getText().trim());
            } catch (NumberFormatException ex) {
                alerta("Nível de habilidade deve ser um número inteiro.");
                return;
            }

            if (especialidadeEmEdicao == null) {
                Especialidade nova =
                        new Especialidade(nome, descricao, nivel);
                especialidades.add(nova);
            } else {
                especialidadeEmEdicao.setNome(nome);
                especialidadeEmEdicao.setDescricao(descricao);
                especialidadeEmEdicao.setNivelHabilidade(nivel);
                tabela.refresh();
                especialidadeEmEdicao = null;
                btnCancelar.setDisable(true);
            }

            limparFormulario();
            salvarDados();
        });

        btnCancelar.setOnAction(e -> {
            especialidadeEmEdicao = null;
            limparFormulario();
            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {
            Especialidade selecionada =
                    tabela.getSelectionModel().getSelectedItem();

            if (selecionada == null) {
                alerta("Selecione uma especialidade para editar.");
                return;
            }

            especialidadeEmEdicao = selecionada;
            campoNome.setText(selecionada.getNome());
            campoDescricao.setText(selecionada.getDescricao());
            campoNivelHabilidade.setText(String.valueOf(selecionada.getNivelHabilidade()));
            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {
            Especialidade selecionada =
                    tabela.getSelectionModel().getSelectedItem();

            if (selecionada == null) {
                alerta("Selecione uma especialidade para deletar.");
                return;
            }

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Deletar \"" + selecionada.getNome() + "\"?",
                    ButtonType.YES, ButtonType.NO
            );
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    especialidades.remove(selecionada);
                    salvarDados();
                }
            });
        });
    }

    private boolean validarCampos() {
        if (campoNome.getText().isBlank()) {
            alerta("Informe o nome da especialidade.");
            return false;
        }
        if (campoDescricao.getText().isBlank()) {
            alerta("Informe a descrição.");
            return false;
        }
        if (campoNivelHabilidade.getText().isBlank()) {
            alerta("Informe o nível de habilidade.");
            return false;
        }
        try {
            int nivel = Integer.parseInt(campoNivelHabilidade.getText().trim());
            if (nivel < 1) {
                alerta("Nível de habilidade deve ser um número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            alerta("Nível de habilidade deve ser um número inteiro.");
            return false;
        }
        return true;
    }

    private void limparFormulario() {
        campoNome.clear();
        campoDescricao.clear();
        campoNivelHabilidade.clear();
    }

    private void salvarDados() {
        ArrayList<Object> dados = new ArrayList<>(especialidades);
        RepositoryManager.salvar(ARQUIVO, dados);
    }

    private void carregarDados() {
        ArrayList<Object> dados = RepositoryManager.carregar(ARQUIVO);
        for (Object obj : dados) {
            if (obj instanceof Especialidade) {
                especialidades.add((Especialidade) obj);
            }
        }
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait();
    }
}