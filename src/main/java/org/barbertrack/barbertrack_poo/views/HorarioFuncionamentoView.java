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
import org.barbertrack.barbertrack_poo.model.HorarioFuncionamento;
import org.barbertrack.barbertrack_poo.repository.RepositoryManager;

import java.util.ArrayList;

public class HorarioFuncionamentoView extends Application {

    private static final String ARQUIVO = "data/horario_funcionamento.dat";

    private final ObservableList<HorarioFuncionamento> horarioFuncionamentos =
            FXCollections.observableArrayList();

    private TableView<HorarioFuncionamento> tabela;

    private TextField campoDiaSemana;
    private TextField campoHorarioAbertura;
    private TextField campoHorarioFechamento;

    private HorarioFuncionamento horarioFuncionamentoEmEdicao = null;

    @Override
    public void start(Stage stage) {

        stage.setTitle("Gerenciar Horários de Funcionamento");

        Label labelDiaSemana = new Label("Dia da Semana:");
        campoDiaSemana = new TextField();

        Label labelHorarioAbertura = new Label("Horário Abertura:");
        campoHorarioAbertura = new TextField();
        campoHorarioAbertura.setPromptText("HH:mm");
        aplicarMascaraHorario(campoHorarioAbertura);

        Label labelHorarioFechamento = new Label("Horário Fechamento:");
        campoHorarioFechamento = new TextField();
        campoHorarioFechamento.setPromptText("HH:mm");
        aplicarMascaraHorario(campoHorarioFechamento);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.setDisable(true);

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));

        form.add(labelDiaSemana, 0, 0);
        form.add(campoDiaSemana, 1, 0);

        form.add(labelHorarioAbertura, 0, 1);
        form.add(campoHorarioAbertura, 1, 1);

        form.add(labelHorarioFechamento, 0, 2);
        form.add(campoHorarioFechamento, 1, 2);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

        tabela = new TableView<>();

        tabela.setItems(horarioFuncionamentos);
        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<HorarioFuncionamento, String> colDiaSemana =
                new TableColumn<>("Dia da Semana");

        colDiaSemana.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDiaSemana()));

        TableColumn<HorarioFuncionamento, String> colHorarioAbertura =
                new TableColumn<>("Horário de Abertura");

        colHorarioAbertura.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getHorarioAbertura()));

        TableColumn<HorarioFuncionamento, String> colHorarioFechamento =
                new TableColumn<>("Horário de Fechamento");

        colHorarioFechamento.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getHorarioFechamento()));

        tabela.getColumns().addAll(
                colDiaSemana,
                colHorarioAbertura,
                colHorarioFechamento
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

            String diaSemana =
                    campoDiaSemana.getText().trim();

            String horarioAbertura =
                    campoHorarioAbertura.getText().trim();

            String horarioFechamento =
                    campoHorarioFechamento.getText().trim();

            if (horarioFuncionamentoEmEdicao == null) {

                HorarioFuncionamento novo =
                        new HorarioFuncionamento(
                                diaSemana,
                                horarioAbertura,
                                horarioFechamento
                        );

                horarioFuncionamentos.add(novo);

            } else {

                horarioFuncionamentoEmEdicao.setDiaSemana(diaSemana);
                horarioFuncionamentoEmEdicao.setHorarioAbertura(horarioAbertura);
                horarioFuncionamentoEmEdicao.setHorarioFechamento(horarioFechamento);

                tabela.refresh();

                horarioFuncionamentoEmEdicao = null;

                btnCancelar.setDisable(true);
            }

            limparFormulario();

            salvarDados();
        });

        btnCancelar.setOnAction(e -> {

            horarioFuncionamentoEmEdicao = null;

            limparFormulario();

            btnCancelar.setDisable(true);
        });

        btnEditar.setOnAction(e -> {

            HorarioFuncionamento selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta(
                        "Selecione um horário de funcionamento para editar."
                );

                return;
            }

            horarioFuncionamentoEmEdicao = selecionado;

            campoDiaSemana.setText(
                    selecionado.getDiaSemana());

            campoHorarioAbertura.setText(
                    selecionado.getHorarioAbertura());

            campoHorarioFechamento.setText(
                    selecionado.getHorarioFechamento());

            btnCancelar.setDisable(false);
        });

        btnDeletar.setOnAction(e -> {

            HorarioFuncionamento selecionado =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                alerta(
                        "Selecione um horário de funcionamento para deletar."
                );

                return;
            }

            Alert confirm =
                    new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Deletar \"" +
                                    selecionado.getDiaSemana()
                                    + "\"?",
                            ButtonType.YES,
                            ButtonType.NO
                    );

            confirm.showAndWait()
                    .ifPresent(resp -> {

                        if (resp == ButtonType.YES) {

                            horarioFuncionamentos.remove(
                                    selecionado
                            );

                            salvarDados();
                        }
                    });
        });
    }

    private boolean validarCampos() {

        if (campoDiaSemana.getText().isBlank()) {
            alerta("Informe o dia da semana.");
            return false;
        }

        if (!validarHorario(campoHorarioAbertura.getText())) {
            alerta("Horário de abertura inválido. Use o formato HH:mm (ex: 08:00).");
            return false;
        }

        if (!validarHorario(campoHorarioFechamento.getText())) {
            alerta("Horário de fechamento inválido. Use o formato HH:mm (ex: 18:00).");
            return false;
        }

        if (!horarioFechamentoMaiorQueAbertura()) {
            alerta("O horário de fechamento deve ser maior que o de abertura.");
            return false;
        }

        return true;
    }

    private boolean validarHorario(String horario) {
        if (!horario.matches("\\d{2}:\\d{2}")) return false;
        String[] partes = horario.split(":");
        int hora = Integer.parseInt(partes[0]);
        int minuto = Integer.parseInt(partes[1]);
        return hora >= 0 && hora <= 23 && minuto >= 0 && minuto <= 59;
    }

    private boolean horarioFechamentoMaiorQueAbertura() {
        String[] abertura = campoHorarioAbertura.getText().split(":");
        String[] fechamento = campoHorarioFechamento.getText().split(":");
        int minutosAbertura = Integer.parseInt(abertura[0]) * 60 + Integer.parseInt(abertura[1]);
        int minutosFechamento = Integer.parseInt(fechamento[0]) * 60 + Integer.parseInt(fechamento[1]);
        return minutosFechamento > minutosAbertura;
    }

    private void aplicarMascaraHorario(TextField campo) {
        campo.textProperty().addListener((obs, oldValue, newValue) -> {
            String numeros = newValue.replaceAll("[^0-9]", "");

            if (numeros.length() > 4) {
                numeros = numeros.substring(0, 4);
            }

            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < numeros.length(); i++) {
                if (i == 2) texto.append(":");
                texto.append(numeros.charAt(i));
            }

            if (!texto.toString().equals(newValue)) {
                campo.setText(texto.toString());
                campo.positionCaret(texto.length());
            }
        });
    }

    private void limparFormulario() {

        campoDiaSemana.clear();
        campoHorarioAbertura.clear();
        campoHorarioFechamento.clear();
    }

    private void salvarDados() {

        ArrayList<Object> dados =
                new ArrayList<>(horarioFuncionamentos);

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

            if (obj instanceof HorarioFuncionamento) {

                horarioFuncionamentos.add(
                        (HorarioFuncionamento) obj
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