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

import org.barbertrack.barbertrack_poo.model.Servico;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;

public class ServicoView extends Application {

    private static final String ARQUIVO = "data/servicos.dat";

    private final ObservableList<Servico> servicos = FXCollections.observableArrayList();
    private TableView<Servico> tabela;

    private TextField campoNome;
    private TextField campoDuracao;
    private CheckBox checkStatus;

    private Servico servicoEmEdicao = null;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gerenciar Serviços");

        Label labelNome = new Label("Nome:");
        campoNome = new TextField();
        campoNome.setPromptText("Ex: Corte masculino");

        Label labelDuracao = new Label("Duração (min):");
        campoDuracao = new TextField();
        campoDuracao.setPromptText("Ex: 30");

        Label labelStatus = new Label("Ativo:");
        checkStatus = new CheckBox();
        checkStatus.setSelected(true);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setDisable(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(labelNome, 0, 0);
        form.add(campoNome, 1, 0);
        form.add(labelDuracao, 0, 1);
        form.add(campoDuracao, 1, 1);
        form.add(labelStatus, 0, 2);
        form.add(checkStatus, 1, 2);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(servicos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Servico, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Servico, String> colDuracao = new TableColumn<>("Duração (min)");
        colDuracao.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getDuracao())));

        TableColumn<Servico, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isStatus() ? "Ativo" : "Inativo"));

        tabela.getColumns().addAll(colNome, colDuracao, colStatus);

        Button btnEditar = new Button("Editar selecionado");
        Button btnDeletar = new Button("Deletar selecionado");

        HBox acoesTabela = new HBox(8, btnEditar, btnDeletar);
        acoesTabela.setPadding(new Insets(4, 0, 0, 0));

        VBox root = new VBox(12, form, new Separator(), tabela, acoesTabela);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 520, 460);
        stage.setScene(scene);
        stage.show();

        carregarDados();

        btnSalvar.setOnAction(e -> {
            if (!validarCampos()) return;

            String nome = campoNome.getText().trim();
            int duracao = Integer.parseInt(campoDuracao.getText().trim());
            boolean status = checkStatus.isSelected();

            if (servicoEmEdicao == null) {
                // Novo serviço
                Servico novo = new Servico(nome, duracao);
                novo.setStatus(status);
                servicos.add(novo);
            } else {
                // Atualizar existente
                servicoEmEdicao.setNome(nome);
                servicoEmEdicao.setDuracao(duracao);
                servicoEmEdicao.setStatus(status);
                tabela.refresh();
                servicoEmEdicao = null;
                btnCancelar.setDisable(true);
            }

            limparFormulario();
            salvarDados();
        });

        btnCancelar.setOnAction(e -> {
            servicoEmEdicao = null;
            limparFormulario();
            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {
            Servico selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione um serviço para editar.");
                return;
            }
            servicoEmEdicao = selecionado;
            campoNome.setText(selecionado.getNome());
            campoDuracao.setText(String.valueOf(selecionado.getDuracao()));
            checkStatus.setSelected(selecionado.isStatus());
            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {
            Servico selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione um serviço para deletar.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Deletar \"" + selecionado.getNome() + "\"?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    servicos.remove(selecionado);
                    salvarDados();
                }
            });
        });
    }


    private boolean validarCampos() {
        if (campoNome.getText().isBlank()) {
            alerta("Informe o nome do serviço.");
            return false;
        }
        try {
            int d = Integer.parseInt(campoDuracao.getText().trim());
            if (d <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            alerta("Duração deve ser um número inteiro positivo.");
            return false;
        }
        return true;
    }

    private void limparFormulario() {
        campoNome.clear();
        campoDuracao.clear();
        checkStatus.setSelected(true);
    }

    private void salvarDados() {
        ArrayList<Object> dados = new ArrayList<>(servicos);
        RepositoryManager.salvar(ARQUIVO, dados);
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        ArrayList<Object> dados = RepositoryManager.carregar(ARQUIVO);
        for (Object obj : dados) {
            if (obj instanceof Servico) {
                servicos.add((Servico) obj);
            }
        }
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait();
    }
}