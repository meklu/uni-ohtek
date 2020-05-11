
package org.meklu.patkis.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.meklu.patkis.domain.Logic;

import javafx.stage.Stage;
import javafx.util.Callback;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;

public class ListSnippets implements View {
    private final Stage stage = new Stage();
    private final ObservableList<Snippet> snippets;
    private final ObservableSet<Tag> tags;
    // pairs of (foreground, background)
    private final Map<Tag, Pair<Color, Color>> tagColors;
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
        this.generateTagColors();
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

    private void deleteTableSnippet(TableView table) {
        try {
            int row = table.getFocusModel().getFocusedCell().getRow();
            this.clearFormElements();
            this.deleteSnippet(this.snippets.get(row));
        } catch (Exception e) {
            System.out.println("failed to delete snippet, possibly no focus");
        }
    }

    private void logout(PatkisUi ui) {
        logic.logout();
        this.clearFormElements();
        this.refreshData();
        ui.toLoginScreen();
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

    private void deleteSnippet(Snippet s) {
        if (!s.getOwner().equals(logic.getCurrentUser())) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.initOwner(stage);
            a.setTitle("Can't delete snippet!");
            a.setContentText(a.getTitle() + " You are not the creator of this snippet.");
            a.showAndWait();
            return;
        }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initOwner(stage);
        a.setTitle("Delete snippet");
        a.setContentText("Are you sure you want to delete this?");
        a.showAndWait();
        if (a.getResult() != ButtonType.OK) {
            return;
        }
        this.logic.deleteSnippet(s);
        this.refreshData();
    }

    private void generateTagColors() {
        this.tagColors.clear();
        Color fg, bg;
        for (Tag t : this.tags) {
            double hueShift = 0.0;
            double saturationFactor = 0.6;
            double brightnessFactor = 1.0;
            double opacityFactor = 1.0;
            bg = this.stringHashToColor(t.getTag());
            if (Math.abs(bg.getBrightness() - 0.5) < 0.3) {
                brightnessFactor *= bg.getBrightness() > 0.5 ? 1.6 : 1.0/1.6;
            }
            bg = bg.deriveColor(
                hueShift,
                saturationFactor,
                brightnessFactor,
                opacityFactor
            );
            fg = bg.getBrightness() > 0.5 ? Color.BLACK : Color.WHITE;
            this.tagColors.put(t, new Pair<>(fg, bg));
        }
    }

    // For autogenerating colors for tags
    private Color stringHashToColor(String str) {
        // we'll just disregard the top 2 bits
        int hash = str.hashCode();
        int r =  hash        & ((1 << 10) - 1);
        int g = (hash >> 10) & ((1 << 10) - 1);
        int b = (hash >> 20) & ((1 << 10) - 1);
        double max = Math.max(Math.max(r, g), Math.max(b, 1));
        return Color.color(r/max, g/max, b/max);
    }

    private String colorToString(Color c) {
        return "rgb(" + c.getRed()*255 + "," + c.getGreen()*255 + "," + c.getBlue()*255 + ")";
    }

    ListSnippets(PatkisUi ui) {
        logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 512.0, 512.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Snippets - Pätkis");

        KeyCodeCombination quitKCC = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        layout.setOnKeyPressed(e -> {
            if (quitKCC.match(e)) {
                this.logout(ui);
            }
        });

        Font monoFont = Font.font("monospace");
        Font sansFont = Font.font("sans-serif");

        TableView table = new TableView();
        TableColumn tagCol = new TableColumn("Tags");
        tagCol.setSortable(false);
        TableColumn titleCol = new TableColumn("Title");
        TableColumn descCol = new TableColumn("Description");
        TableColumn codeCol = new TableColumn("Code");
        table.getColumns().addAll(tagCol, titleCol, descCol, codeCol);

        this.snippets = FXCollections.observableArrayList(
            new ArrayList<>()
        );
        this.tags = FXCollections.observableSet(
            new HashSet<>()
        );
        this.tagColors = new HashMap<>();

        tagCol.setCellValueFactory(
            new PropertyValueFactory<Snippet, Set<Tag>>("tags")
        );
        tagCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Snippet, Set<Tag>>() {
                    @Override
                    public void updateItem(Set<Tag> item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setFont(monoFont);
                        if (empty || item == null) {
                            this.setText(null);
                            this.setGraphic(null);
                            return;
                        }
                        HBox hbox = new HBox();
                        hbox.setAlignment(Pos.CENTER_RIGHT);
                        hbox.setSpacing(3);
                        Label tl;
                        for (Tag t : item) {
                            tl = new Label(t.getTag());
                            Pair<Color, Color> colors =  tagColors.get(t);
                            tl.setStyle(
                                "-fx-text-fill: " + colorToString(colors.getA()) + ";" +
                                "-fx-border-color: " + colorToString(colors.getA()) + ";" +
                                "-fx-background-color: " + colorToString(colors.getB()) + ";" +
                                "-fx-padding: 0 2;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-family: sans-serif;" +
                                "-fx-background-radius: 6;" +
                                "-fx-border-radius: 5;"
                            );
                            hbox.getChildren().add(tl);
                        }
                        this.setGraphic(hbox);
                        this.setAlignment(Pos.CENTER_RIGHT);
                    }
                };
            }
        });

        titleCol.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("title")
        );

        descCol.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("description")
        );

        codeCol.setCellValueFactory(
            new PropertyValueFactory<Snippet, String>("snippet")
        );
        codeCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Snippet, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setFont(monoFont);
                        if (item == null) {
                            this.setText(null);
                            return;
                        }
                        // strip extra lines if necessary, looking for both LF and CR
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
            this.logout(ui);
        });

        Button copyBtn = new Button("Copy");
        copyBtn.setOnAction(eh -> {
            this.copyToClipboard(table, ui);
        });

        Button editBtn = new Button("View/Edit");
        editBtn.setOnAction(eh -> {
            this.editTableSnippet(table);
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(eh -> {
            this.deleteTableSnippet(table);
        });

        Region menuPad = new Region();
        HBox.setHgrow(menuPad, Priority.ALWAYS);

        menubar.getChildren().addAll(copyBtn, editBtn, deleteBtn, menuPad, logout);
        menubar.autosize();

        ContextMenu ctxmenu = new ContextMenu();
        table.setContextMenu(ctxmenu);

        MenuItem copyCtx = new MenuItem("Copy to clipboard");
        copyCtx.setOnAction(copyBtn.getOnAction());
        ctxmenu.getItems().addAll(copyCtx);

        MenuItem editCtx = new MenuItem("View/edit snippet");
        editCtx.setOnAction(editBtn.getOnAction());
        ctxmenu.getItems().addAll(editCtx);

        MenuItem deleteCtx = new MenuItem("Delete snippet");
        editCtx.setOnAction(deleteBtn.getOnAction());
        ctxmenu.getItems().addAll(deleteCtx);

        KeyCodeCombination copyKCC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        table.setOnKeyPressed(e -> {
            if (copyKCC.match(e)) {
                this.copyToClipboard(table, ui);
            } else if (e.getCode() == KeyCode.ENTER) {
                this.editTableSnippet(table);
            } else if (e.getCode() == KeyCode.DELETE) {
                this.deleteTableSnippet(table);
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
