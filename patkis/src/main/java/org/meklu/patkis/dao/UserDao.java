
package org.meklu.patkis.dao;

import org.meklu.patkis.domain.User;

public interface UserDao extends Dao<User> {
    default User findByLogin(String login) {
        return this.find("login", login);
    }
}
