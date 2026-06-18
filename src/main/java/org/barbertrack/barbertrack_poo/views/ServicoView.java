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

import org.barbertrack.barbertrack_poo.model.CategoriaServico;
import org.barbertrack.barbertrack_poo.model.Servico;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;

public class ServicoView extends Application {

    private static final String ARQUIVO = "data/servicos.dat";
    private static final String CATEGORIA_ARQUIVO = "data/categorias.dat";

    private final ObservableList<Servico> servicos = FXCollections.observableArrayList();

    // categoria: somente as categorias ATIVAS
    private final ObservableList<CategoriaServico> categoria = FXCollections.observableArrayList();

    // categoria: todas as categorias
    private final ObservableList<CategoriaServico> todasCategorias = FXCollections.observableArrayList();

    private TableView<Servico> tabela;

    private TextField campoNome;
    private TextField campoDuracao;
    private CheckBox checkStatus;
    private ComboBox<CategoriaServico> campoCategoria;

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

        campoCategoria = new ComboBox<>();
        campoCategoria.setItems(categoria);
        campoCategoria.setPromptText("Selecione uma categoria");

        Button btnEditarCategoria = new Button("Editar Categoria");
        Button btnAddCategoria = new Button("Adicionar Categoria");
        Button btnRemoverCategoria = new Button("Remover Categoria");
        Button btnDetalhesCategoria = new Button("Ver Detalhes da Categoria");

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
        form.add(campoCategoria, 2, 0);
        form.add(btnAddCategoria, 2, 1);
        form.add(btnEditarCategoria, 2, 2);
        form.add(btnRemoverCategoria, 2, 3);
        form.add(btnDetalhesCategoria, 2, 4);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();
        tabela.setItems(servicos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Servico, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Servico, String> colDuracao = new TableColumn<>("Duração (min)");
        colDuracao.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getDuracao())));

        TableColumn<Servico, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isStatus() ? "Ativo" : "Inativo"));

        TableColumn<Servico, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(c -> {
            CategoriaServico categoriaServico = c.getValue().getCategoriaServico();

            if (categoriaServico == null) {
                return new SimpleStringProperty("Sem categoria");
            }

            return new SimpleStringProperty(categoriaServico.getNome());
        });

        tabela.getColumns().addAll(colNome, colDuracao, colStatus, colCategoria);

        Button btnEditar = new Button("Editar selecionado");
        Button btnDeletar = new Button("Deletar selecionado");

        HBox acoesTabela = new HBox(8, btnEditar, btnDeletar);
        acoesTabela.setPadding(new Insets(4, 0, 0, 0));

        VBox root = new VBox(12, form, new Separator(), tabela, acoesTabela);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 560, 480);
        stage.setScene(scene);
        stage.show();

        carregarCategorias();
        carregarServicos();

        btnSalvar.setOnAction(e -> {
            if (!validarCampos()) return;

            String nome = campoNome.getText().trim();
            int duracao = Integer.parseInt(campoDuracao.getText().trim());
            CategoriaServico categoria = campoCategoria.getSelectionModel().getSelectedItem();
            boolean status = checkStatus.isSelected();

            try {
                if (servicoEmEdicao == null) {
                    Servico novo = new Servico(nome, duracao, categoria);
                    novo.setStatus(status);
                    servicos.add(novo);
                } else {
                    servicoEmEdicao.setNome(nome);
                    servicoEmEdicao.setDuracao(duracao);
                    servicoEmEdicao.setStatus(status);
                    servicoEmEdicao.setCategoriaServico(categoria);
                    tabela.refresh();
                    servicoEmEdicao = null;
                    btnCancelar.setDisable(true);
                }
                limparFormulario();
                salvarDados();
            } catch (Exception ex) {
                alerta(ex.getMessage());
            }
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
            campoCategoria.getSelectionModel().select(selecionado.getCategoriaServico());
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

        btnAddCategoria.setOnAction(e -> {
            CategoriaServico novaCategoria = new CategoriaModal().show(stage);

            if (novaCategoria != null) {
                todasCategorias.add(novaCategoria);

                if (novaCategoria.isStatus()) {
                    categoria.add(novaCategoria);
                    campoCategoria.getSelectionModel().select(novaCategoria);
                }

                salvarCategorias();
            }
        });

        btnEditarCategoria.setOnAction(e -> {
            CategoriaServico categoriaSelecionada = campoCategoria.getSelectionModel().getSelectedItem();

            if (categoriaSelecionada == null) {
                alerta("Selecione uma categoria para editar.");
                return;
            }

            new CategoriaEditarModal().show(stage, categoriaSelecionada);

            for (Servico s : servicos) {
                if (s.getCategoriaServico() != null
                        && s.getCategoriaServico().equals(categoriaSelecionada)) {
                    s.setCategoriaServico(categoriaSelecionada);
                }
            }

            salvarCategorias();
            salvarDados();
            tabela.refresh();

            CategoriaServico selecionadaAtual = categoriaSelecionada;
            recarregarComboCategorias();
            if (selecionadaAtual.isStatus()) {
                campoCategoria.getSelectionModel().select(selecionadaAtual);
            } else {
                campoCategoria.getSelectionModel().clearSelection();
            }
        });

        btnRemoverCategoria.setOnAction(e -> {
            CategoriaServico categoriaSelecionada = campoCategoria.getSelectionModel().getSelectedItem();

            if (categoriaSelecionada == null) {
                alerta("Selecione uma categoria para remover.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Remover \"" + categoriaSelecionada.getNome() + "\"?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    categoria.remove(categoriaSelecionada);
                    todasCategorias.remove(categoriaSelecionada);
                    campoCategoria.getSelectionModel().clearSelection();
                    salvarCategorias();
                    salvarDados();
                    tabela.refresh();
                }
            });
        });

        btnDetalhesCategoria.setOnAction(e -> {
            CategoriaServico categoriaSelecionada = campoCategoria.getSelectionModel().getSelectedItem();

            if (categoriaSelecionada == null) {
                alerta("Selecione uma categoria para ver os detalhes.");
                return;
            }

            new CategoriaDetalhesModal().show(stage, categoriaSelecionada);
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

        if (campoCategoria.getSelectionModel().getSelectedItem() == null) {
            alerta("Selecione uma categoria para o serviço.");
            return false;
        }

        return true;
    }

    private void limparFormulario() {
        campoNome.clear();
        campoDuracao.clear();
        checkStatus.setSelected(true);
        campoCategoria.getSelectionModel().clearSelection();
    }

    private void salvarDados() {
        ArrayList<Object> dados = new ArrayList<>(servicos);
        RepositoryManager.salvar(ARQUIVO, dados);
    }

    private void salvarCategorias() {
        RepositoryManager.salvar(CATEGORIA_ARQUIVO, new ArrayList<>(todasCategorias));
    }

    private void carregarServicos() {
        ArrayList<Object> dados = RepositoryManager.carregar(ARQUIVO);
        for (Object obj : dados) {
            if (obj instanceof Servico) {
                servicos.add((Servico) obj);
            }
        }
    }

    private void carregarCategorias() {
        ArrayList<Object> dados = RepositoryManager.carregar(CATEGORIA_ARQUIVO);
        todasCategorias.clear();
        categoria.clear();

        for (Object obj : dados) {
            if (obj instanceof CategoriaServico categoriaServico) {
                todasCategorias.add(categoriaServico);
                if (categoriaServico.isStatus()) {
                    categoria.add(categoriaServico);
                }
            }
        }
    }

    private void recarregarComboCategorias() {
        categoria.clear();
        for (CategoriaServico c : todasCategorias) {
            if (c.isStatus()) {
                categoria.add(c);
            }
        }
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait();
    }
}