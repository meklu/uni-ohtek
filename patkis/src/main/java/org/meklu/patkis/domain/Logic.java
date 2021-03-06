
package org.meklu.patkis.domain;

import java.util.List;
import org.meklu.patkis.dao.*;

/** Handles application logic
 */
public class Logic {
    private Database db;

    private UserDao ud;
    private SnippetDao sd;
    private TagDao td;

    private User currentUser;

    /** Instantiates a Logic object
     *
     * @param db The Database to associate with this instance
     * @param ud The UserDao to use
     * @param sd The SnippetDao to use
     * @param td The TagDao to use
     */
    public Logic(Database db, UserDao ud, SnippetDao sd, TagDao td) {
        this.db = db;
        db.createTables();

        this.ud = ud;
        this.sd = sd;
        this.td = td;
        this.currentUser = null;
    }

    /** Gets the currently logged in user
     *
     * @return An instance of User or null if no one's logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /** Logs in a user by username
     *
     * @param username The username of the user to log in as
     * @return True if the user was successfully logged in, false otherwise
     */
    public boolean login(String username) {
        if (null != this.currentUser) {
            this.logout();
        }
        User u = ud.findByLogin(username);
        if (u == null) {
            return false;
        }
        this.currentUser = u;
        return true;
    }

    /** Logs out the currently logged in user
     */
    public void logout() {
        this.currentUser = null;
        // prune unused tags while we're at it
        this.td.pruneUnused();
    }

    /** Registers a user
     *
     * @param user A new User to register
     * @return True when the User object was saved successfully
     */
    public boolean register(User user) {
        return this.ud.save(user);
    }

    /** Resets the application database
     */
    public void resetDb() {
        this.db.reset();
    }

    /** Creates a Tag according to the given object
     *
     * @param t Tag to be created
     * @return Whether the creation succeeded
     */
    public boolean createTag(Tag t) {
        if (null == this.currentUser) {
            return false;
        }
        Tag ck = this.td.findByTag(t.getTag());
        if (ck != null) {
            t.setId(ck.getId());
            t.setTag(ck.getTag());
            return true;
        }
        return this.td.save(t);
    }

    /** Gets the Snippets visible to the current user
     *
     * @return The list of Snippets available
     */
    public List<Snippet> getAvailableSnippets() {
        if (null == this.currentUser) {
            return List.of();
        }
        return this.sd.getAvailableSnippets(this.currentUser);
    }

    /** Creates a Snippet according to the given object
     *
     * @param s Snippet to be created
     * @return Whether the creation succeeded
     */
    public boolean createSnippet(Snippet s) {
        if (null == this.currentUser) {
            return false;
        }
        s.setOwner(this.currentUser);
        return this.sd.save(s);
    }

    /** Deletes a Snippet according to the given object
     *
     * @param s Snippet to delete
     * @return Whether the deletion succeeded
     */
    public boolean deleteSnippet(Snippet s) {
        if (null == this.currentUser) {
            return false;
        }
        if (!this.currentUser.equals(s.getOwner())) {
            return false;
        }
        return this.sd.delete(s);
    }

    /** Updates a Snippet according to the given object
     *
     * @param s Snippet to update
     * @return Whether the update succeeded
     */
    public boolean updateSnippet(Snippet s) {
        if (null == this.currentUser) {
            return false;
        }
        if (!this.currentUser.equals(s.getOwner())) {
            return false;
        }
        return this.sd.update(s);
    }

    /** Gets the Tags visible to the current user
     *
     * @return The list of Tags available
     */
    public List<Tag> getAvailableTags() {
        if (null == this.currentUser) {
            return List.of();
        }
        return this.td.getAll();
    }
}
