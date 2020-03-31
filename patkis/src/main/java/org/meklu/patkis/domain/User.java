
package org.meklu.patkis.domain;

public class User {
    private final String login;
    private String pass;

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
