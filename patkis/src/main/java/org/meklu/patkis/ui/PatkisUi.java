
package org.meklu.patkis.ui;

import org.meklu.patkis.dao.*;
import org.meklu.patkis.domain.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class PatkisUi extends Application {
    private Stage stage;
    private Database database;

    protected LoginScreen loginScreen;
    protected RegisterUser registerUser;
    protected ListSnippets listSnippets;
    protected Logic logic;

    @Override
    public void start(Stage stage) throws Exception {
        database = new Database("patkisdb.db");
        logic = new Logic(database, new DBUserDao(database), new SpoofSnippetDao(), new SpoofTagDao());

        loginScreen = new LoginScreen(this);
        registerUser = new RegisterUser(this);
        listSnippets = new ListSnippets(this);

        this.setStage(loginScreen.stage);
    }

    public void setStage(Stage stage) {
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        if (this.stage != null) {
            this.stage.hide();
        }
        this.stage = stage;
    }
}
