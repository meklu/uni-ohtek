
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Tag;

public class DBTagDao extends DBDao<Tag> implements TagDao {
    public DBTagDao(Database db) {
        super(db);
    }

    @Override
    public String tableName() {
        return "tags";
    }

    @Override
    public boolean save(Tag t) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("tag", t.getTag()));

        int id = db.saveReturningId(this.tableName(), fields);
        t.setId(id);
        return id != -1;
    }

    @Override
    public boolean update(Tag t) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("tag", t.getTag()));

        return db.update(this.tableName(), "id", "" + t.getId(), fields);
    }

    @Override
    public boolean delete(Tag t) {
        if (t.getId() == -1) {
            System.err.println("tried to delete object not yet saved in db");
            return false;
        }
        ArrayList<Pair<String, String>> l = new ArrayList<>();
        l.add(new Pair("id", "" + t.getId()));
        return db.delete(this.tableName(), l);
    }

    @Override
    public Tag fromResultSet(ResultSet rs) {
        try {
            Tag t = new Tag(rs.getString("tag"));
            t.setId(rs.getInt("id"));
            return t;
        } catch (Exception e) {
            System.err.println("failed to convert resultset to tag");
        }
        return null;
    }

    /** Finds a Tag by id
     *
     * @param id The id of the tag you want to find
     * @return The tag, if any was found
     */
    public Tag findById(int id) {
        return this.find("id", "" + id);
    }
}
