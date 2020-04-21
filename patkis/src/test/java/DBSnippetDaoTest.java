
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
