
package org.meklu.patkis.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.meklu.patkis.domain.Logic;
import org.meklu.patkis.domain.User;

public class RegisterUser implements View {
    private Stage stage = new Stage();

    @Override
    public Stage getStage() {
        return stage;
    }

    RegisterUser(PatkisUi ui) {
        Logic logic = ui.logic;
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 380.0, 380.0);
        this.stage.setScene(scene);
        this.stage.setTitle("Register - Pätkis");
        Label userLabel = new Label("Username");

        TextField userField = new TextField();
        Button registerButton = new Button("Register");
        registerButton.setOnAction((_ignore) -> {
            User u = new User(userField.getText());
            if (!logic.register(u)) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.initOwner(stage);
                a.setTitle("Error!");
                a.setHeaderText("Error registering user!");
                a.setContentText("Registration failed for user " + userField.getText() + ".");
                a.showAndWait();
                return;
            }
            userField.clear();
            ui.toLoginScreen();
        });
        userField.setOnAction(registerButton.getOnAction());

        Button backButton = new Button("Back to login screen");
        backButton.setOnAction((_ignore) -> {
            userField.clear();
            ui.toLoginScreen();
        });

        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(7);
        layout.setPadding(new Insets(7));
        layout.getChildren().addAll(userLabel, userField, registerButton, backButton);
    }
}
