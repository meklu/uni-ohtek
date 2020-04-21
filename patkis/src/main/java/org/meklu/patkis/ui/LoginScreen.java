
package org.meklu.patkis.ui;

import org.meklu.patkis.domain.Logic;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class LoginScreen implements View {
    private Stage stage = new Stage();

    @Override
    public Stage getStage() {
        return stage;
    }

    LoginScreen(PatkisUi ui) {
        Logic logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 380.0, 380.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Log in");
        Label userLabel = new Label("Username");

        TextField userField = new TextField();
        Button loginButton = new Button("Log in");
        loginButton.setOnAction((_ignore) -> {
            if (!logic.login(userField.getText())) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.initOwner(stage);
                a.setTitle("Error!");
                a.setHeaderText("Error while logging in!");
                a.setContentText("Login failed for user " + userField.getText() + ".");
                a.showAndWait();
                return;
            }
            userField.clear();
            ui.toSnippets();
        });
        userField.setOnAction(loginButton.getOnAction());

        Button registerButton = new Button("Register an account");
        registerButton.setOnAction((_ignore) -> {
            ui.toRegistrationScreen();
        });

        Button resetButton = new Button("Reset the database");
        resetButton.setOnAction((_ignore) -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.initOwner(stage);
            a.setTitle("Reset the database");
            a.setContentText("Are you sure?");
            a.showAndWait();
            if (a.getResult() == ButtonType.OK) {
                logic.resetDb();
            }
        });

        layout.getChildren().addAll(userLabel, userField, loginButton, registerButton, resetButton);
    }
}
