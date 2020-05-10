
package org.meklu.patkis.ui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.meklu.patkis.domain.Logic;

import javafx.stage.Stage;
import javafx.util.Callback;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;

public class ListSnippets implements View {
    private final Stage stage = new Stage();
    private final ObservableList<Snippet> snippets;
    private final ObservableList<Tag> tags;
    private final Logic logic;

    private TextField addTitle;
    private TextField addDescription;
    private TextArea addSnippet;
    private CheckBox addPublic;
    private Button addBtn;
    private Button cancelBtn;
    private Snippet editSnippet = null;
    private boolean allowEdit = true;

    private final String addStr = "Add snippet";
    private final String updateStr = "Update snippet";
    private final String cancelStr = "Cancel edit";
    private final String closeStr = "Close snippet";

    @Override
    public Stage getStage() {
        return stage;
    }

    public void refreshData() {
        this.refreshSnippets();
        this.refreshTags();
    }

    public void refreshSnippets() {
        this.snippets.clear();
        this.snippets.addAll(this.logic.getAvailableSnippets());
    }

    public void refreshTags() {
        this.tags.clear();
        this.tags.addAll(this.logic.getAvailableTags());
    }

    private void copyToClipboard(TableView table, PatkisUi ui) {
        try {
            int row = table.getFocusModel().getFocusedCell().getRow();
            ui.copyToClipboard(this.snippets.get(row).getSnippet());
        } catch (Exception e) {
            System.out.println("failed to copy item to clipboard, possibly no focus");
        }
    }

    private void editTableSnippet(TableView table) {
        try {
            int row = table.getFocusModel().getFocusedCell().getRow();
            this.clearFormElements();
            this.startEdit(this.snippets.get(row));
        } catch (Exception e) {
            System.out.println("failed to start snippet edit, possibly no focus");
        }
    }

    private void saveOrUpdateSnippet() {
        if (!this.allowEdit) {
            return;
        }
        Snippet s;
        if (this.editSnippet != null) {
            s = this.editSnippet;
        } else {
            s = new Snippet(logic.getCurrentUser());
        }
        s.setTitle(addTitle.getText());
        s.setDescription(addDescription.getText());
        s.setSnippet(addSnippet.getText());
        s.setPublic(addPublic.isSelected());
        boolean succ;
        if (s.getId() == -1) {
            succ = logic.createSnippet(s);
        } else {
            succ = logic.updateSnippet(s);
        }
        if (succ) {
            this.clearFormElements();
        }
        this.refreshData();
    }

    private void setAllowEditable() {
        addTitle.setEditable(this.allowEdit);
        addDescription.setEditable(this.allowEdit);
        addSnippet.setEditable(this.allowEdit);
        addPublic.setDisable(!this.allowEdit);
        addBtn.setVisible(allowEdit);
        addBtn.setManaged(allowEdit);
        if (!allowEdit) {
            cancelBtn.setVisible(true);
            cancelBtn.setManaged(true);
            cancelBtn.setText(closeStr);
        } else {
            cancelBtn.setText(cancelStr);
        }
    }

    private void clearFormElements() {
        addTitle.clear();
        addDescription.clear();
        addSnippet.clear();
        addPublic.setSelected(false);
        addBtn.setText(addStr);
        this.editSnippet = null;
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
        addTitle.requestFocus();
        this.allowEdit = true;
        this.setAllowEditable();
    }

    private void startEdit(Snippet s) {
        if (s == null) {
            return;
        }
        if (!s.getOwner().equals(this.logic.getCurrentUser())) {
            this.allowEdit = false;
        }
        this.setAllowEditable();
        this.editSnippet = s;
        addTitle.setText(s.getTitle());
        addDescription.setText(s.getDescription());
        addSnippet.setText(s.getSnippet());
        addPublic.setSelected(s.isPublic());
        addBtn.setText(updateStr);
        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
        addTitle.requestFocus();
    }

