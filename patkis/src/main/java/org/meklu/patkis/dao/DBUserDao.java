
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Triple;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User find(String field, String value) {
        ResultSet rs = db.find(this.tableName(), field, value);
        try {
            if (!rs.next()) { return null; }
        } catch (Exception e) {
            return null;
        }
        return this.fromResultSet(rs);
    }

    @Override
    public List<User> findLike(String field, String pattern) {
        List<User> ret = new ArrayList<>();
        try {
            ResultSet rs = db.findLike(this.tableName(), field, pattern);
            while (rs != null && rs.next()) {
                User u = fromResultSet(rs);
                if (null == u) { continue; }
                ret.add(u);
            }
        } catch (Exception e) {}
        return ret;
    }

    @Override
    public List<User> findWhere(List<Triple<String, String, String>> fields, List<String> additionalOrders) {
        List<User> ret = new ArrayList<>();
        try {
            ResultSet rs = db.findWhere(this.tableName(), fields, additionalOrders);
            while (rs != null && rs.next()) {
                User u = fromResultSet(rs);
                if (null == u) { continue; }
                ret.add(u);
            }
        } catch (Exception e) {}
        return ret;
    }

    @Override
    public boolean delete(User u) {
        if (u.getId() == -1) {
            System.err.println("tried to delete object not yet saved in db");
            return false;
        }
        ArrayList<Pair<String, String>> l = new ArrayList<>();
        l.add(new Pair("id", u.getId()));
        return db.delete(this.tableName(), l);
    }

    @Override
    public User fromResultSet(ResultSet rs) {
        try {
            return new User(rs.getInt("id"), rs.getString("login"), rs.getString("pass"));
        } catch (Exception e) { System.err.println("failed to convert resultset to user"); }
        return null;
    }
}
