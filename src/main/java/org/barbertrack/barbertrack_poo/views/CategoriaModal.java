package org.barbertrack.barbertrack_poo.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.model.CategoriaServico;

public class CategoriaModal {

    private CategoriaServico categoriaCriada;

    public CategoriaServico show(Stage stage) {
        categoriaCriada = null;

        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Nova Categoria");

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();
        campoNome.setPromptText("Ex: Barba");

        Label labelDescricao = new Label("Descrição:");
        TextField campoDescricao = new TextField();
        campoDescricao.setPromptText("Ex: Serviços relacionados à barba");

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));

        form.add(labelNome, 0, 0);
        form.add(campoNome, 1, 0);

        form.add(labelDescricao, 0, 1);
        form.add(campoDescricao, 1, 1);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 2);

        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String descricao = campoDescricao.getText().trim();

            if (nome.isEmpty()) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Informe o nome da categoria.",
                        ButtonType.OK
                ).showAndWait();
                return;
            }

            if (descricao.isEmpty()) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Informe a descrição da categoria.",
                        ButtonType.OK
                ).showAndWait();
                return;
            }

            categoriaCriada = new CategoriaServico(nome, descricao);
            modal.close();
        });

        btnCancelar.setOnAction(e -> modal.close());

        Scene scene = new Scene(form, 420, 180);
        modal.setScene(scene);
        modal.showAndWait();

        return categoriaCriada;
    }
}
