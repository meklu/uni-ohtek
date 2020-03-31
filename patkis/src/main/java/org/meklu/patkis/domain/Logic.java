
package org.meklu.patkis.domain;

import org.meklu.patkis.dao.*;

public class Logic {
    private UserDao ud;
    private SnippetDao sd;
    private TagDao td;

    private User currentUser;

    public Logic(UserDao ud, SnippetDao sd, TagDao td) {
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
}
