
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.meklu.patkis.dao.DBSnippetDao;
import org.meklu.patkis.dao.DBTagDao;
import org.meklu.patkis.dao.DBUserDao;
import org.meklu.patkis.domain.Database;
import org.meklu.patkis.domain.Logic;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;
import org.meklu.patkis.domain.User;

public class LogicTest {
    private static Database db;
    private static DBSnippetDao dsd;
    private static DBTagDao dtd;
    private static DBUserDao dud;
    private static Logic logic;
    private static User u;

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
        u = new User("fooman");
    }

    @After
    public void closeUpShop() throws SQLException {
        db.rollback();
        db.close();
    }

    @Test
    public void registrationWorks() {
        assertEquals(true, logic.register(u));
        assertEquals(true, u.equals(dud.findById(u.getId())));
    }

    @Test
    public void loginWorks() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));
    }

    @Test
    public void cannotLoginNonexistent() {
        assertEquals(false, logic.login(u.getLogin()));
        assertEquals(null, logic.getCurrentUser());
    }

    @Test
    public void logoutWorks() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));
        logic.logout();
        assertEquals(null, logic.getCurrentUser());
    }

    @Test
    public void loggedOutCannotCreateTags() {
        assertEquals(false, logic.createTag(new Tag("haskell")));
    }

    @Test
    public void loggedOutCannotCreateSnippets() {
        Snippet s = new Snippet(u);
        s.setSnippet("fool's errand");
        assertEquals(false, logic.createSnippet(s));
    }

    @Test
    public void loggedOutCannotDeleteSnippets() {
        assertEquals(true, logic.register(u));
        Snippet s = new Snippet(u);
        s.setSnippet("fool's errand");
        assertEquals(true, dsd.save(s));
        assertEquals(false, logic.deleteSnippet(s));
    }

    @Test
    public void loggedOutHasNoSnippetsAvailable() {
        assertEquals(0, logic.getAvailableSnippets().size());
    }

    @Test
    public void loggedOutHasNoTagsAvailable() {
        assertEquals(0, logic.getAvailableTags().size());
    }

    @Test
    public void userCanCreateTags() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        Tag t = new Tag("haskell");
        assertEquals(true, logic.createTag(t));
        assertEquals(true, t.equals(dtd.findById(t.getId())));
    }

    @Test
    public void userCanPseudoRecreateTags() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        Tag t = new Tag("haskell");
        assertEquals(true, logic.createTag(t));
        assertEquals(true, logic.createTag(t));
        assertEquals(true, t.equals(dtd.findById(t.getId())));
    }

    @Test
    public void userCanCreateSnippets() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        Tag t = new Tag("haskell");
        assertEquals(true, logic.createTag(t));
        assertEquals(true, t.equals(dtd.findById(t.getId())));

        // This should work even with null here
        Snippet s = new Snippet(null);
        s.setSnippet("fool's errand");
        assertEquals(true, logic.createSnippet(s));
        assertEquals(true, u.equals(s.getOwner()));
        s.addTag(t);
        assertEquals(true, logic.updateSnippet(s));
        assertEquals(true, s.equals(dsd.findById(s.getId())));
    }

    @Test
    public void userCanDeleteSnippets() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        // This should work even with null here
        Snippet s = new Snippet(null);
        s.setSnippet("fool's errand");
        assertEquals(true, logic.createSnippet(s));
        assertEquals(true, u.equals(s.getOwner()));
        assertEquals(true, logic.deleteSnippet(s));
        assertEquals(null, dsd.findById(s.getId()));
    }

    @Test
    public void userSeesTags() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        Tag t = new Tag("haskell");
        assertEquals(true, logic.createTag(t));
        assertEquals(1, logic.getAvailableTags().size());
    }

    @Test
    public void userSeesSnippets() {
        assertEquals(true, logic.register(u));
        assertEquals(true, logic.login(u.getLogin()));
        assertEquals(true, u.equals(logic.getCurrentUser()));

        Snippet s = new Snippet(u);
        s.setSnippet("fool's errand");
        assertEquals(true, logic.createSnippet(s));
        assertEquals(1, logic.getAvailableSnippets().size());
    }
}
