
package org.meklu.patkis.domain;

import java.util.Objects;

/** Serves as the data model for users
 */
public class User {
    private int id;
    private String login;
    private String pass;

    /** Instantiates a User object
     *
     * @param login The login name to use
     */
    public User(String login) {
        this(-1, login, "");
    }

    /** Instantiates a User object
     *
     * @param id The id of this object in the database
     * @param login The login name
     * @param pass The password
     */
    public User(int id, String login, String pass) {
        this.id = id;
        this.login = login;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.login);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.login, other.login);
    }
}
