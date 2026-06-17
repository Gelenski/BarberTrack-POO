package org.barbertrack.barbertrack_poo.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.barbertrack.barbertrack_poo.model.CategoriaServico;

public class CategoriaDetalhesModal {

    public void show(Stage stage, CategoriaServico categoria) {

        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Detalhes da Categoria");

        Label labelNome = new Label("Nome:");
        Label valorNome = new Label(categoria.getNome());

        Label labelDescricao = new Label("Descrição:");
        Label valorDescricao = new Label(categoria.getDescricao());
        valorDescricao.setWrapText(true);
        valorDescricao.setMaxWidth(260);

        Label labelStatus = new Label("Status:");
        Label valorStatus = new Label(categoria.isStatus() ? "Ativo" : "Inativo");

        Button btnFechar = new Button("Fechar");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(labelNome, 0, 0);
        form.add(valorNome, 1, 0);

        form.add(labelDescricao, 0, 1);
        form.add(valorDescricao, 1, 1);

        form.add(labelStatus, 0, 2);
        form.add(valorStatus, 1, 2);

        HBox botoes = new HBox(8, btnFechar);
        form.add(botoes, 1, 3);

        btnFechar.setOnAction(e -> modal.close());

        Scene scene = new Scene(form, 420, 220);
        modal.setScene(scene);
        modal.showAndWait();
    }
}