package org.barbertrack.barbertrack_poo.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.model.CategoriaServico;

public class CategoriaEditarModal {

    public CategoriaServico show(Stage stage, CategoriaServico categoria) {

        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Editar Categoria: " + categoria.getNome());

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();
        campoNome.setText(categoria.getNome());

        Label labelDescricao = new Label("Descrição:");
        TextField campoDescricao = new TextField();
        campoDescricao.setText(categoria.getDescricao());

        Label labelStatus = new Label("Ativo:");
        CheckBox checkStatus = new CheckBox();
        checkStatus.setSelected(categoria.isStatus());

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

        form.add(labelStatus, 0, 2);
        form.add(checkStatus, 1, 2);

        HBox botoes = new HBox(8, btnSalvar, btnCancelar);
        form.add(botoes, 1, 3);

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

            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            categoria.setStatus(checkStatus.isSelected());

            modal.close();
        });

        btnCancelar.setOnAction(e -> modal.close());

        Scene scene = new Scene(form, 420, 210);
        modal.setScene(scene);
        modal.showAndWait();

        return categoria;
    }
}