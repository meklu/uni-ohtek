
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.dao.DBSnippetDao;
import org.meklu.patkis.dao.DBUserDao;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.User;

public class DBSnippetDaoTest {
    private static Database db;
    private static User u;
    private static DBSnippetDao dsd;
    private static DBUserDao dud;

    @Before
    public void setItUp() throws SQLException {
        db = new Database("testdb.db");
        db.reset();
        dsd = new DBSnippetDao(db);
        dud = new DBUserDao(db);
        dsd.setUserDao(dud);
        u = new User("foodman");
        dud.save(u);
    }

    @After
    public void closeUpShop() throws SQLException {
        db.rollback();
        db.close();
    }

    @Test
    public void creationSucceeds() {
        Snippet s = new Snippet(u);
        assertEquals(true, dsd.save(s));
        assertNotNull(dsd.find("id", "" + s.getId()));
    }

    @Test
    public void deletionSucceeds() {
        Snippet s = new Snippet(u);
        assertEquals(true, dsd.save(s));
        assertEquals(true, dsd.delete(s));
        assertNull(dsd.find("id", "" + s.getId()));
    }

    @Test
    public void cannotDeleteUnsaved() {
        Snippet s = new Snippet(u);
        assertEquals(false, dsd.delete(s));
    }

    @Test
    public void canFindById() {
        Snippet s = new Snippet(u);
        dsd.save(s);
        Snippet c = dsd.findById(s.getId());
        assertEquals(true, s.equals(c));
    }

    @Test
    public void incompleteRowsFailToConvert() {
        ResultSet rs = db.find(dsd.tableName(), "asdf", "yup");
        assertEquals(null, dsd.fromResultSet(rs));
    }

    @Test
    public void updateSucceeds() {
        Snippet s = new Snippet(u);
        assertEquals(true, dsd.save(s));

        User u2 = new User("asdfa");
        assertEquals(true, dud.save(u2));

        s.setOwner(u2);
        s.setTitle("ayup");
        s.setDescription("foo");
        s.setPublic(true);
        s.setSnippet(":(){:|:&};:");

        assertEquals(true, dsd.update(s));
        assertTrue(s.equals(dsd.findById(s.getId())));
    }

    @Test
    public void rollingUpdateSucceeds() {
        Snippet s = new Snippet(u);
        assertEquals(true, dsd.save(s));

        User u2 = new User("asdfa");
        assertEquals(true, dud.save(u2));

        s.setOwner(u2);
        assertEquals(true, dsd.update(s));
        s.setTitle("ayup");
        assertEquals(true, dsd.update(s));
        s.setDescription("foo");
        assertEquals(true, dsd.update(s));
        s.setPublic(true);
        assertEquals(true, dsd.update(s));
        s.setSnippet(":(){:|:&};:");
        assertEquals(true, dsd.update(s));

        assertTrue(s.equals(dsd.findById(s.getId())));
    }

    @Test
    public void correctSnippetsAreAvailable() {
        assertEquals(true, dsd.getAvailableSnippets(null).isEmpty());

        Snippet s = new Snippet(u);
        assertEquals(true, dsd.save(s));
        Snippet s1 = new Snippet(u);
        assertEquals(true, dsd.save(s1));

        User u2 = new User("asdfa");
        assertEquals(true, dud.save(u2));
        Snippet s2 = new Snippet(u2);
        assertEquals(true, dsd.save(s2));

        List<Snippet> ss = dsd.getAvailableSnippets(u);
        assertEquals(2, ss.size());

        s2.setPublic(true);
        dsd.save(s2);
        ss = dsd.getAvailableSnippets(u);
        assertEquals(3, ss.size());
    }
}
