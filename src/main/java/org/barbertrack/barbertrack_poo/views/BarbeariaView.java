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
import org.barbertrack.barbertrack_poo.model.Barbearia;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;


public class BarbeariaView extends Application {
    private static final String ARQUIVO = "data/barbearia.dat";

    private final ObservableList<Barbearia> barbearias = FXCollections.observableArrayList();
    private TableView<Barbearia> tabela;

    private TextField campoRazaoSocial;
    private TextField campoNomeFantasia;
    private TextField campoEmail;

    private Barbearia barbeariaEmEdicao = null;

    @Override
    public void start(Stage stage){
        stage.setTitle("Gerenciar Barbearias");

        Label labelRazaoSocial = new Label("Razão Social:");
        campoRazaoSocial = new TextField();

        Label labelNomeFantasia = new Label("Nome Fantasia:");
        campoNomeFantasia = new TextField();

        Label labelEmail = new Label("E-mail:");
        campoEmail = new TextField();

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setDisable(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(labelRazaoSocial, 0, 0);
        form.add(campoRazaoSocial, 1, 0);
        form.add(labelNomeFantasia, 0, 1);
        form.add(campoNomeFantasia, 1, 1);
        form.add(labelEmail, 0, 2);
        form.add(campoEmail, 1, 2);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(barbearias);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Barbearia, String> colRazao = new TableColumn<>("Razão Social");
        colRazao.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRazaoSocial()));

        TableColumn<Barbearia, String> colNome = new TableColumn<>("Nome Fantasia");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomeFantasia()));

        TableColumn<Barbearia, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        tabela.getColumns().addAll(colRazao, colNome, colEmail);

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

            String razaoSocial = campoRazaoSocial.getText().trim();
            String nomeFantasia = campoNomeFantasia.getText().trim();
            String email = campoEmail.getText().trim();

            try {
                if (barbeariaEmEdicao == null) {
                    Barbearia novo = new Barbearia(razaoSocial, nomeFantasia, email);
                    barbearias.add(novo);
                } else {
                    barbeariaEmEdicao.setRazaoSocial(razaoSocial);
                    barbeariaEmEdicao.setNomeFantasia(nomeFantasia);
                    barbeariaEmEdicao.setEmail(email);
                    tabela.refresh();
                    barbeariaEmEdicao = null;
                    btnCancelar.setDisable(true);
                }
                limparFormulario();
                salvarDados();
            } catch (Exception ex) {
                alerta(ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> {
            barbeariaEmEdicao = null;
            limparFormulario();
            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {
            Barbearia selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione uma barbearia para editar.");
                return;
            }
            barbeariaEmEdicao = selecionado;
            campoRazaoSocial.setText(selecionado.getRazaoSocial());
            campoNomeFantasia.setText(selecionado.getNomeFantasia());
            campoEmail.setText(selecionado.getEmail());
            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {
            Barbearia selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                alerta("Selecione uma barbearia para deletar.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Deletar \"" + selecionado.getRazaoSocial() + "\"?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    barbearias.remove(selecionado);
                    salvarDados();
                }
            });
        });
    }
    private boolean validarCampos() {
        String razaoSocial = campoRazaoSocial.getText().trim();
        String nomeFantasia = campoNomeFantasia.getText().trim();
        String email = campoEmail.getText().trim();

        if (razaoSocial.isBlank()) {
            alerta("Informe a razão social da barbearia.");
            return false;
        }

        if (nomeFantasia.isBlank()) {
            alerta("Informe o nome fantasia da barbearia.");
            return false;
        }

        if (!email.contains("@")) {
            alerta("E-mail inválido.");
            return false;
        }

        return true;
    }

    private void limparFormulario() {
        campoRazaoSocial.clear();
        campoNomeFantasia.clear();
        campoEmail.clear();
    }

    private void salvarDados() {
        ArrayList<Object> dados = new ArrayList<>(barbearias);
        RepositoryManager.salvar(ARQUIVO, dados);
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        ArrayList<Object> dados = RepositoryManager.carregar(ARQUIVO);
        for (Object obj : dados) {
            if (obj instanceof Barbearia) {
                barbearias.add((Barbearia) obj);
            }
        }
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait();
    }

}
