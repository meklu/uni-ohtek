
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
    public void setUp() {
        db.reset();
    }

    @BeforeClass
    public static void setItUp() throws SQLException {
        db = new Database("testdb.db");
        dud = new DBUserDao(db);
    }

    @AfterClass
    public static void closeUpShop() {
        db.reset();
        db.close();
    }

    @Test
    public void creationSucceeds() {
        User u = new User("foodman");
        assertEquals(true, dud.save(u));
        assertNotNull(dud.find("id", "" + u.getId()));
    }
}
