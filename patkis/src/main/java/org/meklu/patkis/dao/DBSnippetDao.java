
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Logic;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Triple;
import org.meklu.patkis.domain.User;

public class DBSnippetDao extends DBDao<Snippet> implements SnippetDao {
    private Logic logic = null;
    private DBUserDao userDao = null;
    private DBTagDao tagDao = null;

    public DBSnippetDao(Database db) {
        super(db);
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
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

    private boolean saveTags(Snippet s) {
        boolean ret = true;
        ret = ret && s.getUnlinkTags().stream().map(
            t -> tagDao.unlinkFromSnippet(t, s, logic.getCurrentUser())
        ).allMatch(succ -> succ);
        if (ret) {
            s.setUnlinkTags(new ArrayList<>());
        }
        ret = ret && s.getUserTags().stream().map(
            t -> tagDao.linkToSnippet(t, s, logic.getCurrentUser())
        ).allMatch(succ -> succ);
        return ret;
    }

    @Override
    public boolean save(Snippet s) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("title", s.getTitle()));
        fields.add(new Pair<>("description", s.getDescription()));
        fields.add(new Pair<>("snippet", s.getSnippet()));
        fields.add(new Pair<>("is_public", s.isPublic() ? "1" : "0"));
        fields.add(new Pair<>("owner_id", s.getOwner().getId() + ""));

        int id = db.saveReturningId(this.tableName(), fields);
        s.setId(id);
        boolean ret = id != -1;
        // update the tag list
        return ret && saveTags(s);
    }

    @Override
    public boolean update(Snippet s) {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>("title", s.getTitle()));
        fields.add(new Pair<>("description", s.getDescription()));
        fields.add(new Pair<>("snippet", s.getSnippet()));
        fields.add(new Pair<>("is_public", s.isPublic() ? "1" : "0"));
        fields.add(new Pair<>("owner_id", s.getOwner().getId() + ""));

        boolean ret = db.update(this.tableName(), "id", "" + s.getId(), fields);
        // update the tag list
        return ret && saveTags(s);
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
            User u = this.userDao.findById(rs.getInt("owner_id"));
            Snippet s = new Snippet(u);
            s.setId(rs.getInt("id"));
            s.setTitle(rs.getString("title"));
            s.setDescription(rs.getString("description"));
            s.setSnippet(rs.getString("snippet"));
            s.setPublic(rs.getBoolean("is_public"));
            this.refreshTags(s);
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

    private void refreshTags(Snippet s) {
        if (s.getId() == -1) {
            System.err.println("tried to refresh tags for an unsaved snippet");
            return;
        }
        s.setTags(this.tagDao.findBySnippet(s));
        s.setUserTags(this.tagDao.findBySnippet(s, this.logic.getCurrentUser()));
    }
}
