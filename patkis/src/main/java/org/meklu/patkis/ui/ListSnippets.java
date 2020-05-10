
package org.meklu.patkis.ui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
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

    private void copyToClipboard(TableView table, PatkisUi ui) {
        try {
            int row = table.getFocusModel().getFocusedCell().getRow();
            ui.copyToClipboard(this.snippets.get(row).getSnippet());
        } catch (Exception e) {
            System.out.println("failed to copy item to clipboard, possibly no focus");
        }
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

        HBox menubar = new HBox();

        Button logout = new Button("Log out");
        logout.setOnAction((_ignore) -> {
            logic.logout();
            this.refreshSnippets();
            ui.toLoginScreen();
        });

        Button copyBtn = new Button("Copy");
        copyBtn.setOnAction(eh -> {
            this.copyToClipboard(table, ui);
        });

        menubar.getChildren().addAll(copyBtn, logout);
        menubar.autosize();

        ContextMenu ctxmenu = new ContextMenu();
        table.setContextMenu(ctxmenu);

        MenuItem copyCtx = new MenuItem("Copy to clipboard");
        copyCtx.setOnAction(copyBtn.getOnAction());
        ctxmenu.getItems().addAll(copyCtx);

        KeyCodeCombination copyKCC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        table.setOnKeyPressed(e -> {
            if (copyKCC.match(e)) {
                this.copyToClipboard(table, ui);
            }
        });

        layout.getChildren().addAll(menubar, table);

        layout.setMinWidth(layout.getPrefWidth());
        layout.setMinHeight(layout.getPrefHeight());

        table.autosize();
    }
}
