
package org.meklu.patkis.ui;

import org.meklu.patkis.dao.*;
import org.meklu.patkis.domain.*;

import javafx.application.Application;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
        DBUserDao ud = new DBUserDao(database);
        DBSnippetDao sd = new DBSnippetDao(database);
        sd.setUserDao(ud);
        DBTagDao td = new DBTagDao(database);
        sd.setTagDao(td);
        logic = new Logic(database, ud, sd, td);
        sd.setLogic(logic);

        loginScreen = new LoginScreen(this);
        registerUser = new RegisterUser(this);
        listSnippets = new ListSnippets(this);

        this.setStage(loginScreen.getStage());
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

    public void toLoginScreen() {
        this.setStage(loginScreen.getStage());
    }

    public void toRegistrationScreen() {
        this.setStage(registerUser.getStage());
    }

    public void toSnippets() {
        listSnippets.refreshSnippets();
        this.setStage(listSnippets.getStage());
    }

    public void copyToClipboard(String s) {
        Clipboard clippy = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(s);
        clippy.setContent(content);
    }
}
