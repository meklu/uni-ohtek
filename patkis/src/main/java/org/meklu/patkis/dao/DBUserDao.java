
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.User;

public class DBUserDao extends DBDao<User> implements UserDao {
    public DBUserDao(Database db) {
        super(db);
    }

    @Override
    public String tableName() {
        return "users";
    }

    @Override
    public boolean save(User u) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("login", u.getLogin()));
        fields.add(new Pair<>("pass", u.getPass()));

        int id = db.saveReturningId(this.tableName(), fields);
        u.setId(id);
        return id != -1;
    }

    @Override
    public boolean update(User u) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("login", u.getLogin()));
        fields.add(new Pair<>("pass", u.getPass()));

        return db.update(this.tableName(), "id", "" + u.getId(), fields);
    }

    @Override
    public boolean delete(User u) {
        if (u.getId() == -1) {
            System.err.println("tried to delete object not yet saved in db");
            return false;
        }
        ArrayList<Pair<String, String>> l = new ArrayList<>();
        l.add(new Pair("id", "" + u.getId()));
        return db.delete(this.tableName(), l);
    }

    @Override
    public User fromResultSet(ResultSet rs) {
        try {
            return new User(rs.getInt("id"), rs.getString("login"), rs.getString("pass"));
        } catch (Exception e) {
            System.err.println("failed to convert resultset to user");
        }
        return null;
    }

    /** Finds a User by id
     *
     * @param id
     * @return The user, if any was found
     */
    public User findById(int id) {
        return this.find("id", "" + id);
    }
}
