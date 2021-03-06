
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Triple;

/** Serves as a base class for DB-backed Data Access Objects
 * with some useful default methods
 *
 * @param <T> The model type to implement this for
 */
abstract public class DBDao<T> implements Dao<T> {
    protected Database db;

    /** Constructs a DBDao
     *
     * @param db The Database object to use for this DBDao
     */
    public DBDao(Database db) {
        this.db = db;
    }

    /** Gets the backing database table name for the model
     *
     * @return The name of the database table for the model */
    abstract String tableName();

    @Override
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

    @Override
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

    @Override
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

    /** Returns an object based on a DB result
     *
     * @param rs The ResultSet to try and convert into an object of type T
     * @return An object of type T or null if conversion fails
     */
    abstract public T fromResultSet(ResultSet rs);
}
