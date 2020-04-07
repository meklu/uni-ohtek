
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Triple;

abstract public class DBDao<T> implements Dao<T> {
    protected Database db;

    public DBDao(Database db) {
        this.db = db;
    }

    public T find(String field, String value) {
        ResultSet rs = db.find(this.tableName(), field, value);
        try {
            if (!rs.next()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return this.fromResultSet(rs);
    }

    public List<T> findLike(String field, String pattern) {
        List<T> ret = new ArrayList<>();
        try {
            ResultSet rs = db.findLike(this.tableName(), field, pattern);
            while (rs != null && rs.next()) {
                T obj = fromResultSet(rs);
                if (null == obj) {
                    continue;
                }
                ret.add(obj);
            }
        } catch (Exception e) {}
        return ret;
    }

    public List<T> findWhere(List<Triple<String, String, String>> fields, List<String> additionalOrders) {
        List<T> ret = new ArrayList<>();
        try {
            ResultSet rs = db.findWhere(this.tableName(), fields, additionalOrders);
            while (rs != null && rs.next()) {
                T obj = fromResultSet(rs);
                if (null == obj) {
                    continue;
                }
                ret.add(obj);
            }
        } catch (Exception e) {}
        return ret;
    }
}
