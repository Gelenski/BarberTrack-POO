package org.barbertrack.barbertrack_poo.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CategoriaModal {
    public void show(Stage stage) {

        TextField campoNome;

        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Nova Categoria");

        Label labelNome = new Label("Nome:");
        campoNome = new TextField();
        campoNome.setPromptText("Ex: Barba");


        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(labelNome, 0, 0);
        form.add(campoNome, 1,0);


    }
}
