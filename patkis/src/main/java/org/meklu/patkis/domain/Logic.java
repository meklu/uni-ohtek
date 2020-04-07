
package org.meklu.patkis.domain;

import org.meklu.patkis.dao.*;

public class Logic {
    private Database db;

    private UserDao ud;
    private SnippetDao sd;
    private TagDao td;

    private User currentUser;

    public Logic(Database db, UserDao ud, SnippetDao sd, TagDao td) {
        this.db = db;
        this.ud = ud;
        this.sd = sd;
        this.td = td;
        this.currentUser = null;
    }

    public boolean login(String username) {
        User u = ud.findByLogin(username);
        if (u == null) {
            return false;
        }
        this.currentUser = u;
        return true;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean register(User user) {
        return this.ud.save(user);
    }

    public void resetDb() {
        this.db.reset();
    }
}
