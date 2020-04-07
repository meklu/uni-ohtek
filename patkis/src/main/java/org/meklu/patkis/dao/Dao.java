
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Triple;

public interface Dao<T> {
    /** @return The name of the database table for the Model */
    String tableName();
    /** Saves an object in the database
     * @return Whether the operation was successful or not */
    boolean save(T obj);
    /** Updates an object in the database
     * @return Whether the operation was successful or not */
    boolean update(T obj);
    /** Fetches an object by field
     * @return The found object or null */
    T find(
        String field,
        String value
    );
    /** Fetches objects by field, matching a pattern
     * @return The list of found object */
    List<T> findLike(
        String field,
        String pattern
    );
    /** Fetches objects by a list of (field, operator, value)
     * @return The list of found objects */
    List<T> findWhere(
        List<Triple<String, String, String>> fields,
        List<String> additionalOrders
    );
    /** Fetches objects by a list of (field, operator, value)
     * @return The list of found objects */
    default List<T> findWhere(
        List<Triple<String, String, String>> fields
    ) {
        return this.findWhere(fields, new ArrayList<>());
    }
    /** Fetches all objects
     * @return The list of found objects */
    default List<T> getAll() {
        return this.findWhere(new ArrayList<>());
    }
    /** Deletes an object from the database
     * @return Whether the operation was successful or not */
    boolean delete(T obj);
    /** Returns an object based on a DB result */
    T fromResultSet(ResultSet rs);
}
