
package org.meklu.patkis.dao;

import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Triple;

/** The basic Data Access Object template
 *
 * @param <T> The type of data model to deal with
 */
public interface Dao<T> {
    /** Saves an object in the database
     *
     * @param obj The object to be saved
     * @return Whether the operation was successful or not */
    boolean save(T obj);

    /** Updates an object in the database
     *
     * @param obj The object to be updated
     * @return Whether the operation was successful or not */
    boolean update(T obj);

    /** Fetches an object by field
     *
     * This means an SQL WHERE-clause of roughly field='value';
     *
     * @param field The field to search by
     * @param value The value that field should have
     * @return The found object or null */
    T find(
        String field,
        String value
    );

    /** Fetches objects by field, matching a pattern
     *
     * This means an SQL WHERE-clause of roughly field LIKE 'value';
     *
     * @param field The field to search by
     * @param pattern The pattern that field should match
     * @return The list of found object */
    List<T> findLike(
        String field,
        String pattern
    );

    /** Fetches objects by a list of (field, operator, value)
     *
     * Operator can e.g. be something like '=', 'LIKE', etc.
     *
     * @param fields The fields to search by
     * @param additionalOrders Additional SQL to be inserted at the tail end
     *                         of the query like ORDER BY/GROUP BY statements
     * @return The list of found objects */
    List<T> findWhere(
        List<Triple<String, String, String>> fields,
        List<String> additionalOrders
    );

    /** Fetches objects by a list of (field, operator, value)
     *
     * Operator can e.g. be something like '=', 'LIKE', etc.
     *
     * @param fields The fields to look for
     * @return The list of found objects */
    default List<T> findWhere(
        List<Triple<String, String, String>> fields
    ) {
        return this.findWhere(fields, new ArrayList<>());
    }

    /** Fetches all objects
     *
     * @return The list of found objects */
    default List<T> getAll() {
        return this.findWhere(new ArrayList<>());
    }

    /** Deletes an object from the database
     *
     * @param obj The object to delete
     * @return Whether the operation was successful or not */
    boolean delete(T obj);
}
