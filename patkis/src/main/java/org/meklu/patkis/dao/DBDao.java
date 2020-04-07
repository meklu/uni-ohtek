
package org.meklu.patkis.dao;

import org.meklu.patkis.domain.Database;

abstract public class DBDao<T> implements Dao<T> {
    protected Database db;

    public DBDao(Database db) {
        this.db = db;
    }
}
