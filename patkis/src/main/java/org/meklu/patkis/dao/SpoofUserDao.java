
package org.meklu.patkis.dao;

import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.User;

public class SpoofUserDao implements UserDao {
    List<User> users = new ArrayList<>();

    @Override
    public User create(User user) {
        if (users.contains(user)) {
            return user;
        }
        users.add(user);
        return user;
    }

    @Override
    public User findByLogin(String login) {
        for (User u : users) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return users;
    }
}
