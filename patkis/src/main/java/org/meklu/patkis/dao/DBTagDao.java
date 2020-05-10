
package org.meklu.patkis.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Pair;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;
import org.meklu.patkis.domain.Triple;
import org.meklu.patkis.domain.User;

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

    @Override
    public Set<Tag> findBySnippet(Snippet s) {
        return this.findBySnippet(s, null);
    }

    @Override
    public Set<Tag> findBySnippet(Snippet s, User u) {
        if (s.getId() == -1) {
            System.err.println("tried to look up tags for an unsaved snippet");
            return new HashSet<>();
        }
        List<Triple<String, String, String>> fields = new ArrayList<>();
        fields.add(new Triple("snippet_id", "=", "" + s.getId()));
        if (u != null) {
            fields.add(new Triple("tagger_id", "=", "" + u.getId()));
        }
        try {
            Set<Tag> ts = new HashSet<>();
            ResultSet rs = this.db.findWhere("tags_snippets", fields);
            // This is quite inefficient, a JOIN + DBTagDao.fromResultSet() loop
            // with a GROUP BY tag_id would be better
            while (rs.next()) {
                Tag t = this.findById(rs.getInt("tag_id"));
                if (t != null) {
                    ts.add(t);
                }
            }
            return ts;
        } catch (Exception e) {}
        return new HashSet<>();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public boolean linkToSnippet(Tag t, Snippet s, User u) {
        if (t.getId() == -1) {
            System.err.println("tried to link an unsaved tag to a snippet");
            return false;
        }
        if (s.getId() == -1) {
            System.err.println("tried to link a tag to an unsaved snippet");
            return false;
        }
        if (s.getId() == -1) {
            System.err.println("tried to link a tag for an unsaved user");
            return false;
        }
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair("tag_id", "" + t.getId()));
        fields.add(new Pair("snippet_id", "" + s.getId()));
        fields.add(new Pair("tagger_id", "" + u.getId()));
        // See if this relation already exists in the database first
        try {
            if (
                this.db.findWhere(
                    "tags_snippets",
                    fields.stream().map(
                        p -> new Triple<>(
                            p.getA(), "=", p.getB()
                        )
                    ).collect(Collectors.toList())
                ).next()
            ) {
                return true;
            }
        } catch (Exception e) {}
        return this.db.save("tags_snippets", fields);
    }

    @Override
    public boolean unlinkFromSnippet(Tag t, Snippet s, User u) {
        // since the effect of removing stuff for unsaved things is a noop,
        // we return true on those types of errors
        if (t.getId() == -1) {
            System.err.println("tried to unlink an unsaved tag from a snippet");
            return true;
        }
        if (s.getId() == -1) {
            System.err.println("tried to unlink a tag from an unsaved snippet");
            return true;
        }
        if (s.getId() == -1) {
            System.err.println("tried to unlink a tag for an unsaved user");
            return true;
        }
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair("tag_id", "" + t.getId()));
        fields.add(new Pair("snippet_id", "" + s.getId()));
        fields.add(new Pair("tagger_id", "" + u.getId()));
        return this.db.delete("tags_snippets", fields);
    }
}
