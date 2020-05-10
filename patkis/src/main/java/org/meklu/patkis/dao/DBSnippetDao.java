
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Triple;
import org.meklu.patkis.domain.User;

public class DBSnippetDao extends DBDao<Snippet> implements SnippetDao {
    private DBUserDao userDao = null;
    private DBTagDao tagDao = null;

    public DBSnippetDao(Database db) {
        super(db);
    }

    public void setUserDao(DBUserDao userDao) {
        this.userDao = userDao;
    }

    public void setTagDao(DBTagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public String tableName() {
        return "snippets";
    }

    @Override
    public boolean save(Snippet s) {
        //TODO: We'll want to save the tag list here as well at some point
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("title", s.getTitle()));
        fields.add(new Pair<>("description", s.getDescription()));
        fields.add(new Pair<>("snippet", s.getSnippet()));
        fields.add(new Pair<>("is_public", s.isPublic() ? "1" : "0"));
        fields.add(new Pair<>("owner_id", s.getOwner().getId() + ""));

        int id = db.saveReturningId(this.tableName(), fields);
        s.setId(id);
        return id != -1;
    }

    @Override
    public boolean update(Snippet s) {
        //TODO: We'll want to update the tag list here as well at some point
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("title", s.getTitle()));
        fields.add(new Pair<>("description", s.getDescription()));
        fields.add(new Pair<>("snippet", s.getSnippet()));
        fields.add(new Pair<>("is_public", s.isPublic() ? "1" : "0"));
        fields.add(new Pair<>("owner_id", s.getOwner().getId() + ""));

        return db.update(this.tableName(), "id", "" + s.getId(), fields);
    }

    @Override
    public boolean delete(Snippet s) {
        if (s.getId() == -1) {
            System.err.println("tried to delete object not yet saved in db");
            return false;
        }
        ArrayList<Pair<String, String>> l = new ArrayList<>();
        l.add(new Pair("id", "" + s.getId()));
        return db.delete(this.tableName(), l);
    }

    @Override
    public Snippet fromResultSet(ResultSet rs) {
        try {
            Snippet s = new Snippet(this.userDao.findById(rs.getInt("owner_id")));
            s.setId(rs.getInt("id"));
            s.setTitle(rs.getString("title"));
            s.setDescription(rs.getString("description"));
            s.setSnippet(rs.getString("snippet"));
            s.setPublic(rs.getBoolean("is_public"));
            return s;
        } catch (Exception e) {
            System.err.println("failed to convert resultset to snippet");
        }
        return null;
    }

    /** Finds a Snippet by id
     *
     * @param id The id of the snippet you want to find
     * @return The snippet, if any was found
     */
    public Snippet findById(int id) {
        return this.find("id", "" + id);
    }

    @Override
    public List<Snippet> getAvailableSnippets(User u) {
        if (null == u) {
            return new ArrayList<>();
        }
        List<Triple<String, String, String>> fields = new ArrayList<>();
        fields.add(new Triple<>("owner_id", "=", "" + u.getId()));

        List<String> addtl = new ArrayList<>();
        addtl.add("OR is_public = '1'");
        addtl.add("ORDER BY created DESC");

        return this.findWhere(fields, addtl);
    }
}
