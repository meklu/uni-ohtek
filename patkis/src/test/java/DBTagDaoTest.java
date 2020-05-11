
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.dao.DBSnippetDao;
import org.meklu.patkis.dao.DBTagDao;
import org.meklu.patkis.dao.DBUserDao;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Logic;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;
import org.meklu.patkis.domain.User;

public class DBTagDaoTest {
    private static Database db;
    private static DBSnippetDao dsd;
    private static DBTagDao dtd;
    private static DBUserDao dud;
    private static Logic logic;

    @Before
    public void setItUp() throws SQLException {
        db = new Database("testdb.db");
        dsd = new DBSnippetDao(db);
        dtd = new DBTagDao(db);
        dud = new DBUserDao(db);
        logic = new Logic(db, dud, dsd, dtd);
        dsd.setLogic(logic);
        dsd.setTagDao(dtd);
        dsd.setUserDao(dud);
        db.reset();
    }

    @After
    public void closeUpShop() throws SQLException {
        db.rollback();
        db.close();
    }

    @Test
    public void creationSucceeds() {
        Tag t = new Tag("haskell");
        assertEquals(true, dtd.save(t));
        assertNotNull(dtd.find("id", "" + t.getId()));
    }

    @Test
    public void deletionSucceeds() {
        Tag t = new Tag("haskell");
        assertEquals(true, dtd.save(t));
        assertEquals(true, dtd.delete(t));
        assertNull(dtd.find("id", "" + t.getId()));
    }

    @Test
    public void cannotDeleteUnsaved() {
        Tag t = new Tag("haskell");
        assertEquals(false, dtd.delete(t));
    }

    @Test
    public void canFindById() {
        Tag t = new Tag("haskell");
        dtd.save(t);
        Tag c = dtd.findById(t.getId());
        assertEquals(true, t.equals(c));
    }

    @Test
    public void canFindByName() {
        Tag t = new Tag("haskell");
        dtd.save(t);
        Tag c = dtd.findByTag(t.getTag());
        assertEquals(true, t.equals(c));
    }

    @Test
    public void incompleteRowsFailToConvert() {
        ResultSet rs = db.find(dtd.tableName(), "asdf", "yup");
        assertEquals(null, dtd.fromResultSet(rs));
    }

    @Test
    public void updateSucceeds() {
        Tag t = new Tag("haskell");
        assertEquals(true, dtd.save(t));
        t.setTag("lleksah");
        assertEquals(true, dtd.update(t));
        assertTrue(t.equals(dtd.findByTag("lleksah")));
    }

    @Test
    public void pruningSucceeds() {
        Tag t = new Tag("haskell");
        assertEquals(true, dtd.save(t));
        Tag t2 = new Tag("lleksah");
        assertEquals(true, dtd.save(t2));
        User u = new User("foodman");
        assertEquals(true, dud.save(u));
        logic.login(u.getLogin());
        Snippet s = new Snippet(u);
        s.addTag(t);
        assertEquals(true, dsd.save(s));
        assertEquals(true, dtd.pruneUnused());
        assertEquals(1, dtd.getAll().size());
        assertEquals(true, t.equals(dtd.findById(t.getId())));
    }
}
