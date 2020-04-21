
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.dao.DBUserDao;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.User;

public class DBUserDaoTest {
    private static Database db;
    private static DBUserDao dud;

    @Before
    public void setItUp() throws SQLException {
        db = new Database("testdb.db");
        dud = new DBUserDao(db);
        db.reset();
    }

    @After
    public void closeUpShop() throws SQLException {
        db.rollback();
        db.close();
    }

    @Test
    public void creationSucceeds() {
        User u = new User("foodman");
        assertEquals(true, dud.save(u));
        assertNotNull(dud.find("id", "" + u.getId()));
    }

    @Test
    public void deletionSucceeds() {
        User u = new User("foodman");
        assertEquals(true, dud.save(u));
        assertEquals(true, dud.delete(u));
        assertNull(dud.find("id", "" + u.getId()));
    }

    @Test
    public void cannotDeleteUnsaved() {
        User u = new User("foodman");
        assertEquals(false, dud.delete(u));
    }

    @Test
    public void canFindById() {
        User u = new User("foodman");
        dud.save(u);
        User c = dud.findById(u.getId());
        assertEquals(true, u.equals(c));
    }

    @Test
    public void canFindByLogin() {
        User u = new User("foodman");
        dud.save(u);
        User c = dud.findByLogin(u.getLogin());
        assertEquals(true, u.equals(c));
    }

    @Test
    public void incompleteRowsFailToConvert() {
        ResultSet rs = db.find(dud.tableName(), "asdf", "yup");
        assertEquals(null, dud.fromResultSet(rs));
    }
}
