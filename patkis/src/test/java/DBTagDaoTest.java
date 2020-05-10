
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.dao.DBTagDao;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Tag;

public class DBTagDaoTest {
    private static Database db;
    private static DBTagDao dtd;

    @Before
    public void setItUp() throws SQLException {
        db = new Database("testdb.db");
        dtd = new DBTagDao(db);
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
}
