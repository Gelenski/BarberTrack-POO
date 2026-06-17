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
import org.barbertrack.barbertrack_poo.model.Cliente;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;


public class ClienteView extends Application {
    private static final String ARQUIVO = "data/clientes.dat";

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private TableView<Cliente> tabela;

    private TextField campoNome;
    private TextField campoTelefone;
    private TextField campoEmail;

    private Cliente clienteEmEdicao = null;

    @Override
    public void start(Stage stage){
        stage.setTitle("Gerenciar clientes");

        Label labelNome = new Label("Nome:");
        campoNome = new TextField();

        Label labelTel = new Label("Telefone:");
        campoTelefone = new TextField();

        Label labelEmail = new Label("E-mail:");
        campoEmail = new TextField();

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setDisable(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(labelNome, 0, 0);
        form.add(campoNome, 1, 0);
        form.add(labelTel, 2, 0);
        form.add(campoTelefone, 3, 0);
        form.add(labelEmail, 0, 1);
        form.add(campoEmail, 1, 1);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(clientes);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Cliente, String> colTel = new TableColumn<>("Telefone");
        colTel.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        tabela.getColumns().addAll(colNome, colTel, colEmail);

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
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();

            if (clienteEmEdicao == null) {
                // Novo cliente
                Cliente novo = new Cliente(nome, telefone, email);
                clientes.add(novo);
            } else {
                // Atualizar existente
                clienteEmEdicao.setNome(nome);
                clienteEmEdicao.setTelefone(telefone);
                clienteEmEdicao.setEmail(email);
                tabela.refresh();
                clienteEmEdicao = null;
                btnCancelar.setDisable(true);
            }

            limparFormulario();
            salvarDados();
        });

        btnCancelar.setOnAction(e -> {
            clienteEmEdicao = null;
            limparFormulario();
            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione um cliente para editar.");
                return;
            }
            clienteEmEdicao = selecionado;
            campoNome.setText(selecionado.getNome());
            campoTelefone.setText(selecionado.getTelefone());
            campoEmail.setText(selecionado.getEmail());
            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione um cliente para deletar.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Deletar \"" + selecionado.getNome() + "\"?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    clientes.remove(selecionado);
                    salvarDados();
                }
            });
        });
    }
    private boolean validarCampos() {
        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email = campoEmail.getText().trim();

        if (nome.isBlank()) {
            alerta("Informe o nome do cliente.");
            return false;
        }

        if (!telefone.matches("\\d+")) {
            alerta("Telefone deve conter apenas números.");
            return false;
        }

        if (!email.contains("@")) {
            alerta("E-mail inválido.");
            return false;
        }

        return true;
    }

    private void limparFormulario() {
        campoNome.clear();
        campoTelefone.clear();
        campoEmail.clear();
    }

    private void salvarDados() {
        ArrayList<Object> dados = new ArrayList<>(clientes);
        RepositoryManager.salvar(ARQUIVO, dados);
    }

    private void carregarDados() {
        ArrayList<Object> dados = RepositoryManager.carregar(ARQUIVO);
        for (Object obj : dados) {
            if (obj instanceof Cliente) {
                clientes.add((Cliente) obj);
            }
        }
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait();
    }

}