    ListSnippets(PatkisUi ui) {
        logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 512.0, 512.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Snippets - Pätkis");

        Font monoFont = Font.font("monospace");

        TableView table = new TableView();
        TableColumn title = new TableColumn("Title");
        TableColumn desc = new TableColumn("Description");
        TableColumn code = new TableColumn("Code");
        table.getColumns().addAll(title, desc, code);

        this.snippets = FXCollections.observableArrayList(
            new ArrayList<>()
        );
        this.tags = FXCollections.observableArrayList(
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
        code.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Snippet, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setFont(monoFont);
                        // strip extra lines if necessary, looking for both LF and CR
                        if (item == null) {
                            return;
                        }
                        int nlpos = item.indexOf('\n');
                        int crpos = item.indexOf('\r');
                        if (nlpos == -1) {
                            nlpos = crpos;
                        } else if (crpos != -1) { // neither is -1
                            nlpos = Math.min(nlpos, crpos);
                        }
                        if (nlpos == -1) {
                            this.setText(item);
                        } else {
                            this.setText(item.substring(0, nlpos) + "…");
                        }
                    }
                };
            }
        });

        table.setItems(this.snippets);

        HBox menubar = new HBox();

        Button logout = new Button("Log out");
        logout.setOnAction((_ignore) -> {
            logic.logout();
            this.clearFormElements();
            this.refreshData();
            ui.toLoginScreen();
        });

        Button copyBtn = new Button("Copy");
        copyBtn.setOnAction(eh -> {
            this.copyToClipboard(table, ui);
        });

        Button editBtn = new Button("View/Edit");
        copyBtn.setOnAction(eh -> {
            this.editTableSnippet(table);
        });

        Region menuPad = new Region();
        HBox.setHgrow(menuPad, Priority.ALWAYS);

        menubar.getChildren().addAll(copyBtn, editBtn, menuPad, logout);
        menubar.autosize();

        ContextMenu ctxmenu = new ContextMenu();
        table.setContextMenu(ctxmenu);

        MenuItem copyCtx = new MenuItem("Copy to clipboard");
        copyCtx.setOnAction(copyBtn.getOnAction());
        ctxmenu.getItems().addAll(copyCtx);

        MenuItem editCtx = new MenuItem("View/edit snippet");
        editCtx.setOnAction(editBtn.getOnAction());
        ctxmenu.getItems().addAll(editCtx);

        KeyCodeCombination copyKCC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        table.setOnKeyPressed(e -> {
            if (copyKCC.match(e)) {
                this.copyToClipboard(table, ui);
            } else if (e.getCode() == KeyCode.ENTER) {
                this.editTableSnippet(table);
            }
        });
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                this.editTableSnippet(table);
            }
        });

        VBox adder = new VBox();
        addTitle = new TextField();
        addTitle.setPromptText("Title");
        addTitle.setFocusTraversable(true);

        addDescription = new TextField();
        addDescription.setPromptText("Description");
        addDescription.setFocusTraversable(true);

        addSnippet = new TextArea();
        addSnippet.setPromptText("<code>");
        addSnippet.setFocusTraversable(true);
        addSnippet.setFont(monoFont);

        addPublic = new CheckBox();
        addPublic.setSelected(false);
        addPublic.setFocusTraversable(true);
        Label lblAddPublic = new Label("Public snippet");

        addBtn = new Button(this.addStr);
        addBtn.setOnAction(e -> {
            this.saveOrUpdateSnippet();
        });

        cancelBtn = new Button(this.cancelStr);
        cancelBtn.setCancelButton(true);
        cancelBtn.setOnAction(e -> {
            this.clearFormElements();
        });

        addTitle.setOnAction(e -> {
            addDescription.requestFocus();
        });
        addDescription.setOnAction(e -> {
            addSnippet.requestFocus();
        });

        KeyCodeCombination saveKCC = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCodeCombination cancelKCC = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
        adder.setOnKeyPressed(e -> {
            if (saveKCC.match(e)) {
                this.saveOrUpdateSnippet();
            } else if (cancelKCC.match(e)) {
                this.clearFormElements();
            }
        });

        HBox adderFoot = new HBox();
        adderFoot.getChildren().addAll(addBtn, cancelBtn, lblAddPublic, addPublic);
        adderFoot.setSpacing(6);
        adderFoot.setAlignment(Pos.CENTER_LEFT);

        adder.getChildren().addAll(addTitle, addDescription, addSnippet, adderFoot);

        layout.getChildren().addAll(menubar, table, adder);

        layout.setMinWidth(layout.getPrefWidth());
        layout.setMinHeight(layout.getPrefHeight());

        this.clearFormElements();

        table.autosize();
        table.requestFocus();
    }
}
