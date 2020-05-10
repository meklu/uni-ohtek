
package org.meklu.patkis.ui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.meklu.patkis.domain.Logic;

import javafx.stage.Stage;
import org.meklu.patkis.domain.Snippet;

public class ListSnippets implements View {
    private final Stage stage = new Stage();
    private final ObservableList<Snippet> snippets;
    private final Logic logic;

    @Override
    public Stage getStage() {
        return stage;
    }

    public void refreshSnippets() {
        this.snippets.clear();
        this.snippets.addAll(this.logic.getAvailableSnippets());
    }

    ListSnippets(PatkisUi ui) {
        logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 512.0, 512.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Snippets - Pätkis");

        TableView table = new TableView();
        TableColumn title = new TableColumn("Title");
        TableColumn desc = new TableColumn("Description");
        TableColumn code = new TableColumn("Code");
        table.getColumns().addAll(title, desc, code);

        this.snippets = FXCollections.observableArrayList(
            new ArrayList<>()
        );

        title.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("title")
        );

        desc.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("description")
        );

        code.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("snippet")
        );

        table.setItems(this.snippets);

        Button logout = new Button("Log out");
        logout.setOnAction((_ignore) -> {
            logic.logout();
            this.refreshSnippets();
            ui.toLoginScreen();
        });

        layout.getChildren().addAll(logout, table);

        layout.setMinWidth(layout.getPrefWidth());
        layout.setMinHeight(layout.getPrefHeight());

        table.autosize();
    }
}
