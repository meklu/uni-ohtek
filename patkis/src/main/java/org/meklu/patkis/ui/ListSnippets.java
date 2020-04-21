
package org.meklu.patkis.ui;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.meklu.patkis.domain.Logic;

import javafx.stage.Stage;

public class ListSnippets implements View {
    private Stage stage = new Stage();

    @Override
    public Stage getStage() {
        return stage;
    }

    ListSnippets(PatkisUi ui) {
        Logic logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 512.0, 512.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Snippets - Pätkis");

        TableView table = new TableView();
        TableColumn title = new TableColumn("Title");
        TableColumn desc = new TableColumn("Description");
        TableColumn code = new TableColumn("Code");
        table.getColumns().addAll(title, desc, code);

        layout.getChildren().addAll(table);

        layout.setMinWidth(layout.getPrefWidth());
        layout.setMinHeight(layout.getPrefHeight());

        table.autosize();
    }
}
