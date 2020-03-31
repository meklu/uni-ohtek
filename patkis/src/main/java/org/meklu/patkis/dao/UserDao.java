
package org.meklu.patkis.dao;

import java.util.List;
import org.meklu.patkis.domain.User;

public interface UserDao {
    User create(User user) throws Exception;
    User findByLogin(String login);
    List<User> getAll() throws Exception;
}
